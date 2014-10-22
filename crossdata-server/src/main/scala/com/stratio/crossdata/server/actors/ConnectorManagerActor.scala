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

package com.stratio.crossdata.server.actors

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.crossdata.common.connector.ConnectorClusterConfig
import com.stratio.crossdata.common.data
import com.stratio.crossdata.common.data.ConnectorName
import com.stratio.crossdata.common.result.ConnectResult
import com.stratio.crossdata.common.statements.structures.selectors.SelectorHelper
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.communication._
import com.stratio.crossdata.core.connector.ConnectorManager
import com.stratio.crossdata.core.execution.ExecutionManager
import com.stratio.crossdata.core.metadata.MetadataManager
import org.apache.log4j.Logger

import scala.collection.JavaConversions._

object ConnectorManagerActor {
  def props(connectorManager: ConnectorManager): Props = Props(new ConnectorManagerActor(connectorManager))
}

class ConnectorManagerActor(connectorManager: ConnectorManager) extends Actor with ActorLogging {

  lazy val logger = Logger.getLogger(classOf[ConnectorManagerActor])
  logger.info("Lifting connectormanager manager actor")
  val coordinatorActorRef = context.actorSelection("../CoordinatorActor")

  val connectors = MetadataManager.MANAGER.getConnectorNames(data.Status.ONLINE)
  MetadataManager.MANAGER.setConnectorStatus(connectors, data.Status.OFFLINE)

  override def preStart(): Unit = {
    Cluster(context.system).subscribe(self, classOf[ClusterDomainEvent])
  }

  override def postStop(): Unit =
    Cluster(context.system).unsubscribe(self)

  def receive = {

    /**
     * A new actor connects to the cluster. If the new actor is a connectormanager, we requests its name.
     */
    //TODO Check that new actors are recognized and their information stored in the MetadataManager
    case mu: MemberUp => {
      logger.info("Member is Up: " + mu.toString + mu.member.getRoles)
      val it = mu.member.getRoles.iterator()
      while (it.hasNext()) {
        val rol = it.next()
        rol match {
          case "connectormanager" => {
            val connectorActorRef = context.actorSelection(RootActorPath(mu.member.address) / "user" / "ConnectorActor")
            connectorActorRef ! getConnectorName()
          }
          case _ =>{
            logger.info("MemberUp: rol is not in this actor.")

          }
        }
      }
    }

    /**
     * CONNECTOR answers its name.
     */
    case msg: replyConnectorName => {
      logger.info("Connector Name " + msg.name + " received from " + sender)
      val actorRefUri = StringUtils.getAkkaActorRefUri(sender)
      logger.info("Registering connectormanager at: " + actorRefUri)
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
      MetadataManager.MANAGER.setConnectorStatus(connectorName, data.Status.ONLINE)

    }

    case c: ConnectResult => {
      logger.info("Connect result from " + sender + " => " + c.getSessionId)
    }

    //Pass the message to the connectorActor to extract the member in the cluster
    case state: CurrentClusterState => {
      logger.info("Current members: " + state.members.mkString(", "))
      //TODO Process CurrentClusterState
      try {
        val connectors = MetadataManager.MANAGER.getConnectorNames(data.Status.ONLINE)
        MetadataManager.MANAGER.setConnectorStatus(connectors, data.Status.OFFLINE)
      } catch {
        case e: Exception => {
          log.error("Couldn't set connectors to OFFLINE")
        }
      }

    }

    case member: UnreachableMember => {
      logger.info("Member detected as unreachable: " + member)
      //TODO Process UnreachableMember
    }

    case member: MemberRemoved => {
      logger.info("Member is Removed: " + member.member.address)
      logger.info("Member info: " + member.toString)
      val actorRefUri = StringUtils.getAkkaActorRefUri(member.member.address)
      val connectorName = ExecutionManager.MANAGER.getValue(actorRefUri +"/user/ConnectorActor/")
      MetadataManager.MANAGER.setConnectorStatus(connectorName.asInstanceOf[ConnectorName],
        data.Status.OFFLINE)
    }

    case member: MemberExited => {
      logger.info("Member is exiting: " + member.member.address)
      val actorRefUri = StringUtils.getAkkaActorRefUri(sender)
      val connectorName = ExecutionManager.MANAGER.getValue(actorRefUri)
      MetadataManager.MANAGER.setConnectorStatus(connectorName.asInstanceOf[ConnectorName],
        data.Status.SHUTTING_DOWN)
    }

    case _: MemberEvent => {
      logger.info("Receiving anything else")
      //TODO Process MemberEvent
    }

    case clusterMetricsChanged: ClusterMetricsChanged => {

    }

    case clusterDomainEvent: ClusterDomainEvent => {
      logger.info("ClusterDomainEvent: " + clusterDomainEvent)
      //TODO Process ClusterDomainEvent
    }

    case ReceiveTimeout => {
      logger.info("ReceiveTimeout")
      //TODO Process ReceiveTimeout
    }

    case unknown: Any=> {
      logger.error("Unknown event: " + unknown)
    }
  }

}
