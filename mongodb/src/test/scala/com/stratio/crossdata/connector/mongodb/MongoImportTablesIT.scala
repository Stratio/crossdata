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
package com.stratio.crossdata.connector.mongodb

import com.stratio.datasource.mongodb.MongodbConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoImportTablesIT extends MongoWithSharedContext {

  it should "import all tables from MongoDB" in {
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

    sql(importQuery)
    sql("SHOW TABLES").count() should be > (0l)

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
    sql("SHOW TABLES").count() should be > (0l)

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

    sql(importQuery)
    sql("SHOW TABLES").count() should be (1l)
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

    sql(importQuery)
    sql("SHOW TABLES").count() should be (0l)
  }

  it should "import unique table from a MongoDB collection without indicate the database" in {
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
    sql("SHOW TABLES").count() should be > 0l

  }

}
