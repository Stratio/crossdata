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
package com.stratio.crossdata.connector.mongodb

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.Seq

@RunWith(classOf[JUnitRunner])
class MongoDropExternalTableIT extends MongoWithSharedContext {

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    //Create test tables
    val createTable1 =
      s"""|CREATE EXTERNAL TABLE $Database.drop1 (id Integer, name String)
      USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$Database',
          |collection 'drop1'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTable1).collect()

    val createTable2 =
      s"""|CREATE EXTERNAL TABLE drop2 (id Integer, name String)
      USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$Database',
          |collection 'drop_table_example'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTable2).collect()
  }

  "The Mongo connector" should "execute a DROP EXTERNAL TABLE" in {

    //Precondition
    xdContext.table(s"$Database.drop1") should not be null

    val mongoTableName = "drop1"

    //DROP
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $Database.drop1"
    sql(dropExternalTableQuery).collect() should be(Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$Database.drop1"))
    this.client.get.getDB(Database).collectionExists(mongoTableName) should be(
        false)

  }

  "The Mongo connector" should "execute a DROP EXTERNAL TABLE without specify database" in {

    //Precondition
    xdContext.table("drop2") should not be null

    val mongoTableName = "drop_table_example"

    //DROP
    val dropExternalTableQuery = "DROP EXTERNAL TABLE drop2"
    sql(dropExternalTableQuery).collect() should be(Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table("drop2"))
    this.client.get.getDB(Database).collectionExists(mongoTableName) should be(
        false)

  }

}
