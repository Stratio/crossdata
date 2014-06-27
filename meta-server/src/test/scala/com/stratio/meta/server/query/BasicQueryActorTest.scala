package com.stratio.meta.server.query

import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.actors._
import scala.concurrent.duration._
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.utilities._
import com.stratio.meta.server.config.{ActorReceiveUtils, BeforeAndAfterCassandra}
import com.stratio.meta.common.ask.Query
import akka.pattern.ask
import org.testng.Assert._
import com.stratio.meta.common.result.{Result, ErrorResult, QueryResult}
import com.stratio.meta.communication.ACK

class BasicQueryActorTest extends ActorReceiveUtils with FunSuiteLike with BeforeAndAfterCassandra {

  val engine:Engine = createEngine.create()

  lazy val queryRef = system.actorOf(Props(classOf[QueryActor],engine))

  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  def executeStatement(query: String, keyspace: String, shouldExecute: Boolean) : Result = {
    val stmt = Query("basic-server", keyspace, query, "test_actor")

    queryRef ! stmt
    val result = receiveActorMessages(shouldExecute, false, !shouldExecute)

    if(shouldExecute) {
      assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
                                   + "\n error: " + getErrorMessage(result))
    }else{
      assertTrue(result.hasError, "Statement should report an error")
    }

    result
  }

  test ("Unknown message"){
    within(5000 millis){
      queryRef ! 1
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


