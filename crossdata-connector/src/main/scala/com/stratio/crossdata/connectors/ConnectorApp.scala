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

import akka.actor.{ActorSelection, ActorRef, ActorSystem}
import akka.routing.RoundRobinRouter
import com.stratio.crossdata.common.data.{ConnectionStatus, TableName, CatalogName}
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata}
import com.stratio.crossdata.common.utils.StringUtils
import com.stratio.crossdata.connectors.config.ConnectConfig
import com.stratio.crossdata.common.connector.{IConnectorApp, IConfiguration, IConnector}
import com.stratio.crossdata.communication.Shutdown
import org.apache.log4j.Logger

object ConnectorApp extends App {
  args.length==2
}

class ConnectorApp extends ConnectConfig with IConnectorApp {

  type OptionMap = Map[Symbol, String]
  lazy val system = ActorSystem(clusterName, config)
  override lazy val logger = Logger.getLogger(classOf[ConnectorApp])

  var actorClusterNode: Option[ActorRef] = None

  def shutdown() :Unit= {
  }

  def stop():Unit = {
    actorClusterNode.get ! Shutdown()
    system.shutdown()
  }

  def startup(connector: IConnector): ActorSelection= {
    actorClusterNode = Some(system.actorOf(ConnectorActor.props(connector.getConnectorName,
      connector).withRouter(RoundRobinRouter(nrOfInstances = num_connector_actor)), "ConnectorActor"))
    connector.init(new IConfiguration {})
    system.actorSelection(StringUtils.getAkkaActorRefUri(actorClusterNode.get.toString()))
  }

  override def getTableMetadata(tablename: TableName): TableMetadata = {
    actorClusterNode.asInstanceOf[IConnectorApp].getTableMetadata(tablename)
  }

  override def getCatalogMetadata(catalogname: CatalogName): CatalogMetadata= {
    actorClusterNode.asInstanceOf[IConnectorApp].getCatalogMetadata(catalogname)
  }

  override def getConnectionStatus(): ConnectionStatus = {
    actorClusterNode.asInstanceOf[IConnectorApp].getConnectionStatus
  }

}
