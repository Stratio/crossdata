/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers

import com.stratio.crossdata.common.result.{InternalStreamedSuccessfulSQLResult, InternalStreamedSuccessfulSQLResult$, StreamedRow, StreamedSchema}
import org.json4s.JsonAST.{JField, JObject}
import org.json4s.{CustomSerializer, Extraction, Formats}
import StreamedSuccessfulSQLResultSerializerHelper._
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
private[serializers] object StreamedSuccessfulSQLResultSerializerHelper {
  val SchemaLabel = "streamedSchema"
  val RowLabel = "streamedRow"
}

object StreamedSuccessfulSQLResultSerializer extends CustomSerializer[InternalStreamedSuccessfulSQLResult](
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

class StreamedRowSerializer(schema: StructType) extends CustomSerializer[InternalStreamedSuccessfulSQLResult](
  formats => (
    {
      case JObject(JField(RowLabel, jRow)::Nil) =>
        implicit val _: Formats = formats + new RowSerializer(schema)
        StreamedRow(jRow.extract[Row])
    },
    PartialFunction.empty
  )
)
