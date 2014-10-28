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

package com.stratio.crossdata.server.server

import akka.actor.Props
import com.stratio.crossdata.common.ask.{Connect, Query}
import com.stratio.crossdata.common.result.{ErrorResult, ConnectResult}
import com.stratio.crossdata.server.config.ActorReceiveUtils
import com.stratio.crossdata.server.utilities.{createEngine}
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.core.engine.Engine
import com.stratio.crossdata.server.actors.ServerActor
import org.scalatest.FunSuiteLike
import org.testng.Assert.{assertFalse, assertTrue, assertNotEquals}

import scala.concurrent.duration._

/**
 * Server Actor tests.
 */
class BasicServerActorTest extends ActorReceiveUtils with FunSuiteLike {
  lazy val serverRef = system.actorOf(Props(classOf[ServerActor], engine), "test")
  val engine: Engine = createEngine.create()


  def executeStatement(query: String, keyspace: String, shouldExecute: Boolean): Result = {
    val stmt = Query("basic-server", keyspace, query, "test_actor")

    serverRef ! stmt
    val result = receiveActorMessages(shouldExecute, false, !shouldExecute)

    if (shouldExecute) {
      assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
        + "\n error: " )
    } else {
      assertTrue(result.hasError, "Statement should report an error")
    }

    result
  }

  test("Unknown message") {
    within(5000 millis) {
      serverRef ! 1
      val result = expectMsgClass(classOf[ErrorResult])
      assertTrue(result.hasError, "Expecting error message")
    }
  }

  test("ServerActor Test connect without error") {
    within(5000 millis) {
      serverRef ! Connect("test-user")
      val result = expectMsgClass(classOf[ConnectResult])
      assertFalse(result.hasError, "Error not expected")
      assertNotEquals(result.asInstanceOf[ConnectResult].getSessionId, -1, "Expecting session id")
    }
  }



}









