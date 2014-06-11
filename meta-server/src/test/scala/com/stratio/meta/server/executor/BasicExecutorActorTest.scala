package com.stratio.meta.server.executor

import com.stratio.meta.core.engine.Engine
import akka.actor.ActorSystem
import com.stratio.meta.server.actors.ExecutorActor
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import scala.concurrent.duration._
import org.testng.Assert._
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.stratio.meta.server.utilities._
import scala.collection.mutable
import com.stratio.meta.common.result.{ErrorResult, QueryResult, CommandResult, Result}
import com.stratio.meta.communication.ACK

/**
 * To generate unit test of proxy actor
 */
class BasicExecutorActorTest extends TestKit(ActorSystem("TestKitUsageExectutorActorSpec",
  ConfigFactory.parseString(TestKitUsageSpec.config)))
                                     with ImplicitSender with DefaultTimeout with FunSuiteLike with  BeforeAndAfterCassandra{

  lazy val engine:Engine =  createEngine.create()

  lazy val executorRef = system.actorOf(ExecutorActor.props(engine.getExecutor),"TestExecutorActor")

  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def beforeAll(){
    super.beforeAll()
    dropKeyspaceIfExists("ks_demo")
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  def executeStatement(query: String, keyspace: String, shouldExecute: Boolean, errorMessage: String) : Result = {
    val parsedStmt = engine.getParser.parseStatement(query)
    parsedStmt.setSessionKeyspace(keyspace)
    val validatedStmt=engine.getValidator.validateQuery(parsedStmt)
    val stmt = engine.getPlanner.planQuery(validatedStmt)
    executorRef ! stmt

    val result = expectMsgClass(classOf[Result])

    if(shouldExecute) {
      assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
                                   + "\n error: " + getErrorMessage(result) + " " + errorMessage)
    }else{
      assertTrue(result.hasError, "Statement should report an error. " + errorMessage)
    }

    result
  }

  test ("Unknown message"){
    within(5000 millis){
      executorRef ! 1
      val result = expectMsgClass(classOf[ErrorResult])
      assertTrue(result.hasError, "Expecting error message")
    }
  }

  test ("Create catalog"){
    within(5000 millis){
      val msg= "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      executeStatement(msg, "", true, "Keyspace should be created")
    }
  }

  test ("Create existing catalog"){
    within(5000 millis){
      val msg="create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      executeStatement(msg, "", false, "Keyspace ks_demo already exists.")
    }
  }

  test ("Use keyspace"){
    within(5000 millis){
      val msg="use ks_demo ;"
      val result = executeStatement(msg, "", true, "Keyspace should be used.")
      assertTrue(result.isInstanceOf[QueryResult], "Invalid result type")
      val r = result.asInstanceOf[QueryResult]
      assertTrue(r.isCatalogChanged, "New keyspace should be used");
      assertEquals(r.getCurrentCatalog, "ks_demo", "New keyspace should be used");
    }
  }

  test ("Use keyspace from keyspace"){
    within(5000 millis){
      val msg = "use ks_demo ;"
      val result = executeStatement(msg, "ks_demo", true, "Keyspace should be used.")
      assertTrue(result.isInstanceOf[QueryResult], "Invalid result type")
      val r = result.asInstanceOf[QueryResult]
      assertTrue(r.isCatalogChanged, "New keyspace should be used");
      assertEquals(r.getCurrentCatalog, "ks_demo", "New keyspace should be used");
    }
  }
 
  test ("Insert into non-existing table"){
    within(5000 millis){
      val msg="insert into demo (field1, field2) values ('test1','text2');"
      executeStatement(msg, "ks_demo", false, "Table demo does not exist.")
    }
  }

  test ("Select from non-existing table"){
    within(5000 millis){
      val msg="select * from unknown ;"
      executeStatement(msg, "ks_demo", false, "Table unknown does not exist.")
    }
  }

  test ("Create table"){
    within(5000 millis){
      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      executeStatement(msg, "ks_demo", true, "Table should be created.")
    }
  }

  test ("Create existing table"){
    within(5000 millis){
      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      executeStatement(msg, "ks_demo", false, "Table already exists.")
    }
  }

  test ("Insert into table"){
    within(5000 millis){
      val msg="insert into demo (field1, field2) values ('text1','text2');"
      executeStatement(msg, "ks_demo", true, "Insert should be possible.")
    }
  }

  test ("Select"){
    within(5000 millis){
      val msg="select * from demo ;"
      var result = executeStatement(msg, "ks_demo", true, "Select should work.")
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
      executeStatement(msg, "ks_demo", true, "Drop should work.")
    }
  }

  test ("Drop keyspace"){
    within(5000 millis){
      val msg="drop keyspace ks_demo ;"
      executeStatement(msg, "ks_demo", true, "Drop should work.")
    }
  }

  test ("Drop non-existing keyspace"){
    within(5000 millis){
      val msg="drop keyspace ks_demo ;"
      executeStatement(msg, "ks_demo", false, "Expecting keyspace not exists.")
    }
  }

}
