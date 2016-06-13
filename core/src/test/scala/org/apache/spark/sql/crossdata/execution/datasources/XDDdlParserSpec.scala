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
package org.apache.spark.sql.crossdata.execution.datasources

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, DescribeCommand, RefreshTable}
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class XDDdlParserSpec extends BaseXDTest with MockitoSugar{

  val xdContext = mock[XDContext]
  val parser = new XDDdlParser(_ => null, xdContext)

  "A XDDlParser" should  """successfully parse an "IMPORT TABLES" sentence into
                           |a ImportTablesUsingWithOptions RunnableCommand """.stripMargin in {

    val sentence =
      """IMPORT TABLES
        | USING org.apache.dumypackage.dummyclass
        | OPTIONS (
        |   addr     "dummyaddr",
        |   database "dummydb"
        | )
        | """.stripMargin

    parser.parse(sentence) shouldBe ImportTablesUsingWithOptions(
      "org.apache.dumypackage.dummyclass",
      Map(
        "addr"     -> "dummyaddr",
        "database" -> "dummydb"
      )
    )

  }

  it should "generate an ImportCatalogUsingWithOptions with empty options map when they haven't been provided" in {

    val sentence = "IMPORT TABLES USING org.apache.dumypackage.dummyclass"
    parser.parse(sentence) shouldBe ImportTablesUsingWithOptions("org.apache.dumypackage.dummyclass", Map.empty)

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
    "IMPORT TABLES",
    """IMPORT TABLES
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


  it should "successfully parse a DROP TABLE into a DropTable RunnableCommand" in {

    val sentence = "DROP TABLE tableId"
    parser.parse(sentence) shouldBe DropTable( TableIdentifier("tableId", None))

  }

  it should "successfully parse a DROP TABLE with a qualified table name into a DropTable RunnableCommand" in {

    val sentence = "DROP TABLE dbId.tableId"
    parser.parse(sentence) shouldBe DropTable( TableIdentifier("tableId", Some("dbId")))

  }

  it should "successfully parse a DROP EXTERNAL TABLE into a DropExternalTable RunnableCommand" in {

    val sentence = "DROP EXTERNAL TABLE tableId"
    parser.parse(sentence) shouldBe DropExternalTable( TableIdentifier("tableId", None))

  }

  it should "successfully parse a DROP EXTERNAL TABLE with a qualified table name into a DropExternalTable RunnableCommand" in {

    val sentence = "DROP EXTERNAL TABLE dbId.tableId"
    parser.parse(sentence) shouldBe DropExternalTable( TableIdentifier("tableId", Some("dbId")))

  }



  it should "successfully parse a INSERT TABLE with qualified table name and VALUES provided into InsertTable RunnableCommand" in {

    val sentence = """INSERT INTO tableId VALUES ( 12, 12.01, 'proof', true)"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"), List(List("12", "12.01", "proof", "true")))

  }

  it should "successfully parse a INSERT TABLE with qualified table name and more than one VALUES provided into InsertTable RunnableCommand" in {

    val sentence = """INSERT INTO tableId VALUES ( 12, 12.01, 'proof', true), ( 2, 1.01, 'pof', true), ( 256, 0.01, 'pr', false)"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List("12", "12.01", "proof", "true"),List("2", "1.01", "pof", "true"),List("256", "0.01", "pr", "false")))

  }

  it should "successfully parse a INSERT TABLE with qualified table name, schema and VALUES provided into InsertTable RunnableCommand" in {

    val sentence = """INSERT INTO tableId(Column1, Column2, Column3, Column4) VALUES ( 256, 0.01, 'pr', false)"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"), List(List("256", "0.01", "pr", "false")), Some(List("Column1", "Column2", "Column3", "Column4")))

  }

  it should "successfully parse a INSERT TABLE with qualified table name, schema and more than one VALUES provided into InsertTable RunnableCommand" in {

    val sentence = """INSERT INTO tableId(Column1, Column2, Column3, Column4) VALUES ( 12, 12.01, 'proof', true), ( 2, 1.01, 'pof', true), ( 256, 0.01, 'pr', false)"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List("12", "12.01", "proof", "true"),List("2", "1.01", "pof", "true"),List("256", "0.01", "pr", "false")),
        Some(List("Column1", "Column2", "Column3", "Column4")))

  }

  it should "successfully parse a INSERT TABLE using arrays provided in VALUES" in {

    val sentence = """INSERT INTO tableId VALUES ( [1,2], 12, 12.01, 'proof', [false,true], true, ["proof array", "proof2"])"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List(List("1","2"),"12", "12.01", "proof", List("false","true"), "true", List("proof array","proof2"))))

  }

  it should "successfully parse a INSERT TABLE using arrays with Strings with comma provided in VALUES" in {

    val sentence = """INSERT INTO tableId VALUES ( [1,2], 12, 12.01, 'proof', [false,true], true, ["proof, array", "proof2"])"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List(List("1","2"),"12", "12.01", "proof", List("false","true"), "true", List("proof, array","proof2"))))

  }

  it should "successfully parse a INSERT TABLE using maps provided in VALUES" in {

    val sentence = """INSERT INTO tableId VALUES ( (x -> 1, y -> 2), 12, 12.01, 'proof', (x1 -> false, x2 -> true), true)"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List(Map("x"->"1","y"->"2"),"12", "12.01", "proof", Map("x1"->"false","x2"->"true"), "true")))

  }

  it should "successfully parse a INSERT TABLE using maps with strings provided in VALUES" in {

    val sentence = """INSERT INTO tableId VALUES ( (x -> 1, y -> 2, z -> 3), 12, 12.01, 'proof', (x1 -> "proof,comma", x2 -> "proof2"), true)"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List(Map("x"->"1","y"->"2","z"->"3"),"12", "12.01", "proof", Map("x1"->"proof,comma","x2"->"proof2"), "true")))

  }

  it should "successfully parse a INSERT TABLE using maps with arrays and viceversa provided in VALUES" in {

    val sentence = """INSERT INTO tableId VALUES ( [(x->1, y->2), (z->3)], (x -> [3,4], y -> [5,6]) )"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"),
        List(List( List(Map("x"->"1","y"->"2"), Map("z"->"3")), Map("x" -> List("3","4"), "y" -> List("5","6")) )))

  }

  it should "successfully parse a INSERT TABLE using empty maps and arrays provided in VALUES" in {

    val sentence = """INSERT INTO tableId VALUES ([], ())"""
    parser.parse(sentence) shouldBe
      InsertIntoTable( TableIdentifier("tableId"), List(List(List(), Map())))

  }

  it should "successfully parse a CREATE VIEW into a CreateView RunnableCommand" in {

    val sentence = "CREATE VIEW vn AS SELECT * FROM tn"
    val logicalPlan = parser.parse(sentence)
    logicalPlan shouldBe a [CreateView]
    logicalPlan match {
      case CreateView(tableIdent, lPlan, sqlView) =>
        tableIdent shouldBe TableIdentifier("vn")
        sqlView.trim shouldBe  "SELECT * FROM tn"
    }

  }

  it should "successfully parse a CREATE TEMPORARY VIEW into a CreateTempView RunnableCommand" in {

    val sourceSentence = "SELECT * FROM tn"
    val sentence = s"CREATE TEMPORARY VIEW vn AS $sourceSentence"
    val logicalPlan = parser.parse(sentence)
    logicalPlan shouldBe a [CreateTempView]
    logicalPlan match {
      case CreateTempView(tableIdent, lPlan, sql) =>
        tableIdent shouldBe TableIdentifier("vn")
        sql.map(_.trim) shouldBe Some(sourceSentence)
    }

  }

  it should "successfully parse a ADD JAR into a AddJar RunnableCommand" in {
    val sentence = "ADD JAR /tmp/jar"
    parser.parse(sentence) shouldBe AddJar("/tmp/jar")
  }

}
