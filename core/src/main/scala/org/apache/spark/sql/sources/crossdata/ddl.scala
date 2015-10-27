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


private [crossdata] case class ImportTablesUsingWithOptions(provider: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    //Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()

    val inventoryRelation = resolved.asInstanceOf[TableInventory] //As inventory provider
    val providerRelation = resolved.asInstanceOf[RelationProvider] //As relation provider

    //Obtains the list of tables and persist it (if persistence implemented)
    val tables = inventoryRelation.listTables(sqlContext, opts)

    //Register the source tables in the catalog
    for(
      t: TableInventory.Table <- tables;
      //TODO: WARNING: The change described in the comment below should be done ASAP!
      //TODO: Recover this `t.database.toList :+ t.tableName` when XDCatalog is ready;
      tableid = s"`${(t.database.toList :+ t.tableName) mkString "."}`"::Nil;
      if(inventoryRelation.exclusionFilter(t) && {
        val doExist = sqlContext.catalog.tableExists(tableid);
        if(doExist) log.info(s"IMPORT TABLE omitted already registered table: ${tableid mkString "."}")
        !doExist
      })
    ) sqlContext.
        catalog.asInstanceOf[XDCatalog].persistTable(
        CrossdataTable(t.tableName, t.database,  t.schema, provider,  Array.empty[String], opts),
          Option(LogicalRelation(providerRelation.createRelation(
            sqlContext,
            inventoryRelation.generateConnectorOpts(t, opts)))
          )
        )
    Seq.empty
  }
}
