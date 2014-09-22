package com.stratio.connectors

import java.util

import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.common.logicalplan.{LogicalStep, LogicalWorkflow}
import com.stratio.meta2.common.data.{CatalogName, TableName}
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.SelectStatement
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Suite}
//import com.stratio.meta.server.config.{ServerConfig, ActorReceiveUtils}


//class ConnectorTest extends FunSuite with MockFactory with ServerConfig{
class ConnectorTest extends FunSuite with MockFactory {
   this:Suite =>


  lazy val logger =Logger.getLogger(classOf[ConnectorTest])

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
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)
    var steps: util.ArrayList[LogicalStep] = new util.ArrayList[LogicalStep]()
    val pq = new SelectPlannedQuery(
        new SelectValidatedQuery(
            new SelectParsedQuery(
              new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog") )
              ,new SelectStatement(new TableName("myCatalog","myTable"))
          )
        ), new LogicalWorkflow(steps)
    )
    val selectQ: SelectInProgressQuery = new SelectInProgressQuery(pq)
    /*
    val beanMap: util.Map[String, String] = BeanUtils.recursiveDescribe(selectQ);
    for (s <- beanMap.keySet().toArray()) {
      println(s);
    }
    */
    myReference ! selectQ
    c.shutdown()
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
        new MetaDataParsedQuery(
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

