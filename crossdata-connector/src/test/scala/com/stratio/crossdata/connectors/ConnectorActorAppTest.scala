/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.connectors

import java.util

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import com.stratio.crossdata.common.connector.{IQueryEngine, IMetadataEngine, IConnector, IStorageEngine}
import com.stratio.crossdata.common.data.{ClusterName, ColumnName, IndexName, Row, TableName}
import com.stratio.crossdata.common.logicalplan.{TransformationStep, LogicalStep, LogicalWorkflow}
import com.stratio.crossdata.common.metadata.{Operations, ColumnMetadata, IndexMetadata, TableMetadata}
import com.stratio.crossdata.common.result.{MetadataResult, QueryResult, StorageResult}
import com.stratio.crossdata.common.statements.structures.Selector
import com.stratio.crossdata.communication.{Execute, CreateTable, Insert}
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Suite, FunSuite}
import org.testng.Assert.{assertNotNull}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


//class ConnectorActorAppTest extends FunSuite with MockFactory {
class ConnectorActorAppTest extends TestKit(ActorSystem()) with FunSuite with MockFactory with ImplicitSender {
  this:Suite =>

  lazy val logger = Logger.getLogger(classOf[ConnectorActorAppTest])
  implicit val timeout = Timeout(3 seconds)

  val connector:String="MyConnector"


  //val to initialize and don't use null
  val options:Option[java.util.Map[Selector,Selector]]=Some(new util.HashMap[Selector,Selector]())
  val columns:Option[java.util.LinkedHashMap[ColumnName, ColumnMetadata]]=Some(new util.LinkedHashMap[ColumnName,ColumnMetadata]())
  val indexes:Option[java.util.Map[IndexName,IndexMetadata]]=Some(new util.HashMap[IndexName,IndexMetadata]())
  val clusterRef:Option[ClusterName]=Some(new ClusterName("myCluster"))
  val partitionKey:Option[java.util.LinkedList[ColumnName]]=Some(new java.util.LinkedList[ColumnName]())


  test("Basic Connector Mock") {
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning(connector)
    assert(m.getConnectorName().equals(connector))
  }

  test("Basic Connector App listening on a given port does not break") {
    val m = mock[IConnector]
    (m.init _).expects(*).returning(None)
    (m.getConnectorName _).expects().returning(connector)
    val c = new ConnectorApp()
    val myReference = c.startup(m)
    assertNotNull(myReference, "Null reference returned")
    c.shutdown()
  }


  test("Send SelectInProgressQuery to Connector") {
    val m = mock[IConnector]
    val qe = mock[IQueryEngine]
    (m.getQueryEngine _).expects().returning(qe)
    (qe.execute(_:LogicalWorkflow)).expects(*).returning(QueryResult.createSuccessQueryResult())
    (m.init _).expects(*).returning(None)
    (m.getConnectorName _).expects().returning(connector)

    val queryId = "QID_test"

    val c = new ConnectorApp()
    val myReference = c.startup(m)
    var steps: java.util.ArrayList[LogicalStep] = new java.util.ArrayList[LogicalStep]()
    val step = new TransformationStep(Operations.SELECT_OPERATOR)
    steps.add(step)
    var workflow = new LogicalWorkflow(steps)
    within(6000 millis) {
      myReference ! Execute(queryId, workflow)
      fishForMessage(6 seconds) {
        case msg: QueryResult => {
          assert(msg.getQueryId() == queryId )
          true
        }
        case other: Any => {
          logger.info("receiving message of type" + other.getClass() + " and ignoring it")
          false
        }
      }
      c.stop()
    }
  }

  test("Send MetadataInProgressQuery to Connector") {
    val port = "2560"
    val m = mock[IConnector]
    val me = mock[IMetadataEngine]
    (me.createTable _).expects(*,*).returning(QueryResult.createSuccessQueryResult())
    (m.getMetadataEngine _).expects().returning(me)
    (m.init _).expects(*).returning(None)
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    val myReference = c.startup(m)
    within(6000 millis) {

      val message = CreateTable(
        "queryId", new ClusterName("cluster"), new TableMetadata(new TableName("catalog", "mytable"),
        options.get,
        columns.get,
        indexes.get,
        clusterRef.get,
        partitionKey.get,
        partitionKey.get)
      )
      val future = ask(myReference, message)
      val result = Await.result(future, 3 seconds).asInstanceOf[MetadataResult]
      logger.debug("receive->" + result + " after sending Metadata query")
    }
    c.shutdown()
  }

  test("Send StorageInProgressQuery to Connector") {
    val port = "2561"
    val m = mock[IConnector]
    val ie = mock[IStorageEngine]
    (ie.insert(_: ClusterName,_:TableMetadata,_:Row,_:Boolean)).expects(*,*,*,*).returning()
    (m.init _).expects(*).returning(None)
    (m.getStorageEngine _).expects().returning(ie)
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    val myReference = c.startup(m)


    val message=Insert("query",new ClusterName("cluster"),new TableMetadata(new TableName("catalog","mytable"),
      options.get, columns.get,indexes.get,clusterRef.get,partitionKey.get,partitionKey.get),new Row(), false)
    //val future=myReference ? message
    logger.info("\n\nsending insert message to" + myReference + " \n\n")
    val future=myReference ? message
    val result = Await.result(future, 6 seconds).asInstanceOf[StorageResult]
    logger.info("receiving->" + result + " after sending insert query")
    c.shutdown()
  }

  //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE

}

