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

package com.stratio.crossdata.server

import java.io.Serializable
import java.util
import java.util.concurrent.locks.Lock
import javax.transaction.TransactionManager

import akka.cluster.Cluster
import akka.pattern.ask
import akka.testkit.ImplicitSender
import com.stratio.crossdata.common.data.{CatalogName, ClusterName, ColumnName, ConnectorName, DataStoreName, FirstLevelName, IndexName, Status, TableName}
import com.stratio.crossdata.common.executionplan.{ExecutionType, MetadataWorkflow, QueryWorkflow, ResultType, StorageWorkflow}
import com.stratio.crossdata.common.logicalplan.{LogicalStep, LogicalWorkflow, Project, Select}
import com.stratio.crossdata.common.manifest.PropertyType
import com.stratio.crossdata.common.metadata._
import com.stratio.crossdata.common.metadata.structures.TableType
import com.stratio.crossdata.common.statements.structures.Selector
import com.stratio.crossdata.common.utils.{Constants, StringUtils}
import com.stratio.crossdata.communication.{getConnectorName, replyConnectorName}
import com.stratio.crossdata.core.MetadataManagerTestHelper
import com.stratio.crossdata.core.coordinator.Coordinator
import com.stratio.crossdata.core.execution.ExecutionManager
import com.stratio.crossdata.core.grid.Grid
import com.stratio.crossdata.core.metadata.MetadataManager
import com.stratio.crossdata.core.planner.{PlannerExecutionWorkflowTest, SelectValidatedQueryWrapper}
import com.stratio.crossdata.core.query.{BaseQuery, MetadataParsedQuery, MetadataPlannedQuery, MetadataValidatedQuery, SelectParsedQuery, SelectPlannedQuery, StorageParsedQuery, StoragePlannedQuery, StorageValidatedQuery}
import com.stratio.crossdata.core.statements.{CreateCatalogStatement, CreateTableStatement, InsertIntoStatement, MetadataStatement, SelectStatement}
import com.stratio.crossdata.server.actors.{ConnectorManagerActor, CoordinatorActor}
import com.stratio.crossdata.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.crossdata.server.mocks.MockConnectorActor
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike, Suite}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


trait ServerActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory with ServerConfig with
ImplicitSender with BeforeAndAfterAll{
  this: Suite =>

  //lazy val system = ActorSystem(clusterName, config)
  val metadataManager = MetadataManagerTestHelper.HELPER
  val plannerTest= new PlannerExecutionWorkflowTest()

  override def afterAll() {
    shutdown(system)
  }

  /** *
    * This method return the queryID + increment.
    * @return queryId.
    */

  def incQueryId(): String = {
    queryIdIncrement += 1;
    queryId + queryIdIncrement
  }

  //Actors in this tests
  val connectorManagerActor = system.actorOf(ConnectorManagerActor.props(Cluster(system)), "ConnectorManagerActor")
  val coordinatorActor = system.actorOf(CoordinatorActor.props(connectorManagerActor, new Coordinator()),
    "CoordinatorActor")
  val connectorActor = system.actorOf(MockConnectorActor.props(), "ConnectorActor")

  //Variables
  var queryId = "query_id-2384234-1341234-23434"
  var queryIdIncrement = 0
  val tableName="myTable"
  val tableName1="myTable1"
  val tableName2="myTable2"
  val columnName="columnName"
  val id="id"
  val catalogName = "myCatalog"
  val myClusterName ="myCluster"
  val myExecutionData="myExecutionData"
  val columnNames1: Array[String] = Array(id, "user")
  val columnTypes1: Array[ColumnType] = Array(new ColumnType(DataType.INT), new ColumnType(DataType.TEXT))
  val partitionKeys1: Array[String] = Array(id)
  val clusteringKeys1: Array[String] = Array(id)
  val clusterName1: ClusterName = new ClusterName(myClusterName)
  var selectPlannedQuery: SelectPlannedQuery = null
  val myMetadata="myMetadata"
  val name="name"

  val selectStatement: SelectStatement = new SelectStatement(new TableName(catalogName,tableName))
  val selectParsedQuery = new SelectParsedQuery(new BaseQuery(incQueryId(), "SELECT FROM " + catalogName + "." +
    tableName,
    new CatalogName(catalogName)), selectStatement)
  //val selectValidatedQuery = new SelectValidatedQuery(selectParsedQuery)
  val selectValidatedQueryWrapper = new SelectValidatedQueryWrapper(selectStatement,selectParsedQuery)

  val storageStatement: InsertIntoStatement = null
  //TODO
  //make the implementation of storageStatement.
  val storageParsedQuery = new StorageParsedQuery(new BaseQuery(incQueryId(), "insert (uno," +
    "dos) into " + tableName + ";",
    new CatalogName(catalogName)), storageStatement)
  val storageValidatedQuery = new StorageValidatedQuery(storageParsedQuery)
  val storagePlannedQuery = new StoragePlannedQuery(storageValidatedQuery, new StorageWorkflow(queryId + queryIdIncrement,
    StringUtils.getAkkaActorRefUri(connectorActor, false), ExecutionType.INSERT, ResultType.RESULTS))

  val metadataStatement0: MetadataStatement = new CreateCatalogStatement(
    new CatalogName(catalogName),true,"")
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

  val isExternal = false;
  val metadataStatement1: MetadataStatement =  new CreateTableStatement(TableType.DATABASE,

    new TableName(catalogName,tableName1),
    new ClusterName(myClusterName),
    new util.LinkedHashMap[ColumnName, ColumnType](),
    new util.LinkedHashSet[ColumnName](),
    new util.LinkedHashSet[ColumnName](),
    isExternal
  )
  val metadataParsedQuery1 = new MetadataParsedQuery(new BaseQuery(incQueryId(), "create table " + tableName1 + ";",
    new CatalogName(catalogName)),
    metadataStatement1)
  val metadataValidatedQuery1: MetadataValidatedQuery = new MetadataValidatedQuery(metadataParsedQuery1)
  val metadataWorkflow1=new MetadataWorkflow(queryId + queryIdIncrement,  StringUtils.getAkkaActorRefUri(connectorActor, false),
    ExecutionType.CREATE_TABLE,
    ResultType.RESULTS)
  metadataWorkflow1.setCatalogMetadata(
    new CatalogMetadata(
      new CatalogName(catalogName),
      new util.HashMap[Selector, Selector](),
      new util.HashMap[TableName,TableMetadata]()
    )
  )
  val columnNme= new ColumnName(catalogName,tableName1,columnName)
  val myList=new java.util.LinkedList[ColumnName]()
  myList.add(columnNme)
  metadataWorkflow1.setTableMetadata(
    new TableMetadata(
      new TableName(catalogName, tableName1),
      new util.HashMap[Selector, Selector](),
      new util.LinkedHashMap[ColumnName, ColumnMetadata](),
      new util.HashMap[IndexName, IndexMetadata](),
      new ClusterName(myClusterName),
      myList,
      new java.util.LinkedList[ColumnName]()
    )
  )
  val metadataPlannedQuery1 = new MetadataPlannedQuery(metadataValidatedQuery1,metadataWorkflow1)
  val port:Int=7800
  val timeoutinMs:Int=5000

  def initialize() : Unit= {
    Grid.initializer.withContactPoint("127.0.0.1").withPort(port).withListenAddress("127.0.0.1")
      .withMinInitialMembers(1)
      .withJoinTimeoutInMs(timeoutinMs)
      .withPersistencePath("/tmp/toBeRemoved").init()
    val executionMap = Grid.INSTANCE.map(myExecutionData).asInstanceOf[util.Map[String, Serializable]]
    val lockExecution: Lock = Grid.INSTANCE.lock(myExecutionData)
    val tmExecution: TransactionManager = Grid.INSTANCE.transactionManager(myExecutionData)
    ExecutionManager.MANAGER.init(executionMap, lockExecution, tmExecution)
    ExecutionManager.MANAGER.clear()

    val metadataMap = Grid.INSTANCE.map(myMetadata).asInstanceOf[util.Map[FirstLevelName, IMetadata]]
    val lock: Lock = Grid.INSTANCE.lock(myMetadata)
    val tm = Grid.INSTANCE.transactionManager(myMetadata)
    MetadataManager.MANAGER.init(metadataMap, lock, tm.asInstanceOf[TransactionManager])
    MetadataManager.MANAGER.clear()

    val dataStoreRefs = new util.ArrayList[String]().asInstanceOf[util.List[String]]
    val requiredProperties = new util.ArrayList[PropertyType]().asInstanceOf[util.List[PropertyType]]
    val optionalProperties = new util.ArrayList[PropertyType]().asInstanceOf[util.List[PropertyType]]

  }

  def initializeTablesInfinispan(){//: TableMetadata = {
  val operations=new java.util.HashSet[Operations]()
    operations.add(Operations.PROJECT)
    operations.add(Operations.SELECT_OPERATOR)
    operations.add(Operations.CREATE_TABLE)
    operations.add(Operations.CREATE_CATALOG)
    operations.add(Operations.INSERT)

    //create metadatamanager
    val myDatastore = metadataManager.createTestDatastore()

    //Create cluster
    val testcluster= metadataManager.createTestCluster(myClusterName, myDatastore)
    val clusterwithPriorities=new java.util.LinkedHashMap[ClusterName, Integer]()
    clusterwithPriorities.put(testcluster, Constants.DEFAULT_PRIORITY)

    val future = connectorActor ? getConnectorName()
    val connectorName = Await.result(future, 3 seconds).asInstanceOf[replyConnectorName]
    logger.debug("creating connector " + connectorName.name)


    //Create connector
    val myConnector=metadataManager.createTestConnector(connectorName.name,new DataStoreName(myDatastore.getName()),
      clusterwithPriorities,
      operations,
      StringUtils.getAkkaActorRefUri(connectorActor, false))

    //create catalog
    metadataManager.createTestCatalog(catalogName)
    MetadataManager.MANAGER.setConnectorStatus(new ConnectorName(connectorName.name), Status.ONLINE)

    val clusterMetadata = MetadataManager.MANAGER.getCluster(testcluster)
    val connectorsMap = new java.util.HashMap[ConnectorName, ConnectorAttachedMetadata]()
    connectorsMap.put(new ConnectorName(connectorName.name), new ConnectorAttachedMetadata(new ConnectorName
    (connectorName.name), testcluster, new util.HashMap[Selector, Selector](),Constants.DEFAULT_PRIORITY))
    clusterMetadata.setConnectorAttachedRefs(connectorsMap)
    MetadataManager.MANAGER.createCluster(clusterMetadata, false)

    //create table
    val table1= metadataManager.createTestTable(clusterName1, catalogName, tableName, Array(name, "age"),
      Array(new ColumnType(DataType.TEXT), new ColumnType(DataType.INT)), Array(name), Array(name), null)

    val initialSteps: java.util.List[LogicalStep] = new java.util.LinkedList[LogicalStep]
    val project: Project = getProject(tableName2)
    val columns: Array[ColumnName] = Array(new ColumnName(table1.getName, id), new ColumnName(table1.getName, "user"))
    val types: Array[ColumnType] = Array(new ColumnType(DataType.INT), new ColumnType(DataType.TEXT))
    val select: Select = plannerTest.getSelect(columns, types)
    project.setNextStep(select)


    initialSteps.add(project)
    // Add initial steps
    val workflow : LogicalWorkflow = new LogicalWorkflow(initialSteps)

    selectPlannedQuery = new SelectPlannedQuery(selectValidatedQueryWrapper,
      new QueryWorkflow(queryId + queryIdIncrement,
        StringUtils.getAkkaActorRefUri(connectorActor, false),
        ExecutionType.SELECT, ResultType.RESULTS,workflow))
  }

  /**
   * Create a test Project operator.
   *
   * @param tableName Name of the table.
   * @param columns   List of columns.
   * @return A { @link com.stratio.com.stratio.crossdata.common.logicalplan.Project}.
   */
  def getProject (tableName: String, columns: ColumnName *): Project = {
    val operation: Operations = Operations.PROJECT
    val project: Project = new Project (operation, new TableName (catalogName, tableName), new ClusterName (myClusterName) )
    for (cn <- columns) {
      project.addColumn (cn)
    }
    project
  }

}
