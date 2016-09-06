package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.{Command, SQLCommand}
import org.json4s._
import java.util.UUID

object CommandSerializer extends CustomSerializer[Command](
  format => (
    {
      case JObject(
        List(
          JField("flattenResults", JBool(flattenResults)),
          JField("queryId"       , JObject(List(("id",JString(qid))))),
          JField("sql"           , JString(sql))
        )
      ) => SQLCommand(sql, UUID.fromString(qid), flattenResults)
    },
    {
      case command: SQLCommand => Extraction.decompose(command)(DefaultFormats + SessionSerializer)
    }
    )
)
