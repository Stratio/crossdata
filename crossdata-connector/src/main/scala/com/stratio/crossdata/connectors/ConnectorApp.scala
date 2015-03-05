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
import akka.pattern.ask
import akka.routing.RoundRobinRouter
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata}
import com.stratio.crossdata.common.utils.{Metrics, StringUtils}
import com.stratio.crossdata.connectors.config.ConnectConfig
import com.stratio.crossdata.common.connector.{IMetadataListener, IConnectorApp, IConfiguration, IConnector}
import org.apache.log4j.Logger
import scala.collection.mutable.Set
import com.codahale.metrics.{Gauge, MetricRegistry}
import scala.Some
import com.stratio.crossdata.communication.Shutdown
import scala.concurrent.Await
import akka.util.Timeout
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

object ConnectorApp extends App {
  args.length==2
}

class ConnectorApp extends ConnectConfig with IConnectorApp {

  lazy val defaultTimeout: Timeout = new Timeout(Duration(5, TimeUnit.SECONDS))
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
    val actorSelection: ActorSelection = system.actorSelection(
      StringUtils.getAkkaActorRefUri(actorClusterNode.get.toString(), false))

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
    /*TODO: for querying actor internal state, only messages should be used.
      i.e.{{{
        import scala.concurrent.duration._
        val timeout: akka.util.Timeout = 2.seconds
        val response: Option[TableMetadata] = 
          actorClusterNode.map(actor => Await.result((actor ? GetTableMetadata).mapTo[TableMetadata],timeout))
        response.getOrElse(throw new IllegalStateException("Actor cluster node is not initialized"))
      }}}
    */
    //actorClusterNode.get.asInstanceOf[ConnectorActor].getTableMetadata(clusterName, tableName)
    val future = actorClusterNode.get.ask(clusterName, tableName)(defaultTimeout)
    val result = Await.result(future, defaultTimeout.duration)
    result.asInstanceOf[TableMetadata]
  }

  override def getCatalogMetadata(catalogName: CatalogName): CatalogMetadata ={
    //actorClusterNode.get.asInstanceOf[ConnectorActor].getCatalogMetadata(catalogName)
    val future = actorClusterNode.get.ask(catalogName)(defaultTimeout)
    val result = Await.result(future, defaultTimeout.duration)
    result.asInstanceOf[CatalogMetadata]
  }

  override def getCatalogs(cluster: ClusterName): util.List[CatalogMetadata] ={
    //actorClusterNode.get.asInstanceOf[ConnectorActor].getCatalogs(cluster);
    val future = actorClusterNode.get.ask(cluster)(defaultTimeout)
    val result = Await.result(future, defaultTimeout.duration)
    result.asInstanceOf[util.List[CatalogMetadata]]
  }

  override def getConnectionStatus(): ConnectionStatus = {
    var status: ConnectionStatus = ConnectionStatus.CONNECTED
    if (connectedServers.isEmpty){
      status = ConnectionStatus.DISCONNECTED
    }
    status
  }

  override def subscribeToMetadataUpdate(mapListener: IMetadataListener) ={
    //actorClusterNode.get.asInstanceOf[ConnectorActor].subscribeToMetadataUpdate(mapListener)
    actorClusterNode.get ! mapListener
  }

}
