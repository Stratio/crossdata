package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.result.{ErrorSQLResult, SQLResult, SuccessfulSQLResult}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.json4s.{CustomSerializer, Extraction}
import org.json4s._
import org.json4s.JsonDSL._

object SQLResultSerializer extends CustomSerializer[SQLResult]( format => (
  {
    case JObject(JField("successfulResult", JBool(false))::JField("error", JString(message))::Nil) =>
      ErrorSQLResult(message)
    case JObject(JField("successfulResult", JBool(true))::JField("schema", json_schema)::JField("rows", JArray(json_rows)) ::Nil) =>

      val schema = {
        implicit val formats = DefaultFormats + StructTypeSerializer
        json_schema.extract[StructType]
      }

      implicit val formats = DefaultFormats + StructTypeSerializer + RowSerializer(schema)

      val rows = json_rows.map(_.extract[Row]).toArray
      SuccessfulSQLResult(rows, schema)
  },
  {
    case ErrorSQLResult(message, _) =>
      ("successfulResult" -> false) ~ ("error" -> message)
    case SuccessfulSQLResult(resultSet, schema) =>
      implicit val formats = DefaultFormats + StructTypeSerializer + RowSerializer(schema)
      ("successfulResult" -> true) ~ ("schema" -> Extraction.decompose(schema)) ~ ("rows" -> Extraction.decompose(resultSet))
  }
  )
)
