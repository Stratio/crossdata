package com.stratio.meta.server.validator

import com.stratio.meta.core.engine.Engine
import akka.actor.ActorSystem
import com.stratio.meta.server.actors.{ValidatorActor, PlannerActor, ExecutorActor}
import akka.testkit._
import org.scalatest.FunSuiteLike
import com.stratio.meta.common.result._
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import org.testng.Assert._
import scala.util.Success
import com.stratio.meta.server.utilities._
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.typesafe.config.ConfigFactory
import scala.collection.mutable
import com.stratio.meta.common.ask.Query
import com.stratio.meta.communication.ACK
import com.stratio.meta.communication.ACK
import scala.util.Success

/**
 * Validator actor tests.
 */
class BasicValidatorActorTest extends TestKit(ActorSystem("TestKitUsageExectutorActorSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
                                      with ImplicitSender with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra{

  lazy val engine:Engine =  createEngine.create()
  lazy val executorRef = system.actorOf(ExecutorActor.props(engine.getExecutor),"TestExecutorActor")
  lazy val plannerRef = system.actorOf(PlannerActor.props(executorRef,engine.getPlanner),"TestPlanerActor")
  lazy val validatorRef = system.actorOf(ValidatorActor.props(plannerRef,engine.getValidator),"TestValidatorActor")

  /**
   * Validator actor that sends messages to the current actor.
   */
  lazy val validatorRefTest= system.actorOf(ValidatorActor.props(testActor,engine.getValidator),"TestPlanerActorTest")

  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  def executeStatement(query: String, keyspace: String, shouldExecute: Boolean, errorMessage: String) : Result = {
    val stmt = engine.getParser.parseStatement(query)
    stmt.setSessionKeyspace(keyspace)
    validatorRef ! stmt
    if(shouldExecute) {
      expectMsgClass(classOf[ACK])
    }

    val result = expectMsgClass(classOf[Result])

    if(shouldExecute) {
      assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
                                   + "\n error: " + getErrorMessage(result) + " " + errorMessage)
    }else{
      assertTrue(result.hasError, "Statement should report an error. " + errorMessage)
    }

    result
  }

  test("validator resend to planner message 1"){
    within(5000 millis){
      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(query)
      stmt.setSessionKeyspace("")
      validatorRefTest ! stmt
      expectMsg(engine.getValidator.validateQuery(stmt))
    }
  }

  test("validator resend to planner message 2"){
    within(5000 millis){
      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(query)
      stmt.setSessionKeyspace("ks_demo1")
      stmt.setErrorMessage(ErrorType.VALIDATION, "Error creating KEYSPACE ks_demo1- resent 2")
      validatorRefTest ! stmt
      val result = expectMsgClass(classOf[Result])
      assertTrue(result.hasError, "Error expected");
    }
  }

  test("validator resend to planner message 3"){
    within(5000 millis){
      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(query)
      stmt.setSessionKeyspace("ks_demo1")
      stmt.setErrorMessage(ErrorType.VALIDATION, "it is a test of error")
      var complete:Boolean=true
      val futureExecutorResponse=validatorRefTest.ask(stmt)(2 second)
      try{
        val result = Await.result(futureExecutorResponse, 1 seconds)
      }catch{
        case ex:Exception =>
          println("\n\n\n"+ex.getMessage+"\n\n\n")
          complete=false

      }
      if (complete&&futureExecutorResponse.isCompleted){
        val value_response= futureExecutorResponse.value.get

        value_response match{
          case Success(value:Result)=>
            if (value.hasError){
              assertEquals(getErrorMessage(value),"it is a test of error")

          }
          case _ =>
        }

      }
    }
  }

  test ("Unknown message"){
    within(5000 millis){
      validatorRef ! 1
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
      val msg = "use ks_demo ;"
      val result = executeStatement(msg, "", true, "Keyspace should be used.")
      assertTrue(result.isInstanceOf[QueryResult], "Invalid result type")
      val r = result.asInstanceOf[QueryResult]
      assertTrue(r.isCatalogChanged, "New keyspace should be used");
      assertEquals(r.getCurrentCatalog, "ks_demo", "New keyspace should be used");
    }
  }

  test ("validatorActor use KS from current catalog"){
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




