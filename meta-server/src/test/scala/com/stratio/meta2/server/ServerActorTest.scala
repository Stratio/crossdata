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

package com.stratio.meta2.server

import java.io.Serializable
import java.util
import java.util.concurrent.locks.Lock
import javax.transaction.TransactionManager




import akka.testkit.ImplicitSender

import com.stratio.connectors.MockConnectorActor
import com.stratio.meta.common.connector.Operations
import com.stratio.meta.common.executionplan._
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.common.utils.StringUtils
import com.stratio.meta.communication.{replyConnectorName, getConnectorName}
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.api.PropertyType
import com.stratio.meta2.common.data._
import com.stratio.meta2.common.metadata._
import com.stratio.meta2.common.metadata.structures.TableType
import com.stratio.meta2.common.statements.structures.selectors.Selector
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.execution.ExecutionManager
import com.stratio.meta2.core.grid.Grid
import com.stratio.meta2.core.metadata.{MetadataManager, MetadataManagerTestHelper}
import com.stratio.meta2.core.planner.SelectValidatedQueryWrapper
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.{CreateCatalogStatement,CreateTableStatement, InsertIntoStatement, MetadataStatement,
SelectStatement}
import com.stratio.meta2.server.actors.CoordinatorActor
import com.stratio.meta2.server.mocks.MockConnectorManagerActor
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Suite}
import scala.concurrent.duration.DurationInt
import akka.pattern.ask

import scala.concurrent.Await


trait ServerActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory with ServerConfig with
ImplicitSender {
  this: Suite =>

  val metadataManager=new MetadataManagerTestHelper()

  def incQueryId(): String = {
    queryIdIncrement += 1; return queryId + queryIdIncrement
  }

  //lazy val system1 = ActorSystem(clusterName, config)

  val connectorManagerActor = system.actorOf(MockConnectorManagerActor.props(), "ConnectorManagerActor")
  val coordinatorActor = system.actorOf(CoordinatorActor.props(connectorManagerActor, new Coordinator()), "CoordinatorActor")
  val connectorActor = system.actorOf(MockConnectorActor.props(), "ConnectorActor")


  var queryId = "query_id-2384234-1341234-23434"
  var queryIdIncrement = 0
  val tableName="myTable"
  val columnName="columnName"

  val catalogName = "myCatalog"
  val myClusterName ="myCluster"
  val selectStatement: SelectStatement = new SelectStatement(new TableName(catalogName,tableName))

  val selectParsedQuery = new SelectParsedQuery(new BaseQuery(incQueryId(), "SELECT FROM "+catalogName+".mytable",
    new CatalogName(catalogName)), selectStatement)
  //val selectValidatedQuery = new SelectValidatedQuery(selectParsedQuery)
  val selectValidatedQueryWrapper = new SelectValidatedQueryWrapper(selectStatement,selectParsedQuery)
  val selectPlannedQuery = new SelectPlannedQuery(selectValidatedQueryWrapper,
    new QueryWorkflow(queryId + queryIdIncrement,
    StringUtils.getAkkaActorRefUri(connectorActor),
    ExecutionType.SELECT, ResultType.RESULTS, new LogicalWorkflow(null)))

  val storageStatement: InsertIntoStatement = null
  val storageParsedQuery = new StorageParsedQuery(new BaseQuery(incQueryId(), "insert (uno,dos) into "+tableName+";",
    new CatalogName(catalogName)), storageStatement)
  val storageValidatedQuery = new StorageValidatedQuery(storageParsedQuery)
  val storagePlannedQuery = new StoragePlannedQuery(storageValidatedQuery, new StorageWorkflow(queryId + queryIdIncrement,
    StringUtils.getAkkaActorRefUri(connectorActor), ExecutionType.INSERT, ResultType.RESULTS))

  val metadataStatement0: MetadataStatement = new CreateCatalogStatement(
    new CatalogName(catalogName),
    true,
    ""
  )
  val metadataParsedQuery0 = new MetadataParsedQuery(new BaseQuery(incQueryId(), "", new CatalogName(catalogName)),
    metadataStatement0)
  val metadataValidatedQuery0: MetadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery0)
  val metadataWorkflow0=new MetadataWorkflow(queryId + queryIdIncrement,  null, ExecutionType.CREATE_CATALOG, ResultType.RESULTS)
  metadataWorkflow0.setCatalogMetadata(
    new CatalogMetadata(
      new CatalogName(catalogName),
      new util.HashMap[Selector, Selector](),
      new util.HashMap[TableName,TableMetadata]()
    )
  )
  val metadataPlannedQuery0 = new MetadataPlannedQuery(metadataValidatedQuery0,metadataWorkflow0)


  val metadataStatement1: MetadataStatement =  new CreateTableStatement(TableType.DATABASE,

      new TableName(catalogName,tableName),
      new ClusterName(myClusterName),


      new util.HashMap[ColumnName, ColumnType](),
      new util.ArrayList[ColumnName](),
      new util.ArrayList[ColumnName]()
    )
  val metadataParsedQuery1 = new MetadataParsedQuery(new BaseQuery(incQueryId(), "create table "+tableName+"1"+";",
    new CatalogName(catalogName)),
    metadataStatement1)
  val metadataValidatedQuery1: MetadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery1)
  val metadataWorkflow1=new MetadataWorkflow(queryId + queryIdIncrement,  StringUtils.getAkkaActorRefUri(connectorActor),
    ExecutionType.CREATE_TABLE,
    ResultType.RESULTS)
  metadataWorkflow1.setCatalogMetadata(
    new CatalogMetadata(
      new CatalogName(catalogName),
      new util.HashMap[Selector, Selector](),
      new util.HashMap[TableName,TableMetadata]()
    )
  )
  val columnNme= new ColumnName(catalogName,tableName+"1",columnName)
  val myList=new java.util.ArrayList[ColumnName]()
  myList.add(columnNme)
  metadataWorkflow1.setTableMetadata(
  new TableMetadata(
    false,
    new TableName(catalogName, tableName+"1"),
    new util.HashMap[Selector, Selector](),
    new util.HashMap[ColumnName, ColumnMetadata](),
    new util.HashMap[IndexName, IndexMetadata](),
    new ClusterName(myClusterName),
    myList,
    new java.util.ArrayList[ColumnName]()
    )
  )
  val metadataPlannedQuery1 = new MetadataPlannedQuery(metadataValidatedQuery1,metadataWorkflow1)

  def initialize() = {
    var grid = Grid.initializer.withContactPoint("127.0.0.1").withPort(7800).withListenAddress("127.0.0.1")
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

    val dataStoreRefs = new util.ArrayList[String]().asInstanceOf[util.List[String]]
    val requiredProperties = new util.ArrayList[PropertyType]().asInstanceOf[util.List[PropertyType]]
    val optionalProperties = new util.ArrayList[PropertyType]().asInstanceOf[util.List[PropertyType]]

  }

  def initializeTablesInfinispan(): TableMetadata = {
    val operations=new java.util.HashSet[Operations]()
    operations.add(Operations.PROJECT)
    operations.add(Operations.SELECT_OPERATOR)
    val myDatastore = metadataManager.createTestDatastore()

    metadataManager.createTestCluster(myClusterName, myDatastore)
    metadataManager.createTestCatalog(catalogName)

    val clustername=metadataManager.createTestCluster(myClusterName, myDatastore)
    val clusternames=new java.util.HashSet[ClusterName]()
    clusternames.add(clustername)
    val future = connectorActor ? getConnectorName()
    val connectorName = Await.result(future, 3 seconds).asInstanceOf[replyConnectorName]
    println("creating connector "+connectorName.name)
    val myConnector=metadataManager.createTestConnector(connectorName.name,new DataStoreName(myDatastore.getName()),
      clusternames,
      operations,
      StringUtils.getAkkaActorRefUri(connectorActor))
    metadataManager.createTestCatalog(catalogName)
    MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(connectorName.name), Status.ONLINE)

    val clusterMetadata = MetadataManager.MANAGER.getCluster(clustername)
    val connectorsMap = new java.util.HashMap[ConnectorName, ConnectorAttachedMetadata]()
    connectorsMap.put(new ConnectorName(connectorName.name), new ConnectorAttachedMetadata(new ConnectorName
    (connectorName.name), clustername, new util.HashMap[Selector, Selector]()))
    clusterMetadata.setConnectorAttachedRefs(connectorsMap)
    MetadataManager.MANAGER.createCluster(clusterMetadata, false)

    metadataManager.createTestTable(clustername, catalogName, "myTable", Array("name", "age"),

      Array(ColumnType.VARCHAR, ColumnType.INT), Array("name"), Array("name"))
  }


}
