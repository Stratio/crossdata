package com.stratio.meta.server.server.statements

import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import com.stratio.meta.server.utilities.{createEngine, TestKitUsageSpec}
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.server.actors.ServerActor
import org.testng.Assert._
import com.stratio.meta.common.result.Result
import com.stratio.meta.common.ask.Query
import scala.concurrent.{Await, Future}
import akka.pattern.ask
import scala.concurrent.duration._


class CreateKeyspaceActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
  with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra{

  lazy val engine:Engine =  createEngine.create()

  lazy val serverRef = system.actorOf(Props(classOf[ServerActor],engine),"create-keyspace-actor")

  def executeStatement(query: String, keyspace: String) : Result = {
    val stmt = Query(keyspace, query, "test_actor")
    val futureExecutorResponse:Future[Any]= {
      serverRef.ask(stmt)(3 second)
    }

    var result : Result = null
    try{
      val r = Await.result(futureExecutorResponse, 3 seconds)
      result = r.asInstanceOf[Result]
    }catch{
      case ex:Exception =>
      fail("Cannot execute statement: " + stmt.toString + " Exception: " + ex.getMessage)
    }

    assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
      + "\n error: " + result.getErrorMessage)

    result
  }

  test ("Create keyspace SimpleStrategy ok"){
    val query = "create KEYSPACE demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
    within(3000 millis){
      executeStatement(query, "")
    }
    dropKeyspaceIfExists("demo1")

  }

  test ("Create keyspace NetworkTopolyStrategy quoted ok"){
    val query = "CREATE KEYSPACE demo1 WITH replication = {'class': 'NetworkTopologyStrategy'};"
    within(3000 millis){
      executeStatement(query, "")
    }
    dropKeyspaceIfExists("demo1")
  }
}
