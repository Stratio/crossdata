/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$Database.drop1"))
    this.client.get.getDB(Database).collectionExists(mongoTableName) should be (false)

  }

  "The Mongo connector" should "execute a DROP EXTERNAL TABLE without specify database" in {

    //Precondition
    xdContext.table("drop2") should not be null

    val mongoTableName = "drop_table_example"

    //DROP
    val dropExternalTableQuery = "DROP EXTERNAL TABLE drop2"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table("drop2"))
    this.client.get.getDB(Database).collectionExists(mongoTableName) should be (false)

  }

}
