package executor

import com.stratio.meta.core.engine.{Engine, EngineConfig}
import akka.actor.{ActorSystem, Props}
import com.stratio.meta.server.actors.{ExecutorActor}
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.stratio.meta.core.utils.MetaQuery
import com.stratio.meta.common.result.{QueryResult, Result}
import com.stratio.meta.common.data.CassandraResultSet

/**
 * Created by aalcocer on 4/8/14.
 * To generate unit test of proxy actor
 */
class BasicExecutorActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterAll
{

  val engineConfig: EngineConfig = {
    val result=new EngineConfig
    result.setCassandraHosts(Array[String]("127.0.0.1"))
    result.setCassandraPort(9042)
    result
  }
  lazy val engine: Engine = new Engine(engineConfig)

  val executorRef=system.actorOf(Props(classOf[ExecutorActor],engine.getExecutor))


  val query= new MetaQuery("create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};")
  executorRef ! query

  def receive= {
    case result:Result =>{
      println("algo hace"+ result.asInstanceOf[QueryResult].getResultSet.asInstanceOf[CassandraResultSet].getRows.get(0).getCells)

    }
  }

}




object TestKitUsageSpec {
  val config = """
    akka {
    loglevel = "WARNING"
    }
               """
}