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
package org.elasticsearch.spark.sql

import java.sql.{Date, Timestamp}

import com.stratio.crossdata.connector.NativeScan
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchQueryProcessor
import org.apache.spark.{Logging, Partition, SparkContext, TaskContext}
import org.apache.spark.sql.catalyst.plans.logical.{LeafNode, LogicalPlan, Project, UnaryNode, Filter => FilterPlan}
import org.apache.spark.sql.catalyst.plans.logical.Limit
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.elasticsearch.hadoop.rest.RestService.PartitionDefinition
import org.elasticsearch.spark.rdd.EsPartition

import scala.collection.mutable

class ScalaXDEsRowRDDIterator(
                               context: TaskContext,
                               partition: PartitionDefinition,
                               schema: SchemaUtils.Schema,
                               userSchema: Option[StructType]
                             ) extends ScalaEsRowRDDIterator(context, partition, schema) {

  override def createValue(value: Array[Object]): Row = {
    val v: ScalaEsRow = super.createValue(value).asInstanceOf[ScalaEsRow]
    userSchema foreach { uschema: StructType =>

      val name2expectedType: Map[String, DataType] = uschema.map(field => field.name -> field.dataType).toMap
      val transformations: Map[String, Any => Any] = name2expectedType.collect {
        case (k, _: DateType) => k -> ((timestamp: Any) => new Date(timestamp.asInstanceOf[Timestamp].getTime))
      }

      for(
        ((value, name), idx) <- (v.values zip v.rowOrder) zipWithIndex;
        trans <- transformations.get(name)
      ) v.values.update(idx, trans(value))
    }

    v
  }

}

class ScalaXDEsRowRDD(
                       sc: SparkContext,
                       params: mutable.Map[String, String] = mutable.Map.empty,
                       schema: SchemaUtils.Schema,
                       userSchema: Option[StructType]
                     ) extends ScalaEsRowRDD(sc, params, schema) {

  override def compute(split: Partition, context: TaskContext): ScalaEsRowRDDIterator = {
    new ScalaXDEsRowRDDIterator(context, split.asInstanceOf[EsPartition].esPartition, schema, userSchema)
  }

}

/**
 * ElasticSearchXDRelation inherits from <code>ElasticsearchRelation</code>
 * and adds the NativeScan support to make Native Queries from the XDContext
 *
 * @param parameters Configuration form ElasticSearch
 * @param sqlContext Spark SQL Context
 * @param userSchema Spark User Defined Schema
 */
class ElasticsearchXDRelation(parameters: Map[String, String], sqlContext: SQLContext, userSchema: Option[StructType] = None)
  extends ElasticsearchRelation(parameters, sqlContext, userSchema) with NativeScan with Logging {


  override def buildScan(requiredColumns: Array[String], filters: Array[Filter]) = {
    val rdd = super.buildScan(requiredColumns, filters)
    userSchema map { uschema: StructType =>
      val name2expectedType: Map[String, DataType] = uschema.map(field => field.name -> field.dataType).toMap

      val transformations: Map[String, Any => Any] = name2expectedType.collect {
        case (k, _: DateType) => k -> ((timestamp: Any) => new Date(timestamp.asInstanceOf[Timestamp].getTime))
      }
      rdd.map {
        case esRow: ScalaEsRow =>
          lazy val newRow = esRow.copy()
          var hasChanges = false

          for(
            ((value, name), idx) <- (esRow.values zip esRow.rowOrder) zipWithIndex;
            trans <- transformations.get(name)
          ) {
            hasChanges = true
            newRow.values.update(idx, trans(value))
          }

          (if(hasChanges) newRow else esRow) : Row

        case other: Row => other
      }
    } getOrElse rdd
  }

  /**
   * Build and Execute a NativeScan for the [[LogicalPlan]] provided.
    *
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
      case Project(_, _) | FilterPlan(_, _)  => true
      case Limit(_, _)=> false //TODO add support to others
      case _ => false

    }
    case unsupportedLogicalPlan => false //TODO log.debug(s"LogicalPlan $unsupportedLogicalPlan cannot be executed natively");
  }
}
