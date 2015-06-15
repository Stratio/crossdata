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

package com.stratio.crossdata.driver.actor

import akka.actor._
import akka.contrib.pattern.ClusterClient
import akka.util.Timeout
import com.stratio.crossdata.common.ask.{Command, Connect, Query}
import com.stratio.crossdata.common.result.{ErrorResult, Result}
import com.stratio.crossdata.communication._
import com.stratio.crossdata.driver.BasicDriver
import org.apache.log4j.Logger

import scala.concurrent.duration._
import akka.actor.SupervisorStrategy.{Stop, Escalate, Restart, Resume}
import com.stratio.crossdata.common.ask.Connect
import akka.actor.OneForOneStrategy
import com.stratio.crossdata.common.ask.Command
import com.stratio.crossdata.common.ask.Query

/**
 * Companion object.
 */
object ProxyActor {
  /**
   * Initial path for actor's identify.
   */
  val INIT_PATH = "/user/"

  /**
   * Config prop in ProxyActor.
   * @param clusterClientActor ActorRef to ClusterClientActor pattern.
   * @param remoteActor Remote actor's name.
   * @return Actor's props.
   */
  def props(clusterClientActor: ActorRef, remoteActor: String, driver: BasicDriver): Props = Props(new ProxyActor(clusterClientActor,
    remoteActor, driver))

  /**
   * Create path with actor's name.
   * @param remoteActor Remote actor's name.
   * @return Complete path.
   */
  def remotePath(remoteActor: String) : String= INIT_PATH + remoteActor
}

/**
 * Actor to connect with receptionist actor in the remote cluster.
 * @param clusterClientActor ActorRef to ClusterClientActor pattern.
 * @param remoteActor Remote actor's name.
 */
class ProxyActor(clusterClientActor: ActorRef, remoteActor: String, driver: BasicDriver) extends Actor {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: StashOverflowException => Stop
      case _: Any => Resume
    }

  import context.dispatcher
  if(driver.balancing)context.system.scheduler.schedule(
    driver.cpuLoadPingTimeInMillis seconds,
    driver.cpuLoadPingTimeInMillis seconds,
    self, "watchload")

  /**
   * Class logger.
   */
  lazy val logger = Logger.getLogger(getClass)

  var CPUUsages = new scala.collection.immutable.TreeMap[Long,(String,Double)]()

  implicit val timeout = Timeout(5 seconds)
  val localAffinity=driver.localAffinity
  logger.info("Up!")

  def chooseServerNode():String={
    logger.debug(s"choosing cluster target node from ${CPUUsages} different options (current time=${
      System.currentTimeMillis() - 10000})")
    CPUUsages.size match {
     case 0=> ""
     case _=>
       val min = CPUUsages.minBy(_._2._2)
       val destNode = min._2._1
        s"akka.tcp://${driver.crossdataServerClusterName}@$destNode/user/${driver.serverPathName}"
    }
 }


  override def receive: Actor.Receive = {

    case "watchload"=>
      clusterClientActor forward ClusterClient.SendToAll(ProxyActor.remotePath(remoteActor), "watchload" )
      
    case CPUUsage(value)=>
      val senderip=s"${sender.path.parent.parent.address.host.get}:${sender.path.parent.parent.address.port.get}"
      CPUUsages = CPUUsages.dropWhile((System.currentTimeMillis() - (2*driver.cpuLoadPingTimeInMillis)) > _._1)+
        (System.currentTimeMillis() ->   (senderip,value))
      logger.debug(s"CPU Usages=${CPUUsages}")

    /* The driver sends the connect message. */
    case c: Connect => {
      clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor), c, localAffinity)
    }

    case c: Disconnect => {
      logger.debug("Send connect " + c)
      clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor), c, localAffinity)
    }

    /* API Command */
    case cmd: Command => {
      val dest=chooseServerNode()
      if(dest.length<1)
        clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor), cmd, localAffinity)
      else 
        context.actorSelection(dest) forward cmd
    }

    /* ACK received */
    case ack: ACK => {
      val handler = driver.getResultHandler(ack.queryId)
      if (handler != null) {
        handler.processAck(ack.queryId, ack.status)
      } else {
        logger.warn("ACK not expected received: " + ack)
      }
    }

    /* Send a query to the remote com.stratio.crossdata-server infrastructure. */
    case message: Query => {
      val dest=chooseServerNode()
      if(dest.length<1)
        clusterClientActor ! ClusterClient.Send(ProxyActor.remotePath(remoteActor), message, localAffinity)
      else
        context.actorSelection(dest) forward message
    }

    case result: Result => {
      logger.debug(s"\nReceiving result ${result}")
      val handler = driver.getResultHandler(result.getQueryId)
      if (handler != null) {
        if (!result.isInstanceOf[ErrorResult]) {
          handler.processResult(result)
        } else {
          handler.processError(result)
        }
      } else {
        logger.info("Result not expected received for QID: " + result.getQueryId)
      }

    }
    case "test"=> {
      logger.error("Unknown message: test" )
      sender ! "Message type not supported"
    }

    case InfoResult(ref:String, queryId:String) => {
      logger.info("Query:" + queryId +" sended to " + ref)
      driver.connectorOfQueries.put(queryId,ref)
    }
    case unknown: Any => {
      logger.error("Unknown message: " + unknown)
      sender ! "Message type not supported"
    }
  }

}


