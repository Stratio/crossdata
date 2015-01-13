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

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{ClusterDomainEvent, CurrentClusterState, MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import akka.util.Timeout
import com.stratio.crossdata
import com.stratio.crossdata.common.connector.{IConnector, IConnectorApp, IMetadataEngine, IResultHandler}
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.exceptions.{ConnectionException, ExecutionException}
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata, _}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.communication.{ACK, AlterCatalog, AlterTable, AsyncExecute, CreateCatalog, CreateIndex, CreateTable, CreateTableAndCatalog, DeleteRows, DropCatalog, DropIndex, DropTable, Execute, HeartbeatSig, IAmAlive, Insert, InsertBatch, ProvideCatalogMetadata, ProvideCatalogsMetadata, ProvideMetadata, ProvideTableMetadata, Truncate, Update, UpdateMetadata, getConnectorName, replyConnectorName, _}
import org.apache.log4j.Logger

import scala.collection.mutable.{ListMap, Map}
import scala.concurrent.duration.DurationInt
import com.codahale.metrics.{MetricRegistry, Gauge}
import com.stratio.crossdata.common.utils.Metrics

object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}
object ConnectorActor {
  def props(connectorName: String, connector: IConnector): Props = Props(new ConnectorActor
  (connectorName, connector))
}

class ConnectorActor(connectorName: String, conn: IConnector) extends HeartbeatActor with
ActorLogging with IResultHandler {

  override lazy val logger = Logger.getLogger(classOf[ConnectorActor])
  val metadata: util.Map[FirstLevelName, IMetadata]=new util.HashMap[FirstLevelName,IMetadata]()

  logger.info("Lifting connector actor")

  implicit val timeout = Timeout(20 seconds)

  //TODO: test if it works with one thread and multiple threads
  val connector = conn
  var state = State.Stopped
  var runningJobs: Map[String, ActorRef] = new ListMap[String, ActorRef]()
  var connectedServers: Set[ActorRef] = Set()
  Metrics.getRegistry.register(MetricRegistry.name(self.path.name, connectorName, "connection", "status"),
    new Gauge[Boolean] {
    override def getValue: Boolean = {
      var status: Boolean = false
      if (getConnectionStatus eq ConnectionStatus.CONNECTED) {
        status = true
      }
      return status
    }
  })

  override def handleHeartbeat(heartbeat: HeartbeatSig): Unit = {
    runningJobs.foreach {
      keyval: (String, ActorRef) => keyval._2 ! IAmAlive(keyval._1)
    }
  }

  override def preStart(): Unit = {
    Cluster(context.system).subscribe(self, classOf[ClusterDomainEvent])
  }

  def getTableMetadata(clusterName: ClusterName, tableName: TableName): TableMetadata = {
      val catalogname = tableName.getCatalogName
      return metadata.get(catalogname).asInstanceOf[CatalogMetadata].getTables.get(tableName)
  }

  def getCatalogMetadata(clusterName: ClusterName, catalogName: CatalogName): CatalogMetadata={
    return metadata.get(catalogName).asInstanceOf[CatalogMetadata]
  }

  def getCatalogs(cluster: ClusterName): util.List[CatalogMetadata] = {
    //TODO: Return the list of catalogs.
    return null;
  }

  def getConnectionStatus(): ConnectionStatus = {
    var status: ConnectionStatus = ConnectionStatus.CONNECTED
    if (connectedServers.isEmpty){
      status = ConnectionStatus.DISCONNECTED
    }
    status
  }

  override def receive: Receive = super.receive orElse {

    //TODO:
    case u: PatchMetadata=> {
      u.metadataClass match{
        //case tmd:com.stratio.crossdata.common.metadata.TableMetadata => {
        case clss:Class[TableMetadata]=> {
          System.out.println("++>>>>>>>>>")
        }
        case _=> {
          System.out.println("-->>>>>>>>>")
        }
      }
    }

    case u: UpdateMetadata=> {
      u.metadata match{
        case _:CatalogMetadata => {
          metadata.put(u.metadata.asInstanceOf[CatalogMetadata].getName,u.metadata)
        }
        case _:ClusterMetadata => {
          metadata.put(u.metadata.asInstanceOf[ClusterMetadata].getName,u.metadata)
        }
        case _:ConnectorMetadata => {
          metadata.put(u.metadata.asInstanceOf[ConnectorMetadata].getName,u.metadata)
        }
        case _:DataStoreMetadata =>{
          metadata.put(u.metadata.asInstanceOf[DataStoreMetadata].getName,u.metadata)
        }
        case _:NodeMetadata =>{
          metadata.put(u.metadata.asInstanceOf[NodeMetadata].getName,u.metadata)
        }
        case _:TableMetadata => {
          val tablename = u.metadata.asInstanceOf[TableMetadata].getName
          //val clusterref = u.metadata.asInstanceOf[TableMetadata].getClusterRef
          val catalogname = tablename.getCatalogName
          metadata.get(catalogname).asInstanceOf[CatalogMetadata].getTables.put(
            tablename,u.metadata.asInstanceOf[TableMetadata]
          )
        }
        case _:ColumnMetadata => {
          val columname = u.metadata.asInstanceOf[ColumnMetadata].getName
          val tablename = columname.getTableName
          val catalogname = tablename.getCatalogName
          metadata.get(catalogname).asInstanceOf[CatalogMetadata].getTables.get(tablename).getColumns.put(
            columname,u.metadata.asInstanceOf[ColumnMetadata]
          )
        }
        case _:IndexMetadata => {
          val indexname = u.metadata.asInstanceOf[IndexMetadata].getName
          val tablename = indexname.getTableName
          val catalogname = tablename.getCatalogName
          metadata.get(catalogname).asInstanceOf[CatalogMetadata].getTables.get(tablename).getIndexes.put(
            indexname,u.metadata.asInstanceOf[IndexMetadata]
          )
        }
      }
      sender ! true
    }
    case connectRequest: com.stratio.crossdata.communication.Connect => {
      logger.debug("->" + "Receiving MetadataRequest")
      logger.info("Received connect command")
      try {
        connector.connect(connectRequest.credentials, connectRequest.connectorClusterConfig)
        this.state = State.Started //if it doesn't connect, an exception will be thrown and we won't get here
        val result = ConnectResult.createConnectResult("Connected successfully")
        result.setQueryId(connectRequest.queryId)
        sender ! result //TODO once persisted sessionId,
      } catch {
        case e: ConnectionException => {
          val result = Result.createErrorResult(e)
          result.setQueryId(connectRequest.queryId)
          sender ! result
        }
      }
    }
    case disconnectRequest: com.stratio.crossdata.communication.DisconnectFromCluster => {
      logger.debug("->" + "Receiving MetadataRequest")
      logger.info("Received disconnectFromCluster command")
      var result: Result = null
      try {
        connector.close(new ClusterName(disconnectRequest.clusterName))
        result = ConnectResult.createConnectResult(
          "Disconnected successfully from " + disconnectRequest.clusterName)
      } catch {
        case ex: ConnectionException => {
          result = Result.createConnectionErrorResult("Cannot disconnect from " + disconnectRequest.clusterName)
        }
      }
      result.setQueryId(disconnectRequest.queryId)
      this.state = State.Started //if it doesn't connect, an exception will be thrown and we won't get here
      sender ! result //TODO once persisted sessionId,
    }
    case _: com.stratio.crossdata.communication.Shutdown => {
      logger.debug("->" + "Receiving Shutdown")
      this.shutdown()
    }
    case ex: Execute => {
      logger.info("Processing query: " + ex)
      methodExecute(ex, sender)
    }
    case aex: AsyncExecute => {
      logger.info("Processing asynchronous query: " + aex)
      methodAsyncExecute(aex, sender)
    }
    case metadataOp: MetadataOperation => {
      methodMetadataOp(metadataOp, sender)
    }
    case storageOp: StorageOperation => {
      methodStorageop(storageOp, sender)
    }
    case msg: getConnectorName => {
      logger.info(sender + " asked for my name")
      connectedServers += sender
      sender ! replyConnectorName(connectorName)
    }
    case MemberUp(member) => {
      logger.info("Member up")
      logger.debug("Member is Up: " + member.toString + member.getRoles + "!")
    }
    case state: CurrentClusterState => {
      logger.info("Current members: " + state.members.mkString(", "))
    }
    case UnreachableMember(member) => {
      if(member.hasRole("server")){
        connectedServers -= sender
      }
      logger.info("Member detected as unreachable: " + member)
    }
    case MemberRemoved(member, previousStatus) => {
      if(member.hasRole("server")){
        connectedServers -= sender
      }
      logger.info("Member is Removed: " + member.address + " after " + previousStatus)
    }
    case _: MemberEvent => {
      logger.info("Receiving anything else")
    }
  }

  def shutdown(): Unit = {
    logger.debug("ConnectorActor is shutting down")
    this.state = State.Stopping
    connector.shutdown()
    this.state = State.Stopped
  }

  override def processException(queryId: String, exception: ExecutionException): Unit = {
    logger.info("Processing exception for async query: " + queryId)
    val source = runningJobs.get(queryId).get
    if(source != None) {
      source ! Result.createErrorResult(exception)
    }else{
      logger.error("Exception for query " + queryId + " cannot be sent", exception)
    }
  }

  override def processResult(result: QueryResult): Unit = {
    logger.info("Processing results for async query: " + result.getQueryId)
    val source = runningJobs.get(result.getQueryId).get
    if(source != None) {
      source ! result
    }else{
      logger.error("Results for query " + result.getQueryId + " cannot be sent")
    }
  }

  private def methodExecute(ex:Execute, s:ActorRef): Unit ={
    try {
      runningJobs.put(ex.queryId, s)
      val result = connector.getQueryEngine().execute(ex.workflow)
      result.setQueryId(ex.queryId)
      s ! result
    } catch {
      case e: Exception => {
        val result = Result.createErrorResult(e)
        result.setQueryId(ex.queryId)
        s ! result
      }
      case err: Error =>
        logger.error("Error in ConnectorActor (Receiving LogicalWorkflow)")
        val result = new ErrorResult(err.getCause.asInstanceOf[Exception])
        result.setQueryId(ex.queryId)
        s ! result
    } finally {
      runningJobs.remove(ex.queryId)
    }
  }

  private def methodAsyncExecute(aex: AsyncExecute, sender:ActorRef) : Unit = {
    val asyncSender = sender
    try {
      runningJobs.put(aex.queryId, asyncSender)
      connector.getQueryEngine().asyncExecute(aex.queryId, aex.workflow, this)
      asyncSender ! ACK(aex.queryId, QueryStatus.IN_PROGRESS)
    } catch {
      case e: Exception => {
        val result = Result.createErrorResult(e)
        result.setQueryId(aex.queryId)
        asyncSender ! result
        runningJobs.remove(aex.queryId)
      }
      case err: Error =>
        logger.error("error in ConnectorActor( receiving async LogicalWorkflow )")
    }
  }

  private def methodMetadataOp(metadataOp: MetadataOperation, s: ActorRef): Unit = {
    var qId: String = metadataOp.queryId
    var metadataOperation: Int = 0
    logger.info("Received queryId = " + qId)
    var result: Result = null
    try {
      val eng = connector.getMetadataEngine()

      val answer = methodOpMetadata( metadataOp, eng)
      qId = answer._1
      metadataOperation = answer._2
      result = MetadataResult.createSuccessMetadataResult(metadataOperation)
      if(answer._3!=null){
        if(metadataOperation == MetadataResult.OPERATION_DISCOVER_METADATA
           || metadataOperation == MetadataResult.OPERATION_IMPORT_CATALOGS) {
          result.asInstanceOf[MetadataResult].setCatalogMetadataList(
            answer._3.asInstanceOf[java.util.List[CatalogMetadata]])
        } else if(metadataOperation == MetadataResult.OPERATION_IMPORT_CATALOG){
          val catalogList: util.ArrayList[CatalogMetadata] = new util.ArrayList[CatalogMetadata]()
          catalogList.add(answer._3.asInstanceOf[CatalogMetadata])
          result.asInstanceOf[MetadataResult].setCatalogMetadataList(catalogList)
        } else if (metadataOperation == MetadataResult.OPERATION_IMPORT_TABLE){
          val tableList: util.ArrayList[TableMetadata] = new util.ArrayList[TableMetadata]()
          tableList.add(answer._3.asInstanceOf[TableMetadata])
          result.asInstanceOf[MetadataResult].setTableList(tableList)
        }
      }
    } catch {
      case ex: Exception => {
        logger.error("Connector exception: " + ex.getMessage)
        result = Result.createExecutionErrorResult(ex.getMessage)
      }
      case err: Error => {
        logger.error("Error in ConnectorActor(Receiving CrossdataOperation)")
        result = Result.createExecutionErrorResult("Connector exception: " + err.getMessage)
      }
    }
    result.setQueryId(qId)
    logger.info("Sending back queryId = " + qId)
    s ! result
  }

  private def methodStorageop(storageOp: StorageOperation, s: ActorRef): Unit = {
    val qId: String = storageOp.queryId
    try {
      val eng = connector.getStorageEngine()
      storageOp match {
        case Insert(queryId, clustername, table, row, ifNotExists) => {
          eng.insert(clustername, table, row, ifNotExists)
        }
        case InsertBatch(queryId, clustername, table, rows, ifNotExists) => {
          eng.insert(clustername, table, rows, ifNotExists)
        }
        case DeleteRows(queryId, clustername, table, whereClauses) => {
          eng.delete(clustername, table, whereClauses)
        }
        case Update(queryId, clustername, table, assignments, whereClauses) => {
          eng.update(clustername, table, assignments, whereClauses)
        }
        case Truncate(queryId, clustername, table) => {
          eng.truncate(clustername, table)
        }
      }
      val result = StorageResult.createSuccessFulStorageResult("STORED successfully");
      result.setQueryId(qId)
      s ! result
    } catch {
      case ex: Exception => {
        logger.error(ex.getMessage)
        val result = Result.createErrorResult(ex)
        result.setQueryId(qId)
        s ! result
      }
      case err: Error => {
        logger.error("Error in ConnectorActor(Receiving StorageOperation)")
        val result = crossdata.common.result.Result.createExecutionErrorResult("Error in ConnectorActor")
        result.setQueryId(qId)
        s ! result
      }
    }
  }

  private def methodOpMetadata(metadataOp: MetadataOperation, eng: IMetadataEngine):
  (String,  Int, Object) = {
    metadataOp match {
      case _:CreateTable => {
        eng.createTable(metadataOp.asInstanceOf[CreateTable].targetCluster,
          metadataOp.asInstanceOf[CreateTable].tableMetadata)
        (metadataOp.asInstanceOf[CreateTable].queryId, MetadataResult.OPERATION_CREATE_TABLE,null)
      }
      case _:CreateCatalog => {
        eng.createCatalog(metadataOp.asInstanceOf[CreateCatalog].targetCluster,
          metadataOp.asInstanceOf[CreateCatalog].catalogMetadata)
        (metadataOp.asInstanceOf[CreateCatalog].queryId, MetadataResult.OPERATION_CREATE_CATALOG,null)
      }
      case _:AlterCatalog => {
        eng.alterCatalog(metadataOp.asInstanceOf[AlterCatalog].targetCluster,
          metadataOp.asInstanceOf[AlterCatalog].catalogMetadata.getName,
          metadataOp.asInstanceOf[AlterCatalog].catalogMetadata.getOptions)
        (metadataOp.asInstanceOf[AlterCatalog].queryId, MetadataResult.OPERATION_CREATE_CATALOG,null)
      }
      case _:CreateIndex => {
        eng.createIndex(metadataOp.asInstanceOf[CreateIndex].targetCluster,
          metadataOp.asInstanceOf[CreateIndex].indexMetadata)
        (metadataOp.asInstanceOf[CreateIndex].queryId, MetadataResult.OPERATION_CREATE_INDEX,null)
      }
      case _:DropCatalog => {
        eng.dropCatalog(metadataOp.asInstanceOf[DropCatalog].targetCluster,
          metadataOp.asInstanceOf[DropCatalog].catalogName)
        (metadataOp.asInstanceOf[DropCatalog].queryId, MetadataResult.OPERATION_DROP_CATALOG,null)
      }
      case _:DropIndex => {
        eng.dropIndex(metadataOp.asInstanceOf[DropIndex].targetCluster, metadataOp.asInstanceOf[DropIndex].indexMetadata)
        (metadataOp.asInstanceOf[DropIndex].queryId, MetadataResult.OPERATION_DROP_INDEX,null)
      }
      case _:DropTable => {
        eng.dropTable(metadataOp.asInstanceOf[DropTable].targetCluster, metadataOp.asInstanceOf[DropTable].tableName)
        (metadataOp.asInstanceOf[DropTable].queryId, MetadataResult.OPERATION_DROP_TABLE,null)
      }
      case _:AlterTable => {
        eng.alterTable(metadataOp.asInstanceOf[AlterTable].targetCluster, metadataOp.asInstanceOf[AlterTable]
          .tableName, metadataOp.asInstanceOf[AlterTable].alterOptions)
        (metadataOp.asInstanceOf[AlterTable].queryId, MetadataResult.OPERATION_ALTER_TABLE,null)
      }
      case _:CreateTableAndCatalog => {
        eng.createCatalog(metadataOp.asInstanceOf[CreateTableAndCatalog].targetCluster,
          metadataOp.asInstanceOf[CreateTableAndCatalog].catalogMetadata)
        eng.createTable(metadataOp.asInstanceOf[CreateTableAndCatalog].targetCluster,
          metadataOp.asInstanceOf[CreateTableAndCatalog].tableMetadata)
        (metadataOp.asInstanceOf[CreateTableAndCatalog].queryId, MetadataResult.OPERATION_CREATE_TABLE,null)
      }
      case _:ProvideMetadata => {
        val listmetadata=eng.provideMetadata(metadataOp.asInstanceOf[ProvideMetadata].targetCluster)
        (metadataOp.asInstanceOf[ProvideMetadata].queryId, MetadataResult.OPERATION_DISCOVER_METADATA,listmetadata)

      }
      case _:ProvideCatalogsMetadata => {
        val listmetadata=eng.provideMetadata(metadataOp.asInstanceOf[ProvideCatalogsMetadata].targetCluster)
        (metadataOp.asInstanceOf[ProvideCatalogsMetadata].queryId, MetadataResult.OPERATION_IMPORT_CATALOGS,
          listmetadata)
      }
      case _:ProvideCatalogMetadata => {
        val listmetadata=eng.provideCatalogMetadata(metadataOp.asInstanceOf[ProvideCatalogMetadata].targetCluster,
          metadataOp.asInstanceOf[ProvideCatalogMetadata].catalogName)
        (metadataOp.asInstanceOf[ProvideCatalogMetadata].queryId, MetadataResult.OPERATION_IMPORT_CATALOG,
          listmetadata)
      }
      case _:ProvideTableMetadata => {
        val listmetadata=eng.provideTableMetadata(metadataOp.asInstanceOf[ProvideTableMetadata].targetCluster,
          metadataOp.asInstanceOf[ProvideTableMetadata].tableName)
        (metadataOp.asInstanceOf[ProvideTableMetadata].queryId, MetadataResult.OPERATION_IMPORT_TABLE,
          listmetadata)
      }
      case _ => ???
    }
  }

}
