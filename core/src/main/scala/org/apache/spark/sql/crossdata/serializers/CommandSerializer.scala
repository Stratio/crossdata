package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.{Command, SQLCommand}
import org.json4s._
import org.json4s.ext.UUIDSerializer
import java.util.UUID

object CommandSerializer extends CustomSerializer[Command](
  format => (
    {
      case JObject(
        List(
          JField("sql"           , JString(sql)),
          JField("queryId"       , jqueryId),
          JField("flattenResults", JBool(flattenResults))
        )
      ) =>
        implicit val _ = DefaultFormats + UUIDSerializer
        SQLCommand(sql, jqueryId.extract[UUID], flattenResults)
    },
    {
      case command: SQLCommand => Extraction.decompose(command)(DefaultFormats + UUIDSerializer)
    }
    )
)
