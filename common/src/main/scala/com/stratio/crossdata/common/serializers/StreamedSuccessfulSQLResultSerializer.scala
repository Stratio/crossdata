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
      case JObject(JField(SchemaLabel, jSchema)::Nil) =>
        implicit val _: Formats = formats
        StreamedSchema(jSchema.extract[StructType])
    },
    {
      case StreamedSchema(schema) => JObject(JField(SchemaLabel, Extraction.decompose(schema)(formats)))
      case StreamedRow(row, Some(providedSchema)) =>
        JObject(JField(RowLabel, Extraction.decompose(row)(formats + RowSerializer(providedSchema))))
    }
  )
)

class StreamedRowSerializer(schema: StructType) extends CustomSerializer[StreamedSuccessfulSQLResult](
  formats => (
    {
      case JObject(JField(RowLabel, jRow)::Nil) =>
        implicit val _: Formats = formats + new RowSerializer(schema)
        StreamedRow(jRow.extract[Row])
    },
    PartialFunction.empty
  )
)
