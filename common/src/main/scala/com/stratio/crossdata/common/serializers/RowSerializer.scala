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
package com.stratio.crossdata.common.serializers

import java.sql.Timestamp

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.{GenericRow, GenericRowWithSchema}
import org.apache.spark.sql.types._
import org.json4s.JsonAST.{JNumber, JObject}
import org.json4s.JsonDSL._
import org.json4s._
import org.apache.spark.sql.catalyst.util.{DateTimeUtils, ArrayBasedMapData => ArrayBasedMapDataNotDeprecated, ArrayData => ArrayDataNotDeprecated, MapData => MapDataNotDeprecated}
import org.apache.spark.sql.crossdata.serializers.StructTypeSerializer

import scala.collection.mutable

case class RowSerializer(providedSchema: StructType) extends Serializer[Row] {


  private def deserializeWithSchema(
                                     schema: StructType,
                                     fields: JArray,
                                     includeSchema: Boolean
                                   )(implicit formats: Formats): Row = {

    def extractField(tv: (DataType, JValue)): Any = tv match {
      case (_, JNull) | (NullType, _) => null
      case (StringType, JString(str)) => str
      case (TimestampType, JString(tsStr)) => Timestamp.valueOf(tsStr)
      case (DateType, JString(strDate)) => java.sql.Date.valueOf(strDate)
      case (IntegerType, JInt(v)) => v.toInt
      case (ShortType, JInt(v)) => v.toShort
      case (FloatType, JDouble(v)) => v.toFloat
      case (DoubleType, JDouble(v)) => v.toDouble
      case (LongType, JInt(v)) => v.toLong
      case (_: DecimalType, v: JNumber) =>
        v match {
          case JInt(v) => Decimal(v.toString)
          case JDecimal(v) => Decimal(v)
          case JDouble(v) => Decimal(v)
        }
      case (ByteType, JInt(v)) => v.toByte
      case (BinaryType, JString(binaryStr)) => binaryStr.getBytes
      case (BooleanType, JBool(v)) => v
      case (udt: UserDefinedType[_], jobj) => extractField(udt.sqlType -> jobj)
      case (ArrayType(ty, _), JArray(arr)) =>
        mutable.WrappedArray make arr.map(extractField(ty, _)).toArray
      /* Maps will be serialized as sub-objects so keys are constrained to be strings */
      case (MapType(kt, vt, _), JObject(JField("map", JObject(JField("keys", JArray(mapKeys)) :: JField("values", JArray(mapValues)) :: _) ) :: _)) =>
        val unserKeys = mapKeys map (jval => extractField(kt, jval))
        val unserValues = mapValues map (jval => extractField(vt, jval))
        ArrayBasedMapDataNotDeprecated(unserKeys.toArray, unserValues.toArray)
      case (st: StructType, JObject(JField("values",JArray(values))::_)) =>
        deserializeWithSchema(st, values, true)
    }

    val values: Array[Any] = (schema.view.map(_.dataType) zip fields.arr).map(extractField).toArray

    if(includeSchema) new GenericRowWithSchema(values, schema)
    else new GenericRow(values)

  }

  private def serializeWithSchema(
                                   schema: StructType,
                                   row: Row, includeSchema: Boolean
                                 )(implicit formats: Formats): JValue = {


    def serializeField(tv: (DataType, Any)): JValue = tv match {
      // Cases brought in from Spark's DataFrame JSON serializer (`DataFrame#toJSON`)
      case (_, null) | (NullType, _) => JNull
      case (StringType, v: String) => JString(v)
      case (TimestampType, v: Long) =>
        JString(DateTimeUtils.toJavaTimestamp(v).toString)
      case (TimestampType, v: java.sql.Timestamp) =>
        JString(v.toString)
      case (IntegerType, v: Int) => JInt(v)
      case (ShortType, v: Short) => JInt(v.toInt)
      case (FloatType, v: Float) => JDouble(v)
      case (DoubleType, v: Double) => JDouble(v)
      case (LongType, v: Long) => JInt(v)
      case (_: DecimalType, v: Decimal) => JDecimal(v.toBigDecimal)
      case (_: DecimalType, v: Double) => JDecimal(BigDecimal(v))
      case (_: DecimalType, v: Float) => JDecimal(BigDecimal(v))
      case (ByteType, v: Byte) => JInt(v.toInt)
      case (BinaryType, v: Array[Byte]) => JString(new String(v))
      case (BooleanType, v: Boolean) => JBool(v)
      case (DateType, v: Int) => JString(DateTimeUtils.toJavaDate(v).toString)
      case (DateType, v: java.sql.Date) => JString(v.toString)
      case (udt: UserDefinedType[_], v) => serializeField(udt.sqlType -> v)
      case (ArrayType(ty, _), v) =>
        v match {
          case v: ArrayDataNotDeprecated => JArray(v.array.toList.map(v => Extraction.decompose(v)))
          case v: mutable.WrappedArray[_] => JArray(v.toList.map(v => Extraction.decompose(v)))
        }
      case (MapType(kt, vt, _), v: MapDataNotDeprecated) =>
        /* Maps will be serialized as sub-objects so keys are constrained to be strings */
        val serKeys = v.keyArray().array.map(v => serializeField(kt -> v))
        val serValues = v.valueArray.array.map(v => serializeField(vt -> v))
        JField("map",
          JObject(
            JField("keys", JArray(serKeys.toList)),
            JField("values", JArray(serValues.toList))
          )
        )
      case (st: StructType, v: Row) => serializeWithSchema(st, v, true)
    }

    val valuesList: List[JValue] = for((t, idx) <- schema.zipWithIndex.toList) yield
      serializeField(t.dataType -> row.get(idx))


    val justValues: JObject = ("values" -> JArray(valuesList))

    if(includeSchema) justValues ~ ("schema" -> Extraction.decompose(schema))
    else justValues

  }

  override def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Row] = {
    case (_, JObject(JField("values", JArray(values))::Nil)) => deserializeWithSchema(providedSchema, values, false)
    case (_, JObject(JField("values", JArray(values))::JField("schema", jschema)::Nil)) =>
      val _format = format;
      {
        implicit val format = _format + StructTypeSerializer
        deserializeWithSchema(jschema.extract[StructType], values, true)
      }
  }

  override def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case row: Row =>
      serializeWithSchema(Option(row.schema).getOrElse(providedSchema), row, row.schema != null)
  }

}