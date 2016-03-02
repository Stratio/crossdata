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

import com.stratio.crossdata.connector.TableInventory
import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.connector.TableManipulation
import org.apache.spark.Logging
import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog._
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.datasources.ResolvedDataSource
import org.apache.spark.sql.sources.RelationProvider
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.BooleanType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import scala.reflect.io.File

private[crossdata] case class ImportTablesUsingWithOptions(datasource: String, opts: Map[String, String])
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

    // Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(datasource).newInstance()
    val inventoryRelation = resolved.asInstanceOf[TableInventory]

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

        DdlUtils.registerTable(sqlContext, datasource, resolved, table.tableName, table.database, table.schema, opts)
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

private[crossdata] case class DropView(viewIdentifier: TableIdentifier)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.dropView(viewIdentifier.toSeq)
    Seq.empty
  }
}

private[crossdata] case class AddJar(jarPath: String)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    if ((jarPath.toLowerCase.startsWith("hdfs://")) || (File(jarPath).exists)) {
      sqlContext.sparkContext.addJar(jarPath)
      Seq.empty
    } else {
      throw new Error("File doesn't exists or is not a hdfs file")
    }
  }
}

case class CreateExternalTable(
                                tableIdent: TableIdentifier,
                                userSpecifiedSchema: StructType,
                                provider: String,
                                options: Map[String, String]) extends LogicalPlan with RunnableCommand {


  override def run(sqlContext: SQLContext): Seq[Row] = {

    require(tableIdent.database.isDefined, "Catalog is required required when use CREATE EXTERNAL TABLE command")

    val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()

    if (!resolved.isInstanceOf[TableManipulation]){
      sys.error("The Datasource does not support CREATE EXTERNAL TABLE command")
    }

    val tableManipulation = resolved.asInstanceOf[TableManipulation]
    tableManipulation.createExternalTable(sqlContext, tableIdent.table, userSpecifiedSchema, options)

    DdlUtils.registerTable(sqlContext, provider, resolved, tableIdent.table, tableIdent.database, Some(userSpecifiedSchema), options)
    Seq.empty

  }

}

private object DdlUtils{

  def registerTable(sqlContext:SQLContext,
                    provider: String,
                    resolved:Any,
                    table:String, database:Option[String],
                    userSpecifiedSchema:Option[StructType],
                    options: Map[String, String]): Unit ={

    val tableInventory = resolved.asInstanceOf[TableInventory]
    val optionsWithTable = tableInventory.generateConnectorOpts(
      Table(table, database, userSpecifiedSchema), options)

    val resolvedDS = ResolvedDataSource(sqlContext,userSpecifiedSchema,Array.empty, provider, optionsWithTable)

    sqlContext.catalog.persistTable(
      CrossdataTable(table, database, userSpecifiedSchema, provider, Array.empty[String],
        optionsWithTable),
      LogicalRelation(resolvedDS.relation, None)
    )
  }
}
