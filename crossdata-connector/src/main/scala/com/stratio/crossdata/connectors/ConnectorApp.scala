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

import akka.actor.{ActorSelection, ActorRef, ActorSystem}
import akka.routing.RoundRobinRouter
import com.stratio.crossdata.common.data.{ClusterName, ConnectionStatus, TableName, CatalogName}
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata}
import com.stratio.crossdata.common.utils.{Metrics, StringUtils}
import com.stratio.crossdata.connectors.config.ConnectConfig
import com.stratio.crossdata.common.connector.{IConnectorApp, IConfiguration, IConnector}
import com.stratio.crossdata.communication.Shutdown
import org.apache.log4j.Logger
import scala.collection.mutable.Set
import com.codahale.metrics.{Gauge, MetricRegistry}

object ConnectorApp extends App {
  args.length==2
}

class ConnectorApp extends ConnectConfig with IConnectorApp {

  type OptionMap = Map[Symbol, String]
  lazy val system = ActorSystem(clusterName, config)
  val connectedServers: Set[String] = Set()
  override lazy val logger = Logger.getLogger(classOf[ConnectorApp])

  var actorClusterNode: Option[ActorRef] = None

  var metricName: String = "connector"

  def stop():Unit = {
    actorClusterNode.get ! Shutdown()
    system.shutdown()
    Metrics.getRegistry.remove(metricName)
  }

  def startup(connector: IConnector): ActorSelection= {
    actorClusterNode = Some(system.actorOf(ConnectorActor.props(connector.getConnectorName,
      connector, connectedServers).withRouter(RoundRobinRouter(nrOfInstances = num_connector_actor)), "ConnectorActor"))
    connector.init(new IConfiguration {})
    val actorSelection = system.actorSelection(StringUtils.getAkkaActorRefUri(actorClusterNode.get.toString(), false))

    metricName = MetricRegistry.name(connector.getConnectorName, "connection", "status")
    Metrics.getRegistry.register(metricName,
      new Gauge[Boolean] {
        override def getValue: Boolean = {
          var status: Boolean = true
          if (connectedServers.isEmpty) {
            status = false
          }
          return status
        }
      })

    actorSelection
  }

  override def getTableMetadata(clusterName: ClusterName, tableName: TableName): TableMetadata = {
    actorClusterNode.asInstanceOf[ConnectorActor].getTableMetadata(clusterName, tableName)
  }

  override def getCatalogMetadata(catalogName: CatalogName): CatalogMetadata= {
    actorClusterNode.asInstanceOf[ConnectorActor].getCatalogMetadata(catalogName)
  }

  override def getCatalogs(cluster: ClusterName): util.List[CatalogMetadata] = {
    actorClusterNode.asInstanceOf[ConnectorActor].getCatalogs(cluster);
  }

  override def getConnectionStatus(): ConnectionStatus = {
    var status: ConnectionStatus = ConnectionStatus.CONNECTED
    if (connectedServers.isEmpty){
      status = ConnectionStatus.DISCONNECTED
    }
    status
  }

}
