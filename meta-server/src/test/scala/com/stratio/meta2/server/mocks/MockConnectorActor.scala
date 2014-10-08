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

/**
 * Created by carlos on 1/10/14.
 */


package com.stratio.connectors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.result.{ErrorResult, ErrorType, MetadataResult}
import com.stratio.meta.communication._
;


object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object MockConnectorActor {
  def props(): Props = Props(new MockConnectorActor())
}


class MockConnectorActor() extends Actor with ActorLogging {


  // subscribe to cluster changes, re-subscribe when restart

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def receive = {

    /*
    case s: String=>
       println("mock receiving string="+s)
    case _: com.stratio.meta.communication.Start =>
       println("start")
     case connectRequest: com.stratio.meta.communication.Connect =>
       println("connect")
     case _: com.stratio.meta.communication.Shutdown =>
       println("shutdown")
     case MemberUp(member) =>
       println("memberup")
     case state: CurrentClusterState =>
       println("currentclusterstate")
     case UnreachableMember(member) =>
       println("UnreachableMember")
     case MemberRemoved(member, previousStatus) =>
       println("MemverRemoved")
     case _: MemberEvent =>
       println("MemberEvent")
       */

    case ex:Execute=>{
      println("execute")
      val result=MetadataResult.createSuccessMetadataResult()
      result.setQueryId(ex.queryId)
      sender ! result
    }

    case metadataOp: MetadataOperation => {
      val result=MetadataResult.createSuccessMetadataResult()
      val opclass=metadataOp.getClass().toString().split('.')
      opclass( opclass.length -1 ) match{
        case "CreateTable" =>{
          result.setQueryId( metadataOp.asInstanceOf[CreateTable].queryId )
          sender ! result
        }
        case "CreateCatalog"=>{
          result.setQueryId( metadataOp.asInstanceOf[CreateCatalog].queryId )
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

    case msg: getConnectorName => {
      println("getconnectorname")
       sender ! replyConnectorName("myConnector")
     }

    case _=> {
      println("non recogniced message")
      val err=new ErrorResult(ErrorType.EXECUTION,null)
      sender ! err
    }

  }
}
