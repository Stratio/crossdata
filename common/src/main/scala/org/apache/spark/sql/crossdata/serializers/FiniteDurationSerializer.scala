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

import org.json4s.{CustomSerializer, Extraction, Formats, JObject}

import scala.concurrent.duration._

private[serializers] case class ProtoDuration(duration_ms: Long)

object FiniteDurationSerializer extends CustomSerializer[FiniteDuration](formats =>
  (
    {
      case jduration: JObject =>
        implicit val _: Formats = formats
        jduration.extract[ProtoDuration].duration_ms milliseconds
    },
    {
      case d: FiniteDuration =>
        Extraction.decompose(ProtoDuration(d.toMillis))(formats)
    }
  )
)
