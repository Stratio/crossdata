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
package com.stratio.crossdata.common.serializers

import com.stratio.crossdata.common.result.{ErrorSQLResult, SQLResult, SuccessfulSQLResult}
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.serializers.StructTypeSerializer
import org.apache.spark.sql.types.StructType
import org.json4s.JsonDSL._
import org.json4s.{CustomSerializer, Extraction, _}

object SQLResultSerializerHelper {
  object FieldLabels {
    val SUCCESS  = "successfulResult"
    val ERRORMSG = "error"
    val SCHEMA   = "schema"
    val ROWS     = "rows"
  }
}

import SQLResultSerializerHelper.FieldLabels._

object SQLResultSerializer extends CustomSerializer[SQLResult]( format => (
  {
    case JObject(JField(SUCCESS, JBool(false))::JField(ERRORMSG, JString(message))::_) =>
      ErrorSQLResult(message)
    case JObject(JField(SUCCESS, JBool(true))::JField(SCHEMA, json_schema)::JField(ROWS, JArray(json_rows)) ::_) =>

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
      (SUCCESS -> false) ~ (ERRORMSG -> message)
    case SuccessfulSQLResult(resultSet, schema) =>
      implicit val formats = DefaultFormats + StructTypeSerializer + RowSerializer(schema)
      (SUCCESS -> true) ~ (SCHEMA -> Extraction.decompose(schema)) ~ (ROWS -> Extraction.decompose(resultSet))
  }
  )
)
