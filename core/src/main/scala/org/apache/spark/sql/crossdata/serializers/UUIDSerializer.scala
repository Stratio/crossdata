package org.apache.spark.sql.crossdata.serializers

import java.util.UUID

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JField, JInt, JObject, JString}

object UUIDSerializer extends CustomSerializer[UUID]( format => (
  {
    case JObject(JField("mostSigBits", JInt(s)) :: JField("leastSigBits", JInt(e)) :: Nil) =>
      new UUID(s.longValue, e.longValue)
  },
  {
    case x: UUID =>  JObject(JField("id", JString(x.toString)))
  }
  )
)
