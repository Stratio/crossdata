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

import akka.util.Timeout
import com.codahale.metrics._
import akka.actor._
import akka.pattern.ask
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.metadata.{UpdatableMetadata, CatalogMetadata, TableMetadata}
import com.stratio.crossdata.common.utils.{Metrics, StringUtils}
import com.stratio.crossdata.connectors.config.ConnectConfig
import com.stratio.crossdata.common.connector._
import org.apache.log4j.Logger
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success, Try}
import com.stratio.crossdata.communication.GetTableMetadata
import com.stratio.crossdata.communication.Shutdown



class ConnectorApp extends ConnectConfig with IConnectorApp {

  var system: Option[ActorSystem] = null
  override lazy val logger = Logger.getLogger(classOf[ConnectorApp])
  var connectorActor: Option[ActorRef] = None
  implicit val timeout = Timeout(FiniteDuration(10,TimeUnit.SECONDS))

  def startup(connector: IConnector): Option[ActorRef] = {

    system = Some(ActorSystem(clusterName, config))

    logger.info("Connector Name: " + connectorName)

    if((connectorName.isEmpty) || (connectorName.equalsIgnoreCase("XconnectorX"))){
      logger.error("##########################################################################################")
      logger.error("# ERROR ##################################################################################")
      logger.error("##########################################################################################")
      logger.error("# USING DEFAULT CONNECTOR NAME: XconnectorX                                              #")
      logger.error("# CHANGE PARAMETER crossdata-connector.config.connector.name FROM THE CONFIGURATION FILE #")
      logger.error("##########################################################################################")
    }

    connectorActor = system.map( _.actorOf(Props(classOf[ConnectorActor], this, connector), "ConnectorActor"))
    connectorActor
  }

  def stop():Unit = {
    logger.info(s"Stopping the connector app")

    system.foreach(_.shutdown())
  }


  override def getTableMetadata(clusterName: ClusterName, tableName: TableName, timeout: Int): Option[TableMetadata] = {
    /*TODO: for querying actor internal state, only messages should be used.
      i.e.{{{
        import scala.concurrent.duration._
        val timeout: akka.util.Timeout = 2.seconds
        val response: Option[TableMetadata] = 
          actorClusterNode.map(actor => Await.result((actor ? GetTableMetadata).mapTo[TableMetadata],timeout))
        response.getOrElse(throw new IllegalStateException("Actor cluster node is not initialized"))
      }}}
    */
    implicit val timeout: Timeout = new Timeout(FiniteDuration.apply(2, TimeUnit.SECONDS))
    val future = connectorActor.get ? GetTableMetadata(clusterName,tableName)

    Try(Await.result(future.mapTo[TableMetadata],FiniteDuration.apply(2, TimeUnit.SECONDS))).map{ Some (_)}.recover{
      case e: Exception => logger.debug("Error fetching the catalog metadata from the ObservableMap: "+e.getMessage); None
    }.get

  }

  /*
  TODO Review 0.4.0
  override def getCatalogMetadata(catalogName: CatalogName, timeout: Int): Option[CatalogMetadata] ={
    val future = actorClusterNode.get.?(GetCatalogMetadata(catalogName))(timeout)
    Try(Await.result(future.mapTo[CatalogMetadata],Duration.fromNanos(timeout*1000000L))).map{ Some (_)}.recover{
      case e: Exception => logger.debug("Error fetching the catalog metadata from the ObservableMap: "+e.getMessage); None
    }.get

  }
 */

  /*
  /**
   * Recover the list of catalogs associated to the specified cluster.
   * @param cluster the cluster name.
   * @param timeout the timeout in ms.
   * @return The list of catalog metadata or null if the list is not ready after waiting the specified time.
   */
  @Experimental
  override def getCatalogs(cluster: ClusterName,timeout: Int = 10000): Option[util.List[CatalogMetadata]] ={
    val future = actorClusterNode.get.ask(GetCatalogs(cluster))(timeout)
    Try(Await.result(future.mapTo[util.List[CatalogMetadata]],Duration.fromNanos(timeout*1000000L))).map{ Some (_)}.recover {
      case e: Exception => logger.debug("Error fetching the catalogs from the ObservableMap: "+e.getMessage); None
    }.get
  }
 */


  override def getConnectionStatus(): ConnectionStatus = {
  var status: ConnectionStatus = ConnectionStatus.CONNECTED
    /*connectorActor.foreach(_ ! getStatus)

    if (connectedServersAgent.get.isEmpty){
      status = ConnectionStatus.DISCONNECTED
    }
    status
  */status
  }

  override def subscribeToMetadataUpdate(mapListener: IMetadataListener) ={
    connectorActor.foreach(_ ! mapListener)
  }

  override def registerMetric(name: String, metric: Metric): Metric = {
    Metrics.getRegistry.register(name, metric)
  }
}
