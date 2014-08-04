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

class CreateKeyspaceActorTest extends ActorReceiveUtils with FunSuiteLike with BeforeAndAfterCassandra {

  val engine:Engine =  createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"create-keyspace-actor")

  def executeStatement(query: String, keyspace: String) : Result = {
    val stmt = Query("create-keyspace", keyspace, query, "test_actor")
    serverRef ! stmt
    val result = receiveActorMessages(true, false, false)

    assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
      + "\n error: " + getErrorMessage(result))

    result
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  test ("Create keyspace SimpleStrategy ok"){
    dropKeyspaceIfExists("demo1")
    val query = "create KEYSPACE demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
    within(5000 millis){
      executeStatement(query, "")
    }
  }

  test ("Create keyspace NetworkTopolyStrategy quoted ok"){
    dropKeyspaceIfExists("demo1")
    val query = "CREATE KEYSPACE demo1 WITH replication = {'class': 'NetworkTopologyStrategy'};"
    within(5000 millis){
      executeStatement(query, "")
    }
  }
}
