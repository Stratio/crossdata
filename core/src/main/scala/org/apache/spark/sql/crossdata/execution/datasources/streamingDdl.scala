/**
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
package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.config.StreamingConfig._
import org.apache.spark.sql.crossdata.models.EphemeralQueryModel
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Ephemeral Table Functions
  */

private[crossdata] case class ExistsEphemeralTable(tableIdent: TableIdentifier) extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Exists table?", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val tableExist : Boolean = sqlContext.asInstanceOf[XDContext].streamingCatalog.exists {
      streamingCatalog => streamingCatalog.existsEphemeralTable(tableIdent.table)
    }

    val result = if (tableExist) "EXISTS" else "NOT EXISTS"

    Seq(Row(s"${tableIdent.table} $result"))
  }
}

private[crossdata] case class GetEphemeralTable(tableIdent: TableIdentifier) extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephTable =
      sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
        streamingCatalog => streamingCatalog.getEphemeralTable(tableIdent.table)
          .map(_.toStringPretty)
          .getOrElse(s"${tableIdent.table} doesn't exists")
      }
    Seq(ephTable.map(Row(_)).getOrElse(Row.empty))
  }
}

private[crossdata] case class GetAllEphemeralTables() extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral tables", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephTables =
      sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
        streamingCatalog =>
          streamingCatalog.getAllEphemeralTables.map(_.toStringPretty)
      }

    ephTables.map(_.map(Row(_))).getOrElse(Seq(Row.empty))
  }
}

private[crossdata] case class CreateEphemeralTable(
                                                    tableIdent: TableIdentifier,
                                                    opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val result = sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
      streamingCatalog =>
        val ephTable = createEphemeralTableModel(tableIdent.table, opts)
        streamingCatalog.createEphemeralTable(ephTable) match {
          case Right(table)  => table.toStringPretty
          case Left(message)    => message
        }
    }

    /*
        // TODO: Blocked by CROSSDATA-148 and CROSSDATA-205
        // * This query will trigger 3 actions in the catalog persistence:
        //   1.- Associate the table with the schema.
        val schema = columns.json
        //   2.- Associate the table with the configuration.
        val options = JSONObject(opts).toString(JSONFormat.defaultFormatter)
        //   3.- Associate the QueryID with the involved table.

        // * SparkLauncher of StreamingProcess
        val params = tableId :: opts.values.toList
        val sparkApp = new SparkLauncher()
          .setAppName(tableId)
          .setMaster(sqlContext.conf.getConfString("spark.master"))
          .setAppResource("streamingProcess.jar")
          .setMainClass("StreamingProcess")
          .setDeployMode("cluster")
          .addAppArgs(params:_*)
          .launch()

        // * Return the UUID of the process
    */

    Seq(result.map(Row(_)).getOrElse(Row.empty))

  }
}

private[crossdata] case class UpdateEphemeralTable(tableIdent: TableIdentifier , opts: Map[String, String])
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Updated table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog =>

        val ephTable = createEphemeralTableModel(tableIdent.table, opts)
        streamingCatalog.updateEphemeralTable(ephTable)
    }

    Seq(Row(tableIdent.table))
  }
}

private[crossdata] case class DropEphemeralTable(tableIdent: TableIdentifier)
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Dropped table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog => streamingCatalog.dropEphemeralTable(tableIdent.table)
    }

    Seq(Row(tableIdent.table))
  }
}

private[crossdata] case class DropAllEphemeralTables() extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Dropped tables", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val deletedTables = sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
      streamingCatalog =>
        val ephTables = streamingCatalog.getAllEphemeralTables
        streamingCatalog.dropAllEphemeralTables
        ephTables.map(_.name)
    }

    deletedTables.map(_.map(Row(_))).getOrElse(Seq(Row.empty))
  }
}

/**
  * Ephemeral Status Functions
  */

private[crossdata] case class GetEphemeralStatus(tableIdent: TableIdentifier) extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField(s"${tableIdent.table} status", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephStatus =
      sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
        streamingCatalog => streamingCatalog.getEphemeralStatus(tableIdent.table)
          .map(_.toStringPretty)
          .getOrElse(s"${tableIdent.table} status doesn't exists")
      }

    Seq(ephStatus.map(Row(_)).getOrElse(Row.empty))
  }
}

private[crossdata] case class GetAllEphemeralStatuses() extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Statuses", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephStatuses =
      sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
        streamingCatalog =>
          streamingCatalog.getAllEphemeralStatuses.map(_.toStringPretty)
      }

    ephStatuses.map(_.map(Row(_))).getOrElse(Seq(Row.empty))
  }
}

/**
  * Ephemeral Queries Functions
  */

private[crossdata] case class ExistsEphemeralQuery(queryIdent: TableIdentifier)
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Exists query?", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val queryExist : Boolean = sqlContext.asInstanceOf[XDContext].streamingCatalog.exists {
      streamingCatalog => streamingCatalog.existsEphemeralQuery(queryIdent.table)
    }

    val result = if (queryExist ) "EXISTS" else "NOT EXISTS"

    Seq(Row(s"${queryIdent.table} $result"))
  }
}


private[crossdata] case class GetEphemeralQuery(queryIdent: TableIdentifier)
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral query", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephQuery =
      sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
        streamingCatalog => streamingCatalog.getEphemeralQuery(queryIdent.table)
          .map(_.toStringPretty)
          .getOrElse(s"${queryIdent.table} doesn't exists")
      }
    Seq(ephQuery.map(Row(_)).getOrElse(Row.empty))
  }
}


private[crossdata] case class GetAllEphemeralQueries()
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral queries", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val ephQueries =
      sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
        streamingCatalog =>
          streamingCatalog.getAllEphemeralQueries.map(_.toStringPretty)
      }

    ephQueries.map(_.map(Row(_))).getOrElse(Seq(Row.empty))
  }
}


private[crossdata] case class AddEphemeralQuery(ephemeralTablename: String,
                                                sql: String,
                                                alias: String,
                                                window: Int,
                                                opts: Map[String, String] = Map.empty)

  extends LogicalPlan with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral query", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val result = sqlContext.asInstanceOf[XDContext].streamingCatalog.map {
      streamingCatalog =>
        val eitherCreate = streamingCatalog.createEphemeralQuery(EphemeralQueryModel(ephemeralTablename, sql, alias, window, opts))
        if (eitherCreate.isLeft)
          sys.error(eitherCreate.left.get)
        else
          alias
    }.getOrElse(sys.error("StreamingXDCatalog is empty. Cannot create ephemeral queries"))

    /*// TODO: Blocked by CROSSDATA-148 and CROSSDATA-205
        // * This query will trigger 3 actions in the catalog persistence:
        //   1.- Associate the table with the schema.
        val schema = columns.json
        //   2.- Associate the table with the configuration.
        val options = JSONObject(opts).toString(JSONFormat.defaultFormatter)
        //   3.- Associate the QueryID with the involved table.

        // * SparkLauncher of StreamingProcess
        val params = tableId :: opts.values.toList
        val sparkApp = new SparkLauncher()
          .setAppName(tableId)
          .setMaster(sqlContext.conf.getConfString("spark.master"))
          .setAppResource("streamingProcess.jar")
          .setMainClass("StreamingProcess")
          .setDeployMode("cluster")
          .addAppArgs(params:_*)
          .launch()

    */

    Seq(Row(result))

  }
}

private[crossdata] case class DropEphemeralQuery(queryIdent: TableIdentifier)
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Dropped query", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog => streamingCatalog.dropEphemeralQuery(queryIdent.table)
    }

    Seq(Row(queryIdent.table))
  }
}

private[crossdata] case class DropAllEphemeralQueries() extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Dropped queries", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val deletedQueries = sqlContext.asInstanceOf[XDContext].streamingCatalog.map{
      streamingCatalog =>
        val ephTables = streamingCatalog.getAllEphemeralQueries
        streamingCatalog.dropAllEphemeralQueries()
        ephTables.map(_.toStringPretty)
    }

    deletedQueries.map(_.map(Row(_))).getOrElse(Seq(Row.empty))
  }
}