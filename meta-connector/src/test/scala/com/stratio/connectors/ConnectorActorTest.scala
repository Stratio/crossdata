package com.stratio.connectors

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.util.Timeout
import com.stratio.meta.common.connector.{IConnector, IQueryEngine}
import com.stratio.meta.common.logicalplan.{LogicalStep, LogicalWorkflow}
import com.stratio.meta.common.result.QueryResult
import com.stratio.meta2.common.data.{CatalogName, TableName}
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.SelectStatement
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.concurrent.Await
import akka.pattern.ask

//import com.stratio.meta2.server.config.{ServerConfig, ActorReceiveUtils}
//import com.stratio.meta2.server.actors.{ConnectorManagerActor, CoordinatorActor}
//import org.scalamock.scalatest.MockFactory
import scala.concurrent.duration.DurationInt

//import com.stratio.meta2.server.config.ActorReceiveUtils




//class ConnectorActorTest extends FunSuite with MockFactory with ServerConfig{
//class ConnectorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory {
//class ConnectorActorTest extends FunSuite with MockFactory {
class ConnectorActorTest extends TestKit(ActorSystem()) with FunSuiteLike with MockFactory {



  implicit val timeout = Timeout(3 seconds) // needed for `?` below
  //lazy val logger =Logger.getLogger(classOf[ConnectorActorTest])

  test("Basic Connector Mock") {
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    assert(m.getConnectorName().equals("My New Connector"))
  }

  test("Basic Connector App listening on a given port does not break") {
    val port = "2558"
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)
    myReference ! "Hello World"
    assert("Hello World" == "Hello World")
    c.shutdown()
  }



  test("Send SelectInProgressQuery to Connector") {
    val port = "2559"
    val m = mock[IConnector]
    val qe = mock[IQueryEngine]
    (qe.execute _).expects(*,*).returning(QueryResult.createSuccessQueryResult())
    (m.getQueryEngine _).expects().returning(qe)
    (m.getConnectorName _).expects().returning("My New Connector")

    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)
    var steps: java.util.ArrayList[LogicalStep] = new java.util.ArrayList[LogicalStep]()
    val pq = new SelectPlannedQuery(
        new SelectValidatedQuery(
            new SelectParsedQuery(
              new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog") )
              ,new SelectStatement(new TableName("myCatalog","myTable"))
          )
        ), new LogicalWorkflow(steps)
    )
    val selectQ: SelectInProgressQuery = new SelectInProgressQuery(pq)
    //val beanMap: util.Map[String, String] = BeanUtils.recursiveDescribe(selectQ);
    //for (s <- beanMap.keySet().toArray()) { println(s); }

    val future=ask(myReference , selectQ)
    val result = Await.result(future, timeout.duration).asInstanceOf[String]
    println("receiving->"+result+" after sending select query")
    c.stop()
  }

  /*
  test("Send MetadataInProgressQuery to Connector") {
    val port = "2560"
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)

    var steps: util.ArrayList[LogicalStep] = new util.ArrayList[LogicalStep]()
    steps.add(null)
    val pq = new MetadataPlannedQuery(
      new MetadataValidatedQuery(
        new MetadataParsedQuery(
          new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog") )
          ,null
      ), null
      )
    )
    val metadataQ: MetadataInProgressQuery = new MetadataInProgressQuery(pq)
    val mystatement_before_sending_message = metadataQ.getStatement()
    //myReference ! ConnectToConnector("cassandra connector")
    myReference ! metadataQ
    c.shutdown()
  }

  test("Send StorageInProgressQuery to Connector") {
    val port = "2561"
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)

    var steps: util.ArrayList[LogicalStep] = new util.ArrayList[LogicalStep]()
    steps.add(null)
    val pq = new StorageInProgressQuery(
      new StoragePlannedQuery( null)
    )
    //TODO: create object
    val storageQ: StorageInProgressQuery = new StorageInProgressQuery(pq)
    val mystatement_before_sending_message = storageQ.getStatement()
    //myReference ! ConnectToConnector("cassandra connector")
    myReference ! storageQ
    c.shutdown()
  }

  //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE
  */

}

