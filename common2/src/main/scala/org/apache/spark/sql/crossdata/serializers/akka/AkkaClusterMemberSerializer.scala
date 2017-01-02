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

import akka.cluster.{Member, MemberStatus, UniqueAddress}
import akka.cluster.crossdata.builders.MemberBuilder
import org.json4s.JsonAST.{JField, JInt, JObject}
import org.json4s.{CustomSerializer, Extraction}

object AkkaClusterMemberSerializer extends CustomSerializer[Member] ( formats =>
  (
    {
      case JObject(
        List(
          JField("uniqueAddress", jUniqueAddress),
          JField("upNumber", JInt(upNumber)),
          JField("status", jStatus),
          JField("roles", jRoles)
        )
      ) =>

        implicit val _ = formats

        MemberBuilder(
          jUniqueAddress.extract[UniqueAddress],
          upNumber.toInt,
          jStatus.extract[MemberStatus],
          jRoles.extract[List[String]].toSet
        )
    },
    {
      case member: Member =>
        import member._

        implicit val _ = formats

        JObject(
          List(
            JField("uniqueAddress", Extraction.decompose(uniqueAddress)),
            JField("upNumber", JInt(MemberBuilder.extractUpNumber(member))),
            JField("status", Extraction.decompose(status)),
            JField("roles", Extraction.decompose(roles.toList))
          )
        )
    }
    )
)
