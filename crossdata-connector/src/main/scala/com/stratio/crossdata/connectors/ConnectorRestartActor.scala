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

import akka.actor._
import akka.agent.Agent
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{ClusterDomainEvent, MemberEvent}
import akka.remote.QuarantinedEvent
import akka.util.Timeout
import com.stratio.crossdata
import com.stratio.crossdata
import com.stratio.crossdata.common.ask.APICommand
import com.stratio.crossdata.common.ask.APICommand
import com.stratio.crossdata.common.connector.IConnector
import com.stratio.crossdata.common.connector.IMetadataEngine
import com.stratio.crossdata.common.connector.IMetadataListener
import com.stratio.crossdata.common.connector.IResultHandler
import com.stratio.crossdata.common.connector.ObservableMap
import com.stratio.crossdata.common.connector._
import com.stratio.crossdata.common.data.CatalogName
import com.stratio.crossdata.common.data.ClusterName
import com.stratio.crossdata.common.data.Name
import com.stratio.crossdata.common.data.TableName
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.exceptions.ConnectionException
import com.stratio.crossdata.common.exceptions.ExecutionException
import com.stratio.crossdata.common.exceptions.{ConnectionException, ExecutionException}
import com.stratio.crossdata.common.metadata.CatalogMetadata
import com.stratio.crossdata.common.metadata.ClusterMetadata
import com.stratio.crossdata.common.metadata.ConnectorMetadata
import com.stratio.crossdata.common.metadata.DataStoreMetadata
import com.stratio.crossdata.common.metadata.TableMetadata
import com.stratio.crossdata.common.metadata.UpdatableMetadata
import com.stratio.crossdata.common.metadata.{CatalogMetadata, TableMetadata, _}
import com.stratio.crossdata.common.result.ConnectResult
import com.stratio.crossdata.common.result.DisconnectResult
import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.common.result.MetadataResult
import com.stratio.crossdata.common.result.QueryResult
import com.stratio.crossdata.common.result.QueryStatus
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.common.result.StorageResult
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.communication.AlterCatalog
import com.stratio.crossdata.communication.AlterTable
import com.stratio.crossdata.communication.AsyncExecute
import com.stratio.crossdata.communication.ConnectorUp
import com.stratio.crossdata.communication.CreateCatalog
import com.stratio.crossdata.communication.CreateIndex
import com.stratio.crossdata.communication.CreateTable
import com.stratio.crossdata.communication.CreateTableAndCatalog
import com.stratio.crossdata.communication.DeleteRows
import com.stratio.crossdata.communication.DropCatalog
import com.stratio.crossdata.communication.DropIndex
import com.stratio.crossdata.communication.DropTable
import com.stratio.crossdata.communication.Execute
import com.stratio.crossdata.communication.GetCatalogMetadata
import com.stratio.crossdata.communication.GetCatalogs
import com.stratio.crossdata.communication.GetTableMetadata
import com.stratio.crossdata.communication.Insert
import com.stratio.crossdata.communication.InsertBatch
import com.stratio.crossdata.communication.MetadataOperation
import com.stratio.crossdata.communication.PagedExecute
import com.stratio.crossdata.communication.ProvideCatalogMetadata
import com.stratio.crossdata.communication.ProvideCatalogsMetadata
import com.stratio.crossdata.communication.ProvideMetadata
import com.stratio.crossdata.communication.ProvideTableMetadata
import com.stratio.crossdata.communication.Request
import com.stratio.crossdata.communication.StopProcess
import com.stratio.crossdata.communication.StorageOperation
import com.stratio.crossdata.communication.Truncate
import com.stratio.crossdata.communication.Update
import com.stratio.crossdata.communication.UpdateMetadata
import com.stratio.crossdata.communication._
import com.stratio.crossdata.communication.getConnectorName
import com.stratio.crossdata.communication.replyConnectorName
import com.stratio.crossdata.connectors.ConnectorRestartActor.RestartConnector
import org.apache.log4j.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable.{ListMap, Set}
import scala.concurrent.duration.DurationInt
import com.stratio.crossdata.communication.CreateIndex
import com.stratio.crossdata.communication.Update
import akka.cluster.ClusterEvent.MemberRemoved
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.communication.CreateTableAndCatalog
import com.stratio.crossdata.communication.AlterTable
import com.stratio.crossdata.communication.Truncate
import com.stratio.crossdata.communication.CreateTable
import com.stratio.crossdata.communication.AlterCatalog
import com.stratio.crossdata.communication.InsertBatch
import com.stratio.crossdata.communication.AsyncExecute
import akka.cluster.ClusterEvent.UnreachableMember
import com.stratio.crossdata.communication.ProvideCatalogMetadata
import com.stratio.crossdata.communication.ProvideCatalogsMetadata
import com.stratio.crossdata.communication.CreateCatalog
import com.stratio.crossdata.communication.ProvideTableMetadata
import com.stratio.crossdata.communication.PagedExecute
import com.stratio.crossdata.communication.replyConnectorName
import com.stratio.crossdata.communication.Execute
import akka.cluster.ClusterEvent.MemberUp
import com.stratio.crossdata.communication.getConnectorName
import com.stratio.crossdata.communication.ProvideMetadata
import com.stratio.crossdata.communication.DropIndex
import com.stratio.crossdata.communication.UpdateMetadata
import akka.cluster.ClusterEvent.CurrentClusterState
import com.stratio.crossdata.communication.DeleteRows
import com.stratio.crossdata.communication.DropCatalog
import com.stratio.crossdata.communication.DropTable
import com.stratio.crossdata.communication.Insert
import scala.concurrent.ExecutionContext.Implicits.global

object ConnectorRestartActor {
  def props( connectorApp: ConnectorApp, connector: IConnector):
  Props = Props(new ConnectorRestartActor(connectorApp, connector))


  case class RestartConnector()
}

/**
 * Restart the actor only one time => It could store the metadata to restart the connector succesfully if needed
 */
class ConnectorRestartActor(connectorApp: ConnectorApp, connector: IConnector) extends Actor with ActorLogging {

  lazy val logger = Logger.getLogger(classOf[ConnectorActor])

  logger.info("Lifting connectorRestart actor")

  var cancelableRestart: Option[Cancellable] = None

  val timeout = 10 seconds

  def restart: Unit = {
    connectorApp.stop()
    connectorApp.startup(connector)
  }


  override def receive: Receive = initialState

  def initialState: Receive = {

    case RestartConnector => {
      context.become(expectingKill)
      cancelableRestart = Some(context.system.scheduler.scheduleOnce(timeout){
        restart
        context.stop(self)
      }(context.dispatcher))
    }

  }


  def expectingKill: Receive = {
    case msg => {
      cancelableRestart.foreach( _.cancel)
      restart
      context.stop(self)
  }
}

}
