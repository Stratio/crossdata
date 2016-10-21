package com.stratio.crossdata.common.serializers

import com.stratio.crossdata.common.result.{StreamedRow, StreamedSchema, StreamedSuccessfulSQLResult}
import org.json4s.JsonAST.{JField, JObject}
import org.json4s.{CustomSerializer, Extraction, Formats}
import StreamedSuccessfulSQLResultSerializerHelper._
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

private[serializers] object StreamedSuccessfulSQLResultSerializerHelper {
  val SchemaLabel = "streamedSchema"
  val RowLabel = "streamedRow"
}

object StreamedSuccessfulSQLResultSerializer extends CustomSerializer[StreamedSuccessfulSQLResult](
  formats => (
    {
      implicit val _: Formats = formats;
      {
        case JObject(JField(SchemaLabel, jSchema)::Nil) => StreamedSchema(jSchema.extract[StructType])
        case JObject(JField(RowLabel, jRow)::Nil) => StreamedRow(jRow.extract[Row])
      }
    },
    {
      implicit val _: Formats = formats;
      {
        case StreamedSchema(schema) => JObject(JField(SchemaLabel, Extraction.decompose(schema)))
        case StreamedRow(row) => JObject(JField(RowLabel, Extraction.decompose(row)))
      }
    }
  )
)
