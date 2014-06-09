package com.stratio.meta.server.query

import akka.actor.{Props, ActorSystem}
import com.stratio.meta.core.engine.{Engine, EngineConfig}
import org.testng.annotations.{AfterClass, Test}
import akka.testkit.{DefaultTimeout, TestKit, TestActorRef}
import com.stratio.meta.server.actors.QueryActor
import akka.pattern.ask
import com.stratio.meta.common.ask.Query
import com.stratio.meta.common.result.Result
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import org.apache.log4j.Logger
import com.typesafe.config.ConfigFactory
import com.stratio.meta.server.utilities.{createEngine, TestKitUsageSpec}
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.stratio.meta.communication.ACK
import org.scalatest.FunSuiteLike

class QueryActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
                             with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra{

  val logger: Logger = Logger.getLogger(classOf[Result])
  lazy val queryRef = system.actorOf(Props(classOf[QueryActor],engine))

  //val system: ActorSystem = ActorSystem.create("TestSystem")
//  val engineConfig: EngineConfig = {
//    val result=new EngineConfig
//    result.setCassandraHosts(Array[String]("127.0.0.1"))
//    result.setCassandraPort(9042)
//    result.setSparkMaster("local[2]")
//    result.setClasspathJars("");
//    result
//  }
//
//  lazy val engine: Engine = new Engine(engineConfig)

  lazy val engine:Engine =  createEngine.create()


  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  test ("KS query test"){
    fail("asf")
    val createKs: String = "CREATE KEYSPACE testKS WITH replication = {class: SimpleStrategy, replication_factor: 1};"
    within(5000 millis){
      queryRef ! new Query("query-actor", "system", createKs, "test")
      expectMsg("ACK")
    }
  }

//
//  @Test def basicTest() = {
//    //val queryActor = TestActorRef.create(system,QueryActor.props(engine))
//    //val qA = new QueryActor(engine)
//    //val qAR = TestActorRef(qA)
//
//
//    val createKs: String = "CREATE KEYSPACE testKS WITH replication = {class: SimpleStrategy, replication_factor: 1};"
//
//    //val futureCreate: Future[Any] = queryActor.ask(new Query("query-actor", "system", createKs, "test"))(1000 second)
//    queryRef ! new Query("query-actor", "system", createKs, "test")
//    expectMsg("ACK")
//    /*
//
//    val resultCreate= Await.result(futureCreate.mapTo[Result],1000 second)
//    logger.info(resultCreate)
//
//
//    val dropKs: String = "DROP KEYSPACE testks;"
//
//    val futureDrop: Future[Any] = queryActor.ask(new Query("query-actor", "system", dropKs, "test"))(1000 second)
//    val resultDrop= Await.result(futureDrop.mapTo[Result],1000 second)
//    logger.info(resultDrop)
//*/
//
//  }
}

