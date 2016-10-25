package com.stratio.crossdata.common.serializers

import com.stratio.crossdata.common.result.{StreamedRow, StreamedSchema, StreamedSuccessfulSQLResult}
import org.json4s.JsonAST.{JField, JObject}
import org.json4s.{CustomSerializer, Extraction}
import StreamedSuccessfulSQLResultSerializerHelper._
private[serializers] object StreamedSuccessfulSQLResultSerializerHelper {
  val SchemaLabel = "streamedSchema"
  val RowLabel = "streamedRow"
}

object StreamedSuccessfulSQLResultSerializer extends CustomSerializer[StreamedSuccessfulSQLResult](
  formats => (
    PartialFunction.empty,
    {
      case StreamedSchema(schema) => JObject(JField(SchemaLabel, Extraction.decompose(schema)(formats)))
      case StreamedRow(row, Some(providedSchema)) =>
        JObject(JField(RowLabel, Extraction.decompose(row)(formats + RowSerializer(providedSchema))))
    }

  )
)
