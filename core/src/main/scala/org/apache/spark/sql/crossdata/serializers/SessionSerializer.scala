package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.security.Session
import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JField, JNull, JObject, JString}
import java.util.UUID

import scala.collection.immutable.::


object SessionSerializer extends CustomSerializer[Session](
  format => (
    {
      case JObject(JField("id", JObject(List(("id",JString(uuidStr)))))::Nil) =>
        Session(UUID.fromString(uuidStr), null)
      case JObject(JField("clientRef", JNull)::JField("id", JObject(List(("id",JString(uuidStr)))))::Nil) =>
        Session(UUID.fromString(uuidStr), null)
    },
    {
      case Session(id, _) =>
        JObject(JField("clientRef", JNull)::JField("id", JString(id.toString))::Nil)
    }
    )
)
