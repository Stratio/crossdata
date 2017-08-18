/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.mongodb

import com.stratio.datasource.mongodb.config.MongodbConfig
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoImportTablesIT extends MongoDataTypesCollection {

  /**All tables imported after dropAllTables won't be temporary**/

  "MongoConnector" should "import all tables from MongoDB" in {
    assumeEnvironmentIsUpAndRunning

    xdContext.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |host '$MongoHost:${MongoPort.toString}',
         |${MongodbConfig.SamplingRatio} '0.1'
         |)
      """.stripMargin

    val importedTables = sql(importQuery).collect().map(_.getSeq(0))

    importedTables should contain allOf (Seq("highschool",Collection), Seq("highschool",DataTypesCollection))
  }

  it should "import tables from a MongoDB database" in {
    assumeEnvironmentIsUpAndRunning

    xdContext.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |host '$MongoHost:${MongoPort.toString}',
         |${MongodbConfig.Database} '$Database',
         |${MongodbConfig.SamplingRatio} '0.1'
         |)
      """.stripMargin

    sql(importQuery)
    sql("SHOW TABLES").collect() should contain allOf (Row(s"highschool.$Collection", false),Row(s"highschool.$DataTypesCollection", false))

  }

  it should "import unique table from a MongoDB collection" in {
    assumeEnvironmentIsUpAndRunning

    xdContext.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |host '$MongoHost:${MongoPort.toString}',
         |${MongodbConfig.Database} '$Database',
         |${MongodbConfig.Collection} '$Collection',
         |${MongodbConfig.SamplingRatio} '0.1'
         |)
      """.stripMargin

    val importedTables = sql(importQuery).collect().map(_.getSeq(0))

    importedTables should contain only Seq("highschool",Collection)
  }

  it should "import table from a collection with incorrect database" in {
    assumeEnvironmentIsUpAndRunning

    xdContext.dropAllTables()
    val wrongCollection = "wrongCollection"
    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |host '$MongoHost:${MongoPort.toString}',
         |${MongodbConfig.Database} '$Database',
         |${MongodbConfig.Collection} '$wrongCollection',
         |${MongodbConfig.SamplingRatio} '0.1'
         |)
      """.stripMargin

    sql(importQuery).collect().isEmpty shouldBe true
  }

  it should "import table from a MongoDB collection without indicate the database" in {
    assumeEnvironmentIsUpAndRunning

    xdContext.dropAllTables()

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
         |OPTIONS (
         |host '$MongoHost:${MongoPort.toString}',
         |${MongodbConfig.Collection} '$Collection',
         |${MongodbConfig.SamplingRatio} '0.1'
         |)
      """.stripMargin

    sql(importQuery)
    sql("SHOW TABLES").collect() should contain (Row(s"highschool.$Collection", false))

  }

}
