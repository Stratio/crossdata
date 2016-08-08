/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.catalyst.streaming

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog._
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._
import org.apache.spark.sql.crossdata.config.StreamingConfig
import org.apache.spark.sql.crossdata.launcher.SparkJobLauncher
import org.apache.spark.sql.crossdata.models.{EphemeralExecutionStatus, EphemeralQueryModel, EphemeralStatusModel}
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Ephemeral Table Functions
  */
private[crossdata] case class DescribeEphemeralTable(tableIdent: TableIdentifier)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Ephemeral table", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] =
    sqlContext.catalog
      .getEphemeralTable(normalizeTableIdentifier(tableIdent, sqlContext.conf))
      .map(ephTable => Seq(Row(ephTable.toPrettyString)))
      .getOrElse(throw new RuntimeException(s"${tableIdent.unquotedString} doesn't exist"))

}

private[crossdata] case object ShowEphemeralTables extends LogicalPlan with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(
            StructField("name", StringType, nullable = false),
            StructField("status", StringType, nullable = false),
            StructField("atomicWindow", IntegerType, nullable = false)
        ))
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    val catalog = sqlContext.catalog

    val tables = catalog.getAllEphemeralTables
    val status = tables.map(etm => catalog.getEphemeralStatus(etm.name).map(_.status))

    tables zip status map {
      case (tableModel, tableStatus) =>
        Row(tableModel.name, tableStatus.mkString, tableModel.options.atomicWindow)
    }

  }
}

private[crossdata] case class CreateEphemeralTable(tableIdent: TableIdentifier,
                                                   userSchema: Option[StructType],
                                                   opts: Map[String, String])
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Ephemeral table", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephTable = StreamingConfig.createEphemeralTableModel(
        normalizeTableIdentifier(tableIdent, sqlContext.conf),
        opts,
        userSchema)
    sqlContext.catalog.createEphemeralTable(ephTable) match {
      case Right(table) => Seq(Row(table.toPrettyString))
      case Left(message) => sys.error(message)
    }
  }

}

private[crossdata] case class DropEphemeralTable(tableIdent: TableIdentifier)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Dropped table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.dropEphemeralTable(normalizeTableIdentifier(tableIdent, sqlContext.conf))
    Seq(Row(tableIdent.unquotedString))
  }
}

private[crossdata] case object DropAllEphemeralTables extends LogicalPlan with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Dropped tables", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val catalog = sqlContext.catalog

    val ephTables = catalog.getAllEphemeralTables
    catalog.dropAllEphemeralTables
    ephTables.map(eTable => Row(eTable.name))

  }
}

/**
  * Ephemeral Table Status Functions
  */
private[crossdata] case class ShowEphemeralStatus(tableIdent: TableIdentifier)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField(s"status", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog
      .getEphemeralStatus(normalizeTableIdentifier(tableIdent, sqlContext.conf))
      .map(ephStatus => Seq(Row(ephStatus.status.toString)))
      .getOrElse(sys.error(s"${tableIdent.unquotedString} status doesn't exist"))
  }
}

private[crossdata] case object ShowAllEphemeralStatuses extends LogicalPlan with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(
            StructField("name", StringType, nullable = false),
            StructField("status", StringType, nullable = false)
        ))
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] =
    sqlContext.catalog.getAllEphemeralStatuses.map { ephStat =>
      Row(ephStat.ephemeralTableName, ephStat.status.toString)
    }

}

private[crossdata] case class StartProcess(tableIdentifier: String)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(
            StructField("infoMessage", StringType, nullable = true)
        ))
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    val xdContext = sqlContext.asInstanceOf[XDContext]
    val sparkJob =
      SparkJobLauncher.getSparkStreamingJob(xdContext, XDContext.xdConfig, tableIdentifier)

    sparkJob match {
      case Failure(exception) =>
        logError(exception.getMessage, exception)
        sys.error("Validation error: " + exception.getMessage)
        Seq(Row(exception.getMessage))

      case Success(job) =>
        job.submit()
        Seq(Row("Spark app launched"))
    }

  }
}

private[crossdata] case class StopProcess(tableIdentifier: String)
    extends LogicalPlan
    with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    val xdContext = sqlContext.asInstanceOf[XDContext]

    logInfo(s"Stopping process $tableIdentifier")
    xdContext.catalog.getEphemeralStatus(tableIdentifier) match {
      case Some(currentStatus) =>
        if (currentStatus == EphemeralExecutionStatus.Started || currentStatus == EphemeralExecutionStatus.Starting) {
          xdContext.catalog.updateEphemeralStatus(
              tableIdentifier,
              EphemeralStatusModel(tableIdentifier, EphemeralExecutionStatus.Stopping)
          )
        } else {
          sys.error(s"Cannot stop process. $tableIdentifier status is $currentStatus")
        }
      case None =>
        sys.error(s"Process $tableIdentifier not found")
    }
    Seq.empty
  }
}

/**
  * Ephemeral Queries Functions
  */
private[crossdata] case class ShowEphemeralQueries(tableIdentifier: Option[String] = None)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Ephemeral query", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val queries = sqlContext.catalog.getAllEphemeralQueries
    val filteredQueries = tableIdentifier
      .map(table => queries.filter(eqm => eqm.ephemeralTableName == table))
      .getOrElse(queries)
    filteredQueries.map(q => Row(q.toPrettyString))

  }
}

private[crossdata] case class AddEphemeralQuery(ephemeralTablename: String,
                                                sql: String,
                                                alias: String,
                                                window: Int,
                                                opts: Map[String, String] = Map.empty)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Ephemeral query", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.createEphemeralQuery(
        EphemeralQueryModel(ephemeralTablename, sql, alias, window, opts)) match {
      case Left(errorMessage) => sys.error(errorMessage)
      case Right(query) => Seq(Row(query.toPrettyString))
    }
  }

}

private[crossdata] case class DropEphemeralQuery(queryIdent: String)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Dropped query", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.dropEphemeralQuery(queryIdent)
    Seq(Row(queryIdent))
  }
}

private[crossdata] case class DropAllEphemeralQueries(tableName: Option[String] = None)
    extends LogicalPlan
    with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
        Seq(StructField("Dropped query", StringType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val catalog = sqlContext.catalog

    val filteredQueryAliases = {
      val queries = catalog.getAllEphemeralQueries
      tableName
        .map(tname => queries.filter(eqm => eqm.ephemeralTableName == tname))
        .getOrElse(queries)
        .map(q => q.alias)
    }

    filteredQueryAliases.map { queryAlias =>
      catalog.dropEphemeralQuery(queryAlias)
      Row(queryAlias)
    }

  }
}
