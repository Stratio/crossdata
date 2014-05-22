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

import com.stratio.meta.common.ask.{APICommand, Command}
import com.stratio.meta.common.result.MetadataResult
import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import com.stratio.meta.server.utilities.{createEngine, TestKitUsageSpec}
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.actors.ServerActor
import org.testng.Assert._
import scala.concurrent.{Await, Future}
import akka.pattern.ask
import scala.concurrent.duration._
import scala.collection.JavaConversions._

/**
 * To generate unit test of query actor
 */
class APIServerActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra
{

  lazy val engine:Engine =  createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"api-commands-actor")

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

  def executeAPICommand(cmd: Command, shouldExecute: Boolean) : MetadataResult = {

    val futureExecutorResponse:Future[Any]= {
      serverRef.ask(cmd)(3 second)
    }

    var result : MetadataResult = null
    try{
      val r = Await.result(futureExecutorResponse, 3 seconds)
      result = r.asInstanceOf[MetadataResult]
    }catch{
      case ex:Exception =>
        fail("Cannot execute API command: " + cmd.toString + " Exception: " + ex.getMessage)
    }

    if(shouldExecute) {
      assertFalse(result.hasError,
        "API execution failed for:\n" + cmd.toString + "\n error: " + result.getErrorMessage)
    }else{
      assertTrue(result.hasError, "API execution should report an error")
    }

    result
  }

  test ("API List catalogs"){
    val cmd: Command = new Command(APICommand.LIST_CATALOGS, null)
    var result : MetadataResult = null
    within(5000 millis){
      result = executeAPICommand(cmd, true)
    }
    //Check that demo_server exists
    assertNotNull(result.getCatalogList, "Cannot obtain catalog list")
    assertTrue(result.getCatalogList.contains("demo_server"), "Cannot find demo_server")
  }

  test ("API List tables"){
    var params : java.util.List[String] = new java.util.ArrayList[String]
    params.add("demo")
    val cmd: Command = new Command(APICommand.LIST_TABLES, params)
    var result : MetadataResult = null
    within(5000 millis){
      result = executeAPICommand(cmd, true)
    }
    //Check that table demo_server exists
    assertNotNull(result.getCatalogList, "Cannot obtain catalog list")
    val toCheck = List("users", "users_info")
    toCheck.foreach(
      table => assertTrue(result.getTableList.contains(table), "Cannot find table " + table))

  }

}









