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

package com.stratio.meta2.server.server

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.stratio.meta.common.ask.{APICommand, Command}
import com.stratio.meta.common.result.{MetadataResult, Result}
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.stratio.meta.server.utilities.{TestKitUsageSpec, createEngine}
import com.stratio.meta2.core.engine.Engine
import com.stratio.meta2.server.actors.ServerActor
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import org.testng.Assert._

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * To generate unit test of query actor
 */
class APIServerActorTest extends TestKit(ActorSystem("TestKitUsageSpec", ConfigFactory.parseString(TestKitUsageSpec.config)))
with ImplicitSender with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra {

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor], engine), "api-commands-actor")
  val engine: Engine = createEngine.create()

  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    loadTestData("demo_server", "demoServerKeyspace.cql")
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  def executeAPICommand(cmd: Command, shouldExecute: Boolean): MetadataResult = {

    val futureExecutorResponse: Future[Any] = {
      serverRef.ask(cmd)(3 second)
    }

    var result: MetadataResult = null
    var returned: Any = null
    try {
      returned = Await.result(futureExecutorResponse, 3 seconds)
    } catch {
      case ex: Exception =>
        fail("Cannot execute API command: " + cmd.toString + " Exception: " + ex.getMessage)
    }

    if (shouldExecute) {
      assertFalse(returned.asInstanceOf[Result].hasError,
        "API execution failed for:\n" + cmd.toString
          + "\n error: " + getErrorMessage(returned.asInstanceOf[Result]))
      result = returned.asInstanceOf[MetadataResult]
    } else {
      assertTrue(returned.asInstanceOf[Result].hasError, "API execution should report an error")
    }

    result
  }

  test("API List catalogs") {
    val cmd: Command = new Command(APICommand.LIST_CATALOGS, null)
    var result: MetadataResult = null
    within(5000 millis) {
      result = executeAPICommand(cmd, true)
    }
    //Check that demo_server exists
    assertNotNull(result.getCatalogList, "Cannot obtain catalog list")
    assertTrue(result.getCatalogList.contains("demo_server"), "Cannot find demo_server")
  }

  test("API List tables") {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add("demo_server")
    val cmd: Command = new Command(APICommand.LIST_TABLES, params)
    var result: MetadataResult = null
    within(5000 millis) {
      result = executeAPICommand(cmd, true)
    }
    //Check that table demo_server exists
    assertNotNull(result.getTableList, "Cannot obtain table list")
    var retrieved = ListBuffer[String]()
    val it = result.getTableList.iterator
    while (it.hasNext) {
      retrieved += it.next().getTableName
    }

    val toCheck = List("users", "users_info")
    toCheck.foreach(
      table => assertTrue(retrieved.contains(table), "Cannot find table " + table))

  }

  test("API List tables from unknown catalog") {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add("unknown")
    val cmd: Command = new Command(APICommand.LIST_TABLES, params)
    var result: MetadataResult = null
    within(5000 millis) {
      result = executeAPICommand(cmd, false)
    }
  }

}









