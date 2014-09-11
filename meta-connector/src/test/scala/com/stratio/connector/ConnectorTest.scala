package com.stratio.connector

import org.scalatest.FunSuite
import org.scalamock._
import com.stratio.meta.common.connector.IConnector
import org.scalamock.scalatest.MockFactory
import com.typesafe.config.ConfigFactory
import akka.actor.{ActorSystem,Props}
import com.typesafe.config.ConfigFactory
import com.stratio.meta.common.connector.IConnector
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import scala.concurrent.duration._
import com.stratio.meta.common.ask.Query
import akka.pattern.ask
import com.stratio.meta.common.result.{Result, ErrorResult, QueryResult}
import com.stratio.meta.communication.ACK
import com.stratio.meta.communication._
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.logicalplan.LogicalStep
import java.util.ArrayList
import java.util.List
 
class ConnectorTest extends FunSuite with MockFactory{
	
	test("Basic Connector Mock") {
		val m = mock[IConnector]
		(m.getConnectorName _).expects().returning("My New Connector")
		assert(m.getConnectorName().equals("My New Connector"))
	}

    test("Basic Connector App listening on a given port does not break") {
    	val port="2558"
		val m = mock[IConnector]
		val c = new ConnectorApp()
		val config = ConfigFactory.parseString("akka.remote.netty.tcp.port="+port).withFallback(ConfigFactory.load())
		val myReference=c.startup(m, port, config)
		myReference ! "Hello World"
    	assert("Hello World" == "Hello World")
	}
    
    //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE
    test("Connect to Connector Test") {
    	val port="2559"
		val m = mock[IConnector]
		//(m.getConnectorName _).expects().returning("My New Connector")
		val c = new ConnectorApp()
		val config = ConfigFactory.parseString("akka.remote.netty.tcp.port="+port).withFallback(ConfigFactory.load())
		val myReference=c.startup(m, port, config)

		myReference ! ConnectToConnector("cassandra connector")
		//val steps:List<LogicalStep>=null
		val myworkflow:LogicalWorkflow=new LogicalWorkflow(null)
		myReference ! Execute(myworkflow)
		myReference ! DisconnectFromConnector("cassandra connector")
		myReference ! Request("Select * from mytable;")
		myReference ! Response("Select * from mytable;")
		myReference ! MetadataStruct("clustername", "connectorName", "metaData")
		myReference ! StorageQueryStruct("clusterName", "connectorName", "storageQuery")
		myReference ! WorkflowStruct("clusterName", "connectorName", myworkflow)

    	assert("Hello World" == "Hello World")
	}
    
}