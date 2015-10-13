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
package com.stratio.crossdata.connector.mongodb

import java.util.regex.Pattern

import com.mongodb.casbah.Imports._
import com.mongodb.{DBObject, QueryBuilder}
import com.stratio.provider.Config
import com.stratio.provider.mongodb.MongodbRelation._
import com.stratio.provider.mongodb.schema.MongodbRowConverter
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.sources
import org.apache.spark.sql.sources.{CatalystToCrossdataAdapter, Filter => SourceFilter}
import org.apache.spark.sql.types.StructType

import org.apache.spark.sql.Row

object MongoQueryProcessor {

  val DefaultLimit = 10000
  type ColumnName = String

  def apply(logicalPlan: LogicalPlan, config: Config, schemaProvided: Option[StructType] = None) = new MongoQueryProcessor(logicalPlan, config, schemaProvided)

  def buildNativeQuery(requiredColums: Array[ColumnName], filters: Array[SourceFilter]): (DBObject, DBObject) =
    (filtersToDBObject(filters), selectFields(requiredColums))

  private def filtersToDBObject(sFilters: Array[SourceFilter]): DBObject = {
    val queryBuilder: QueryBuilder = QueryBuilder.start

    sFilters.foreach {
      case sources.EqualTo(attribute, value) =>
        queryBuilder.put(attribute).is(convertToStandardType(value))
      case sources.GreaterThan(attribute, value) =>
        queryBuilder.put(attribute).greaterThan(convertToStandardType(value))
      case sources.GreaterThanOrEqual(attribute, value) =>
        queryBuilder.put(attribute).greaterThanEquals(convertToStandardType(value))
      case sources.In(attribute, values) =>
        queryBuilder.put(attribute).in(values.map(convertToStandardType))
      case sources.LessThan(attribute, value) =>
        queryBuilder.put(attribute).lessThan(convertToStandardType(value))
      case sources.LessThanOrEqual(attribute, value) =>
        queryBuilder.put(attribute).lessThanEquals(convertToStandardType(value))
      case sources.IsNull(attribute) =>
        queryBuilder.put(attribute).is(null)
      case sources.IsNotNull(attribute) =>
        queryBuilder.put(attribute).notEquals(null)
      case sources.And(leftFilter, rightFilter) =>
        queryBuilder.and(filtersToDBObject(Array(leftFilter)), filtersToDBObject(Array(rightFilter)))
      case sources.Or(leftFilter, rightFilter) =>
        queryBuilder.or(filtersToDBObject(Array(leftFilter)), filtersToDBObject(Array(rightFilter)))
      case sources.StringStartsWith(attribute, value) =>
        queryBuilder.put(attribute).regex(Pattern.compile("^" + value + ".*$"))
      case sources.StringEndsWith(attribute, value) =>
        queryBuilder.put(attribute).regex(Pattern.compile("^.*" + value + "$"))
      case sources.StringContains(attribute, value) =>
        queryBuilder.put(attribute).regex(Pattern.compile(".*" + value + ".*"))
      // TODO Not filter
    }
    queryBuilder.get
  }

  private def convertToStandardType(value: Any): Any = value

  /**
   *
   * Prepared DBObject used to specify required fields in mongodb 'find'
   * @param fields Required fields
   * @return A mongodb object that represents required fields.
   */
  private def selectFields(fields: Array[String]): DBObject =
    MongoDBObject(
      if (fields.isEmpty) List()
      else fields.toList.filterNot(_ == "_id").map(_ -> 1) ::: {
        List("_id" -> fields.find(_ == "_id").fold(0)(_ => 1))
      })


}


// TODO logs, doc, tests
class MongoQueryProcessor(logicalPlan: LogicalPlan, config: Config, schemaProvided: Option[StructType] = None) extends Logging {

  import MongoQueryProcessor._

  def execute(): Option[Array[Row]] = {

    // TODO convert to Spark result using an iterator with batches instead of an array
    if (schemaProvided.isEmpty) {
      None
    } else {
      try {
        validatedNativePlan.map { case (requiredColumns, filters, limit) =>
          val (mongoFilters, mongoRequiredColumns) = buildNativeQuery(requiredColumns, filters)
          val resultSet = MongodbConnection.withCollectionDo(config) { collection =>
            logDebug(s"Executing native query: filters => $mongoFilters projects => $mongoRequiredColumns")
            val cursor = collection.find(mongoFilters, mongoRequiredColumns)
            val result = cursor.limit(limit.getOrElse(DefaultLimit)).toArray[DBObject]
            cursor.close()
            result
          }
          sparkResultFromMongodb(requiredColumns, schemaProvided.get, resultSet)
        }
      } catch {
        case exc: Exception =>
          log.warn(s"Exception executing the native query $logicalPlan", exc.getMessage); None
      }
    }

  }


  def validatedNativePlan: Option[(Array[ColumnName], Array[SourceFilter], Option[Int])] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findProjectsFilters(lplan: LogicalPlan): (Array[ColumnName], Array[SourceFilter], Boolean) = {
      lplan match {
        case Limit(_, child) => findProjectsFilters(child)
        case PhysicalOperation(projectList, filterList, _) =>
          val (prjcts, fltrs, _, fltrsig) =
            CatalystToCrossdataAdapter.getFilterProject(logicalPlan, projectList, filterList)
          (prjcts.map(_.name), fltrs, fltrsig)
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

  private[this] def sparkResultFromMongodb(requiredColumns: Array[ColumnName], schema: StructType, resultSet: Array[DBObject]): Array[Row] = {
    MongodbRowConverter.asRow(pruneSchema(schema, requiredColumns), resultSet)
  }

}


