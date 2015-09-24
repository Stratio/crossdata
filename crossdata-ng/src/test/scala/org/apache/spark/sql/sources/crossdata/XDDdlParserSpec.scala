package org.apache.spark.sql.sources.crossdata
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.execution.datasources.{DescribeCommand, RefreshTable, CreateTableUsing}

class XDDdlParserSpec extends BaseXDTest {

  val parser = new XDDdlParser(_ => null)

  "A XDDlParser" should  """successfully parse an "IMPORT CATALOG" sentence into
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

  it should "generate an ImportCatalogUsingWithOptionsan with empty options map when they haven't been provided" in {

    val sentence = "IMPORT CATALOG USING org.apache.dumypackage.dummyclass"
    parser.parse(sentence) shouldBe ImportCatalogUsingWithOptions("org.apache.dumypackage.dummyclass", Map.empty)

  }

  //Sentences and expected values for SparkSQL core DDL
  val rightSentences = List[(String, PartialFunction[Any, Unit])] (
    ("""CREATE TEMPORARY TABLE words
       |USING org.apache.spark.sql.cassandra
       |OPTIONS (
       |  table "words",
       |  keyspace "test",
       |  cluster "Test Cluster",
       |  pushdown "true"
       |)""".stripMargin,            { case _: CreateTableUsing => () } ),
    ("REFRESH TABLE ddb.dummyTable", { case _: RefreshTable => ()     } ),
    ("DESCRIBE ddb.dummyTable",      { case _: DescribeCommand => (); } )
  )
  for((sentence, expect) <- rightSentences)
    it should s"keep parsing SparkSQL core DDL sentences: $sentence" in {
      expect.lift(parser.parse(sentence)) should not be None
    }

  //Malformed sentences and their expectations
  val wrongSentences =  List[String] (
    "IMPORT TABLE dummy",
    """IMPORT CATALOG
      | OPTIONS (
      |   addr     "dummyaddr",
      |   database "dummydb"
      | )
      | """.stripMargin
  )
  wrongSentences foreach { sentence =>
    it should s"fail when parsing wrong sentences: $sentence" in {
      an [Exception] should be thrownBy parser.parse(sentence)
    }
  }
}
