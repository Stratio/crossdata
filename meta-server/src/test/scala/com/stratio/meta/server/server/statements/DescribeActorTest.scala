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

package com.stratio.meta.server.server.statements

import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import com.stratio.meta.server.utilities.{createEngine, TestKitUsageSpec}
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.config.{ActorReceiveUtils, BeforeAndAfterCassandra}
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.actors.ServerActor
import org.testng.Assert._
import com.stratio.meta.common.result.Result
import com.stratio.meta.common.ask.Query
import scala.concurrent.{Await, Future}
import akka.pattern.ask
import scala.concurrent.duration._
import com.stratio.meta.communication.ACK

class DescribeActorTest extends ActorReceiveUtils with FunSuiteLike with BeforeAndAfterCassandra {

  val engine:Engine =  createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"describe-actor")

  def executeStatement(query: String, keyspace: String, shouldExecute: Boolean) : Result = {
    val stmt = Query("describe", keyspace, query, "test_actor")

    serverRef ! stmt
    val result = receiveActorMessages(shouldExecute, false, !shouldExecute)
  
    if(shouldExecute) {
      assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
        + "\n error: " + getErrorMessage(result))
    }else{
      assertTrue(result.hasError, "Statement should report an error")
    }

    result
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  test ("describe keyspace system"){
    val query = "describe keyspace system;"
    within(7000 millis){
      val result = executeStatement(query, "", true)
      assertNotNull(result, "Cannot describe keyspace system")
    }
  }

  test ("describe table system.schema_columns"){
    val query = "describe table system.schema_columns;"
    within(7000 millis){
      val result = executeStatement(query, "", true)
      assertNotNull(result, "Cannot describe table system.schema_columns")
    }
  }

  test ("describe table schema_columns on keyspace system"){
    val query = "describe table schema_columns;"
    within(7000 millis){
      val result = executeStatement(query, "system", true)
      assertNotNull(result, "Cannot describe schema_columns on keyspace system")
    }
  }

  test ("describe keyspace unknown should fail"){
    val query = "describe keyspace unknown;"
    within(7000 millis){
      val result = executeStatement(query, "", false)
    }
  }

  test ("describe table schema_columns on unknown keyspace should fail"){
    val query = "describe table unknown.schema_columns;"
    within(7000 millis){
      val result = executeStatement(query, "", false)
    }
  }

}
