/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License")
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

package org.apache.spark.sql

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan


private[sql] object CrossdataFrame {
  def apply(sqlContext: SQLContext, logicalPlan: LogicalPlan): DataFrame = {
    new CrossdataFrame(sqlContext, logicalPlan)
  }
}

private[sql] class CrossdataFrame(@transient override val sqlContext: SQLContext,
                                @transient override val queryExecution: SQLContext#QueryExecution) extends DataFrame(sqlContext, queryExecution) {

  def this(sqlContext: SQLContext, logicalPlan: LogicalPlan) = {
    this(sqlContext, {
      val qe = sqlContext.executePlan(logicalPlan)
      if (sqlContext.conf.dataFrameEagerAnalysis) {
        qe.assertAnalyzed() // This should force analysis and throw errors if there are any
      }
      qe
    })
  }
}



