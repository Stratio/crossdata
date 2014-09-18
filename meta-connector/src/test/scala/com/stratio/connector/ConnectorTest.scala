package com.stratio.connector

import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta2.common.data.CatalogName
import com.stratio.meta2.core.query._
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
 
class ConnectorTest extends FunSuite with MockFactory{

	test("Basic Connector Mock") {
		val m = mock[IConnector]
		(m.getConnectorName _).expects().returning("My New Connector")
		assert(m.getConnectorName().equals("My New Connector"))
	}

  test("Basic Connector App listening on a given port does not break") {
    val port="2558"
		val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port="+port).withFallback(ConfigFactory.load())
		val c = new ConnectorApp()
		val myReference=c.startup(m, port, config)
		myReference ! "Hello World"
    assert("Hello World" == "Hello World")
	}

  test("Send MetadataInProgressQuery to Connector") {
    	val port="2559"
		  val m = mock[IConnector]
		  (m.getConnectorName _).expects().returning("My New Connector")
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port="+port).withFallback(ConfigFactory.load())
		  val c = new ConnectorApp()
		  val myReference=c.startup(m, port, config)

      val pq = new SelectPlannedQuery(
        new ValidatedQuery(
          new NormalizedQuery(
            new SelectParsedQuery(
              new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog") )
              ,null)
          )
        ), new LogicalWorkflow(null)
      )

      val metadataQ:MetadataInProgressQuery=new MetadataInProgressQuery(pq)
      //myReference ! ConnectToConnector("cassandra connector")
      myReference ! metadataQ

		  //val steps:List<LogicalStep>=null
    /*
    val myworkflow:LogicalWorkflow=new LogicalWorkflow(null)
    myReference ! Execute(myworkflow)
    myReference ! DisconnectFromConnector("cassandra connector")
    myReference ! Request("Select * from mytable;")
    myReference ! Response("Select * from mytable;")
    myReference ! MetadataStruct("clustername", "connectorName", "metaData")
    myReference ! StorageQueryStruct("clusterName", "connectorName", "storageQuery")
    myReference ! WorkflowStruct("clusterName", "connectorName", myworkflow)

      assert("Hello World" == "Hello World")
    */
  }

  //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE

}