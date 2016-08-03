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

@RunWith(classOf[JUnitRunner])
class MongoCreateExternalTableIT extends MongoWithSharedContext {

  "The Mongo connector" should "execute a CREATE EXTERNAL TABLE" in {

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE $Database.newtable (id Integer, name String)
          |USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$Database',
          |collection 'newtable'
          |)
      """.stripMargin.replaceAll("\n", " ")
    //Experimentation
    val result = sql(createTableQueryString).collect()

    //Expectations
    val table = xdContext.table(s"$Database.newtable")
    table should not be null
    table.schema.fieldNames should contain ("name")
  }


  it should "execute a CREATE EXTERNAL TABLE with options" in {

    val createTableQUeryString =
      s"""|CREATE EXTERNAL TABLE $Database.cappedTable (id Integer, name String)
      USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$Database',
          |collection 'cappedTable',
          |capped 'true',
          |size '100'
          |)
      """.stripMargin.replaceAll("\n", " ")
    //Experimentation
    val result = sql(createTableQUeryString).collect()

    //Expectations
    val table = xdContext.table(s"$Database.cappedTable")
    table should not be null
    table.schema.fieldNames should contain ("name")
    this.client.get.getDB(Database).getCollection("cappedTable").isCapped should be (true)
  }

  it should "execute a CREATE EXTERNAL TABLE with a different tableName" in {

    val createTableQUeryString =
      s"""|CREATE EXTERNAL TABLE other (id Integer, name String) USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$Database',
          |collection 'cTable',
          |capped 'true',
          |size '100'
          |)
      """.stripMargin.replaceAll("\n", " ")
    //Experimentation
    val result = sql(createTableQUeryString).collect()

    //Expectations
    val table = xdContext.table("other")
    table should not be null
    table.schema.fieldNames should contain ("name")
    this.client.get.getDB(Database).getCollection("cTable").isCapped should be (true)
  }

  it should "execute a CREATE EXTERNAL TABLE without specific db and table options options" in {

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE dbase.tbase (id Integer, name String) USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |capped 'true',
          |size '100'
          |)
      """.stripMargin.replaceAll("\n", " ")
    //Experimentation
    val result = sql(createTableQueryString).collect()

    //Expectations
    val table = xdContext.table("dbase.tbase")
    table should not be null
    table.schema.fieldNames should contain ("name")
    this.client.get.getDB("dbase").getCollection("tbase").isCapped should be (true)

    this.client.get.dropDatabase("dbase")
  }

}
