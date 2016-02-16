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

import com.stratio.crossdata.launcher.SparkJobLauncher
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDStreamingCatalog
import org.apache.spark.sql.crossdata.config.StreamingConfig._
import org.apache.spark.sql.crossdata.models.{EphemeralExecutionStatus, EphemeralStatusModel, EphemeralQueryModel}
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

// TODO avoid this ec??
import scala.concurrent.ExecutionContext.Implicits.global

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

private[crossdata] case class DescribeEphemeralTable(tableIdent: TableIdentifier) extends LogicalPlan with RunnableCommand {

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] =
    sqlContext.asInstanceOf[XDContext].streamingCatalog.map {
      streamingCatalog => streamingCatalog.getEphemeralTable(tableIdent.table)
        .map(epht => Seq(Row(epht.toStringPretty)))
        .getOrElse(throw new RuntimeException(s"${tableIdent.table} doesn't exists"))
    }.getOrElse(Seq.empty)

}

private[crossdata] case class ShowEphemeralTables() extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(Seq(
      StructField("name", StringType, nullable = false),
      StructField("status", StringType, nullable = false),
      StructField("atomic window", IntegerType, nullable = false)
    ))
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.asInstanceOf[XDContext].streamingCatalog.map{ streamCatalog =>
      val tables = streamCatalog.getAllEphemeralTables

      val status= tables.map(etm => streamCatalog.getEphemeralStatus(etm.name).map(_.status))

      tables zip status map { case (tableModel, tableStatus) =>
        Row(tableModel.name, tableStatus.mkString, tableModel.options.atomicWindow)
      }
    }.getOrElse(Seq.empty)


  }
}

private[crossdata] case class CreateEphemeralTable(
                                                    tableIdent: TableIdentifier,
                                                    userSchema: Option[StructType],
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
        val ephTable = createEphemeralTableModel(tableIdent.table, userSchema, opts)
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


private[crossdata] case class DropEphemeralTable(tableIdent: TableIdentifier)
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Dropped table", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {


    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{ streamingCatalog =>
      streamingCatalog.dropEphemeralTable(tableIdent.table)
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
 * Ephemeral Table Status Functions
 */

private[crossdata] case class ShowEphemeralStatus(tableIdent: TableIdentifier) extends LogicalPlan with RunnableCommand{

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

private[crossdata] case class ShowAllEphemeralStatuses() extends LogicalPlan with RunnableCommand{

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

private[crossdata] case class ShowEphemeralQueries(tableIdentifier: Option[String])
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Ephemeral queries", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.asInstanceOf[XDContext].streamingCatalog.map{ streamingCatalog =>
      val queries = streamingCatalog.getAllEphemeralQueries
      val filteredQueries =
        tableIdentifier.map( table => queries.filter( eqm => eqm.ephemeralTableName == table)).getOrElse(queries)
      filteredQueries.map( q => Row(q.toStringPretty))
    } getOrElse Seq.empty
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

private[crossdata] case class StartProcess(tableIdentifier: String) extends LogicalPlan with RunnableCommand{


  override def run(sqlContext: SQLContext): Seq[Row] = {
    val xdContext = sqlContext.asInstanceOf[XDContext]
    val streamCatalog: XDStreamingCatalog = xdContext.streamingCatalog.getOrElse(
      sys.error("A streaming catalog must be configured")
    )
    val jobLauncher = new SparkJobLauncher(xdContext.xdConfig, streamCatalog)
    jobLauncher.doInitSparkStreamingJob(tableIdentifier)
    Seq.empty
  }
}

private[crossdata] case class StopProcess(tableIdentifier: String) extends LogicalPlan with RunnableCommand{

  override def run(sqlContext: SQLContext): Seq[Row] = {
    val xdContext = sqlContext.asInstanceOf[XDContext]
    val streamCatalog: XDStreamingCatalog = xdContext.streamingCatalog.getOrElse(
      sys.error("A streaming catalog must be configured")
    )
    logInfo(s"Stopping process $tableIdentifier")
    val currentStatus = streamCatalog.getEphemeralStatus(tableIdentifier).get.status
    if (currentStatus == EphemeralExecutionStatus.Started || currentStatus == EphemeralExecutionStatus.Started){
      streamCatalog.updateEphemeralStatus(
        tableIdentifier,
        EphemeralStatusModel(tableIdentifier, EphemeralExecutionStatus.Stopping)
      )
    } else {
      sys.error(s"Cannot stop process. $tableIdentifier status is $currentStatus")
    }
    Seq.empty
  }
}