package com.stratio.meta.server.server.statements

import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import com.stratio.meta.server.utilities.{createEngine, TestKitUsageSpec}
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.actors.ServerActor
import com.stratio.meta.common.result.{QueryResult, Result}
import scala.concurrent.{Await, Future}
import org.testng.Assert._
import com.stratio.meta.common.ask.Query
import akka.pattern.ask
import scala.concurrent.duration._
import com.stratio.meta.test.CCMHandler
import org.apache.log4j.Logger


class CreateIndexActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra {

  lazy val engine:Engine =  createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"create-keyspace-actor")

  /**
   * Class logger.
   */
  private final val logger: Logger = Logger.getLogger(classOf[CreateIndexActorTest])

  def executeStatement(query: String, keyspace: String) : Result = {
    val stmt = Query(keyspace, query, "test_actor")
    val futureExecutorResponse:Future[Any]= {
      serverRef.ask(stmt)(20 second)
    }

    var result : Result = null
    try{
      val r = Await.result(futureExecutorResponse, 20 seconds)
      result = r.asInstanceOf[Result]
    }catch{
      case ex:Exception =>
        fail("Cannot execute statement: " + stmt.toString + " Exception: " + ex.getMessage)
    }

    assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
      + "\n error: " + result.getErrorMessage)

    result
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    loadTestData("demo_server", "demoServerKeyspace.cql")
  }

  def waitForLucene(){
    Thread.sleep(2000);
  }


  def createLuceneIndex_ok(iteration : Int) = {
    val createQuery = "CREATE LUCENE INDEX ON demo_server.users_info(info);"
    val selectQuery = "SELECT * FROM demo_server.users_info WHERE info MATCH 'In*';"
    val expectedTuples = 10;
    val dropQuery = "DROP INDEX demo_server.users_info;"

    logger.info("Create Lucene Index iteration: " + iteration)

    //Create the index
    within(15000 millis){
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
    createLuceneIndex_ok(0)
  }

/*
  test ("Create Lucene index stress ok"){
    var iteration = 0
    for(iteration <- 1 to 100){
      createLuceneIndex_ok(iteration)
    }
  }
*/


}
