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


import java.util.concurrent.{TimeoutException, TimeUnit}

import akka.actor._
import akka.agent.Agent
import akka.cluster.ClusterEvent._
import akka.routing.{RoundRobinPool, DefaultResizer}
import akka.cluster.Cluster
import com.codahale.metrics.{Gauge, MetricRegistry}
import com.stratio.crossdata.common.connector.{IMetadataListener, ObservableMap, IConnector}
import com.stratio.crossdata.common.data.{ClusterName, Name}
import com.stratio.crossdata.common.exceptions.ConnectionException
import com.stratio.crossdata.common.manifest.{ConnectorType, CrossdataManifest}
import com.stratio.crossdata.common.metadata._
import com.stratio.crossdata.common.result.{ConnectToConnectorResult, DisconnectResult, Result}
import com.stratio.crossdata.common.utils.Metrics
import com.stratio.crossdata.communication._
import com.stratio.crossdata.connectors.ConnectorActor.RestartConnector
import com.stratio.crossdata.connectors.config.ConnectConfig
import com.stratio.crossdata.utils.ManifestUtils
import org.apache.log4j.Logger

import scala.collection.mutable
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.FiniteDuration

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

import scala.collection.mutable.{ListMap, Set}
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.ClusterEvent.CurrentClusterState



object ConnectorActor {
  def props(connectorApp: ConnectorApp, connector: IConnector):
  Props = Props(new ConnectorActor(connectorApp, connector))

  case class RestartConnector()
  val ConnectorRestartTimeout = 8
}

/**
 * Restart the actor only one time => It could store the metadata to restart the connector succesfully if needed
 */
class ConnectorActor(connectorApp: ConnectorApp, connector: IConnector) extends Actor with ActorLogging with ConnectConfig {

  override lazy val logger = Logger.getLogger(classOf[ConnectorActor])

  var connectedServers: Set[String] = Set()

  logger.info("Lifting ConnectorActor actor")

  implicit val executionContext = context.system.dispatcher

  val metadataMapAgent = Agent(new ObservableMap[Name, UpdatableMetadata])
  val runningJobsAgent = Agent(new mutable.ListMap[String, ActorRef])

  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)
  val connectorWorkerRef = context.system.actorOf(
    RoundRobinPool(num_connector_actor, Some(resizer))
      .props(Props(classOf[ConnectorWorkerActor], connector, metadataMapAgent, runningJobsAgent)), "ConnectorWorker")

  var metricName: Option[String] = Some(MetricRegistry.name(connectorApp.getConnectorName, "connection", "status"))
  Metrics.getRegistry.register(metricName.get,
    new Gauge[Boolean] {
      override def getValue: Boolean = {
        var status: Boolean = true
        if (connectedServers.isEmpty) {
          status = false
        }
        status
      }
    })

  val cluster = Cluster(context.system)


  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[ClusterDomainEvent])
  }

  override def postStop() {
    cluster.unsubscribe(self)
    Metrics.getRegistry.getNames.foreach(Metrics.getRegistry.remove(_))
  }

  override def postRestart(reason: Throwable): Unit = {
    Metrics.getRegistry.getNames.foreach(Metrics.getRegistry.remove(_))
  }

  override def receive: Receive = normalState

  def normalState: Receive = {

    case gcn: GetConnectorName => {
      logger.info(sender + " asked for my name: " + connectorApp.getConnectorName)
      connectedServers += sender.path.address.toString
      logger.info("Connected to Servers: " + connectedServers)
      sender ! ReplyConnectorName(connectorApp.getConnectorName)
    }

    case Connect(queryId, credentials, connectorClusterConfig) => {
      logger.debug("->" + "Receiving MetadataRequest")
      logger.info("Received connect command")
      try {
        connector.connect(credentials, connectorClusterConfig)
        val result = ConnectToConnectorResult.createSuccessConnectResult(queryId)
        sender ! result //TODO once persisted sessionId,
      } catch {
        case e: ConnectionException => {
          logger.error(e.getMessage)
          val result = ConnectToConnectorResult.createFailureConnectResult(queryId,e)
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
        result = DisconnectResult.createDisconnectResult(
          "Disconnected successfully from " + disconnectRequest.clusterName)
      } catch {
        case ex: ConnectionException => {
          result = Result.createConnectionErrorResult("Cannot disconnect from " + disconnectRequest.clusterName)
        }
      }
      result.setQueryId(disconnectRequest.queryId)
      //this.state = State.Started //if it doesn't connect, an exception will be thrown and we won't get here
      sender ! result //TODO once persisted sessionId,
    }

    case connManifest: GetConnectorManifest => {
      logger.info(sender + " asked for my connector manifest ")
      sender ! ReplyConnectorManifest(connectorApp.getConnectorManifest)
    }
    case datManifest: GetDatastoreManifest => {
      logger.info(sender + " asked for my datastore manifest")
      for (datastore <- connectorApp.getDatastoreManifest){
        sender ! ReplyDatastoreManifest(datastore)
      }
    }

    case MemberUp(member) => {
      logger.info("Member up")
      logger.info("Member is Up: " + member.toString + member.getRoles + "!")

      val roleIterator = member.getRoles.iterator()

      while (roleIterator.hasNext()) {
        val role = roleIterator.next()
        role match {
          case "server" => {
            logger.info("added new server " + member.address.toString)
            connectedServers = connectedServers + member.address.toString
            val connectorManagerActorRef = context.actorSelection(RootActorPath(member.address) / "user" / "crossdata-server" / "ConnectorManagerActor")
            connectorManagerActorRef ! ConnectorUp(self.path.address.toString)
          }
          case _ => {
            logger.debug(member.address + " has the role: " + role)
          }

        }
      }
    }

   case clusterState: CurrentClusterState => {
      logger.info("Current members: " + clusterState.members.mkString(", "))

      clusterState.members.foreach { member =>
        val roleIterator = member.getRoles.iterator()
        while (roleIterator.hasNext()) {
          val role = roleIterator.next()
          role match {
            case "server" => {
              connectedServers = connectedServers  + member.address.toString
              logger.info(s"added server ${member.address}")
            }
            case _ => {
              logger.debug(member.address + " has the role: " + role)
            }
          }
        }
      }
    }

    case UnreachableMember(member) => {
      logger.info("Member detected as unreachable: " + member)
      //TODO an unreachable could become reaachable
      if (member.hasRole("server")) {
        connectedServers = connectedServers - member.address.toString
        log.info("Member removed -> remaining servers" + connectedServers)
        if(connectedServers.isEmpty) {
          context.become(restarting)
          self ! RestartConnector
          log.info("There is no server in the cluster. The connector must be restarted")
        }

      }
    }

    case MemberExited(member) => {
      logger.info("Member exited: " + member)
    }

    case MemberRemoved(member, previousStatus) => {
      if (member.hasRole("server")) {
        connectedServers = connectedServers - member.address.toString
        log.info("Member removed -> remaining servers" + connectedServers)
        if(connectedServers.isEmpty) {
          context.become(restarting)
          self ! RestartConnector
          log.info("There is no server in the cluster. The connector must be restarted")
        }
      }
      logger.info("Member is Removed: " + member.address + " after " + previousStatus)
    }

    case memberEvent: MemberEvent => {
      logger.info("MemberEvent received: " + memberEvent.toString)
    }


    //Messages exchanged with ConnectorApp
    case Shutdown => {
      logger.debug("->" + "Receiving Shutdown")
      Metrics.getRegistry.getNames.foreach(Metrics.getRegistry.remove(_))
      this.shutdown
      sender ! true
    }

    case listener: IMetadataListener => {
      logger.info("Adding new metadata listener")
      metadataMapAgent.send(oMap => {
        oMap.addListener(listener); oMap
      })
    }

    case GetConnectorStatus => {
      sender ! ConnectorStatus(!connectedServers.isEmpty)
    }

    //ConnectorWorkerMessages
    case otherMsg => {
      connectorWorkerRef forward  otherMsg
    }

  }

  def shutdown(): Unit = {
    logger.debug("ConnectorActor is shutting down")
    connector.shutdown()
  }

  //Used to manage queued messages which are dismissed currently.
  def restarting: Receive = {

    case RestartConnector => {

      logger.info("Restarting the connector actor system")

     //TODO move to connectorApp.stop()
     Try(Await.result( Future(connector.shutdown()), FiniteDuration( ConnectorActor.ConnectorRestartTimeout, TimeUnit.SECONDS) )) match {
       case Success(_) => logger.info(s"Connector successfully stopped")
       case Failure(exc) => exc match{
         case _: TimeoutException => logger.warn("Cannot stop connector within the timeout. Anyway, the actor system is going to be restarted.")
         case otherException => logger.warn(s"Cannot stop connector ${exc.getMessage}. Anyway, the actor system is going to be restarted.")
       }
     }

      connectorApp.restart(connector)
      context.stop(self)
    }


    case dismissedMessage => {
      log.debug(s"Discarded message -> $dismissedMessage")
    }
  }


}
