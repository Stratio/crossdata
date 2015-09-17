/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.sql.sources.mongodb

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.scalatest.Suite

trait MongoWithSharedContext extends SharedXDContextTest with MongoDefaultConstants {
  this: Suite =>

  var mongoClient: Option[MongoClient] = None
  var isEnvironmentReady = false


  override protected def beforeAll() = {
    super.beforeAll()

    try {
      mongoClient = Some(prepareEnvironment())

      xdContext.sql(
        s"CREATE TEMPORARY TABLE $Collection (id STRING, age INT, description STRING, enrolled BOOLEAN, name STRING) USING $SourceProvider OPTIONS " +
          s"( host '$MongoHost:$MongoPort'," +
          s" database '$Database', " +
          s" collection '$Collection')".stripMargin)

    } catch {
      case e: Throwable => ()
    }

    isEnvironmentReady = mongoClient.isDefined
  }

  override protected def afterAll() = {
    super.afterAll()
    mongoClient.foreach(cleanEnvironment)
  }

  def prepareEnvironment(): MongoClient = {
    val mongoClient = MongoClient(MongoHost, MongoPort)
    saveTestData(mongoClient)
    mongoClient
  }

  def cleanEnvironment(mongoClient: MongoClient) = {
    cleanTestData(mongoClient)
    mongoClient.close()
  }


  private def saveTestData(client: MongoClient): Unit = {

    val collection = client(Database)(Collection)
    for (a <- 1 to 10) {
      collection.insert {
        MongoDBObject("id" -> a.toString,
          "age" -> (10 + a),
          "description" -> s"description $a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a"
        )
      }
    }
  }

  private def cleanTestData(client: MongoClient): Unit = {
    val collection = client(Database)(Collection)
    collection.dropCollection()
  }
}

sealed trait MongoDefaultConstants {
  val Database = "highschool"
  val Collection = "students"
  val MongoHost = "127.0.0.1"
  val MongoPort = 27017
  val SourceProvider = "com.stratio.crossdata.sql.sources.mongodb"
}