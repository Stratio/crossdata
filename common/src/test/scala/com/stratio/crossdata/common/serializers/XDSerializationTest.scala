/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers

import com.stratio.crossdata.test.BaseXDTest
import org.json4s.jackson.JsonMethods._
import org.json4s.{Extraction, Formats}

import scala.reflect.ClassTag

object XDSerializationTest {
  case class TestCase(description: String, obj: Any)
}

//TODO: Use the template to fully test all interchange messages' serialization (CommandEnvelope, ...)
abstract class XDSerializationTest[T : ClassTag : Manifest] extends BaseXDTest {

  import XDSerializationTest._

  def testCases: Seq[TestCase]
  implicit val formats: Formats

  private val classTag: ClassTag[T] = implicitly[ClassTag[T]]

  testCases foreach {
    case TestCase(description, obj) =>

    s"A ${classTag.toString().split('.').last} serializer" should description in {

      val serialized = compact(render(Extraction.decompose(obj)))
      val extracted = parse(serialized, false).extract[T]

      extracted shouldEqual obj

    }

  }

}
