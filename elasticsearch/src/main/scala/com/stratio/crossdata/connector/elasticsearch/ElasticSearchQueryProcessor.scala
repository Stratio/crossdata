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
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s._
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchConnectionUtils._
import org.apache.spark.sql.catalyst.expressions.{Attribute, Literal}
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.{Limit, LogicalPlan}
import org.apache.spark.sql.{Row, sources}
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter.{BaseLogicalPlan, FilterReport, ProjectReport, SimpleLogicalPlan}
import org.apache.spark.sql.sources.{CatalystToCrossdataAdapter, Filter => SourceFilter}
import org.apache.spark.sql.types.{StructField, StructType}
import org.elasticsearch.action.search.SearchResponse

import scala.util.{Failure, Try}

object ElasticSearchQueryProcessor {

  def apply(logicalPlan: LogicalPlan, parameters: Map[String, String], schemaProvided: Option[StructType] = None)
                                          = new ElasticSearchQueryProcessor(logicalPlan, parameters, schemaProvided)
}

/**
 * Process the logicalPlan to generate the query results
 *
 * @param logicalPlan [[LogicalPlan]]] to be executed
 * @param parameters ElasticSearch Configuration Parameters
 * @param schemaProvided Spark used defined schema
 */
class ElasticSearchQueryProcessor(val logicalPlan: LogicalPlan, val parameters: Map[String, String],
                                  val schemaProvided: Option[StructType] = None) extends SparkLoggerComponent {

  type Limit = Option[Int]

  /**
   * Executes the [[LogicalPlan]]] and query the ElasticSearch database
    *
    * @return the query result
   */
  def execute(): Option[Array[Row]] = {

    def tryRows(requiredColumns: Seq[Attribute], finalQuery: SearchDefinition, esClient: ElasticClient): Try[Array[Row]] = {
      val rows: Try[Array[Row]] = Try {
        val resp: SearchResponse = esClient.execute(finalQuery).await.original
        if (resp.getShardFailures.length > 0) {
          val errors = resp.getShardFailures map { failure => failure.reason() }
          throw new RuntimeException(errors mkString("Errors from ES:", ";\n", ""))
        } else {
          ElasticSearchRowConverter.asRows(schemaProvided.get, resp.getHits.getHits, requiredColumns)
        }
      }
      rows
    }

    val result: Try[Array[Row]] = validatedNativePlan.map {
      case (baseLogicalPlan, limit) =>
        val requiredColumns = baseLogicalPlan match {
          case SimpleLogicalPlan(projects, _, _, _) =>
            projects
        }

        val filters = baseLogicalPlan.filters
        val (esIndex, esType) = extractIndexAndType(parameters).get

        val finalQuery = buildNativeQuery(requiredColumns, filters, search in esIndex / esType)

        withClientDo(parameters) { esClient =>
          tryRows(requiredColumns, finalQuery, esClient)
        }
    }.getOrElse(Failure(new RuntimeException("Invalid native plan")))

    result.toOption
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

    import scala.collection.JavaConversions._

    val searchFilters = sFilters.collect {
      case sources.EqualTo(attribute, value) => termQuery(attribute, value)
      case sources.GreaterThan(attribute, value) => rangeQuery(attribute).from(value).includeLower(false)
      case sources.GreaterThanOrEqual(attribute, value) => rangeQuery(attribute).gte(value.toString)
      case sources.LessThan(attribute, value) => rangeQuery(attribute).to(value).includeUpper(false)
      case sources.LessThanOrEqual(attribute, value) => rangeQuery(attribute).lte(value.toString)
      case sources.In(attribute, value) => termsQuery(attribute, value.map(_.asInstanceOf[AnyRef]): _*)
      case sources.IsNotNull(attribute) => existsQuery(attribute)
      case sources.IsNull(attribute) => must(not(existsQuery(attribute)))
    }

    val matchQuery = query bool must(matchers)

    val finalQuery = if (searchFilters.isEmpty)
      matchQuery
    else matchQuery postFilter bool {
      must(searchFilters)
    }

    log.debug("LogicalPlan transformed to the Elasticsearch query:" + finalQuery.toString())
    finalQuery

  }

  private def selectFields(fields: Seq[Attribute], query: SearchDefinition): SearchDefinition = {
      val subDocuments = schemaProvided.toSeq flatMap {
        _.fields collect {
          case StructField(name, _: StructType, _, _) => name
        }
      }
      val stringFields: Seq[String] = fields.view map (_.name) filterNot (subDocuments contains _)

      val fieldsQuery = query.fields(stringFields.toList: _*)

      if(stringFields.size != fields.size)
        fieldsQuery.sourceInclude(subDocuments: _*).sourceExclude(stringFields:_*)
      else fieldsQuery
  }


  def validatedNativePlan: Option[(BaseLogicalPlan, Limit)] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findProjectsFilters(lplan: LogicalPlan): Option[BaseLogicalPlan] = {
      lplan match {

        case Limit(_, child) =>
          findProjectsFilters(child)

        case PhysicalOperation(projectList, filterList, _) =>
          CatalystToCrossdataAdapter.getConnectorLogicalPlan(logicalPlan, projectList, filterList) match {
            case (_, ProjectReport(exprIgnored), FilterReport(filtersIgnored, _)) if filtersIgnored.nonEmpty || exprIgnored.nonEmpty =>
              None
            case (basePlan, _, _) =>
              Some(basePlan)
          }
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
