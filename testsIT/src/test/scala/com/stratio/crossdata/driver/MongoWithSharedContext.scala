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
package com.stratio.crossdata.driver

import java.util.UUID

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import com.stratio.crossdata.server.CrossdataServer
import com.stratio.crossdata.test.BaseXDTest
import com.stratio.datasource.mongodb.config.MongodbConfig
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID
import org.scalatest.BeforeAndAfterAll

import scala.util.Try

class MongoWithSharedContext extends BaseXDTest with MongoConstants with BeforeAndAfterAll { //TODO: Rename to withoutSharedContext

  var client: MongoClient = MongoClient(MongoHost, MongoPort)

  var crossdataServer: Option[CrossdataServer] = None
  var crossdataSession: Option[XDSession] = None
  val SessionID: SessionID = UUID.randomUUID()

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
          "account" -> MongoDBObject("number" -> (11235813*a), "details" -> MongoDBObject("bank" -> "Mercantil", "office" -> (12357+a))),
          "grades" -> Seq(MongoDBObject("FP" -> Seq(7.0, 8.0)), MongoDBObject("REACTIVEARCHS" -> Seq(9.0)))
        )
      }
    }
  }


  protected def cleanTestData: Unit = {
    val client = this.client

    val collection = client(Database)(Collection)
    collection.dropCollection()

  }



  def init() = {
    crossdataServer = Some(new CrossdataServer)
    crossdataServer.foreach(_.start())
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.newSession(SessionID)))

  }

  def stop() = {
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.session(SessionID).get.dropAllTables()))
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.closeSession(SessionID)))
    crossdataServer.foreach(_.stop())
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
         |${MongodbConfig.SamplingRatio} '0.6'
         |)
      """.stripMargin

    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.session(SessionID).get.sql(importQuery)))
  }

  override protected def afterAll(): Unit = {
    stop()
    cleanTestData
    client.close()
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