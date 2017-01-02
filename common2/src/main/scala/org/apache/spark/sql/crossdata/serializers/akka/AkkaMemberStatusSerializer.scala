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
package org.apache.spark.sql.crossdata.serializers.akka

import akka.cluster.MemberStatus
import org.apache.spark.sql.crossdata.serializers.akka.AkkaMemberStatusSerializerHelper._
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s.JsonDSL._
import org.json4s.{CustomSerializer, Extraction}

object AkkaMemberStatusSerializerHelper {

  import MemberStatus._

  //TODO: Use Scala reflection to dynamically load value cases.
  //def extractObjectTypeTag[T : TypeTag](x: T): TypeTag[T] = typeTag[T]
  val obj2str: Map[MemberStatus, String] = Seq(
    WeaklyUp, Up, Leaving, Exiting, Down, Removed
  ) map (x => x -> x.toString) toMap

  val str2obj: Map[String, MemberStatus] = obj2str map {
    case (k,v) => v -> k
  }

  val typeLabel = "MemberStatus"

}

object AkkaMemberStatusSerializer extends CustomSerializer[MemberStatus] ( formats =>
  (
    {
      case JObject(JField(typeLabel, JString(statusStr))::Nil) if str2obj contains statusStr =>
        str2obj(statusStr)
    },
    {
      case x: MemberStatus =>
        typeLabel -> Extraction.decompose(obj2str(x))(formats)
    }
  )
)
