/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server.server

import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.actors._
import scala.concurrent.duration._
import com.stratio.meta.core.engine.Engine
import org.testng.Assert._
import com.stratio.meta.server.utilities._
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import scala.collection
import com.stratio.meta.common.result.{CommandResult, ConnectResult, QueryResult}
import com.stratio.meta.common.ask.{Query, Connect}
import com.stratio.meta.communication.ACK

/**
 * Server Actor tests.
 */
class BasicServerActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
                                   with ImplicitSender with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra{

  lazy val engine:Engine = createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"test")

  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  test ("Unknown message"){
    within(5000 millis){
      serverRef ! 1
      val result = expectMsgClass(classOf[CommandResult])
      assertTrue(result.hasError, "Expecting error message")
    }
  }

  test ("ServerActor Test connect without error"){
    within(5000 millis){
      serverRef ! Connect("test-user")
      val result = expectMsgClass(classOf[ConnectResult])
      assertFalse(result.hasError, "Error not expected")
      assertNotEquals(result.asInstanceOf[ConnectResult].getSessionId, -1, "Expecting session id")
      assertFalse(result.isKsChanged, "Catalog should not change.")
      assertNull(result.getCurrentKeyspace, "Expecting null catalog.")
    }
  }

  test ("Create KS"){
    within(5000 millis){
      val query = "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      serverRef ! new Query("server-actor", "", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
    }
  }

  test ("Create existing KS"){
    within(5000 millis){
      val query = "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      serverRef ! new Query("server-actor", "", query, "test")
      val result = expectMsgClass(classOf[QueryResult])
      assertTrue(result.hasError, "Expecting ks exists error")
    }
  }

  test ("Use keyspace"){
    within(5000 millis){
      val query = "use ks_demo ;"
      serverRef ! new Query("server-actor", "", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
    }
  }

  test ("Use non-existing keyspace"){
    within(5000 millis){
      val query = "use unknown ;"
      serverRef ! new Query("server-actor", "", query, "test")
      val result = expectMsgClass(classOf[QueryResult])
      assertTrue(result.hasError, "Expecting ks not exists error")
    }
  }

  test ("Insert into non-existing table"){
    within(5000 millis){
      val query = "insert into demo (field1, field2) values ('test1','text2');"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      val result = expectMsgClass(classOf[QueryResult])
      assertTrue(result.hasError, "Expecting table not exists")
    }
  }

  test ("Create table"){
    within(5000 millis){
      val query = "create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
    }
  }

  test ("Create existing table"){
    within(5000 millis){
      val query = "create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      val result = expectMsgClass(classOf[QueryResult])
      assertTrue(result.hasError, "Expecting table exists")
    }
  }

  test ("Insert into table"){
    within(5000 millis){
      val query = "insert into demo (field1, field2) values ('text1','text2');"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
    }
  }

  test ("Select"){
    within(5000 millis){
      val query = "select * from demo ;"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
      assertEquals(result.getResultSet.size(), 1, "Cannot retrieve data")
      val r = result.getResultSet.iterator().next()
      assertEquals(r.getCells.get("field1").getValue, "text1", "Invalid row content")
      assertEquals(r.getCells.get("field2").getValue, "text2", "Invalid row content")
    }
  }

  test ("Drop table"){
    within(5000 millis){
      val query = "drop table demo ;"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
    }
  }

  test ("Drop keyspace"){
    within(5000 millis){
      val query = "drop keyspace ks_demo ;"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      expectMsgClass(classOf[ACK])
      val result = expectMsgClass(classOf[QueryResult])
      assertFalse(result.hasError, "Error not expected: " + result.getErrorMessage)
    }
  }

  test ("Drop non-existing keyspace"){
    within(5000 millis){
      val query = "drop keyspace ks_demo ;"
      serverRef ! new Query("server-actor", "ks_demo", query, "test")
      val result = expectMsgClass(classOf[QueryResult])
      assertTrue(result.hasError, "Expecting table exists")
    }
  }

}









