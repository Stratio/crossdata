package com.stratio.crossdata.common.serializers

import java.util.UUID

import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.{CancelQueryExecution, CommandEnvelope, OpenSessionCommand, SQLCommand}
import com.stratio.crossdata.common.serializers.XDSerializationTest.TestCase
import org.json4s.Formats
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandEnvelopeSpec extends XDSerializationTest[CommandEnvelope] with CrossdataCommonSerializer {

  override implicit val formats: Formats = json4sJacksonFormats

  lazy val session = Session(UUID.randomUUID(), None)

  override def testCases: Seq[TestCase] = Seq(
    TestCase(
      "marshall & unmarshall a SQLCommand within a CommandEnvelope",
      CommandEnvelope(SQLCommand("select * from highschool"), session)
    ),
    TestCase(
      "marshall & unmarshall a OpenSessionCommand within a CommandEnvelope",
      CommandEnvelope(OpenSessionCommand("usr"), session)
    ),
    TestCase(
      "marshall & unmarshall a CancelQueryExecution within a CommandEnvelope",
      CommandEnvelope(CancelQueryExecution(UUID.randomUUID()), session)
    )
  )
}
