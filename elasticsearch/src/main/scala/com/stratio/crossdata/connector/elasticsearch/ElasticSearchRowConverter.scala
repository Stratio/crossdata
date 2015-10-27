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

import java.sql.Timestamp
import java.util.Date
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.types._
import org.elasticsearch.common.joda.time.DateTime
import org.elasticsearch.search.{SearchHitField, SearchHit}
import org.elasticsearch.search.internal.InternalSearchHitField

object ElasticSearchRowConverter {



  def asRows(schema: StructType, array: Array[SearchHit],requiredFields: Array[Attribute]): Array[Row] = {
    import scala.collection.JavaConverters._
    val schemaMap = schema.map(field => field.name -> field.dataType).toMap

    array map { hit =>
      hitAsRow(hit.fields().asScala.toMap, schemaMap, requiredFields.map(_.name))
    }
  }

  def hitAsRow(
                hitFields: Map[String, SearchHitField],
                schemaMap: Map[String, DataType],
                requiredFields:Array[String]): Row = {
    val values: Seq[Any] = requiredFields.map {
      name =>
          hitFields.get(name).flatMap(v => Option(v)).map(
            toSQL(_, schemaMap(name))).orNull
    }
    Row.fromSeq(values)
  }

  def toSQL(value: SearchHitField, dataType: DataType): Any = {

    Option(value).map { value =>
      (value, dataType) match {
        case _ =>
          //Assure value is mapped to schema constrained type.
          enforceCorrectType(value.getValue, dataType)
      }
    }.orNull
  }



  protected def enforceCorrectType(value: Any, desiredType: DataType): Any ={
    if (value == null) {
      null
    } else {
      desiredType match {
        case StringType => value.toString
        case _ if value == "" => null // guard the non string type
        case IntegerType => toInt(value)
        case LongType => toLong(value)
        case DoubleType => toDouble(value)
        case DecimalType() => toDecimal(value)
        case BooleanType => value.asInstanceOf[Boolean]
        case TimestampType => toTimestamp(value)
        case NullType => null
        case DateType => toDate(value)
        case _ =>
          sys.error(s"Unsupported datatype conversion [${value.getClass}},$desiredType]")
          value
      }
    }
  }

  private def toInt(value: Any): Int = {
    import scala.language.reflectiveCalls
    value match {
      case value: String => value.toInt
      case _ => value.asInstanceOf[ {def toInt: Int}].toInt
    }
  }

  private def toLong(value: Any): Long = {
    value match {
      case value: java.lang.Integer => value.asInstanceOf[Int].toLong
      case value: java.lang.Long => value.asInstanceOf[Long]
    }
  }

  private def toDouble(value: Any): Double = {
    value match {
      case value: java.lang.Integer => value.asInstanceOf[Int].toDouble
      case value: java.lang.Long => value.asInstanceOf[Long].toDouble
      case value: java.lang.Double => value.asInstanceOf[Double]
    }
  }

  private def toDecimal(value: Any): Decimal = {
    value match {
      case value: java.lang.Integer => Decimal(value)
      case value: java.lang.Long => Decimal(value)
      case value: java.math.BigInteger => Decimal(new java.math.BigDecimal(value))
      case value: java.lang.Double => Decimal(value)
      case value: java.math.BigDecimal => Decimal(value)
    }
  }

  private def toTimestamp(value: Any): Timestamp = {
    value match {
      case value: java.util.Date => new Timestamp(value.getTime)
    }
  }

  def toDate(value: Any): Date  = {
    value match {
      case value:String => DateTime.parse(value).toDate
    }
  }
}
