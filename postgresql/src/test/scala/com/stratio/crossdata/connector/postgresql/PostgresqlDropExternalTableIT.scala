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
package com.stratio.crossdata.connector.postgresql

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.Seq

@RunWith(classOf[JUnitRunner])
class PostgresqlDropExternalTableIT extends PostgresqlWithSharedContext {

  val dropSchema = "dropschema"

  "The Postgresql connector" should "execute a DROP EXTERNAL TABLE" in {
    val dropTable = "drop1"

    val createTableQueryString1 =
      s"""|CREATE EXTERNAL TABLE $dropSchema.$dropTable (
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |dbtable '$dropSchema.$dropTable',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")

    sql(createTableQueryString1).collect()
    //Precondition
    xdContext.table(s"$dropSchema.$dropTable") should not be null

    //DROP
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $dropSchema.$dropTable"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$dropSchema.$dropTable"))

    client.get._1.getMetaData.getTables(null, dropSchema, dropTable, null).next() shouldBe false

    client.get._2.execute(s"drop schema $dropSchema cascade")
  }

  "The Postgresql connector" should "execute a DROP EXTERNAL TABLE without specify database" in {
    val dropTable = "drop2"

    val createTableQueryString2 =
      s"""|CREATE EXTERNAL TABLE $dropSchema.$dropTable (
          |id Integer,
          |name String,
          |booleanFile Boolean,
          |timeTime Timestamp
          |)
          |USING $SourceProvider
          |OPTIONS (
          |url '$url',
          |dbtable '$dropSchema.$dropTable',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString2).collect()

    //Postgresql datasource doesn't allow tables without specify schema
    an[Exception] should be thrownBy xdContext.table(dropTable)

    //DROP expectations: Postgresql table can't be dropped without specify schema. This table is not in the XDCatalog
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $dropTable"
    an[Exception] shouldBe thrownBy (sql(dropExternalTableQuery).collect())

    client.get._1.getMetaData.getTables(null, dropSchema, dropTable, null).next() shouldBe true
    client.get._2.execute(s"DROP SCHEMA $dropSchema CASCADE")

  }


}
