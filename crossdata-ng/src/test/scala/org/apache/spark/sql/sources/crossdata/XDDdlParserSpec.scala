package org.apache.spark.sql.sources.crossdata

import org.scalatest.{Matchers, WordSpec}

/**
 * Created by pfperez on 17/09/15.
 */
class XDDdlParserSpec extends WordSpec with Matchers {

  "A XDDlParser" should {
    val parser = new XDDdlParser(_ => null)

    """successfully parse an "IMPORT CATALOG" sentence into
      |a ImportCatalogUsingWithOptions RunnableCommand """.stripMargin in {

      val sentence =
        """IMPORT CATALOG
          | USING org.apache.dumypackage.dummyclass
          | OPTIONS (
          |   addr     "dummyaddr",
          |   database "dummydb"
          | )
          | """.stripMargin

      parser.parse(sentence) shouldBe ImportCatalogUsingWithOptions(
        "org.apache.dumypackage.dummyclass",
         Map(
          "addr" -> "dummyaddr",
          "database" -> "dummydb"
         )
      )
    }

  }

}
