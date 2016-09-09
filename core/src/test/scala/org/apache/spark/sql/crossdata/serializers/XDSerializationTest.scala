package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.test.BaseXDTest
import org.json4s.{Extraction, Formats}
import org.json4s.jackson.JsonMethods._

import scala.reflect.ClassTag

object XDSerializationTest {
  case class TestCase(description: String, obj: Any)
}

abstract class XDSerializationTest[T : ClassTag : Manifest] extends BaseXDTest {

  import XDSerializationTest._

  def testCases: Seq[TestCase]
  implicit val formats: Formats

  private val classTag: ClassTag[T] = implicitly[ClassTag[T]]

  testCases foreach {
    case TestCase(description, obj) =>

    s"A $classTag serializer" should description in {

      val serialized = compact(render(Extraction.decompose(obj)))
      val extracted = parse(serialized).extract[T]

      extracted shouldEqual obj

    }

  }

}
