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

import akka.actor.Address
import akka.cluster.crossdata.builders.MemberBuilder
import akka.cluster.{Member, MemberStatus, UniqueAddress}
import org.apache.spark.sql.crossdata.serializers.XDSerializationTest.TestCase
import org.apache.spark.sql.crossdata.serializers.{CrossdataCommonSerializer, XDSerializationTest}
import org.json4s.Formats


class AkkaClusterMemberSerializerSpec extends XDSerializationTest[Member] with CrossdataCommonSerializer {

  override implicit val formats: Formats = json4sJacksonFormats

  lazy val member = MemberBuilder.apply(
    UniqueAddress(Address("akka.tcp", "sys"), 42),
    10,
    MemberStatus.Up,
    Set("a", "b", "c")
  )

  override def testCases: Seq[TestCase] = Seq(
    TestCase("marshall & unmarshall an akka.cluster.Member", member)
  )

}
