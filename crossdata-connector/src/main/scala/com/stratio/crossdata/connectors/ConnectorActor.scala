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

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{ClusterDomainEvent, MemberEvent}
import akka.util.Timeout
import com.stratio.crossdata
import com.stratio.crossdata.common.connector._
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.exceptions.{ConnectionException, ExecutionException}
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata, _}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.communication._
import difflib.Patch
import org.apache.log4j.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable.{ListMap, Set}
import scala.concurrent.duration.DurationInt
import com.stratio.crossdata.communication.CreateIndex
import com.stratio.crossdata.communication.Update
import akka.cluster.ClusterEvent.MemberRemoved
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.communication.CreateTableAndCatalog
import com.stratio.crossdata.communication.AlterTable
import com.stratio.crossdata.communication.Truncate
import com.stratio.crossdata.communication.CreateTable
import com.stratio.crossdata.communication.AlterCatalog
import com.stratio.crossdata.communication.InsertBatch
import com.stratio.crossdata.communication.AsyncExecute
import akka.cluster.ClusterEvent.UnreachableMember
import com.stratio.crossdata.communication.ProvideCatalogMetadata
import com.stratio.crossdata.communication.ProvideCatalogsMetadata
import com.stratio.crossdata.communication.CreateCatalog
import com.stratio.crossdata.communication.ProvideTableMetadata
import com.stratio.crossdata.communication.PagedExecute
import com.stratio.crossdata.communication.replyConnectorName
import com.stratio.crossdata.communication.Execute
import akka.cluster.ClusterEvent.MemberUp
import com.stratio.crossdata.communication.getConnectorName
import com.stratio.crossdata.communication.ProvideMetadata
import com.stratio.crossdata.communication.DropIndex
import com.stratio.crossdata.communication.UpdateMetadata
import akka.cluster.ClusterEvent.CurrentClusterState
import com.stratio.crossdata.communication.DeleteRows
import com.stratio.crossdata.communication.DropCatalog
import com.stratio.crossdata.communication.PatchMetadata
import com.stratio.crossdata.communication.DropTable
import com.stratio.crossdata.communication.Insert


object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}
object ConnectorActor {
  def props(connectorName: String, connector: IConnector, connectedServers: Set[String]):
      Props = Props(new ConnectorActor(connectorName, connector, connectedServers))
}

object ClassExtractor{
 def unapply(myClass:Class[_]):Option[String]=Option(myClass.toString)
}

class ConnectorActor(connectorName: String, conn: IConnector, connectedServers: Set[String])
  extends Actor with ActorLogging with IResultHandler {

  lazy val logger = Logger.getLogger(classOf[ConnectorActor])
  val metadata: ObservableMap[FirstLevelName, IMetadata] = new ObservableMap[FirstLevelName, IMetadata]()

  logger.info("Lifting connector actor")

  implicit val timeout = Timeout(20 seconds)

  //TODO: test if it works with one thread and multiple threads
  val connector = conn
  var state = State.Stopped
  val runningJobs: ListMap[String, ActorRef] = new ListMap[String, ActorRef]()

  /*
  override def handleHeartbeat(heartbeat: HeartbeatSig): Unit = {
    logger.info(s"heartbeat (no of simultaneous running jobs = ${runningJobs.size}")
    runningJobs.foreach {
      keyVal: (String, ActorRef) => {
        logger.info("sending iamalive to "+StringUtils.getAkkaActorRefUri(keyVal._2) )
        context.actorSelection(StringUtils.getAkkaActorRefUri(keyVal._2)) ! IAmAlive(keyVal._1)
      }
    }
  }
  */

  override def preStart():Unit = {
    Cluster(context.system).subscribe(self,classOf[ClusterDomainEvent])
  }

  def getTableMetadata(clusterName: ClusterName, tableName: TableName): TableMetadata = {
      val catalogName = tableName.getCatalogName
      val catalogMetadata = metadata.get(catalogName).asInstanceOf[CatalogMetadata]
      val tableMetadata = catalogMetadata.getTables.get(tableName)
      if(tableMetadata.getClusterRef==clusterName)
        return tableMetadata
      else
        return null
  }

  def getCatalogMetadata(catalogName: CatalogName): CatalogMetadata={
    return metadata.get(catalogName).asInstanceOf[CatalogMetadata]
  }

  def getCatalogs(cluster: ClusterName): util.List[CatalogMetadata] = {
    val r = new util.HashMap[CatalogName,CatalogMetadata]()
    //for(entry:java.util.Map.Entry[FirstLevelName,IMetadata] <- metadata.entrySet()){
    for((k,v) <- metadata){
       k match {
        case name:CatalogName=>{
          val catalogMetadata = metadata.get(k).asInstanceOf[CatalogMetadata]
          val tables = catalogMetadata.getTables
          for((tableName, tableMetadata) <- tables){
            if(tables.get(tableName).getClusterRef==cluster){
              r.put(name,catalogMetadata)
            }
          }
        }
      }
    }
    return new util.ArrayList(r.values())
  }

  def subscribeToMetadataUpdate(listener: IMetadataListener) = {
    metadata.addListener(listener)
  }

  def class2String(clazz:Class[_]):String=clazz.getName
  val patchFunctionHash=new java.util.HashMap[String,(Patch,Name)=>Boolean]()
  //TODO: could it be more generic?
  patchFunctionHash.put(class2String(classOf[CatalogMetadata]),(diff:Patch, catalogName:Name)=>{
    val name = catalogName.asInstanceOf[CatalogName]
    val catalog = metadata.get(catalogName).asInstanceOf[CatalogMetadata]
    val jsonResult = StringUtils.patchObject(catalog, diff); // patch object
    val result = //deserialize
      StringUtils.deserializeObjectFromString(jsonResult,classOf[CatalogMetadata])
    metadata.put(name,result.asInstanceOf[CatalogMetadata])
   true
  })
  patchFunctionHash.put(class2String(classOf[ClusterMetadata]),(diff:Patch, clusterName:Name)=>{
    val name = clusterName.asInstanceOf[ClusterName]
    val cluster = metadata.get(clusterName).asInstanceOf[ClusterMetadata]
    val jsonResult = StringUtils.patchObject(cluster, diff); // patch object
    val result = //deserialize
      StringUtils.deserializeObjectFromString(jsonResult,classOf[ClusterMetadata])
    metadata.put(name,result.asInstanceOf[ClusterMetadata])
   true
  })
  patchFunctionHash.put(class2String(classOf[DataStoreMetadata]),(diff:Patch, datastoreName:Name)=>{
    val name = datastoreName.asInstanceOf[DataStoreName]
    val datastore = metadata.get(datastoreName).asInstanceOf[DataStoreMetadata]
    val jsonResult = StringUtils.patchObject(datastore, diff); // patch object
    val result = //deserialize
      StringUtils.deserializeObjectFromString(jsonResult,classOf[DataStoreMetadata])
    metadata.put(name,result.asInstanceOf[DataStoreMetadata])
   true
  })
  patchFunctionHash.put(class2String(classOf[TableMetadata]),(diff:Patch, tableName:Name)=>{
    val name = tableName.asInstanceOf[TableName]
    val catalogName = name.getCatalogName
    val table = metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(name)
    val jsonResult = StringUtils.patchObject(table, diff); // patch object
    val result = //deserialize
      StringUtils.deserializeObjectFromString(jsonResult,classOf[TableMetadata])
    metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.put(
      name,result.asInstanceOf[TableMetadata]
    )
    true
  })
  patchFunctionHash.put(class2String(classOf[ColumnMetadata]),(diff:Patch,columnName:Name)=>{
    val name = columnName.asInstanceOf[ColumnName]
    val tableName = name.getTableName
    val catalogName = tableName.getCatalogName
    val table = metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(tableName)
    val column = table.getColumns.get(name)
    val jsonResult = StringUtils.patchObject(column, diff); // patch object
    val result = //deserialize
      StringUtils.deserializeObjectFromString(jsonResult,classOf[TableMetadata])
    metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(tableName).getColumns
      .put( name,result.asInstanceOf[ColumnMetadata] )
    true
  })
  patchFunctionHash.put(class2String(classOf[IndexMetadata]),(diff:Patch,indexName:Name)=>{
    val name = indexName.asInstanceOf[IndexName]
    val tableName = name.getTableName
    val catalogName = tableName.getCatalogName
    val table = metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(tableName)
    val index = table.getIndexes.get(name)
    val jsonResult = StringUtils.patchObject(index, diff); // patch object
    val result = //deserialize
      StringUtils.deserializeObjectFromString(jsonResult,classOf[TableMetadata])
    metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(tableName).getIndexes
      .put( name,result.asInstanceOf[IndexMetadata] )
    true
  })

  override def receive: Receive = {
    case u: PatchMetadata=> {
      //TODO: continue
      val r=try{
        patchFunctionHash(class2String(u.metadataClass))(u.diffs,u.name)
      }catch{
        case _=>false
      }
      //println("result="+r)
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
        case _:TableMetadata => {
          val tableName = u.metadata.asInstanceOf[TableMetadata].getName
          val catalogName = tableName.getCatalogName
          metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.put(
            tableName,u.metadata.asInstanceOf[TableMetadata]
          )
        }
        case _:ColumnMetadata => {
          val columName = u.metadata.asInstanceOf[ColumnMetadata].getName
          val tableName = columName.getTableName
          val catalogName = tableName.getCatalogName
          metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(tableName).getColumns.put(
            columName,u.metadata.asInstanceOf[ColumnMetadata]
          )
        }
        case _:IndexMetadata => {
          val indexName = u.metadata.asInstanceOf[IndexMetadata].getName
          val tableName = indexName.getTableName
          val catalogName = tableName.getCatalogName
          metadata.get(catalogName).asInstanceOf[CatalogMetadata].getTables.get(tableName).getIndexes.put(
            indexName,u.metadata.asInstanceOf[IndexMetadata]
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
          logger.error(e.getMessage)
          val result = Result.createConnectionErrorResult(e.getMessage)
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
    case pex: PagedExecute => {
      logger.info("Processing paged query: " + pex)
      methodPagedExecute(pex, sender)
    }
    case metadataOp: MetadataOperation => {
      methodMetadataOp(metadataOp, sender)
    }
    case storageOp: StorageOperation => {
      methodStorageOp(storageOp, sender)
    }
    case msg: getConnectorName => {
      logger.info(sender + " asked for my name: " + connectorName)
      connectedServers += sender.path.address.toString
      logger.info("Connected to Servers: " + connectedServers)
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
        connectedServers -= member.address.toString
        logger.info("Connected to Servers: " + connectedServers)
      }
      logger.info("Member detected as unreachable: " + member)
    }
    case MemberRemoved(member, previousStatus) => {
      if(member.hasRole("server")){
        connectedServers -= member.address.toString
      }
      logger.info("Member is Removed: " + member.address + " after " + previousStatus)
    }
    case memberEvent: MemberEvent => {
      logger.info("MemberEvent received: " + memberEvent.toString)
    }
    case listener: IMetadataListener => {
      logger.info("Adding new metadata listener")
      subscribeToMetadataUpdate(listener)
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
        val result = Result.createExecutionErrorResult(e.getMessage)
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

  private def methodAsyncExecute(aex: AsyncExecute, sender: ActorRef) : Unit = {
    val asyncSender = sender
    try {
      runningJobs.put(aex.queryId, asyncSender)
      connector.getQueryEngine().asyncExecute(aex.queryId, aex.workflow, this)
      asyncSender ! ACK(aex.queryId, QueryStatus.IN_PROGRESS)
    } catch {
      case e: Exception => {
        val result = Result.createExecutionErrorResult(e.getMessage())
        result.setQueryId(aex.queryId)
        asyncSender ! result
      }
      case err: Error =>
        logger.error("error in ConnectorActor (Receiving async LogicalWorkflow)")
    }finally {
      runningJobs.remove(aex.queryId)
    }
  }

  private def methodPagedExecute(pex: PagedExecute, sender: ActorRef): Unit = {
    val pagedSender = sender
    try {
      logger.info("new running job: "+pex.queryId)
      runningJobs.put(pex.queryId, pagedSender)
      logger.info("concurrent running Jobs = "+runningJobs.size)
      //Thread.sleep(4*60*1000)
      connector.getQueryEngine().pagedExecute(pex.queryId, pex.workflow, this, pex.pageSize)
      pagedSender ! ACK(pex.queryId, QueryStatus.IN_PROGRESS)
    } catch {
      case e: Exception => {
        val result = Result.createExecutionErrorResult(e.getMessage())
        result.setQueryId(pex.queryId)
        pagedSender ! result
      }
      case err: Error =>
        logger.error("error in ConnectorActor (Receiving paged LogicalWorkflow)")
    }finally {
      runningJobs.remove(pex.queryId)
      logger.info("end new running job")
    }

  }

  private def methodMetadataOp(metadataOp: MetadataOperation, s: ActorRef): Unit = {
    var qId: String = metadataOp.queryId
    var metadataOperation: Int = 0
    logger.info("Received queryId = " + qId)
    var result: Result = null
    try {
      val eng = connector.getMetadataEngine()

      val answer = methodOpMetadata(metadataOp, eng)
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

  private def methodStorageOp(storageOp: StorageOperation, s: ActorRef): Unit = {
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
      val result = StorageResult.createSuccessfulStorageResult("STORED successfully");
      result.setQueryId(qId)
      s ! result
    } catch {
      case ex: Exception => {
        logger.error(ex.getMessage)
        val result = Result.createExecutionErrorResult(ex.getMessage)
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
        val listMetadata = eng.provideMetadata(metadataOp.asInstanceOf[ProvideMetadata].targetCluster)
        (metadataOp.asInstanceOf[ProvideMetadata].queryId, MetadataResult.OPERATION_DISCOVER_METADATA,listMetadata)

      }
      case _:ProvideCatalogsMetadata => {
        val listMetadata = eng.provideMetadata(metadataOp.asInstanceOf[ProvideCatalogsMetadata].targetCluster)
        (metadataOp.asInstanceOf[ProvideCatalogsMetadata].queryId, MetadataResult.OPERATION_IMPORT_CATALOGS,
          listMetadata)
      }
      case _:ProvideCatalogMetadata => {
        val listMetadata = eng.provideCatalogMetadata(metadataOp.asInstanceOf[ProvideCatalogMetadata].targetCluster,
          metadataOp.asInstanceOf[ProvideCatalogMetadata].catalogName)
        (metadataOp.asInstanceOf[ProvideCatalogMetadata].queryId, MetadataResult.OPERATION_IMPORT_CATALOG,
          listMetadata)
      }
      case _:ProvideTableMetadata => {
        val listMetadata = eng.provideTableMetadata(metadataOp.asInstanceOf[ProvideTableMetadata].targetCluster,
          metadataOp.asInstanceOf[ProvideTableMetadata].tableName)
        (metadataOp.asInstanceOf[ProvideTableMetadata].queryId, MetadataResult.OPERATION_IMPORT_TABLE,
          listMetadata)
      }
      case _ => ???
    }
  }

}
