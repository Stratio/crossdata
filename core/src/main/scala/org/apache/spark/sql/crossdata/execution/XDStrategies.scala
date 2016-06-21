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
import org.apache.spark.sql.catalyst.analysis.UnresolvedAttribute
import org.apache.spark.sql.catalyst.expressions.{AttributeReference, GreaterThan, In, Literal}
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LogicalPlan}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataIndex, CrossdataTable, IndexIdentifier}
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
    import org.apache.spark.sql.execution

    override def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case Filter(condition, ExtendedUnresolvedRelation(tableIdentifier, child)) =>

        val crossdataIndex = sqlContext.catalog.asInstanceOf[XDCatalog].obtainTableIndex(tableIdentifier) getOrElse
          sys.error("Unexpected error. Can't find index for enhance query with indexes")
        val pk = crossdataIndex.pkCols
        val indexedCols = crossdataIndex.indexedCols

        condition match {
          case GreaterThan(AttributeReference(attrName,_,_,_), Literal(value, dataType)) =>
            //TODO: que pasa si vienen varios, o viene como db.attr
            //TODO: Reconstruct logical plan without using parser & analyzer
            val left = sqlContext.analyzer.execute(
              sqlContext.parseSql(s"""|select ${pk mkString(",")} from ${crossdataIndex.indexIdentifier.unquotedString}
                                      |where $attrName > $value""".stripMargin)
            )

            sqlContext.analyzer.checkAnalysis(left)
            val optimized = sqlContext.optimizer.execute(left)

            //Convert from select [] from indexedTable where [] > x to select [] from indexedTable where pks IN [leftResults]
            val inCondition = In(UnresolvedAttribute(pk.head), Seq()) //TODO: Compound PK
            XDIndexJoin(planLater(optimized),Filter(inCondition, child)) :: Nil

          case _ => sys.error("Unsupported operation with index") //Should never be thrown (avoided by analizer)
        }

      case _ => Nil
    }
  }
  
}
