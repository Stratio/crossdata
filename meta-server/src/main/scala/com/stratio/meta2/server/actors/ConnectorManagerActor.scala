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

package com.stratio.meta2.server.actors

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.utils.StringUtils
import com.stratio.meta.communication._
import com.stratio.meta2.common.data.{Status, ConnectorName}
import com.stratio.meta2.core.connector.ConnectorManager
import com.stratio.meta2.core.metadata.MetadataManager
import org.apache.log4j.Logger
import com.stratio.meta2.core.execution.ExecutionManager
import com.stratio.meta.common.connector.ConnectorClusterConfig
import com.stratio.meta2.common.statements.structures.selectors.SelectorHelper
import scala.collection.JavaConversions._

object ConnectorManagerActor {
  def props(connectorManager: ConnectorManager): Props = Props(new ConnectorManagerActor(connectorManager))
}

class ConnectorManagerActor(connectorManager: ConnectorManager) extends Actor with ActorLogging {

  lazy val logger = Logger.getLogger(classOf[ConnectorManagerActor])
  log.info("Lifting connector actor")
  val coordinatorActorRef = context.actorSelection("../CoordinatorActor")

  override def preStart(): Unit = {
    Cluster(context.system).subscribe(self, classOf[ClusterDomainEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit =
    Cluster(context.system).unsubscribe(self)

  def receive = {

    /**
     * A new actor connects to the cluster. If the new actor is a connector, we requests its name.
     */
    //TODO Check that new actors are recognized and their information stored in the MetadataManager
    case mu: MemberUp => {
      log.info("Member is Up: " + mu.toString + mu.member.getRoles)
      val it = mu.member.getRoles.iterator()
      while (it.hasNext()) {
        val rol = it.next()
        rol match {
          case "connector" => {
            val connectorActorRef = context.actorSelection(RootActorPath(mu.member.address) / "user" / "ConnectorActor")
            connectorActorRef ! getConnectorName()
            //connectorActorRef ! Start()
          }
          case _ =>{
            log.debug("MemberUp: rol is not in this actor.")

          }
        }
      }
    }

    /**
     * CONNECTOR answers its name.
     */
    case msg: replyConnectorName => {
      log.info("Connector Name received from " + sender)
      val actorRefUri = StringUtils.getAkkaActorRefUri(sender)
      val connectorName = new ConnectorName(msg.name)
      ExecutionManager.MANAGER.createEntry(actorRefUri, connectorName, true)

      MetadataManager.MANAGER.addConnectorRef(connectorName, actorRefUri)

      val connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName)
      val clusterProps = connectorMetadata.getClusterProperties

      if((clusterProps != null) && (!clusterProps.isEmpty)){
        for(clusterProp <- clusterProps.entrySet()){
          val opts = MetadataManager.MANAGER.getCluster(clusterProp.getKey).getOptions;
          val connectorClusterConfig = new ConnectorClusterConfig(clusterProp.getKey,
            SelectorHelper.convertSelectorMapToStringMap(opts))
          sender ! new Connect(null, connectorClusterConfig)
        }
      }
      MetadataManager.MANAGER.setConnectorStatus(connectorName, Status.ONLINE)

    }

    //Pass the message to the connectorActor to extract the member in the cluster
    case state: CurrentClusterState => {
      logger.info("Current members: " + state.members.mkString(", "))
      //TODO Process CurrentClusterState
    }
    case member: UnreachableMember => {
      logger.info("Member detected as unreachable: " + member)
      //TODO Process UnreachableMember
    }
    case member: MemberRemoved => {
      logger.info("Member is Removed: " + member.member.address)
      val actorRefUri = StringUtils.getAkkaActorRefUri(sender)
      val connectorName = ExecutionManager.MANAGER.getValue(actorRefUri)
      MetadataManager.MANAGER.setConnectorStatus(connectorName.asInstanceOf[ConnectorName],
        com.stratio.meta2.common.data.Status.OFFLINE)
    }
    case _: MemberEvent => {
      logger.info("Receiving anything else")
      //TODO Process MemberEvent
    }
    case _: ClusterDomainEvent => {
      logger.debug("ClusterDomainEvent")
      //TODO Process ClusterDomainEvent
    }
    case ReceiveTimeout => {
      logger.warn("ReceiveTimeout")
      //TODO Process ReceiveTimeout
    }
    case _=>
      logger.error("Unknown event")
    //      sender ! "OK"
    //memberActorRef.tell(objectConWorkflow, context.sender)
  }

}
