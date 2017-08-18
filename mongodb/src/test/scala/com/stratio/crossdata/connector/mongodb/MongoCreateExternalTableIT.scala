/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
