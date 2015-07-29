/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.spark.sql.crossdata

import com.stratio.crossdata.sql.sources.NativeScan
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.plans.logical.{BinaryNode, UnaryNode, LeafNode, LogicalPlan}
import org.apache.spark.sql.sources._
import org.apache.spark.sql._


private[sql] object CrossdataFrame {
  def apply(sqlContext: SQLContext, logicalPlan: LogicalPlan): DataFrame = {
    new CrossdataFrame(sqlContext, logicalPlan)
  }

  def findNativeRelation(optimizedLogicalPlan: LogicalPlan): Option[NativeScan] = {
    // TODO check whether there is multiple baseRelation => avoid executing via NativeScan
    // TODO very simplistic implementation currently

    def findTables(logicalPlan:LogicalPlan): List[BaseRelation] = logicalPlan match{
      case LogicalRelation(baseRelation) =>  List(baseRelation)
      case _: LeafNode => Nil
      case unaryNode: UnaryNode => findTables(unaryNode.child)
      case binaryNode: BinaryNode => findTables(binaryNode.left) ::: findTables(binaryNode.right)
      case _ => Nil
    }

    val tablesInLogicalPlan = findTables(optimizedLogicalPlan)

    if (tablesInLogicalPlan.toSet.size ==1){
      tablesInLogicalPlan.head match {
        case ns: NativeScan => Some(ns)
        case _ => None
      }
    }else {
      None
    }

  }
}


/**
 * Extends a [[DataFrame]] to provide native access to datasources when performing RDD actions.
 *
 * @inheritdoc
 *
 */
// TODO: Improve documentation.
private[sql] class CrossdataFrame(@transient override val sqlContext: SQLContext,
                                  @transient override val queryExecution: SQLContext#QueryExecution)
  extends DataFrame(sqlContext, queryExecution) {

  def this(sqlContext: SQLContext, logicalPlan: LogicalPlan) = {
    this(sqlContext, {
      val qe = sqlContext.executePlan(logicalPlan)
      if (sqlContext.conf.dataFrameEagerAnalysis) {
        qe.assertAnalyzed() // This should force analysis and throw errors if there are any
      }
      qe
    })
  }

  import CrossdataFrame._

  /**
   * @inheritdoc
   */
  override def collect(): Array[Row] = {
    // TODO take
    // if cache don't go through native
    if (sqlContext.cacheManager.lookupCachedData(this).nonEmpty) {
      super.collect()
    } else {
      val nativeRelation: Option[NativeScan] = findNativeRelation(queryExecution.optimizedPlan)
      nativeRelation.flatMap(executeNative).getOrElse(super.collect())
    }
  }


  private[this] def executeNative(provider: NativeScan): Option[Array[Row]] = {
    provider.buildScan(queryExecution.optimizedPlan)
    // TODO cache? results
  }

}



