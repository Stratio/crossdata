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
package com.stratio.crossdata.driver

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import com.stratio.crossdata.server.CrossdataServer
import com.stratio.crossdata.test.BaseXDTest
import com.stratio.datasource.mongodb.MongodbConfig
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll

import scala.util.Try

class MongoWithSharedContext extends BaseXDTest with MongoConstants with BeforeAndAfterAll {

  var client:MongoClient = MongoClient(MongoHost, MongoPort)

  protected def saveTestData: Unit = {
    val collection = client(Database)(Collection)
    for (a <- 1 to 10) {
      collection.insert {
        MongoDBObject("id" -> a,
          "age" -> (10 + a),
          "description" -> s"description$a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a",
          "address" -> MongoDBObject("street" -> s"${a}th Avenue", "city" -> s"City $a", "zip" -> (28000+a)),
          "account" -> MongoDBObject("number" -> (11235813*a), "details" -> MongoDBObject("bank" -> "Mercantil", "office" -> (12357+a)))
        )
      }
    }
  }


  protected def cleanTestData: Unit = {
    val client = this.client

    val collection = client(Database)(Collection)
    collection.dropCollection()

  }

  var crossdataServer: Option[CrossdataServer] = None

  def init() = {
    crossdataServer = Some(new CrossdataServer)
    crossdataServer.foreach(_.init(null))
    crossdataServer.foreach(_.start())

  }

  def stop() = {
    crossdataServer.get.xdContext.get.dropAllTables()
    crossdataServer.foreach(_.stop())
    crossdataServer.foreach(_.destroy())
  }


  override protected def beforeAll(): Unit = {
    init()
    saveTestData

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

    crossdataServer.get.xdContext.get.sql(importQuery)
  }

  override protected def afterAll(): Unit = {
    stop()
  }

  def assumeCrossdataUpAndRunning() = {
    assume(crossdataServer.isDefined, "Crossdata server is not up and running.")
  }
}

sealed trait MongoConstants {
  val Database = "XDDriverTest"
  val Collection = "people"

  //Config
  val MongoHost: String = {
    Try(ConfigFactory.load().getStringList("mongo.hosts")).map(_.get(0)).getOrElse("127.0.0.1")
  }
  val MongoPort = 27017
  val SourceProvider = "com.stratio.crossdata.connector.mongodb"

}