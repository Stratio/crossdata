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
import org.apache.spark.sql.catalyst.plans.logical.{LeafNode, LogicalPlan}
import org.apache.spark.sql._
import org.apache.spark.sql.sources.LogicalRelation


private[sql] object XDDataframe {
  def apply(sqlContext: SQLContext, logicalPlan: LogicalPlan): DataFrame = {
    new XDDataframe(sqlContext, logicalPlan)
  }

  def findNativeQueryExecutor(optimizedLogicalPlan: LogicalPlan): Option[NativeScan] = {

    def allLeafsAreNative(leafs: Seq[LeafNode]): Boolean = {
      leafs.forall {
        case LogicalRelation(ns: NativeScan) => true
        case _ => false
      }
    }

    val leafs = optimizedLogicalPlan.collect { case leafNode: LeafNode => leafNode}
    if (!allLeafsAreNative(leafs)) {
      None
    } else {
      val nativeExecutors: Seq[NativeScan] = leafs.map { case LogicalRelation(ns: NativeScan) => ns}

      nativeExecutors match {
        case Seq(head) => Some(head)
        case _ => {
          if (nativeExecutors.sliding(2).forall { tuple =>
            tuple(0).getClass == tuple(1).getClass
          }) {
            nativeExecutors.headOption
          } else {
            None
          }
        }
      }
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
private[sql] class XDDataframe(@transient override val sqlContext: SQLContext,
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

  import XDDataframe._

  /**
   * @inheritdoc
   */
  override def collect(): Array[Row] = {
    // TODO take
    // if cache don't go through native
    if (sqlContext.cacheManager.lookupCachedData(this).nonEmpty) {
      super.collect()
    } else {
      val nativeQueryExecutor: Option[NativeScan] = findNativeQueryExecutor(queryExecution.optimizedPlan)
      nativeQueryExecutor.flatMap(executeNativeQuery).getOrElse(super.collect())
    }
  }


  private[this] def executeNativeQuery(provider: NativeScan): Option[Array[Row]] = {
    provider.buildScan(queryExecution.optimizedPlan)
    // TODO cache?
  }

}



