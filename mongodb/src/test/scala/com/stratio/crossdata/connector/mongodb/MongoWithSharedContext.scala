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

package com.stratio.crossdata.connector.mongodb

import com.mongodb.QueryBuilder
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.Logging
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.scalatest.Suite

trait MongoWithSharedContext extends SharedXDContextTest with MongoDefaultConstants with Logging {
  this: Suite =>

  var mongoClient: Option[MongoClient] = None
  var isEnvironmentReady = false


  override protected def beforeAll() = {
    super.beforeAll()

    try {
      mongoClient = Some(prepareEnvironment())

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE $Collection
            |(id BIGINT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)
            |USING $SourceProvider
            |OPTIONS (
            |host '$MongoHost:$MongoPort',
            |database '$Database',
            |collection '$Collection'
            |)
         """.stripMargin.replaceAll("\n", " "))

    } catch {
      case e: Throwable => logError(e.getMessage)
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
        MongoDBObject("id" -> a,
          "age" -> (10 + a),
          "description" -> s"description$a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a"
        )
      }
      collection.update(QueryBuilder.start("id").greaterThan(4).get, MongoDBObject( ("$set", MongoDBObject( ("optionalField", true))) ), multi=true)

    }
  }

  private def cleanTestData(client: MongoClient): Unit = {
    val collection = client(Database)(Collection)
    collection.dropCollection()
  }

  lazy val assumeEnvironmentIsUpAndRunning = {
    assume(isEnvironmentReady, "MongoDB and Spark must be up and running")
  }
}

sealed trait MongoDefaultConstants {
  val Database = "highschool"
  val Collection = "students"
  val MongoHost = "127.0.0.1"
  val MongoPort = 27017
  val SourceProvider = "com.stratio.crossdata.connector.mongodb"
}