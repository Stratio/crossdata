package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.SQLCommand
import org.apache.spark.sql.crossdata.serializers.XDSerializationTest.TestCase
import com.stratio.crossdata.common.Command
import org.json4s.Formats

class CommandSerializerSpec extends XDSerializationTest[Command] with CrossdataSerializer {
  
  override implicit val formats: Formats = json4sJacksonFormats

  override def testCases: Seq[TestCase] = Seq(
    TestCase("marshall & unmarshall a SQLCommand", SQLCommand("select * from highschool"))
  )

}