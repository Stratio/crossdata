package org.apache.spark.sql.crossdata.serializers

import org.apache.spark.sql.types.StructType
import org.json4s.JsonAST.{JField, JObject}
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.reflect.TypeInfo
import org.json4s.jackson.JsonMethods._

object StructTypeSerializer extends Serializer[StructType]{

  private val StructTypeClass = classOf[StructType]
  private val StructTypeId = "structType"

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), StructType] = {
    case (TypeInfo(StructTypeClass, _), json) => json match {
      case JObject(JField(StructTypeId, structJson) :: _) =>
        StructType.fromString(compact(structJson))
      case x => throw new MappingException("Can't convert " + x + " to StructType")
    }
  }

  def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case x: StructType =>
      StructTypeId -> parse(x.json, false)
  }
}