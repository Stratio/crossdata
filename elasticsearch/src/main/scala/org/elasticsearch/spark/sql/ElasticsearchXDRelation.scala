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
import javax.xml.bind.DatatypeConverter

import com.stratio.crossdata.connector.NativeScan
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchQueryProcessor
import org.apache.spark.{Logging, Partition, SparkContext, TaskContext}
import org.apache.spark.sql.catalyst.plans.logical.{LeafNode, LogicalPlan, Project, UnaryNode, Filter => FilterPlan}
import org.apache.spark.sql.catalyst.plans.logical.Limit
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}
import org.elasticsearch.hadoop.cfg.{ConfigurationOptions, InternalConfigurationOptions}
import org.elasticsearch.hadoop.rest.RestService.PartitionDefinition
import org.elasticsearch.hadoop.serialization.json.JacksonJsonGenerator
import org.elasticsearch.hadoop.util.{FastByteArrayOutputStream, IOUtils, StringUtils}
import org.elasticsearch.spark.rdd.EsPartition

import scala.collection.mutable
import scala.collection.mutable.LinkedHashSet

import java.util.{Date => UDate, Calendar, Locale}

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
                       @transient sc: SparkContext,
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

  private object CopiedCode {

    def isClass(obj: Any, className: String) = {
    className.equals(obj.getClass().getName())
  }

    def extract(value: Any): String = {
    extract(value, true, false)
  }

    def extractAsJsonArray(value: Any): String = { extract(value, true, true) }

    def extractMatchArray(attribute: String, ar: Array[Any]): String = {
    // use a set to avoid duplicate values
    // especially since Spark conversion might turn each user param into null
    val numbers = LinkedHashSet.empty[AnyRef]
    val strings = LinkedHashSet.empty[AnyRef]

    // move numbers into a separate list for a terms query combined with a bool
    for (i <- ar) i.asInstanceOf[AnyRef] match {
      case null => // ignore
      case n: Number => numbers += extract(i, false, false)
      case _ => strings += extract(i, false, false)
    }

    if (numbers.isEmpty) {
      if (strings.isEmpty) {
        return StringUtils.EMPTY
      }
      return s"""{"query":{"match":{"$attribute":${strings.mkString("\"", " ", "\"")}}}}"""
      //s"""{"query":{"$attribute":${strings.mkString("\"", " ", "\"")}}}"""
    }
    else {
      // translate the numbers into a terms query
      val str =
      s"""{"terms":{"$attribute":${numbers.mkString("[", ",", "]")}}}"""
      if (strings.isEmpty) return str
      // if needed, add the strings as a match query
      else return str + s""",{"query":{"match":{"$attribute":${strings.mkString("\"", " ", "\"")}}}}"""
    }
  }

    def extract(value: Any, inJsonFormat: Boolean, asJsonArray: Boolean): String = {
    // common-case implies primitives and String so try these before using the full-blown ValueWriter
    value match {
      case null => "null"
      case u: Unit => "null"
      case b: Boolean => b.toString
      case by: Byte => by.toString
      case s: Short => s.toString
      case i: Int => i.toString
      case l: Long => l.toString
      case f: Float => f.toString
      case d: Double => d.toString
      case bd: BigDecimal => bd.toString
      case _: Char |
           _: String |
           _: Array[Byte] => if (inJsonFormat) StringUtils.toJsonString(value.toString) else value.toString()
      // handle Timestamp also
      case dt: UDate => {
        val cal = Calendar.getInstance()
        cal.setTime(dt)
        val str = DatatypeConverter.printDateTime(cal)
        if (inJsonFormat) StringUtils.toJsonString(str) else str
      }
      case ar: Array[Any] =>
        if (asJsonArray) (for (i <- ar) yield extract(i, true, false)).distinct.mkString("[", ",", "]")
        else (for (i <- ar) yield extract(i, false, false)).distinct.mkString("\"", " ", "\"")
      // new in Spark 1.4
      case utf if (isClass(utf, "org.apache.spark.sql.types.UTF8String")
        // new in Spark 1.5
        || isClass(utf, "org.apache.spark.unsafe.types.UTF8String"))
      => if (inJsonFormat) StringUtils.toJsonString(utf.toString()) else utf.toString()
      case a: AnyRef => {
        val storage = new FastByteArrayOutputStream()
        val generator = new JacksonJsonGenerator(storage)
        valueWriter.write(a, generator)
        generator.flush()
        generator.close()
        storage.toString()
      }
    }
  }

    // string interpolation FTW
    def translateFilter(filter: Filter, strictPushDown: Boolean): String = {
    // the pushdown can be strict - i.e. use only filters and thus match the value exactly (works with non-analyzed)
    // or non-strict meaning queries will be used instead that is the filters will be analyzed as well
    filter match {

      case EqualTo(attribute, value) => {
        // if we get a null, translate it into a missing query (we're extra careful - Spark should translate the equals into isMissing anyway)
        if (value == null || value == None || value == Unit) {
          return s"""{"missing":{"field":"$attribute"}}"""
        }

        if (strictPushDown)
          s"""{"term":{"$attribute":${extract(value)}}}"""
        else
          s"""{"query":{"match":{"$attribute":${extract(value)}}}}"""
      }
      case GreaterThan(attribute, value) => s"""{"range":{"$attribute":{"gt" :${extract(value)}}}}"""
      case GreaterThanOrEqual(attribute, value) => s"""{"range":{"$attribute":{"gte":${extract(value)}}}}"""
      case LessThan(attribute, value) => s"""{"range":{"$attribute":{"lt" :${extract(value)}}}}"""
      case LessThanOrEqual(attribute, value) => s"""{"range":{"$attribute":{"lte":${extract(value)}}}}"""
      case In(attribute, values) => {
        // when dealing with mixed types (strings and numbers) Spark converts the Strings to null (gets confused by the type field)
        // this leads to incorrect query DSL hence why nulls are filtered
        val filtered = values filter (_ != null)
        if (filtered.isEmpty) {
          return ""
        }

        // further more, match query only makes sense with String types so for other types apply a terms query (aka strictPushDown)
        val attrType = lazySchema.struct(attribute).dataType
        val isStrictType = attrType match {
          case DateType |
               TimestampType => true
          case _ => false
        }

        if (!strictPushDown && isStrictType) {
          if (Utils.LOGGER.isDebugEnabled()) {
            Utils.LOGGER.debug(s"Attribute $attribute type $attrType not suitable for match query; using terms (strict) instead")
          }
        }

        if (strictPushDown || isStrictType)
          s"""{"terms":{"$attribute":${extractAsJsonArray(filtered)}}}"""
        else
          s"""{"or":{"filters":[${extractMatchArray(attribute, filtered)}]}}"""
      }
      case IsNull(attribute) => s"""{"missing":{"field":"$attribute"}}"""
      case IsNotNull(attribute) => s"""{"exists":{"field":"$attribute"}}"""
      case And(left, right) => s"""{"and":{"filters":[${translateFilter(left, strictPushDown)}, ${translateFilter(right, strictPushDown)}]}}"""
      case Or(left, right) => s"""{"or":{"filters":[${translateFilter(left, strictPushDown)}, ${translateFilter(right, strictPushDown)}]}}"""
      case Not(filterToNeg) => s"""{"not":{"filter":${translateFilter(filterToNeg, strictPushDown)}}}"""

      // the filter below are available only from Spark 1.3.1 (not 1.3.0)

      //
      // String Filter notes:
      //
      // the DSL will be quite slow (linear to the number of terms in the index) but there's no easy way around them
      // we could use regexp filter however it's a bit overkill and there are plenty of chars to escape
      // s"""{"regexp":{"$attribute":"$value.*"}}"""
      // as an alternative we could use a query string but still, the analyzed / non-analyzed is there as the DSL is slightly more complicated
      // s"""{"query":{"query_string":{"default_field":"$attribute","query":"$value*"}}}"""
      // instead wildcard query is used, with the value lowercased (to match analyzed fields)

      case f: Product if isClass(f, "org.apache.spark.sql.sources.StringStartsWith") => {
        var arg = f.productElement(1).toString()
        if (!strictPushDown) {
          arg = arg.toLowerCase(Locale.ROOT)
        }
        s"""{"query":{"wildcard":{"${f.productElement(0)}":"$arg*"}}}"""
      }

      case f: Product if isClass(f, "org.apache.spark.sql.sources.StringEndsWith") => {
        var arg = f.productElement(1).toString()
        if (!strictPushDown) {
          arg = arg.toLowerCase(Locale.ROOT)
        }
        s"""{"query":{"wildcard":{"${f.productElement(0)}":"*$arg"}}}"""
      }

      case f: Product if isClass(f, "org.apache.spark.sql.sources.StringContains") => {
        var arg = f.productElement(1).toString()
        if (!strictPushDown) {
          arg = arg.toLowerCase(Locale.ROOT)
        }
        s"""{"query":{"wildcard":{"${f.productElement(0)}":"*$arg*"}}}"""
      }

      // the filters below are available only from Spark 1.5.0

      case f: Product if isClass(f, "org.apache.spark.sql.sources.EqualNullSafe") => {
        var arg = extract(f.productElement(1))
        if (strictPushDown)
          s"""{"term":{"${f.productElement(0)}":$arg}}"""
        else
          s"""{"query":{"match":{"${f.productElement(0)}":$arg}}}"""
      }

      case _ => ""
    }
  }

    def createDSLFromFilters(filters: Array[Filter], strictPushDown: Boolean) = {
      filters.map(filter => translateFilter(filter, strictPushDown)).filter(query => StringUtils.hasText(query))
    }

  }

  // PrunedFilteredScan
  override def buildScan(requiredColumns: Array[String], filters: Array[Filter]) = {
    import CopiedCode._
    val paramWithScan = mutable.LinkedHashMap[String, String]() ++ parameters
    paramWithScan += (InternalConfigurationOptions.INTERNAL_ES_TARGET_FIELDS ->
      StringUtils.concatenate(requiredColumns.asInstanceOf[Array[Object]], StringUtils.DEFAULT_DELIMITER))

    // scroll fields only apply to source fields; handle metadata separately
    if (cfg.getReadMetadata) {
      val metadata = cfg.getReadMetadataField
      // if metadata is not selected, don't ask for it
      if (!requiredColumns.contains(metadata)) {
        paramWithScan += (ConfigurationOptions.ES_READ_METADATA -> false.toString())
      }
    }

    if (filters != null && filters.size > 0) {
      if (Utils.isPushDown(cfg)) {
        if (Utils.LOGGER.isDebugEnabled()) {
          Utils.LOGGER.debug(s"Pushing down filters ${filters.mkString("[", ",", "]")}")
        }
        val filterString = createDSLFromFilters(filters, Utils.isPushDownStrict(cfg))

        if (Utils.LOGGER.isTraceEnabled()) {
          Utils.LOGGER.trace(s"Transformed filters into DSL ${filterString.mkString("[", ",", "]")}")
        }
        paramWithScan += (InternalConfigurationOptions.INTERNAL_ES_QUERY_FILTERS -> IOUtils.serializeToBase64(filterString))
      }
      else {
        if (Utils.LOGGER.isTraceEnabled()) {
          Utils.LOGGER.trace("Push-down is disabled; ignoring Spark filters...")
        }
      }
    }

    new ScalaXDEsRowRDD(sqlContext.sparkContext, paramWithScan, lazySchema, userSchema)
  }

  /*override def buildScan(requiredColumns: Array[String], filters: Array[Filter]) = {
    val rdd = super.buildScan(requiredColumns, filters)
    userSchema map { uschema: StructType =>
      val name2expectedType: Map[String, DataType] = uschema.map(field => field.name -> field.dataType).toMap
      //lazySchema.struct map
      val transformations: Map[String, Any => Any] = name2expectedType.collect {
        case (k, _: DateType) => k -> ((timestamp: Any) => new Date(timestamp.asInstanceOf[Timestamp].getTime))
      }
      rdd.map {
        case esRow: ScalaEsRow => esRow : Row //PERFORM TRANSFORMATIONS HERE
      }
    } getOrElse rdd
  }*/

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
