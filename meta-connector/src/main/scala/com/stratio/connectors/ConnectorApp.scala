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

package com.stratio.connectors

import akka.actor.{ActorRef, ActorSystem}
import akka.routing.RoundRobinRouter
import com.stratio.connectors.config.ConnectConfig
import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.communication.Shutdown
import com.typesafe.config.ConfigFactory
import org.apache.log4j.{BasicConfigurator, Logger}

object ConnectorApp extends ConnectorApp {
  def main(args: Array[String]): Unit = {
    BasicConfigurator.configure()
    //if (args.length == 0) println(usage)
    val options = nextOption(Map(), args.toList)
    var connectortype: Option[String] = options.get(Symbol("connectortype"))
    var port: Option[String] = options.get(Symbol("port"))
    if (port == None) port = Some("2551")
    //val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory
    //  .parseString("akka.cluster.roles = [connector]")).withFallback(ConfigFactory.load())
    if (connectortype == None) connectortype = Some("cassandra")
    val c = getConnector(connectortype.get)
    startup(c, Seq(port.get), config)
  }
}

class ConnectorApp extends ConnectConfig {

  type OptionMap = Map[Symbol, String]
  println(">>>>>>>> TRACE: config = " + config.getValue("akka.remote.netty.tcp.port"))
  println(">>>>>>>> TRACE: roles = " + config.getValue("akka.cluster.roles"))
  lazy val system = ActorSystem(clusterName, config)
  override lazy val logger = Logger.getLogger(classOf[ConnectorApp])
  val usage = """Usage:
      connectorApp [--port <port number>] [--connector-type <connector type name>]
              """
  var actorClusterNode: ActorRef = null
  private var connector = null

  def getConnector(connectortype: String): IConnector = {
    connectortype match {
      //case "cassandra" => new CassandraConnector
      case _ => null //new CassandraConnector
    }
  }

  def nextOption(map: OptionMap, list: List[String]): OptionMap = {
    def isSwitch(s: String) = (s(0) == '-')
    list match {
      case Nil => map
      case "--port" :: value :: tail =>
        nextOption(map ++ Map('port -> value), tail)
      case "--connector-type" :: value :: tail =>
        nextOption(map ++ Map('connectortype -> value), tail)
      case option :: tail =>
        println("Unknown option " + option)
        println(usage)
        exit(1)
    }
  }

  def startup(connector: IConnector, port: String, config: com.typesafe.config.Config): ActorRef = {
    startup(connector, Array(port), config)
  }

  def shutdown() = {
  }

  def stop() = {
    actorClusterNode ! Shutdown()
    system.shutdown()
  }

  def startup(connector: IConnector): ActorRef = {
    actorClusterNode = system.actorOf(ConnectorActor.props(connector.getConnectorName, connector).withRouter(RoundRobinRouter(nrOfInstances = num_connector_actor)), "ConnectorActor")
    actorClusterNode ! "I'm in!!!"
    actorClusterNode
  }

  def startup(connector: IConnector, port: String): ActorRef = {
    startup(connector, Array(port), config)
  }

  def startup(connector: IConnector, ports: Array[String], config: com.typesafe.config.Config): ActorRef = {
    startup(connector, ports.toList, config)
  }

  def startup(connector: IConnector, ports: Seq[String], config: com.typesafe.config.Config): ActorRef = {
    ports foreach { port =>
//      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory
//        .parseString("akka.cluster.roles = [connector]")).withFallback(ConfigFactory.load())
      actorClusterNode = system.actorOf(ConnectorActor.props(connector.getConnectorName(), connector).withRouter(RoundRobinRouter(nrOfInstances = num_connector_actor)), "ConnectorActor")
      actorClusterNode ! "I'm in!!!"
    }
    actorClusterNode
  }

}
