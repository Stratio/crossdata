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


import org.json4s.{FieldSerializer, DefaultFormats}
import org.json4s.jackson.Serialization._
import com.stratio.crossdata.connector.TableInventory
import org.apache.spark.Logging

import org.apache.spark.launcher.SparkLauncher
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.{Row, SQLContext}

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog._
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.daos.{EphemeralTableStatusDAO, EphemeralTableDAO, EphemeralTableMapDAO}
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.ResolvedDataSource
import org.apache.spark.sql.sources.RelationProvider
import org.apache.spark.sql.types._

import scala.util.parsing.json.{JSONFormat, JSONObject, JSON}

import org.apache.spark.sql.crossdata.config._
import org.apache.spark.sql.crossdata.catalog.XDStreamingCatalog._


private [crossdata] case class ImportTablesUsingWithOptions(datasource: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand with Logging {

  // The result of IMPORT TABLE has only tableIdentifier so far.
  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("tableIdentifier", ArrayType(StringType), false), StructField("ignored", BooleanType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    def tableExists(tableId: Seq[String]): Boolean = {
      val doExist = sqlContext.catalog.tableExists(tableId)
      if (doExist) log.warn(s"IMPORT TABLE omitted already registered table: ${tableId mkString "."}")
      doExist
    }

    def persistTable(t: TableInventory.Table, tableInventory: TableInventory, relationProvider: RelationProvider) = {
      val connectorOpts = tableInventory.generateConnectorOpts(t, opts)

      sqlContext.catalog.persistTable(
        CrossdataTable(t.tableName, t.database, t.schema, datasource, Array.empty[String], connectorOpts),
        LogicalRelation(relationProvider.createRelation(sqlContext, connectorOpts)
        )
      )
    }

    // Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(datasource).newInstance()

    val inventoryRelation = resolved.asInstanceOf[TableInventory]
    val relationProvider = resolved.asInstanceOf[RelationProvider]

    // Obtains the list of tables and persist it (if persistence implemented)
    val tables = inventoryRelation.listTables(sqlContext, opts)


    for {
      table: TableInventory.Table <- tables
      tableId = TableIdentifier(table.tableName, table.database).toSeq
      if inventoryRelation.exclusionFilter(table)
    } yield {
      val ignoreTable = tableExists(tableId)
      if (!ignoreTable) {
        logInfo(s"Importing table ${tableId mkString "."}")
        persistTable(table, inventoryRelation, relationProvider)
      }
      Row(tableId, ignoreTable)
    }

  }
}

private[crossdata] case class DropTable(tableIdentifier: TableIdentifier)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.dropTable(tableIdentifier.toSeq)
    Seq.empty
  }

}

private[crossdata] case class CreateTempView(viewIdentifier: TableIdentifier, queryPlan: LogicalPlan)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.registerView(viewIdentifier.toSeq, queryPlan)
    Seq.empty
  }

}

private[crossdata] case class CreateView(viewIdentifier: TableIdentifier, queryPlan: LogicalPlan, sql: String)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.persistView(viewIdentifier, queryPlan, sql)
    Seq.empty
  }
}


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
    }
    Seq(ephTable.map(Row(_)).getOrElse(Row.empty))
    //Seq(Row(ephTable.getOrElse()))
  }
}

private[crossdata] case class GetAllEphemeralTables() extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("OperationId", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val tableId = createId

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog =>
        streamingCatalog.getAllEphemeralTables
    }

    Seq(Row(tableId))
  }
}

private[crossdata] case class CreateEphemeralTable(
                                                    tableIdent: TableIdentifier,
                                                    opts: Map[String, String])
  extends LogicalPlan with RunnableCommand with EphemeralTableDAO {

  // TODO choose config params from file or from OPTIONS()
  override lazy val config: Config =
    new TypesafeConfig(
      None,
      None,
      Some(CoreConfig.CoreBasicConfig),
      Some(CoreConfig.ParentConfigName + "." + XDContext.StreamingConfigKey)
    )

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField(ephemeralTableIdField, StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val tableId = createId

    // TODO fill default values
    //kafka options
    val host = config.getString(kafkaHost,"DefaultHost")
    val consumerPort = config.getString(kafkaConsumerPort, "DefaultConsumer")
    val producerPort = config.getString(kafkaProducerPort, "DefaultProducer")
    val topics= Seq(TopicModel(config.getString(kafkaTopicName, tableId)))
    val connections = Seq(ConnectionHostModel(host, consumerPort, producerPort))
    val groupId = config.getString(kafkaGroupId, tableIdent.table)
    val partition = config.getString(kafkaPartition)
    val kafkaOptions = KafkaOptionsModel(connections, topics, groupId, partition, Map.empty)

    val ephemeralOptions = EphemeralOptionsModel(kafkaOptions)

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog =>
        val ephTable = EphemeralTableModel(tableIdent.table, ephemeralOptions)
        streamingCatalog.createEphemeralTable(ephTable)
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
    Seq(Row(tableId))
  }
}

private[crossdata] case class UpdateEphemeralTable(tableIdent: TableIdentifier , opts: Map[String, String])
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("OperationId", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val tableId = createId

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog =>
      // TODO make EphemeralTableModel from opts. If there are not all the mandatory params throw an error

//        val ephTable = EphemeralTableModel(tableId, tableIdent.table, EphemeralOptionsModel())
//        streamingCatalog.updateEphemeralTable(ephTable)
    }

    Seq(Row(tableId))
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
      Seq(StructField("All tables dropped", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog => streamingCatalog.dropAllEphemeralTables()
    }

    Seq.empty
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

      sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
        streamingCatalog => streamingCatalog.getEphemeralStatus(tableIdent.table)
      }

      //TODO return ephemeralStatus json
      Seq(Row())
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

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog => streamingCatalog.getAllEphemeralStatuses
    }

    //TODO return ephemeralStatuses json
    Seq(Row())
  }
}

private[crossdata] case class UpdateEphemeralStatus(tableIdent: TableIdentifier, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand{

  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField(s"New ${tableIdent.table} status", StringType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.asInstanceOf[XDContext].streamingCatalog.foreach{
      streamingCatalog =>
        //TODO make EphemeralStatusModel from opts
        //streamingCatalog.updateEphemeralStatus(tableIdent.table, EphemeralStatusModel)
    }

    //TODO return new ephemeralStatus json

    Seq(Row())
  }
}


  /**
  * Ephemeral Queries Functions
  */
