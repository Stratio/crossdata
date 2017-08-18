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