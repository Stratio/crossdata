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
package org.apache.spark.sql.crossdata


import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.QueryExecution

/**
  * The primary workflow for executing relational queries using Spark.  Designed to allow easy
  * access to the intermediate phases of query execution for developers.
  *
  * While this is not a public class, we should avoid changing the function names for the sake of
  * changing them, because a lot of developers use the feature for debugging.
  */
class XDQueryExecution(sqlContext: SQLContext, logical: LogicalPlan) extends QueryExecution(sqlContext, logical){

  override lazy val analyzed: LogicalPlan = sqlContext.analyzer.execute(logical)

  // NOTE authorize and return the analyzed plan or Exception
  lazy val authorized: LogicalPlan = {
    analyzed
    // TODO do whatever to authorize
    // TODO add proxy user
  }

  // NOTE use authorized instead of analyzed
  override lazy val withCachedData: LogicalPlan = {
    assertAnalyzed()
    sqlContext.cacheManager.useCachedData(authorized)
  }

  // TODO executedPlan is always used => lazy val executedPlan: SparkPlan = sqlContext.prepareForExecution.execute(sparkPlan)

  // TODO but toRdd is not used for all cases ( collect() use SparkPlan::doExecute() => override every sparkPlan => envelopeSparkPlan (KerberRDD => add something)

  // TODO nativeExecution => use a SparkPlan (NativePlan) wrapping the real plan whose executeCollect tries to resolve the query natively; otherwise, fallback to Spark the wrapped plan
}
