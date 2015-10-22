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
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s._
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.expressions.{Attribute, Literal}
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.{Limit, LogicalPlan}
import org.apache.spark.sql.crossdata.execution.NativeUDF
import org.apache.spark.sql.sources.{CatalystToCrossdataAdapter, Filter => SourceFilter}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, sources}
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object ElasticSearchQueryProcessor {
  def apply(logicalPlan: LogicalPlan, parameters: Map[String, String], schemaProvided: Option[StructType] = None) = new ElasticSearchQueryProcessor(logicalPlan, parameters, schemaProvided)
}

/**
 * Process the logicalPlan to generate the query results
 *
 * @param logicalPlan [[LogicalPlan]]] to be executed
 * @param parameters ElasticSearch Configuration Parameters
 * @param schemaProvided Spark used defined schema
 */
class ElasticSearchQueryProcessor(val logicalPlan: LogicalPlan, val parameters: Map[String, String], val schemaProvided: Option[StructType] = None) extends Logging {

  /**
   * Executes the [[LogicalPlan]]] and query the ElaticSearch database
   * @return the query result
   */
  def execute(): Option[Array[Row]] = {
    validatedNativePlan.map { case (requiredColumns, filters, limit) =>

      val (esIndex, esType) = {
        val resource = parameters.get(ES_RESOURCE).get.split("/")
        (resource(0), resource(1))
      }

      val host: String = parameters.getOrElse(ES_NODES, ES_NODES_DEFAULT)
      val port: Int = parameters.getOrElse("es.nativePort", 9300).toString.toInt
      val clusterName: String = parameters.get("es.cluster").get

      val finalQuery = buildNativeQuery(requiredColumns, filters, search in esIndex / esType)

      val settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build()
      val client = ElasticClient.remote(settings, host, port)

      val resp: RichSearchResponse = client.execute(finalQuery).await

      ElasticSearchRowConverter.asRows(schemaProvided.get, resp.hits)
    }
  }

  def buildNativeQuery(requiredColumns: Array[Attribute], filters: Array[SourceFilter], query: SearchDefinition): SearchDefinition = {
    val queryWithFilters = filtersToDBObject(filters, query)
    selectFields(requiredColumns, queryWithFilters)
  }

  private def filtersToDBObject(sFilters: Array[SourceFilter], query: SearchDefinition): SearchDefinition = {

    val result = (query /: sFilters) { (prev, next) =>
      prev postFilter (
        next match {
          case sources.EqualTo(attribute, value) => termFilter(attribute, value)
          case sources.GreaterThan(attribute, value) => rangeFilter(attribute).gt(value)
        }
        )
    }

    log.debug(result.toString())

    result
  }

  private def selectFields(fields: Array[Attribute], query: SearchDefinition): SearchDefinition = {
    query //TODO build projection
  }


  def validatedNativePlan: Option[(Array[Attribute], Array[SourceFilter], Option[Int])] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findProjectsFilters(lplan: LogicalPlan): (Array[Attribute], Array[SourceFilter], Map[Attribute, NativeUDF], Boolean) = {
      lplan match {
        case Limit(_, child) => findProjectsFilters(child)
        case PhysicalOperation(projectList, filterList, _) => CatalystToCrossdataAdapter.getFilterProject(logicalPlan, projectList, filterList)
      }
    }

    val (projects, filters, udf, filtersIgnored) = findProjectsFilters(logicalPlan)

    if (filtersIgnored || !checkNativeFilters(filters)) {
      None
    } else {
      Option(projects, filters, limit)
    }
  }

  private[this] def checkNativeFilters(filters: Array[SourceFilter]): Boolean = filters.forall {
    case _: sources.EqualTo => true
        case _: sources.In => true
        case _: sources.LessThan => true
        case _: sources.GreaterThan => true
        case _: sources.LessThanOrEqual => true
        case _: sources.GreaterThanOrEqual => true
        case _: sources.IsNull => true
        case _: sources.IsNotNull => true
        case _: sources.StringStartsWith => true
        case _: sources.StringEndsWith => true
        case _: sources.StringContains => true
        case sources.And(left, right) => checkNativeFilters(Array(left, right))
        case sources.Or(left, right) => checkNativeFilters(Array(left, right))
        // TODO add more filters (Not?)
    case _ => false

  }
}