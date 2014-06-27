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

package com.stratio.meta.server.server.statements

import akka.actor.{Props}
import com.stratio.meta.server.utilities.{createEngine}
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.config.{ActorReceiveUtils, BeforeAndAfterCassandra}
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.actors.ServerActor
import com.stratio.meta.common.result.{QueryResult, Result}
import org.testng.Assert._
import com.stratio.meta.common.ask.Query
import scala.concurrent.duration._
import org.apache.log4j.Logger

class CreateIndexActorTest extends ActorReceiveUtils with FunSuiteLike with BeforeAndAfterCassandra{

  val engine:Engine =  createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"create-keyspace-actor")

  /**
   * Class logger.
   */
  private final val logger: Logger = Logger.getLogger(classOf[CreateIndexActorTest])

  /**
   * Launch a query to the remote server.
   * @param query The query.
   * @param catalog The catalog.
   * @return The result message.
   */
  def executeStatement(query: String, catalog: String) : Result = {
  println("Execute: " + query)
    val stmt = Query("create-index", catalog, query, "test_actor")
    serverRef ! stmt
    val result = receiveActorMessages(true, false, false)

    assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
                                 + "\n error: " + getErrorMessage(result))

    result
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    dropKeyspaceIfExists("demo_server")
    loadTestData("demo_server", "demoServerKeyspace.cql")
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  def waitForLucene(){
    Thread.sleep(1100);
  }

  def createLuceneIndexOk(iteration : Int) = {
    val createQuery = "CREATE LUCENE INDEX ON demo_server.users_info(info);"
    val selectQuery = "SELECT * FROM demo_server.users_info WHERE info MATCH 'In*';"
    val expectedTuples = 10;
    val dropQuery = "DROP INDEX demo_server.users_info;"

    logger.info("Create Lucene Index iteration: " + iteration)
    //Create the index
    within(35000 millis){
      executeStatement(createQuery, "demo_server")
      assertTrue(checkColumnExists("demo_server", "users_info", "stratio_lucene_users_info"), "Stratio column not found")
      waitForLucene()
      val result = executeStatement(selectQuery, "demo_server")
      println(result)
      assertEquals(result.asInstanceOf[QueryResult].getResultSet.size(), expectedTuples, "Invalid number of tuples returned")
      executeStatement(dropQuery, "demo_server")
      assertFalse(checkColumnExists("demo_server", "users_info", "stratio_lucene_users_info"), "Stratio column should have been removed")
    }
  }

  test ("Create Lucene index ok"){
    createLuceneIndexOk(0)
  }

}
