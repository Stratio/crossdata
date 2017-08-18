package com.stratio.crossdata.common.serializers

import java.util.UUID

import org.json4s._

object UUIDSerializer extends Serializer[UUID] {
  private val UUIDClass = classOf[UUID]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), UUID] = {
    case (TypeInfo(UUIDClass, _), json) => json match {
      case JObject(JField("$uuid", JString(s)) :: Nil) => UUID.fromString(s)
      case x => throw new MappingException(s"Can't convert $x to UUID")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case u: UUID => JObject(JField("$uuid", JString(u.toString)) :: Nil)
  }
}

