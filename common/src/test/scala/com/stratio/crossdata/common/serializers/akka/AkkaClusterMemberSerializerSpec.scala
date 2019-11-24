/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers.akka

import akka.actor.Address
import akka.cluster.crossdata.builders.MemberBuilder
import akka.cluster.{Member, MemberStatus, UniqueAddress}
import com.stratio.crossdata.common.serializers.XDSerializationTest.TestCase
import com.stratio.crossdata.common.serializers.{CrossdataCommonSerializer, XDSerializationTest}
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
