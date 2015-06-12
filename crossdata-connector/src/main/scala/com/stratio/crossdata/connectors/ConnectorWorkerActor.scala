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

import akka.actor._
import akka.agent.Agent
import com.stratio.crossdata
import com.stratio.crossdata.common.ask.APICommand
import com.stratio.crossdata.common.connector._
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.exceptions.ExecutionException
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata, _}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.communication._
import org.apache.log4j.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.concurrent.duration.DurationInt
import com.stratio.crossdata.communication.CreateIndex
import com.stratio.crossdata.communication.Update
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.communication.CreateTableAndCatalog
import com.stratio.crossdata.communication.AlterTable
import com.stratio.crossdata.communication.Truncate
import com.stratio.crossdata.communication.CreateTable
import com.stratio.crossdata.communication.AlterCatalog
import com.stratio.crossdata.communication.InsertBatch
import com.stratio.crossdata.communication.AsyncExecute
import com.stratio.crossdata.communication.ProvideCatalogMetadata
import com.stratio.crossdata.communication.ProvideCatalogsMetadata
import com.stratio.crossdata.communication.CreateCatalog
import com.stratio.crossdata.communication.ProvideTableMetadata
import com.stratio.crossdata.communication.PagedExecute
import com.stratio.crossdata.communication.Execute
import com.stratio.crossdata.communication.ProvideMetadata
import com.stratio.crossdata.communication.DropIndex
import com.stratio.crossdata.communication.UpdateMetadata
import com.stratio.crossdata.communication.DeleteRows
import com.stratio.crossdata.communication.DropCatalog
import com.stratio.crossdata.communication.DropTable
import com.stratio.crossdata.communication.Insert

object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}
object ConnectorWorkerActor {
  def props( connector: IConnector, observableMap:  Agent[ObservableMap[Name, UpdatableMetadata]], runningJobs:  Agent[mutable.ListMap[String, ActorRef]]):
      Props = Props(new ConnectorWorkerActor(connector, observableMap, runningJobs))
}

class ConnectorWorkerActor(connector: IConnector, metadataMapAgent: Agent[ObservableMap[Name, UpdatableMetadata]], runningJobs:  Agent[mutable.ListMap[String, ActorRef]]) extends Actor with ActorLogging with IResultHandler {

  lazy val logger = Logger.getLogger(classOf[ConnectorWorkerActor])

  logger.info("Lifting connector worker actor")

  override def receive: Receive = {

    /** MessageEvents */
    case ex: Execute => {
      logger.info("Processing query: " + ex)
      methodExecute(ex, sender)
    }

    case aex: AsyncExecute => {
      logger.info("Processing asynchronous query: " + aex)
      methodAsyncExecute(aex, sender)
    }

    case StopProcess(queryId, targetQueryId) =>  {
      logger.debug("Processing stop process: " + queryId)
      methodStopProcess(queryId, targetQueryId, sender)
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




    //TODO iface UpdatableMetadata with getName, getMetadata
    case UpdateMetadata(umetadata, java.lang.Boolean.FALSE) => {
      umetadata match{
        case cMetadata:CatalogMetadata => {
          metadataMapAgent.get.put(cMetadata.getName,cMetadata)
        }
        //TODO remove asInstanceOf
        case uMetadata:ClusterMetadata => {
          metadataMapAgent.send(oMap => {oMap.put(uMetadata.getName,umetadata); oMap })

        }
        case uMetadata:ConnectorMetadata => {
          metadataMapAgent.send(oMap => {oMap.put(uMetadata.getName,umetadata); oMap })
        }
        case uMetadata:DataStoreMetadata =>{
          metadataMapAgent.send(oMap => {oMap.put(uMetadata.getName,umetadata); oMap })
        }

        case uMetadata:TableMetadata => {
          metadataMapAgent.send(oMap => {oMap.put(uMetadata.getName,umetadata); oMap })
        }

      }

    }

    case UpdateMetadata(umetadata, java.lang.Boolean.TRUE) => {
      umetadata match{
        case umetadata:CatalogMetadata => {
          metadataMapAgent.send(oMap => {oMap.remove(umetadata.getName); oMap })
        }
        case umetadata:ClusterMetadata => {
          metadataMapAgent.send(oMap => {oMap.remove(umetadata.getName); oMap })
        }
        case umetadata:ConnectorMetadata => {
          metadataMapAgent.send(oMap => {oMap.remove(umetadata.getName); oMap })
        }
        case umetadata:DataStoreMetadata =>{
          metadataMapAgent.send(oMap => {oMap.remove(umetadata.getName); oMap })
        }
        case umetadata:TableMetadata => {
          metadataMapAgent.send(oMap => {oMap.remove(umetadata.getName); oMap })
        }

      }
    }

    case Request(message) if (message.equals(APICommand.CLEAN_METADATA.toString)) => {
      metadataMapAgent.send{ observableMap => observableMap.clear(); observableMap }
    }


    case GetCatalogs(clusterName) =>{
      sender ! getCatalogs(clusterName)
    }

    case GetTableMetadata(clusterName, tableName) =>{
      sender ! getTableMetadata(clusterName,tableName)
    }


    case  GetCatalogMetadata(catalogName) => {
      sender ! getCatalogMetadata(catalogName)
    }


  }

  def extractSender(m: String): String = {
    m.substring(m.indexOf("[")+1, m.indexOf("$")).trim
  }


  override def processException(queryId: String, exception: ExecutionException): Unit = {
    logger.info("Processing exception for async query: " + queryId)
    val source = runningJobs.get.get(queryId).get
    if(source != None) {
      val res = Result.createErrorResult(exception)
      res.setQueryId(queryId)
      source ! res
    }else{
      logger.error("Exception for query " + queryId + " cannot be sent", exception)
    }
    runningJobs.send{runningJobs => runningJobs.remove(queryId); runningJobs}
  }

  override def processResult(result: QueryResult): Unit = {
    logger.info("Processing results for async query: " + result.getQueryId)
    val source = runningJobs.get.get(result.getQueryId).get
    if(source != None) {
      source ! result
    }else{
      logger.error("Results for query " + result.getQueryId + " cannot be sent")
    }
    if(result.isLastResultSet){
      sendACK(source, result.getQueryId)
      runningJobs.send{runningJobs => runningJobs.remove(result.getQueryId); runningJobs}
    }
  }

  private def sendACK(actorRef: ActorRef, qId: String): Unit ={
    implicit val execContext = context.system.dispatcher
    context.system.scheduler.scheduleOnce(4 seconds){
      logger.debug("Sending second ACK to: " + actorRef);
      actorRef ! new ACK(qId, QueryStatus.EXECUTED)
    }
  }

  private def methodExecute(ex:Execute, s:ActorRef): Unit ={
    try {
      runningJobs.send{runningJobs => runningJobs.put(ex.queryId, s); runningJobs}
      val result = connector.getQueryEngine().execute(ex.queryId, ex.workflow)
      result.setQueryId(ex.queryId)
      s ! result
      sendACK(s, ex.queryId)
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
      runningJobs.send{runningJobs => runningJobs.remove(ex.queryId); runningJobs}

    }
  }

  private def methodAsyncExecute(aex: AsyncExecute, sender: ActorRef) : Unit = {
    val asyncSender = sender
    try {
      runningJobs.send{runningJobs => runningJobs.put(aex.queryId, asyncSender); runningJobs}
      connector.getQueryEngine().asyncExecute(aex.queryId, aex.workflow, this)
      asyncSender ! ACK(aex.queryId, QueryStatus.IN_PROGRESS)
    } catch {
      case e: Exception => {
        val result = Result.createExecutionErrorResult(e.getMessage())
        result.setQueryId(aex.queryId)
        asyncSender ! result
      }
      case err: Error =>
        logger.error("Error in ConnectorActor (Receiving async LogicalWorkflow)")
    }
  }

  private def methodStopProcess(queryId: String, targetQueryId: String, sender: ActorRef) : Unit = {
    try {
      if (!runningJobs.get.contains(targetQueryId)){
        logger.debug("Received stop process for non-existing queryId "+targetQueryId)
        sender ! Result.createExecutionErrorResult("Received stop process for non-existing queryId "+targetQueryId)
      }else {
        logger.debug("Processing stop process. Reply to "+sender)
        runningJobs.sendOff{runningJobs => runningJobs.remove(targetQueryId); runningJobs}(context.dispatcher)
        connector.getQueryEngine.stop(targetQueryId)
        sender ! ACK(queryId, QueryStatus.EXECUTED)
      }

    } catch {
      case e: Exception => {
        val result = Result.createExecutionErrorResult(e.getMessage())
        result.setQueryId(queryId)
        sender ! result

      }
      case err: Error =>
        logger.error("Error in ConnectorActor (Receiving stop process)")
    }
  }

  private def methodPagedExecute(pex: PagedExecute, sender: ActorRef): Unit = {
    val pagedSender = sender
    try {
      logger.info("new running job: "+pex.queryId)
      runningJobs.send{runningJobs => runningJobs.put(pex.queryId, pagedSender); runningJobs}
      if(logger.isDebugEnabled){
        logger.debug("concurrent running Jobs = "+runningJobs.get.size)
      }
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
      case _: CreateTable => {
        eng.createTable(metadataOp.asInstanceOf[CreateTable].targetCluster,
          metadataOp.asInstanceOf[CreateTable].tableMetadata)
        (metadataOp.asInstanceOf[CreateTable].queryId, MetadataResult.OPERATION_CREATE_TABLE, null)
      }
      case _: CreateCatalog => {
        eng.createCatalog(metadataOp.asInstanceOf[CreateCatalog].targetCluster,
          metadataOp.asInstanceOf[CreateCatalog].catalogMetadata)
        (metadataOp.asInstanceOf[CreateCatalog].queryId, MetadataResult.OPERATION_CREATE_CATALOG, null)
      }
      case _: AlterCatalog => {
        eng.alterCatalog(metadataOp.asInstanceOf[AlterCatalog].targetCluster,
          metadataOp.asInstanceOf[AlterCatalog].catalogMetadata.getName,
          metadataOp.asInstanceOf[AlterCatalog].catalogMetadata.getOptions)
        (metadataOp.asInstanceOf[AlterCatalog].queryId, MetadataResult.OPERATION_CREATE_CATALOG, null)
      }
      case _: CreateIndex => {
        eng.createIndex(metadataOp.asInstanceOf[CreateIndex].targetCluster,
          metadataOp.asInstanceOf[CreateIndex].indexMetadata)
        (metadataOp.asInstanceOf[CreateIndex].queryId, MetadataResult.OPERATION_CREATE_INDEX, null)
      }
      case _: DropCatalog => {
        eng.dropCatalog(metadataOp.asInstanceOf[DropCatalog].targetCluster,
          metadataOp.asInstanceOf[DropCatalog].catalogName)
        (metadataOp.asInstanceOf[DropCatalog].queryId, MetadataResult.OPERATION_DROP_CATALOG, null)
      }
      case _: DropIndex => {
        eng.dropIndex(metadataOp.asInstanceOf[DropIndex].targetCluster, metadataOp.asInstanceOf[DropIndex].indexMetadata)
        (metadataOp.asInstanceOf[DropIndex].queryId, MetadataResult.OPERATION_DROP_INDEX, null)
      }
      case _: DropTable => {
        eng.dropTable(metadataOp.asInstanceOf[DropTable].targetCluster, metadataOp.asInstanceOf[DropTable].tableName)
        (metadataOp.asInstanceOf[DropTable].queryId, MetadataResult.OPERATION_DROP_TABLE, null)
      }
      case _: AlterTable => {
        eng.alterTable(metadataOp.asInstanceOf[AlterTable].targetCluster, metadataOp.asInstanceOf[AlterTable]
          .tableName, metadataOp.asInstanceOf[AlterTable].alterOptions)
        (metadataOp.asInstanceOf[AlterTable].queryId, MetadataResult.OPERATION_ALTER_TABLE, null)
      }
      case _: CreateTableAndCatalog => {
        eng.createCatalog(metadataOp.asInstanceOf[CreateTableAndCatalog].targetCluster,
          metadataOp.asInstanceOf[CreateTableAndCatalog].catalogMetadata)
        eng.createTable(metadataOp.asInstanceOf[CreateTableAndCatalog].targetCluster,
          metadataOp.asInstanceOf[CreateTableAndCatalog].tableMetadata)
        (metadataOp.asInstanceOf[CreateTableAndCatalog].queryId, MetadataResult.OPERATION_CREATE_TABLE, null)
      }
      case _: ProvideMetadata => {
        val listMetadata = eng.provideMetadata(metadataOp.asInstanceOf[ProvideMetadata].targetCluster)
        (metadataOp.asInstanceOf[ProvideMetadata].queryId, MetadataResult.OPERATION_DISCOVER_METADATA, listMetadata)

      }
      case _: ProvideCatalogsMetadata => {
        val listMetadata = eng.provideMetadata(metadataOp.asInstanceOf[ProvideCatalogsMetadata].targetCluster)
        (metadataOp.asInstanceOf[ProvideCatalogsMetadata].queryId, MetadataResult.OPERATION_IMPORT_CATALOGS,
          listMetadata)
      }
      case _: ProvideCatalogMetadata => {
        val listMetadata = eng.provideCatalogMetadata(metadataOp.asInstanceOf[ProvideCatalogMetadata].targetCluster,
          metadataOp.asInstanceOf[ProvideCatalogMetadata].catalogName)
        (metadataOp.asInstanceOf[ProvideCatalogMetadata].queryId, MetadataResult.OPERATION_IMPORT_CATALOG,
          listMetadata)
      }
      case _: ProvideTableMetadata => {
        val listMetadata = eng.provideTableMetadata(metadataOp.asInstanceOf[ProvideTableMetadata].targetCluster,
          metadataOp.asInstanceOf[ProvideTableMetadata].tableName)
        (metadataOp.asInstanceOf[ProvideTableMetadata].queryId, MetadataResult.OPERATION_IMPORT_TABLE,
          listMetadata)
      }
      case _ => ???
    }
  }


    //TODO 0.4.0 using FirstLevelName
    private def getTableMetadata(clusterName: ClusterName, tableName: TableName): TableMetadata = {
      metadataMapAgent.get.get(tableName).asInstanceOf[TableMetadata]
      /*
      val catalogName = tableName.getCatalogName
      val catalogMetadata = mapAgent.get.get(catalogName).asInstanceOf[CatalogMetadata]
      val tableMetadata = catalogMetadata.getTables.get(tableName)
      if(tableMetadata.getClusterRef==clusterName)
        return tableMetadata
      else
        return null
        */
    }

    private def getCatalogMetadata(catalogName: CatalogName): CatalogMetadata={
      return metadataMapAgent.get.get(catalogName).asInstanceOf[CatalogMetadata]
    }

    private def getCatalogs(cluster: ClusterName): util.List[CatalogMetadata] = {
      val r = new util.HashMap[CatalogName,CatalogMetadata]()
      //for(entry:java.util.Map.Entry[FirstLevelName,IMetadata] <- metadata.entrySet()){
      for((k,v) <- metadataMapAgent.get){
        k match {
          case name:CatalogName=>{
            val catalogMetadata = metadataMapAgent.get.get(k).asInstanceOf[CatalogMetadata]
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

}
