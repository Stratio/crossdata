package com.stratio.crossdata.connector.elasticsearch


import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s._
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.{Limit, LogicalPlan}
import org.apache.spark.sql.sources.{CatalystToCrossdataAdapter, Filter => SourceFilter}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, sources}
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object ElasticSearchQueryProcessor {
  def apply(logicalPlan: LogicalPlan, parameters: Map[String, String], schemaProvided: Option[StructType] = None) = new ElasticSearchQueryProcessor(logicalPlan, parameters, schemaProvided)
}

class ElasticSearchQueryProcessor(val logicalPlan: LogicalPlan, val parameters: Map[String, String], val schemaProvided: Option[StructType] = None) extends Logging {

  def execute(): Option[Array[Row]] = {
    validatedNativePlan.map { case (requiredColumns, filters, limit) =>

      val (esIndex, esType) = {
        val resource = parameters.get(ES_RESOURCE).get.split("/")
        (resource(0), resource(1))
      }

      val host:String = parameters.getOrElse(ES_NODES, ES_NODES_DEFAULT)
      val port:Int = parameters.getOrElse("es.nativePort", 9300).toString.toInt
      val clusterName:String = parameters.get("es.cluster").get

      val finalQuery = buildNativeQuery(requiredColumns, filters, search in esIndex / esType)

      val settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build()
      val client = ElasticClient.remote(settings, host, port)

      val resp:RichSearchResponse = client.execute( finalQuery ).await

      ElasticSearchRowConverter.asRows(schemaProvided.get, resp.hits)
    }
  }

  def buildNativeQuery(requiredColums: Array[String], filters: Array[SourceFilter], query: SearchDefinition): SearchDefinition = {
    val queryWithFilters = filtersToDBObject(filters, query)
    selectFields(requiredColums, queryWithFilters)
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

    //    sFilters.foreach {
    //      case sources.EqualTo(attribute, value) =>
    //         (new TermsFilterDefinition(attribute, value))
    ////      case sources.GreaterThan(attribute, value) =>
    //        queryBuilder.put(attribute).greaterThan(convertToStandardType(value))
    //      case sources.GreaterThanOrEqual(attribute, value) =>
    //        queryBuilder.put(attribute).greaterThanEquals(convertToStandardType(value))
    //      case sources.In(attribute, values) =>
    //        queryBuilder.put(attribute).in(values.map(convertToStandardType))
    //      case sources.LessThan(attribute, value) =>
    //        queryBuilder.put(attribute).lessThan(convertToStandardType(value))
    //      case sources.LessThanOrEqual(attribute, value) =>
    //        queryBuilder.put(attribute).lessThanEquals(convertToStandardType(value))
    //      case sources.IsNull(attribute) =>

    //        queryBuilder.put(attribute).is(null)
    //      case sources.IsNotNull(attribute) =>
    //        queryBuilder.put(attribute).notEquals(null)
    //      case sources.And(leftFilter, rightFilter) =>
    //        queryBuilder.and(filtersToDBObject(Array(leftFilter)), filtersToDBObject(Array(rightFilter)))
    //      case sources.Or(leftFilter, rightFilter) =>
    //        queryBuilder.or(filtersToDBObject(Array(leftFilter)), filtersToDBObject(Array(rightFilter)))
    //      case sources.StringStartsWith(attribute, value) =>
    //        queryBuilder.put(attribute).regex(Pattern.compile("^" + value + ".*$"))
    //      case sources.StringEndsWith(attribute, value) =>
    //        queryBuilder.put(attribute).regex(Pattern.compile("^.*" + value + "$"))
    //      case sources.StringContains(attribute, value) =>
    //        queryBuilder.put(attribute).regex(Pattern.compile(".*" + value + ".*"))
    //      // TODO Not filter
    //    }

  }

  private def selectFields(fields: Array[String], query: SearchDefinition): SearchDefinition = {

    query
    //      if (fields.isEmpty) query.clone()
    //      else fields.toList.filterNot(_ == "_id").map(_ -> 1) ::: {
    //        List("_id" -> fields.find(_ == "_id").fold(0)(_ => 1))
    //      }
  }


  def validatedNativePlan: Option[(Array[String], Array[SourceFilter], Option[Int])] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findProjectsFilters(lplan: LogicalPlan): (Array[String], Array[SourceFilter], Boolean) = {
      lplan match {
        case Limit(_, child) => findProjectsFilters(child)
        case PhysicalOperation(projectList, filterList, _) => CatalystToCrossdataAdapter.getFilterProject(logicalPlan, projectList, filterList)
      }
    }

    val (projects, filters, filtersIgnored) = findProjectsFilters(logicalPlan)

    if (filtersIgnored || !checkNativeFilters(filters)) {
      None
    } else {
      Some(projects, filters, limit)
    }
  }

  private[this] def checkNativeFilters(filters: Array[SourceFilter]): Boolean = filters.forall {
    case _: sources.EqualTo => true
    //    case _: sources.In => true
    //    case _: sources.LessThan => true
    //    case _: sources.GreaterThan => true
    //    case _: sources.LessThanOrEqual => true
    //    case _: sources.GreaterThanOrEqual => true
    //    case _: sources.IsNull => true
    //    case _: sources.IsNotNull => true
    //    case _: sources.StringStartsWith => true
    //    case _: sources.StringEndsWith => true
    //    case _: sources.StringContains => true
    //    case sources.And(left, right) => checkNativeFilters(Array(left, right))
    //    case sources.Or(left, right) => checkNativeFilters(Array(left, right))
    //    // TODO add more filters (Not?)
    case _ => false

  }
}