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

import java.util.concurrent.locks.Lock
import javax.transaction.TransactionManager

import akka.actor.ActorSystem
import akka.pattern.ask
import com.stratio.connectors.MockConnectorActor
import com.stratio.meta.common.exceptions.ExecutionException
import com.stratio.meta.common.executionplan.{ExecutionType, QueryWorkflow, ResultType}
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.utils.StringUtils
import com.stratio.meta.communication.{getConnectorName, replyConnectorName}
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.api.PropertyType
import com.stratio.meta2.common.data.{CatalogName, ConnectorName, FirstLevelName}
import com.stratio.meta2.common.metadata.{ConnectorMetadata, IMetadata}
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.execution.ExecutionManager
import com.stratio.meta2.core.grid.Grid
import com.stratio.meta2.core.metadata.MetadataManager
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.SelectStatement
import com.stratio.meta2.server.actors.CoordinatorActor
import com.stratio.meta2.server.mocks.MockConnectorManagerActor
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

//class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory  with ServerConfig{
class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory with ServerConfig {
  this: Suite =>

  def incQueryId():String={ queryIdIncrement+=1;return queryId+queryIdIncrement}


  override lazy val logger = Logger.getLogger(classOf[CoordinatorActorTest])
  lazy val system1 = ActorSystem(clusterName, config)

  //val connectorManagerActor = system1.actorOf(ConnectorManagerActor.props(null), "ConnectorManagerActor")
  val connectorManagerActor = system1.actorOf(MockConnectorManagerActor.props(), "ConnectorManagerActor")
  val coordinatorActor = system1.actorOf(CoordinatorActor.props(connectorManagerActor, new Coordinator),
    "CoordinatorActor")
  val connectorActor = system1.actorOf(MockConnectorActor.props(), "ConnectorActor")

  var queryId = "query_id-2384234-1341234-23434"
  var queryIdIncrement=0
  val catalogName = "testCatalog"
  val selectStatement:SelectStatement=null
  val selectParsedQuery = new SelectParsedQuery(new BaseQuery(incQueryId(), "",
    new CatalogName(catalogName)),
    selectStatement)
  val selectValidatedQuery = new SelectValidatedQuery(selectParsedQuery);
  val selectPlannedQuery = new SelectPlannedQuery(selectValidatedQuery, new QueryWorkflow(incQueryId(),
    StringUtils.getAkkaActorRefUri(connectorActor),
    ExecutionType.SELECT, ResultType.RESULTS, new LogicalWorkflow(null)));

  def initialize()={
    var grid=Grid.initializer.withContactPoint("127.0.0.1").withPort(7800)
                .withListenAddress("127.0.0.1")
                .withMinInitialMembers(1)
                .withJoinTimeoutInMs(5000)
                .withPersistencePath("/tmp/borrar").init()
    val executionMap = grid.map("myExecutionData").asInstanceOf[java.util.Map[String,java.io.Serializable]]
    val lockExecution:Lock  = grid.lock("myExecutionData")
    val tmExecution:TransactionManager = grid.transactionManager("myExecutionData")
    ExecutionManager.MANAGER.init(executionMap, lockExecution, tmExecution)

    val metadataMap = grid.map("myMetadata").asInstanceOf[java.util.Map[FirstLevelName,IMetadata]]
    val lock:Lock  = grid.lock("myMetadata")
    val tm = grid.transactionManager("myMetadata")
    MetadataManager.MANAGER.init(metadataMap,lock,tm.asInstanceOf[javax.transaction.TransactionManager])
    MetadataManager.MANAGER.clear()
    val future=connectorActor ? getConnectorName()
    val connectorName= Await.result(future, 3 seconds).asInstanceOf[replyConnectorName]
    val dataStoreRefs = new java.util.ArrayList[String]().asInstanceOf[java.util.List[String]]
    val requiredProperties= new java.util.ArrayList[PropertyType]().asInstanceOf[java.util.List[PropertyType]]
    val optionalProperties= new java.util.ArrayList[PropertyType]().asInstanceOf[java.util.List[PropertyType]]
    val supportedOperations= new java.util.ArrayList[String]()
    val connectorMetadata = new ConnectorMetadata(
        new ConnectorName(connectorName.name),
        "version",
        dataStoreRefs,
        requiredProperties,
        optionalProperties,
        supportedOperations
    )

    MetadataManager.MANAGER.createConnector( connectorMetadata )
    //MetadataManager.MANAGER.addConnectorRef(new ConnectorName(connectorName.name),
      //StringUtils.getAkkaActorRefUri(connectorActor))
    //MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(connectorName),Status.ONLINE)
  }

  test("Should return a KO message") {
    initialize()
    within(1000 millis) {
      val coordinatorActor = system.actorOf(CoordinatorActor.props(connectorManagerActor, new Coordinator()),
        "CoordinatorActor")
      coordinatorActor ! "anything; this doesn't make any sense"
      val exception=expectMsgType[ExecutionException]
    }
  }
  test("Select query") {
    initialize()
    coordinatorActor ! selectPlannedQuery
    expectMsg("Ok") // bounded to 1 second
    /*
    val pq= new SelectPlannedQuery(null,null)
    expectMsg("Ok") // bounded to 1 second

    //val m = mock[IConnector]
    //(m.getConnectorName _).expects().returning("My New Connector")
    //assert(m.getConnectorName().equals("My New Connector"))
    */
  }

}
