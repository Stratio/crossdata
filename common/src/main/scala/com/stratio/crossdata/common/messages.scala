/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.common

import java.util.UUID

import akka.cluster.ClusterEvent.CurrentClusterState
import com.stratio.crossdata.common.result.SQLResult
import com.stratio.crossdata.common.security.Session
import com.typesafe.config.Config

import scala.collection._
import scala.concurrent.duration.FiniteDuration

// Driver -> Server messages
private[crossdata] trait Command {
  private[crossdata] val requestId = UUID.randomUUID()
}

private[crossdata] case class SQLCommand private(sql: String,
                                                 queryId: UUID = UUID.randomUUID(),
                                                 flattenResults: Boolean = false,
                                                 timeout: Option[FiniteDuration] = None
                                                ) extends Command {

  def this(query: String,
           retrieveColNames: Boolean,
           timeoutDuration: FiniteDuration
          ) = this(sql = query, flattenResults = retrieveColNames, timeout = Option(timeoutDuration))

  def this(query: String,
           retrieveColNames: Boolean
          ) = this(sql = query, flattenResults = retrieveColNames, timeout = None)

}


case class AddJARCommand(path: String, hdfsConfig: Option[Config] = None,
                         timeout: Option[FiniteDuration] = None, toClassPath:Option[Boolean]= None
                        ) extends Command {
  def this(
            jarpath: String,
            timeout: FiniteDuration
          ) = this(path = jarpath, timeout = Option(timeout))


  def this(jarpath: String) = this(path = jarpath)

  def this(
            jarpath: String,
            hdfsConf: Config
          ) = this(path = jarpath, hdfsConfig = Option(hdfsConf))

  def this (jarpath: String,
            toClassPath: Boolean
           ) = this(path = jarpath, toClassPath = Option(toClassPath))
}

case class AddAppCommand(path: String,alias:String,clss:String,
                         timeout: Option[FiniteDuration] = None
                        ) extends Command {
  def this(
            jarpath: String,
            alias:String,
            clss:String,
            timeout: FiniteDuration
          ) = this(path = jarpath, alias,clss, timeout = Option(timeout))

  def this(
            jarpath: String,
            alias:String,
            clss:String
          )= this(jarpath, alias,clss,None)


}
case class ClusterStateCommand() extends Command

case class OpenSessionCommand() extends Command

case class CloseSessionCommand() extends Command

trait ControlCommand extends Command

private[crossdata] case class GetJobStatus() extends ControlCommand

private[crossdata] case class CancelQueryExecution(queryId: UUID) extends ControlCommand

/*
  Note that this message implies that the server trust the client in regard to the relation between the session id
   and the user. This assumption will be taken for granted until the model of session management changes from
   trusted-client management to server management.
 */
private[crossdata] case class CommandEnvelope(cmd: Command, session: Session, user: String)

// Server -> Driver messages
private[crossdata] trait ServerReply {
  def requestId: UUID
}

private[crossdata] case class QueryCancelledReply(requestId: UUID) extends ServerReply

private[crossdata] case class SQLReply(requestId: UUID, sqlResult: SQLResult) extends ServerReply

private[crossdata] case class ClusterStateReply(
                                                 requestId: UUID,
                                                 clusterState: CurrentClusterState,
                                                 sessionCluster: Set[String] = Set.empty[String]) extends ServerReply

private[crossdata] case class OpenSessionReply(requestId: UUID, isOpen: Boolean) extends ServerReply

private[crossdata] case class AddHdfsFileReply(requestId: UUID, hdfsRoute: String) extends ServerReply

