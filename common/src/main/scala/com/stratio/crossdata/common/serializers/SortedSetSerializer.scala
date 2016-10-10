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

import org.json4s.{CustomSerializer, Extraction}
import org.json4s.JsonAST.JArray

import scala.collection.immutable.SortedSet

class SortedSetSerializer[T : Manifest : Ordering] extends CustomSerializer[collection.immutable.SortedSet[T]](
  formats =>
  (
    {
      case arr: JArray =>
        implicit val _ = formats
        SortedSet(arr.extract[Array[T]]:_*)
    },
    {
      case ss: SortedSet[T] =>
        implicit val _ = formats
        Extraction.decompose(ss.toSeq)
    }
  )
)
