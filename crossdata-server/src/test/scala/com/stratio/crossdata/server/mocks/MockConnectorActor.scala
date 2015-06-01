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


package com.stratio.crossdata.server.mocks

import akka.actor.{Actor, ActorLogging, Props}
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow
import com.stratio.crossdata.common.result.{QueryResult,StorageResult,MetadataResult,ErrorResult}
import com.stratio.crossdata.communication.{Execute,StorageOperation, MetadataOperation,CreateTable, CreateCatalog,
CreateTableAndCatalog, CreateIndex, DropCatalog, DropIndex, DropTable, GetConnectorName, ReplyConnectorName}
import org.apache.log4j.Logger
import java.util.UUID
import com.stratio.crossdata.common.data.ResultSet


object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object MockConnectorActor {
  def props(): Props = Props(new MockConnectorActor())
}


class MockConnectorActor() extends Actor with ActorLogging {
  var lastqueryid="wrongqueryid"
  lazy val logger = Logger.getLogger(classOf[MockConnectorActor])


  // subscribe to cluster changes, re-subscribe when restart

  override def preStart(): Unit = {
    //#subscribe
    //Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def receive : Receive = {
    case (queryid:String,"updatemylastqueryId")=>{
      lastqueryid=queryid
    }
    case lw:LogicalWorkflow=>{
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>> lw ")
      val result=QueryResult.createQueryResult(UUID.randomUUID().toString, new ResultSet(), 0, true)
      result.setQueryId(lastqueryid);
      sender ! result
    }

    case execute: Execute=> {
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>> execute")
      val result=QueryResult.createQueryResult(UUID.randomUUID().toString, new ResultSet(), 0, true)
      sender ! result
    }

    case storageOp: StorageOperation=> {
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>> metadataOp")
      val result=StorageResult.createSuccessfulStorageResult("OK")
      result.setQueryId(storageOp.queryId)
      sender ! result
    }

    case metadataOp: MetadataOperation => {
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>> metadataOp2")
      val result=MetadataResult.createSuccessMetadataResult(0)
      val opclass=metadataOp.getClass().toString().split('.')
      opclass( opclass.length -1 ) match{
        case "CreateTable" =>{
          logger.debug(">>>>>>>>>>>>>>>>>>>>>>> create table")
          result.setQueryId( metadataOp.asInstanceOf[CreateTable].queryId )
          sender ! result
        }
        case "CreateCatalog"=>{
          logger.debug(">>>>>>>>>>>>>>>>>>>>>>> create catalog")
          result.setQueryId( metadataOp.asInstanceOf[CreateCatalog].queryId )
          sender ! result
        }
        case "CreateTableAndCatalog"=>{
          logger.debug(">>>>>>>>>>>>>>>>>>>>>>> create table and catalog")
          result.setQueryId( metadataOp.asInstanceOf[CreateTableAndCatalog].queryId )
          sender ! result
        }
        case "CreateIndex"=>{
          result.setQueryId( metadataOp.asInstanceOf[CreateIndex].queryId )
          sender ! result
        }
        case "DropCatalog"=>{
          result.setQueryId( metadataOp.asInstanceOf[DropCatalog].queryId )
          sender ! result
        }
        case "DropIndex"=>{
          result.setQueryId( metadataOp.asInstanceOf[DropIndex].queryId )
          sender ! result
        }
        case "DropTable"=>{
          result.setQueryId( metadataOp.asInstanceOf[DropTable].queryId )
          sender ! result
        }
      }
    }

    case msg: GetConnectorName => {
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>> connectorName")
      logger.debug("getconnectorname")
       sender ! ReplyConnectorName("myConnector")
     }

    case msg:Object=> {
      val myclass=msg.getClass()
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>>other")
      logger.debug("non recogniced message from " + sender )
      val err= new ErrorResult(null)
      sender ! err
    }
    case _=>{
      logger.debug(">>>>>>>>>>>>>>>>>>>>>>>other _")
    }

  }
}
