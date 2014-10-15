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

import java.io.Serializable
import java.util
import java.util.concurrent.locks.Lock
import javax.transaction.TransactionManager

import akka.pattern.ask
import com.stratio.connectors.MockConnectorActor
import com.stratio.meta.common.exceptions.ExecutionException
import com.stratio.meta.common.executionplan._
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.result.{CommandResult, MetadataResult, QueryResult}
import com.stratio.meta.common.utils.StringUtils
import com.stratio.meta.communication.{getConnectorName, replyConnectorName}
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.api.PropertyType
import com.stratio.meta2.common.data._
import com.stratio.meta2.common.metadata._
import com.stratio.meta2.common.statements.structures.selectors.Selector
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.execution.ExecutionManager
import com.stratio.meta2.core.grid.Grid
import com.stratio.meta2.core.metadata.{MetadataManagerTestHelper, MetadataManager}
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.{InsertIntoStatement, MetadataStatement, SelectStatement}
import com.stratio.meta2.server.actors.CoordinatorActor
import com.stratio.meta2.server.mocks.MockConnectorManagerActor
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory with ServerConfig {
  this: Suite =>

  val metadataManager=new MetadataManagerTestHelper()
  def incQueryId(): String = {
    queryIdIncrement += 1; return queryId + queryIdIncrement
  }


  override lazy val logger = Logger.getLogger(classOf[CoordinatorActorTest])
  //lazy val system1 = ActorSystem(clusterName, config)

  val connectorManagerActor = system.actorOf(MockConnectorManagerActor.props(), "ConnectorManagerActor")
  val coordinatorActor = system.actorOf(CoordinatorActor.props(connectorManagerActor, new Coordinator()),
    "CoordinatorActor")
  val connectorActor = system.actorOf(MockConnectorActor.props(), "ConnectorActor")

  var queryId = "query_id-2384234-1341234-23434"
  var queryIdIncrement = 0
  val catalogName = "testCatalog"

  val selectStatement: SelectStatement = null
  val selectParsedQuery = new SelectParsedQuery(new BaseQuery(incQueryId(), "", new CatalogName(catalogName)), selectStatement)
  val selectValidatedQuery = new SelectValidatedQuery(selectParsedQuery)
  val selectPlannedQuery = new SelectPlannedQuery(selectValidatedQuery, new QueryWorkflow(queryId + queryIdIncrement,
    StringUtils.getAkkaActorRefUri(connectorActor),
    ExecutionType.SELECT, ResultType.RESULTS, new LogicalWorkflow(null)))

  val storageStatement: InsertIntoStatement = null
  val storageParsedQuery = new StorageParsedQuery(new BaseQuery(incQueryId(), "insert (uno,dos) into mytable;",
    new CatalogName(catalogName)), storageStatement)
  val storageValidatedQuery = new StorageValidatedQuery(storageParsedQuery)
  val storagePlannedQuery = new StoragePlannedQuery(storageValidatedQuery, new StorageWorkflow(queryId + queryIdIncrement,
    StringUtils.getAkkaActorRefUri(connectorActor), ExecutionType.INSERT, ResultType.RESULTS))

  val metadataStatement0: MetadataStatement = null
  val metadataParsedQuery0 = new MetadataParsedQuery(new BaseQuery(incQueryId(), "", new CatalogName(catalogName)),
    metadataStatement0)
  val metadataValidatedQuery0: MetadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery0)
  val metadataWorkflow0=new MetadataWorkflow(queryId + queryIdIncrement,  null, ExecutionType.CREATE_CATALOG, ResultType.RESULTS)
  metadataWorkflow0.setCatalogMetadata(
    new CatalogMetadata(
      new CatalogName("myCatalog"),
      new util.HashMap[Selector, Selector](),
      new util.HashMap[TableName,TableMetadata]()
    )
  )
  val metadataPlannedQuery0 = new MetadataPlannedQuery(metadataValidatedQuery0,metadataWorkflow0)


  val metadataStatement1: MetadataStatement = null
  val metadataParsedQuery1 = new MetadataParsedQuery(new BaseQuery(incQueryId(), "", new CatalogName(catalogName)),
    metadataStatement1)
  val metadataValidatedQuery1: MetadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery1)
  val metadataWorkflow1=new MetadataWorkflow(queryId + queryIdIncrement,  null, ExecutionType.CREATE_TABLE,
    ResultType.RESULTS)
  metadataWorkflow1.setCatalogMetadata(
    new CatalogMetadata(
      new CatalogName("myCatalog"),
      new util.HashMap[Selector, Selector](),
      new util.HashMap[TableName,TableMetadata]()
    )
  )
  val metadataPlannedQuery1 = new MetadataPlannedQuery(metadataValidatedQuery1,metadataWorkflow1)

  def initialize() = {
    var grid = Grid.initializer.withContactPoint("127.0.0.1").withPort(7800)
      .withListenAddress("127.0.0.1")
      .withMinInitialMembers(1)
      .withJoinTimeoutInMs(5000)
      .withPersistencePath("/tmp/borrar").init()
    val executionMap = grid.map("myExecutionData").asInstanceOf[util.Map[String, Serializable]]
    val lockExecution: Lock = grid.lock("myExecutionData")
    val tmExecution: TransactionManager = grid.transactionManager("myExecutionData")
    ExecutionManager.MANAGER.init(executionMap, lockExecution, tmExecution)
    ExecutionManager.MANAGER.clear()

    val metadataMap = grid.map("myMetadata").asInstanceOf[util.Map[FirstLevelName, IMetadata]]
    val lock: Lock = grid.lock("myMetadata")
    val tm = grid.transactionManager("myMetadata")
    MetadataManager.MANAGER.init(metadataMap, lock, tm.asInstanceOf[TransactionManager])
    MetadataManager.MANAGER.clear()

    val future = connectorActor ? getConnectorName()
    val connectorName = Await.result(future, 3 seconds).asInstanceOf[replyConnectorName]
    val dataStoreRefs = new util.ArrayList[String]().asInstanceOf[util.List[String]]
    val requiredProperties = new util.ArrayList[PropertyType]().asInstanceOf[util.List[PropertyType]]
    val optionalProperties = new util.ArrayList[PropertyType]().asInstanceOf[util.List[PropertyType]]
    val supportedOperations = new util.ArrayList[String]()
    val connectorMetadata = new ConnectorMetadata(
      new ConnectorName(connectorName.name),
      "version",
      dataStoreRefs,
      requiredProperties,
      optionalProperties,
      supportedOperations
    )

    MetadataManager.MANAGER.createConnector(connectorMetadata)
    MetadataManager.MANAGER.addConnectorRef(new ConnectorName(connectorName.name),
      StringUtils.getAkkaActorRefUri(connectorActor))
    MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(connectorName.name), Status.ONLINE)
  }

  def initializeTablesInfinispan(): TableMetadata = {
    val myDatastore = metadataManager.createTestDatastore()
    metadataManager.createTestCluster("myCluster", myDatastore)
    metadataManager.createTestCatalog("myCatalog")
    metadataManager.createTestTable(new ClusterName("myCluster"), "myCatalog", "myTable", Array("name", "age"),
      Array(ColumnType.VARCHAR, ColumnType.INT), Array("name"), Array("name"))
  }

  test("Should return a KO message") {
    initialize()
    within(1000 millis) {
      coordinatorActor ! "anything; this doesn't make any sense"
      val exception = expectMsgType[ExecutionException]
    }
  }

  test("Select query") {
    initialize()
    connectorActor !(queryId + (1), "updatemylastqueryId")
    coordinatorActor ! selectPlannedQuery
    expectMsgType[QueryResult]
  }

  test("Storage query") {
    initialize()
    initializeTablesInfinispan()
    connectorActor !(queryId + (2), "updatemylastqueryId")
    coordinatorActor ! storagePlannedQuery
    expectMsgType[CommandResult]
  }

  test("Metadata query") {
    initialize()
    connectorActor !(queryId + (3), "updatemylastqueryId")
    coordinatorActor ! metadataPlannedQuery0
    expectMsgType[MetadataResult]

    /*
    connectorActor !(queryId + (4), "updatemylastqueryId")
    coordinatorActor ! metadataPlannedQuery1
    expectMsgType[MetadataResult]
    */

  }

}
