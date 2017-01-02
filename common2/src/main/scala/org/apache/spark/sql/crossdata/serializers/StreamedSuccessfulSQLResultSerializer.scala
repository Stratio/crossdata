/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.result.{InternalStreamedSuccessfulSQLResult, StreamedRow, StreamedSchema}
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.serializers.StreamedSuccessfulSQLResultSerializerHelper._
import org.apache.spark.sql.types.StructType
import org.json4s.JsonAST.{JField, JObject}
import org.json4s.{CustomSerializer, Extraction, Formats}
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
