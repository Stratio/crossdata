package org.apache.spark.sql.crossdata.serializers

import java.sql.Timestamp

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.{GenericRow, GenericRowWithSchema}
import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.types.{DecimalType, _}
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.JsonDSL._

case class RowSerializer(providedSchema: StructType) extends Serializer[Row] {


  private def deserializeWithSchema(
                                     schema: StructType,
                                     fields: JArray,
                                     includeSchema: Boolean
                                   )(implicit formats: Formats): Row = {

    val extractField: (DataType, JValue)=> Any = {
      case (_, JNull) | (NullType, _) => null
      case (StringType, JString(str)) => str
      case (TimestampType, JString(toStr)) => Timestamp.valueOf(toStr)
      case (IntegerType, JInt(v)) => v.toInt
      case (ShortType, JInt(v)) => v.toShort
      case (FloatType, JDouble(v)) => v.toFloat
      case (DoubleType, JDouble(v)) => v.toDouble
      case (LongType, JInt(v)) => v.toLong
      case (_: DecimalType, JDecimal(v)) => Decimal(v)
      case (ByteType, JInt(v)) => v.toByte
      case (BinaryType, JString(binaryStr)) => binaryStr.getBytes
      case (BooleanType, JBool(v)) => v
      case (DateType, JString(strDate)) => java.sql.Date.valueOf(strDate)
      //case (udt: UserDefinedType[_], v) => //TODO
      //case (ArrayType(ty, _), JArray(arr)) => arr.map(extractField(ty, _)) //TODO
      //case (MapType(kt, vt, _), v: MapData) => //TODO
      case (st: StructType, v: Row) => serializeWithSchema(st, v, true)
    }

    val values: Array[Any] = (schema.view.map(_.dataType) zip fields.arr).map(extractField.tupled).toArray

    if(includeSchema) new GenericRowWithSchema(values, schema)
    else new GenericRow(values)

  }

  private def serializeWithSchema(
                                   schema: StructType,
                                   row: Row, includeSchema: Boolean
                                 )(implicit formats: Formats): JValue = {

    val valuesList = List[JValue]()

    schema.zipWithIndex.view map {
      case (t, idx) => t.dataType -> row.get(idx)
    } map { // Cases brought in from Spark's DataFrame JSON serializer (`DataFrame#toJSON`)
      case (_, null) | (NullType, _) => JNull
      case (StringType, v: String) => JString(v)
      case (TimestampType, v: Long) => JString(DateTimeUtils.toJavaTimestamp(v).toString)
      case (IntegerType, v: Int) => JInt(v)
      case (ShortType, v: Short) => JInt(v.toInt)
      case (FloatType, v: Float) => JDouble(v)
      case (DoubleType, v: Double) => JDouble(v)
      case (LongType, v: Long) => JInt(v)
      case (DecimalType(), v: Decimal) => JDecimal(v.toBigDecimal)
      case (ByteType, v: Byte) => JInt(v.toInt)
      case (BinaryType, v: Array[Byte]) => JString(v.toString)
      case (BooleanType, v: Boolean) => JBool(v)
      case (DateType, v: Int) => JString(DateTimeUtils.toJavaDate(v).toString)
      //case (udt: UserDefinedType[_], v) => //TODO
      // case (ArrayType(ty, _), v: ArrayData) => JArray(v.array.toList.map(v => Extraction.decompose(v)))
      //case (MapType(kt, vt, _), v: MapData) =>
      case (st: StructType, v: Row) => serializeWithSchema(st, v, true)
    }

    val justValues: JObject = ("values" -> JArray(valuesList))

    if(includeSchema) justValues ~ ("schema" -> Extraction.decompose(schema))
    else justValues

  }

  override def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Row] = {
    case (_, JObject(JField("values", JArray(values)))) => deserializeWithSchema(providedSchema, values, false)
    case (_, JObject(JField("values", JArray(values))::JField("schema", jschema))) =>
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