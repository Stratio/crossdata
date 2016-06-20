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
package org.apache.spark.sql.crossdata.execution

import org.apache.spark.sql.Strategy
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LogicalPlan}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataIndex, IndexIdentifier, CrossdataTable}
import org.apache.spark.sql.crossdata.catalyst.ExtendedUnresolvedRelation
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, CreateTableUsingAsSelect}
import org.apache.spark.sql.execution.{ExecutedCommand, SparkPlan, SparkStrategies}

trait XDStrategies extends SparkStrategies {
  self: XDContext#XDPlanner =>

  object XDDDLStrategy extends Strategy {
    def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case CreateTableUsing(tableIdent, userSpecifiedSchema, provider, temporary, opts, allowExisting, _) =>

        val crossdataTable = CrossdataTable(
          tableIdent.table,
          tableIdent.database,
          userSpecifiedSchema,
          provider,
          Array.empty[String],
          opts
        )

        val cmd = if(temporary)
          RegisterDataSourceTable(crossdataTable, allowExisting)
        else
          PersistDataSourceTable(crossdataTable, allowExisting)

        ExecutedCommand(cmd) :: Nil

      case CreateTableUsingAsSelect(tableIdent, provider, false, partitionCols, mode, opts, query) =>
        val cmd = PersistSelectAsTable(tableIdent, provider, partitionCols, mode, opts, query)
        ExecutedCommand(cmd) :: Nil

      case _ => Nil
    }
  }

  object GlobalIndexStrategy extends Strategy {
    override def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case Filter(condition, ExtendedUnresolvedRelation(tableIdentifier, child)) =>
        //TODO:Get catalog, check we have indexed table, check the condition has the
        if(true){

          val crossdataIndex: CrossdataIndex = CrossdataIndex(TableIdentifier("proofGlobalIndex",Option("globalIndexDb")), IndexIdentifier("myIndex","gidx"),
            Seq("col1", "col2"), Seq("pk1", "pk2"), "dataSource", Map()) //TODO: Merge with jjlopez

          val left = sqlContext.analyzer.execute(
            sqlContext.parseSql(s"select id from ${crossdataIndex.indexIdentifier.unquotedString} where other > 10")
          )
          /*val right = sqlContext.analyzer.execute(
            sqlContext.parseSql(s"select * from ${crossdataIndex.tableIdentifier.unquotedString} where id IN (22)")
          )*/

          val right = sqlContext.parseSql(s"select * from ${crossdataIndex.tableIdentifier.unquotedString} where id IN (22)")

          XDIndexJoin(planLater(left),right) :: Nil
        } else {
          org.apache.spark.sql.execution.Filter(condition, planLater(child)) :: Nil
        }
      case ExtendedUnresolvedRelation(tableIdentifier, child) => planLater(child) :: Nil
      case _ => Nil
    }
  }
  
}
