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
package org.apache.spark.sql.sources.crossdata

import com.stratio.crossdata.connector.TableInventory
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.{CrossdataTable, XDCatalog}
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.RelationProvider
import org.apache.spark.sql.{Row, SQLContext}


private [crossdata] case class ImportTablesUsingWithOptions(datasource: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {

    def tableExists(tableId: List[String]): Boolean = {
      val doExist = sqlContext.catalog.tableExists(tableId)
      if (doExist) log.info(s"IMPORT TABLE omitted already registered table: ${tableId mkString "."}")
      doExist
    }

    def persistTable(t: TableInventory.Table, tableInventory: TableInventory, relationProvider: RelationProvider) = {
      val connectorOpts = tableInventory.generateConnectorOpts(t, opts)

      sqlContext.catalog.asInstanceOf[XDCatalog].persistTable(
        CrossdataTable(t.tableName, t.database, t.schema, datasource, Array.empty[String], connectorOpts),
        Option(LogicalRelation(relationProvider.createRelation(sqlContext, connectorOpts)))
      )
    }

    //Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(datasource).newInstance()

    val inventoryRelation = resolved.asInstanceOf[TableInventory]
    val relationProvider = resolved.asInstanceOf[RelationProvider]

    //Obtains the list of tables and persist it (if persistence implemented)
    val tables = inventoryRelation.listTables(sqlContext, opts)

    for {
      t: TableInventory.Table <- tables
      tableid = s"`${(t.database.toList :+ t.tableName) mkString "."}`" :: Nil
      if inventoryRelation.exclusionFilter(t) && !tableExists(tableid)
    } persistTable(t, inventoryRelation, relationProvider)
    Seq.empty
  }


}
