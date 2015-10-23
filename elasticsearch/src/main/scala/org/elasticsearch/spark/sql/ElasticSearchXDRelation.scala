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
package org.elasticsearch.spark.sql

import com.stratio.crossdata.connector.NativeScan
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchQueryProcessor
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}


/**
 * ElasticSearchXDRelation inherits from <code>ElasticsearchRelation</code>
 * and adds the NativeScan support to make Native Queries from the XDContext
 *
 * @param parameters Configuration form ElasticSearch
 * @param sqlContext Spark SQL Context
 * @param userSchema Spark User Defined Schema
 */
class ElasticSearchXDRelation(parameters: Map[String, String], sqlContext: SQLContext, userSchema: Option[StructType] = None)
  extends ElasticsearchRelation(parameters, sqlContext, userSchema) with NativeScan with Logging {

  /**
   * Build and Execute a NativeScan for the [[LogicalPlan]] provided.
   * @param optimizedLogicalPlan the [[LogicalPlan]] to be executed
   * @return a list of Spark [[Row]] with the [[LogicalPlan]] execution result.
   */
  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = {
    logDebug(s"Processing ${optimizedLogicalPlan.toString()}")
    val queryExecutor = ElasticSearchQueryProcessor(optimizedLogicalPlan, parameters, userSchema)
    queryExecutor.execute()
  }


  /**
   * Checks the ability to execute a [[LogicalPlan]].
   *
   * @param logicalStep isolated plan
   * @param wholeLogicalPlan the whole DataFrame tree
   * @return whether the logical step within the entire logical plan is supported
   */
  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = logicalStep match {
    case ln: LeafNode => true // TODO leafNode == LogicalRelation(xdSourceRelation)
    case un: UnaryNode => un match {
      case Limit(_, _) | Project(_, _) | Filter(_, _) => false //TODO add support to others
      case _ => false

    }
    case unsupportedLogicalPlan => false //TODO log.debug(s"LogicalPlan $unsupportedLogicalPlan cannot be executed natively");
  }
}
