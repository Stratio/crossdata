/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.server.planner

import com.stratio.meta.core.engine.Engine
import akka.actor.ActorSystem
import com.stratio.meta.server.actors.{PlannerActor,  ExecutorActor}
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import com.stratio.meta.common.result._
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import org.testng.Assert._
import scala.util.Success
import com.stratio.meta.server.utilities._
import scala.collection.mutable
import com.stratio.meta.server.config.{ActorReceiveUtils, BeforeAndAfterCassandra}
import com.stratio.meta.communication.ACK
import com.stratio.meta.communication.ACK
import scala.util.Success
import com.stratio.meta.common.ask.Query
import java.util.UUID

/**
 * Planner actor tests.
 */
class BasicPlannerActorTest extends ActorReceiveUtils with FunSuiteLike with BeforeAndAfterCassandra {

  val engine:Engine =  createEngine.create()

  lazy val executorRef = system.actorOf(ExecutorActor.props(engine.getExecutor),"TestExecutorActor")
  lazy val plannerRef = system.actorOf(PlannerActor.props(executorRef,engine.getPlanner),"TestPlanerActor")
  lazy val plannerRefTest= system.actorOf(PlannerActor.props(testActor,engine.getPlanner),"TestPlanerActorTest")

  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  def executeStatement(query: String, keyspace: String, shouldExecute: Boolean) : Result = {
    val parsedStmt = engine.getParser.parseStatement(UUID.randomUUID().toString, "ks_demo", query)
    parsedStmt.setSessionCatalog(keyspace)
    val stmt=engine.getValidator.validateQuery(parsedStmt)
    plannerRef ! stmt

    val result = receiveActorMessages(shouldExecute, false, !shouldExecute)

    if(shouldExecute) {
      assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
                                   + "\n error: " + getErrorMessage(result))
    }else{
      assertTrue(result.hasError, "Statement should report an error")
    }

    result
  }

  test("executor resend to executor message 1"){
    within(5000 millis){
      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(UUID.randomUUID().toString(), "ks_demo1", query)
      stmt.setSessionCatalog("")
      val stmt1=engine.getValidator.validateQuery(stmt)
      plannerRefTest ! stmt1
      expectMsgClass(classOf[ACK])
      expectMsg(engine.getPlanner.planQuery(stmt1))

    }
  }

  test("executor resend to executor message 2"){
    within(5000 millis){
      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(UUID.randomUUID().toString, "ks_demo", query)
      stmt.setSessionCatalog("")
      val stmt1=engine.getValidator.validateQuery(stmt)
      stmt1.setErrorMessage(ErrorType.PARSING, "Error creating KEYSPACE ks_demo1 - resent 2")
      plannerRefTest ! stmt1
      val result = expectMsgClass(classOf[Result])
      assertTrue(result.hasError, "Error expected");
    }
  }

  test("executor resend to executor message 3"){
    within(5000 millis){
      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(UUID.randomUUID().toString, "ks_demo", query)
      stmt.setSessionCatalog("ks_demo1")
      val stmt1=engine.getValidator.validateQuery(stmt)
      stmt1.setErrorMessage(ErrorType.PARSING, "it is a test of error")
      var complete:Boolean=true
      val futureExecutorResponse=plannerRefTest.ask(stmt1)(2 second)
      try{
        val result = Await.result(futureExecutorResponse, 1 seconds)
      }catch{
        case ex:Exception => {
          println("\n\n\n"+ex.getMessage+"\n\n\n")
          complete=false
        }
      }
      if (complete&&futureExecutorResponse.isCompleted){
        val value_response= futureExecutorResponse.value.get

        value_response match{
        case Success(value:Result)=>
          if (value.hasError){
            assertEquals(getErrorMessage(value),"it is a test of error")
          }
        case _ =>
            fail("Invalid response");
        }
      }
    }
  }

  test ("Unknown message"){
    within(5000 millis){
      plannerRef ! 1
      val result = expectMsgClass(classOf[ErrorResult])
      assertTrue(result.hasError, "Expecting error message")
    }
  }

  test ("Create catalog"){
    within(5000 millis){
      val msg= "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      executeStatement(msg, "", true)
    }
  }

  test ("Create existing catalog"){
    within(7000 millis){
      val msg="create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      executeStatement(msg, "", false)
    }
  }

  test ("Use keyspace"){
    within(5000 millis){
      val msg = "use ks_demo ;"
      val result = executeStatement(msg, "", true)
      assertTrue(result.isInstanceOf[QueryResult], "Invalid result type")
      val r = result.asInstanceOf[QueryResult]
      assertTrue(r.isCatalogChanged, "New keyspace should be used");
      assertEquals(r.getCurrentCatalog, "ks_demo", "New keyspace should be used");
    }
  }

  test ("validatorActor use KS from current catalog"){
    within(5000 millis){
      val msg = "use ks_demo ;"
      val result = executeStatement(msg, "ks_demo", true)
      assertTrue(result.isInstanceOf[QueryResult], "Invalid result type")
      val r = result.asInstanceOf[QueryResult]
      assertTrue(r.isCatalogChanged, "New keyspace should be used");
      assertEquals(r.getCurrentCatalog, "ks_demo", "New keyspace should be used");
    }
  }

  test ("Insert into non-existing table"){
    within(7000 millis){
      val msg="insert into demo (field1, field2) values ('test1','text2');"
      executeStatement(msg, "ks_demo", false)
    }
  }

  test ("Select from non-existing table"){
  within(7000 millis){
      val msg="select * from unknown ;"
      executeStatement(msg, "ks_demo", false)
    }
  }

  test ("Create table"){
    within(5000 millis){
      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      executeStatement(msg, "ks_demo", true)
    }
  }

  test ("Create existing table"){
    within(7000 millis){
      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      executeStatement(msg, "ks_demo", false)
    }
  }

  test ("Insert into table"){
    within(5000 millis){
      val msg="insert into demo (field1, field2) values ('text1','text2');"
      executeStatement(msg, "ks_demo", true)
    }
  }

  test ("Select"){
    within(5000 millis){
      val msg="select * from demo ;"
      var result = executeStatement(msg, "ks_demo", true)
      assertFalse(result.hasError, "Error not expected: " + getErrorMessage(result))
      val queryResult = result.asInstanceOf[QueryResult]
      assertEquals(queryResult.getResultSet.size(), 1, "Cannot retrieve data")
      val r = queryResult.getResultSet.iterator().next()
      assertEquals(r.getCells.get("field1").getValue, "text1", "Invalid row content")
      assertEquals(r.getCells.get("field2").getValue, "text2", "Invalid row content")
    }
  }

  test ("Drop table"){
    within(5000 millis){
      val msg="drop table demo ;"
      executeStatement(msg, "ks_demo", true)
    }
  }

  test ("Drop keyspace"){
    within(5000 millis){
      val msg="drop keyspace ks_demo ;"
      executeStatement(msg, "ks_demo", true)
    }
  }

  test ("Drop non-existing keyspace"){
    within(7000 millis){
      val msg="drop keyspace ks_demo ;"
      executeStatement(msg, "ks_demo", false)
    }
  }

}



