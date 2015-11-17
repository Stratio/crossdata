/**
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
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, DescribeCommand, RefreshTable}

class XDDdlParserSpec extends BaseXDTest {

  val parser = new XDDdlParser(_ => null)

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
}
