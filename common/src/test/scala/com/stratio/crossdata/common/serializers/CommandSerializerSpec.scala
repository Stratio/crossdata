/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers

import java.util.UUID

import com.stratio.crossdata.common.serializers.XDSerializationTest.TestCase
import com.stratio.crossdata.common._
import org.json4s.Formats
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class CommandSerializerSpec extends XDSerializationTest[Command] with CrossdataCommonSerializer {
  
  override implicit val formats: Formats = json4sJacksonFormats

  override def testCases: Seq[TestCase] = Seq(
    TestCase("marshall & unmarshall a SQLCommand 0", SQLCommand("select * from highschool")),
    TestCase("marshall & unmarshall a SQLCommand 1", SQLCommand("select * from highschool", flattenResults = true)),
    TestCase("marshall & unmarshall a SQLCommand 2", SQLCommand("select * from highschool", timeout = Some(5 seconds))),
    TestCase("marshall & unmarshall an OpenSessionCommand", OpenSessionCommand("usr")),
    TestCase("marshall & unmarshall an CloseSessionCommand", CloseSessionCommand()),
    TestCase("marshall & unmarshall an CancelQueryExecution", CancelQueryExecution(UUID.randomUUID()))
  )

}