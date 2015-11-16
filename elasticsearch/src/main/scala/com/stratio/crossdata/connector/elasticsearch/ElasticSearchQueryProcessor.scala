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
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchConnectionUtils._
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.expressions.{Attribute, Literal}
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.{Limit, LogicalPlan}
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter.{BaseLogicalPlan, SimpleLogicalPlan}
import org.apache.spark.sql.sources.{CatalystToCrossdataAdapter, Filter => SourceFilter}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, sources}
import org.elasticsearch.action.search.SearchResponse
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

  type Limit = Option[Int]

  /**
   * Executes the [[LogicalPlan]]] and query the ElasticSearch database
   * @return the query result
   */
  def execute(): Option[Array[Row]] = {
    validatedNativePlan.map { case (baseLogicalPlan, limit) =>
      val requiredColumns = baseLogicalPlan match {
        case SimpleLogicalPlan(projects, _, _) =>
          projects
      }

      val filters = baseLogicalPlan.filters

      val (esIndex, esType) = extractIndexAndType(parameters)

      val finalQuery = buildNativeQuery(requiredColumns, filters, search in esIndex / esType)
      val resp: SearchResponse = buildClient(parameters).execute(finalQuery).await

      ElasticSearchRowConverter.asRows(schemaProvided.get, resp.getHits.getHits, requiredColumns)
    }
  }


  def buildNativeQuery(requiredColumns: Seq[Attribute], filters: Array[SourceFilter], query: SearchDefinition): SearchDefinition = {
    val queryWithFilters = buildFilters(filters, query)
    selectFields(requiredColumns, queryWithFilters)
  }

  private def buildFilters(sFilters: Array[SourceFilter], query: SearchDefinition): SearchDefinition = {

    val matchers = sFilters.collect {
      case sources.StringContains(attribute, value) => termQuery(attribute, value.toLowerCase)
      case sources.StringStartsWith(attribute, value) => prefixQuery(attribute, value.toLowerCase)
    }

    val searchFilters = sFilters.collect {
      case sources.EqualTo(attribute, value) => termFilter(attribute, value)
      case sources.GreaterThan(attribute, value) => rangeFilter(attribute).gt(value)
      case sources.GreaterThanOrEqual(attribute, value) => rangeFilter(attribute).gte(value.toString)
      case sources.LessThan(attribute, value) => rangeFilter(attribute).lt(value)
      case sources.LessThanOrEqual(attribute, value) => rangeFilter(attribute).lte(value.toString)
      case sources.In(attribute, value) => termsFilter(attribute, value.toList: _*)
      case sources.IsNotNull(attribute) => existsFilter(attribute)
      case sources.IsNull(attribute) => missingFilter(attribute)
    }

    val matchQuery = query bool must(matchers)

    val finalQuery = if (searchFilters.isEmpty) matchQuery
    else matchQuery postFilter bool {
      must(searchFilters)
    }

    log.debug("LogicalPlan transformed to the Elasticsearch query:" + finalQuery.toString())
    finalQuery

  }

  private def selectFields(fields: Seq[Attribute], query: SearchDefinition): SearchDefinition = {
      val stringFields: Seq[String] = fields.map(_.name)
      query.fields(stringFields.toList: _*)
  }


  def validatedNativePlan: Option[(BaseLogicalPlan, Limit)] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findProjectsFilters(lplan: LogicalPlan): Option[BaseLogicalPlan] = {
      lplan match {

        case Limit(_, child) =>
          findProjectsFilters(child)

        case PhysicalOperation(projectList, filterList, _) =>
          val (basePlan, filtersIgnored) = CatalystToCrossdataAdapter.getConnectorLogicalPlan(logicalPlan, projectList, filterList)
          if (!filtersIgnored) Some(basePlan) else None
      }
    }

    findProjectsFilters(logicalPlan).collect{ case bp if checkNativeFilters(bp.filters) => (bp, limit) }
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
    case _: sources.StringContains => true
    case sources.And(left, right) => checkNativeFilters(Array(left, right))
    // TODO add more filters (Not?)
    case _ => false

  }
}
