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
import akka.remote.QuarantinedEvent
import akka.routing.{RoundRobinPool, DefaultResizer}
import akka.util.Timeout
import akka.cluster.Cluster
import com.codahale.metrics.{Gauge, MetricRegistry}
import com.stratio.crossdata.common.connector.{IMetadataListener, ObservableMap, IConfiguration, IConnector}
import com.stratio.crossdata.common.data.{ClusterName, Name}
import com.stratio.crossdata.common.exceptions.ConnectionException
import com.stratio.crossdata.common.metadata._
import com.stratio.crossdata.common.result.{DisconnectResult, Result, ConnectResult}
import com.stratio.crossdata.common.utils.Metrics
import com.stratio.crossdata.communication.{Shutdown, ReplyConnectorName, GetConnectorName, ConnectorUp}
import com.stratio.crossdata.connectors.ConnectorActor.RestartConnector
import com.stratio.crossdata.connectors.config.ConnectConfig
import org.apache.log4j.Logger

import scala.collection.mutable
import scala.collection.mutable.Set
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.{FiniteDuration, DurationInt}

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}


object ConnectorActor {
  def props(connectorApp: ConnectorApp, connector: IConnector):
  Props = Props(new ConnectorActor(connectorApp, connector))

  case class RestartConnector()

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
      .props(Props(classOf[ConnectorWorkerActor], connector, metadataMapAgent, runningJobsAgent)), "ConnectorWorkers")


  connector.init(new IConfiguration {})

  var metricName: Option[String] = Some(MetricRegistry.name(connector.getConnectorName, "connection", "status"))
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

  implicit val timeout = Timeout(10 seconds)
  val cluster = Cluster(context.system)

  //TODO: test if it works with one thread and multiple threads
  var stateActor = State.Stopped


  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[ClusterDomainEvent])
    context.system.eventStream.subscribe(self, classOf[QuarantinedEvent])
  }

  override def postStop() {
    cluster.unsubscribe(self)
    context.system.eventStream.unsubscribe(self)
    Metrics.getRegistry.getNames.foreach(Metrics.getRegistry.remove(_))
    super.postStop
    log.info("ConnectorRestart actor postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    //unsubscribe to restore updatable metadata?? or leave
    //super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("removing metrics")
    Metrics.getRegistry.getNames.foreach(Metrics.getRegistry.remove(_))
    //subscribe // or join
    //super.postRestart(reason)
  }

  override def receive: Receive = normalState

  def normalState: Receive = {

    case GetConnectorName => {
      logger.info(sender + " asked for my name: " + connectorName)
      connectedServers += sender.path.address.toString
      logger.info("Connected to Servers: " + connectedServers)
      sender ! ReplyConnectorName(connectorName)
    }

    case connectRequest: com.stratio.crossdata.communication.Connect => {
      logger.debug("->" + "Receiving MetadataRequest")
      logger.info("Received connect command")
      try {
        connector.connect(connectRequest.credentials, connectRequest.connectorClusterConfig)
        //this.state = State.Started //if it doesn't connect, an exception will be thrown and we won't get here
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

    case QuarantinedEvent(remoteAddress, uid) => {
      logger.info(s"Quarentined event received from $remoteAddress with uid: '$uid'")
      logger.info("Connected Servers" + connectedServers)
      if ((connectedServers - remoteAddress.toString).isEmpty) {
        //It should be a new Connector instance??
        /*context.become(expectingKill)
        self ! RestartConnector
        */
      }

    }

    /** ClusterEvents */
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
          context.become(expectingKill)
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
          context.become(expectingKill)
          self ! RestartConnector
          log.info("There is no server in the cluster. The connector must be restarted")
        }

      }
      logger.info("Member is Removed: " + member.address + " after " + previousStatus)
    }

    case memberEvent: MemberEvent => {
      logger.info("MemberEvent received: " + memberEvent.toString)
    }

    case Shutdown => {
      logger.debug("->" + "Receiving Shutdown")
      Metrics.getRegistry.getNames.foreach(Metrics.getRegistry.remove(_))
      this.shutdown
      sender ! true
    }

    case listener: IMetadataListener => {
      logger.info("Adding new metadata listener")
      metadataMapAgent.send(oMap => {oMap.addListener(listener); oMap})
    }


    case otherMsg => {
      connectorWorkerRef forward  otherMsg
    }

  }

  def shutdown(): Unit = {
    logger.debug("ConnectorActor is shutting down")
    this.stateActor = State.Stopping
    connector.shutdown()
    this.stateActor = State.Stopped
  }


  def expectingKill: Receive = {

    case RestartConnector => {
      //TODO is it recommended do things after a system shutdown
      logger.info("Clean restart")

     //TODO move to connectorApp.stop()
     Try(Await.result( Future(connector.shutdown()), FiniteDuration(8, TimeUnit.SECONDS) )) match {
       case Success(_) => logger.info(s"Connector successfully stopped")
       case Failure(exc) => exc match{
         case _: TimeoutException => logger.warn("Cannot stop connector within the timeout. Anyway, the actor system is going to be restarted.")
         case otherException => logger.warn(s"Cannot stop connector ${exc.getMessage}. Anyway, the actor system is going to be restarted.")
       }
     }

      connectorApp.stop()
      connectorApp.startup(connector)
      connector.restart()

      context.stop(self) //self ! PoisonPill
    }

    case unexpectedMessage => {
      log.info("Unexpected message -> "+unexpectedMessage)
    }
  }

}
