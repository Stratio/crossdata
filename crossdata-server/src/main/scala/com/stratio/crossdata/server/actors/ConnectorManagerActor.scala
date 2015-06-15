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


import akka.actor.{ReceiveTimeout, ActorLogging, Actor, Props, Address}
import akka.cluster.{MemberStatus, Cluster}
import akka.cluster.ClusterEvent.{ClusterMetricsChanged, MemberEvent, MemberExited,
MemberRemoved, UnreachableMember, CurrentClusterState, MemberUp, ClusterDomainEvent}
import com.stratio.crossdata.common.connector.ConnectorClusterConfig
import com.stratio.crossdata.common.data
import com.stratio.crossdata.common.data.{NodeName, ConnectorName, Status}
import com.stratio.crossdata.common.executionplan.{ResultType, ExecutionType, ManagementWorkflow}
import com.stratio.crossdata.common.result.{ErrorResult, Result, ConnectResult}
import java.util
import java.util.{Collections, UUID}

import akka.actor.{Actor, ActorLogging, Address, Props, ReceiveTimeout}
import akka.cluster.ClusterEvent.{ClusterDomainEvent, ClusterMetricsChanged, CurrentClusterState, MemberEvent, MemberExited, MemberRemoved, MemberUp, UnreachableMember}
import akka.cluster.{Cluster, MemberStatus}
import com.stratio.crossdata.common.connector.ConnectorClusterConfig
import com.stratio.crossdata.common.data
import com.stratio.crossdata.common.data.{ConnectorName, DataStoreName, NodeName, Status}
import com.stratio.crossdata.common.exceptions.{ExecutionException, ManifestException}
import com.stratio.crossdata.common.executionplan.{ExecutionType, ManagementWorkflow, ResultType}
import com.stratio.crossdata.common.manifest._
import com.stratio.crossdata.common.metadata.{ConnectorMetadata, DataStoreMetadata}
import com.stratio.crossdata.common.result.{ConnectToConnectorResult, ConnectResult, ErrorResult, Result}
import com.stratio.crossdata.common.statements.structures.SelectorHelper
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.communication._
import com.stratio.crossdata.core.execution.{ExecutionInfo, ExecutionManager}
import com.stratio.crossdata.core.loadwatcher.LoadWatcherManager
import com.stratio.crossdata.core.metadata.MetadataManager
import org.apache.log4j.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable

object ConnectorManagerActor {
  def props(cluster: Cluster): Props = Props(new ConnectorManagerActor(cluster))
}

class ConnectorManagerActor(cluster: Cluster) extends Actor with ActorLogging {

  lazy val logger = Logger.getLogger(classOf[ConnectorManagerActor])
  logger.info("Lifting connector manager actor")
  val coordinatorActorRef = context.actorSelection("../CoordinatorActor")


  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberUp],classOf[CurrentClusterState],classOf[UnreachableMember], classOf[MemberRemoved], classOf[MemberExited])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }



  def receive : Receive= {

    case ConnectorUp(memberAddress: String) => {
      log.info("connectorUp " + memberAddress)
      lazy val connectorActorRef = context.actorSelection(memberAddress + "/user/ConnectorActor")
      val nodeName = new NodeName(memberAddress)
      //if the node is offline
      if (MetadataManager.MANAGER.notIsNodeOnline(nodeName)) {
        logger.debug("Asking its name to the connector " + memberAddress)
        connectorActorRef ! GetConnectorName()
        connectorActorRef ! GetConnectorManifest()
        connectorActorRef ! GetDatastoreManifest()
      }
    }

    /**
     * CONNECTOR answers its name.
     */
    case msg: ReplyConnectorName => {
      logger.info("Connector Name " + msg.name + " received from " + sender)
      val actorRefUri = StringUtils.getAkkaActorRefUri(sender, false)
      logger.info("Registering connector from: " + actorRefUri)

      if (actorRefUri != null) {
        val connectorName = new ConnectorName(msg.name)
        ExecutionManager.MANAGER.createEntry(actorRefUri, connectorName, true)

        MetadataManager.MANAGER.addConnectorRef(connectorName, actorRefUri)

        val connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName)
        val clusterProps = connectorMetadata.getClusterProperties

        if ((clusterProps != null) && (!clusterProps.isEmpty)) {

          for (clusterProp <- clusterProps.entrySet()) {
            val clusterName = clusterProp.getKey
            val clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName)
            val optsCluster = MetadataManager.MANAGER.getCluster(clusterName).getOptions

            val optsConnector = clusterMetadata.getConnectorAttachedRefs.get(connectorName).getProperties

            val clusterConfig = new ConnectorClusterConfig(clusterName,
              SelectorHelper.convertSelectorMapToStringMap(optsConnector),
              SelectorHelper.convertSelectorMapToStringMap(optsCluster))

            clusterConfig.setDataStoreName(clusterMetadata.getDataStoreRef)

            val reconnectQueryUUID = UUID.randomUUID().toString
            val executionInfo = new ExecutionInfo
            executionInfo.setRemoveOnSuccess(true)
            executionInfo.setUpdateOnSuccess(true)
            val executionWorkflow = new ManagementWorkflow(reconnectQueryUUID, Collections.emptySet[String](), ExecutionType.ATTACH_CONNECTOR, ResultType.RESULTS)
            executionWorkflow.setClusterName(clusterName)
            executionInfo.setWorkflow(executionWorkflow)
            ExecutionManager.MANAGER.createEntry(reconnectQueryUUID, executionInfo)

            sender ! new Connect(reconnectQueryUUID, null, clusterConfig)
          }
        }
        MetadataManager.MANAGER.setConnectorStatus(connectorName, Status.ONLINE)
        MetadataManager.MANAGER.setNodeStatus(new NodeName(sender.path.address.toString), Status.ONLINE)
      } else {
        logger.error("Actor reference of the sender can't be null")
      }

    }
    case ReplyConnectorManifest(connectorManifest) => {
      persistConnectorManifest(connectorManifest)
    }

    case ReplyDatastoreManifest(datastoreManifest) => {
      persistDatastoreManifest(datastoreManifest)
    }


    case c: ConnectToConnectorResult => {
      logger.info("Connect result from " + sender)
      coordinatorActorRef forward c
    }

      //TODO delete?
    case er: ErrorResult => {
      logger.info("Error result from " + sender)
      coordinatorActorRef ! er
    }

    /**
     * A new actor connects to the cluster. If the new actor is a connector, we requests its name.
     */
    //TODO Check that new actors are recognized and their information stored in the MetadataManager
    case MemberUp(member) => {
      logger.info("Member is Up: " + member.toString + member.getRoles)
      val it = member.getRoles.iterator()
      while (it.hasNext()) {
        val role = it.next()
        role match {
          case "connector" => {
            self ! ConnectorUp(member.address.toString)
          }
          case _ => {
            logger.debug(member.address + " has the role: " + role)
          }
        }
      }
    }

    //Pass the message to the connectorActor to extract the member in the cluster
    case state: CurrentClusterState => {

      logger.info("Current members: " + state.members.mkString(", "))
      val members = state.getMembers

      var foundServers = mutable.HashSet[Address]()
      for (member <- members) {
        logger.info("Address: " + member.address + ", roles: " + member.getRoles)
        if (member.getRoles.contains("server")) {
          foundServers += member.address
          logger.info("New server added. Size: " + foundServers.size)
        }
      }
      if (foundServers.size == 1) {

        logger.info("Resetting Connectors status")
        val connectors = MetadataManager.MANAGER.getConnectorNames(data.Status.ONLINE)
        MetadataManager.MANAGER.setConnectorStatus(connectors, data.Status.OFFLINE)
        val nodes = MetadataManager.MANAGER.getNodeNames(data.Status.ONLINE)
        MetadataManager.MANAGER.setNodeStatus(nodes, data.Status.OFFLINE)

        for (member <- members) {
          logger.info("Address: " + member.address + ", roles: " + member.getRoles)
          if (member.getRoles.contains("connector") && member.status == MemberStatus.Up) {
            if (MetadataManager.MANAGER.isNodeOffline(new NodeName(sender.path.address.toString))) {
              // TODO log.info(s"checking if the connector $member.address is actually online")
            } else {
              logger.debug("New connector joined to the cluster")
              self ! ConnectorUp(member.address.toString)
            }
          }
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

      val actorRefUri = StringUtils.getAkkaActorRefUri(member.member.address, false)+"/user/ConnectorActor"
      if(ExecutionManager.MANAGER.exists(actorRefUri)){

        val connectorName = ExecutionManager.MANAGER.getValue(actorRefUri)
        logger.info("Removing Connector: " + connectorName)
        MetadataManager.MANAGER.removeActorRefFromConnector(connectorName.asInstanceOf[ConnectorName], actorRefUri)
        MetadataManager.MANAGER.setNodeStatus(new NodeName(member.member.address.toString), Status.OFFLINE)
        ExecutionManager.MANAGER.deleteEntry(actorRefUri)
      } else {
        logger.warn(actorRefUri + " not found in the Execution Manager")
      }
      // example of member.member.address = akka.tcp://CrossdataServerCluster@127.0.0.1:13421
      val lwmkey = member.member.address.toString.split("@")(1).split(":")(0)
      if (LoadWatcherManager.MANAGER.exists(lwmkey)) {
        LoadWatcherManager.MANAGER.deleteEntry(lwmkey)
      }
    }

    case member: MemberExited => {
      logger.info("Member is exiting: " + member.member.address)
      val actorRefUri = StringUtils.getAkkaActorRefUri(sender, false)
      val connectorName = ExecutionManager.MANAGER.getValue(actorRefUri)
      MetadataManager.MANAGER.removeActorRefFromConnector(connectorName.asInstanceOf[ConnectorName], actorRefUri)
      MetadataManager.MANAGER.setNodeStatus(new NodeName(member.member.address.toString), Status.OFFLINE)
    }


    case unknown: Any => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
      logger.error("Unknown event: " + unknown)
    }
  }

  def persistDatastoreManifest(dataStoreManifest: CrossdataManifest) {
    dataStoreManifest match {
      case dataStoreType: DataStoreType => {
        val name: DataStoreName = new DataStoreName(dataStoreType.getName)
        /*if (MetadataManager.MANAGER.exists(name)) {
          throw new ManifestException(new ExecutionException(name + " already exists"))
        }*/
        if (!MetadataManager.MANAGER.exists(name)) {
          val version: String = dataStoreType.getVersion
          val requiredProperties: PropertiesType = dataStoreType.getRequiredProperties
          val optionalProperties: PropertiesType = dataStoreType.getOptionalProperties
          val behaviorsType: BehaviorsType = dataStoreType.getBehaviors
          val datastoreFunctions: DataStoreFunctionsType = dataStoreType.getFunctions
          val dataStoreMetadata: DataStoreMetadata = new DataStoreMetadata(name, version, if ((requiredProperties == null)) null else requiredProperties.getProperty, if ((optionalProperties == null)) null else optionalProperties.getProperty, if ((behaviorsType == null)) null else behaviorsType.getBehavior, if ((datastoreFunctions == null)) null else datastoreFunctions.getFunction)
          MetadataManager.MANAGER.createDataStore(dataStoreMetadata)
          logger.debug("DataStore added: " + MetadataManager.MANAGER.getDataStore(name).toString)
        }
      }
      case _ => throw new ClassCastException
    }
  }

  def persistConnectorManifest(connectorManifest: CrossdataManifest) {
    connectorManifest match {
      case connectorType: ConnectorType => {
        val name: ConnectorName = new ConnectorName(connectorType.getConnectorName)
        val dataStoreRefs: DataStoreRefsType = connectorType.getDataStores
        val isNative: Boolean = Option(connectorType.isNative).getOrElse(false).asInstanceOf[Boolean]
        val version: String = connectorType.getVersion
        val requiredProperties: PropertiesType = connectorType.getRequiredProperties
        val optionalProperties: PropertiesType = connectorType.getOptionalProperties
        val supportedOperations: SupportedOperationsType = connectorType.getSupportedOperations
        val connectorFunctions: ConnectorFunctionsType = connectorType.getFunctions
        val excludedFunctions: java.util.List[String] = new java.util.ArrayList[String]
        if (connectorFunctions != null) {
          val convertedExcludes: java.util.Set[String] = ManifestHelper
            .convertManifestExcludedFunctionsToMetadataExcludedFunctions(connectorFunctions.getExclude)
          excludedFunctions.addAll(convertedExcludes)
        }
        var connectorMetadata: ConnectorMetadata = null
        if (MetadataManager.MANAGER.exists(name)) {
          connectorMetadata = MetadataManager.MANAGER.getConnector(name)
          /*if (connectorMetadata.isManifestAdded) {
            throw new ManifestException(new ExecutionException("Connector " + name + " was already added"))
          }*/

          connectorMetadata.setVersion(version)
          connectorMetadata.setNative(isNative)
          connectorMetadata.setDataStoreRefs(ManifestHelper.convertManifestDataStoreNamesToMetadataDataStoreNames(dataStoreRefs.getDataStoreName))
          connectorMetadata.setRequiredProperties(if ((requiredProperties == null)) new util.HashSet[PropertyType] else ManifestHelper.convertManifestPropertiesToMetadataProperties(requiredProperties.getProperty))
          connectorMetadata.setOptionalProperties(if ((optionalProperties == null)) new util.HashSet[PropertyType] else ManifestHelper.convertManifestPropertiesToMetadataProperties(optionalProperties.getProperty))
          connectorMetadata.setSupportedOperations(supportedOperations.getOperation)
          connectorMetadata.setConnectorFunctions(if ((connectorFunctions == null)) new util.HashSet[FunctionType] else ManifestHelper.convertManifestFunctionsToMetadataFunctions(connectorFunctions.getFunction))
          connectorMetadata.setExcludedFunctions(new util.HashSet[String](excludedFunctions))

        } else {
          connectorMetadata = new ConnectorMetadata(name, version, isNative, if ((dataStoreRefs == null)) new util
          .ArrayList[String]
          else dataStoreRefs.getDataStoreName, if ((requiredProperties == null)) new
              java.util.ArrayList[PropertyType]
          else requiredProperties.getProperty, if ((optionalProperties == null))
            new java.util.ArrayList[PropertyType]
          else optionalProperties.getProperty,
            if ((supportedOperations == null)) new java.util.ArrayList[String] else supportedOperations.getOperation, if ((connectorFunctions == null)) new java.util.ArrayList[FunctionType] else connectorFunctions.getFunction, excludedFunctions)
        }
        logger.debug("added connector metadata")
        connectorMetadata.setManifestAdded(true)
        MetadataManager.MANAGER.createConnector(connectorMetadata, false)
      }
      case _ => throw new ClassCastException
    }

  }

}
