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

import org.apache.spark.sql.types.StructType
import org.json4s.JsonAST.{JField, JObject}
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.reflect.TypeInfo
import org.json4s.jackson.JsonMethods._

object StructTypeSerializer extends Serializer[StructType]{

  private val StructTypeClass = classOf[StructType]
  private val StructTypeId = "structType"

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), StructType] = {
    case (TypeInfo(StructTypeClass, _), json) => json match {
      case JObject(JField(StructTypeId, structJson) :: _) =>
        StructType.fromString(compact(structJson))
      case x => throw new MappingException("Can't convert " + x + " to StructType")
    }
  }

  def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case x: StructType =>

      StructTypeId -> parse(x.json)
  }
}