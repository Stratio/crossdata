package com.stratio.meta.server.query

import akka.actor.ActorSystem
import com.stratio.meta.core.engine.{Engine, EngineConfig}
import org.testng.annotations.{Test, BeforeTest}
import akka.testkit.TestActorRef
import com.stratio.meta.server.actors.QueryActor
import akka.pattern.ask
import com.stratio.meta.common.ask.Query
import com.stratio.meta.common.result.MetaResult
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


class QueryActorTest {
  val system: ActorSystem = ActorSystem.create("TestSystem")
  val engineConfig: EngineConfig = {
    val result=new EngineConfig
    result.setCassandraHosts(Array[String]("127.0.0.1"))
    result.setCassandraPort(9042)
    result
  }
  lazy val engine: Engine = new Engine(engineConfig)

  @BeforeTest def init() = {

  }
  @Test def basicTest() = {
    val queryActor = TestActorRef.create(system,QueryActor.props(engine))
    val createKs: String = "CREATE KEYSPACE testKS WITH replication = {class: SimpleStrategy, replication_factor: 1};"

    val futureCreate: Future[Any] = queryActor.ask(new Query("system",createKs, "test"))(1000 second)
    val resultCreate= Await.result(futureCreate.mapTo[MetaResult],1000 second)
    resultCreate.print()

    val dropKs: String = "DROP KEYSPACE testks;"

    val futureDrop: Future[Any] = queryActor.ask(new Query("system",dropKs, "test"))(1000 second)
    val resultDrop= Await.result(futureDrop.mapTo[MetaResult],1000 second)
    resultDrop.print()


  }
}
