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
package com.stratio.crossdata.streaming.actors

import akka.actor.{Actor, Cancellable}
import com.github.nscala_time.time.Imports._
import com.stratio.crossdata.streaming.actors.EphemeralStatusActor._
import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.EphemeralTableStatusMapDAO
import org.apache.spark.sql.crossdata.daos.impl.EphemeralTableMapDAO
import org.apache.spark.sql.crossdata.models.{EphemeralExecutionStatus, EphemeralOptionsModel, EphemeralStatusModel}
import org.apache.spark.streaming.{StreamingContext, StreamingContextState}

import scala.concurrent.duration._
import scala.util.Try

class EphemeralStatusActor(streamingContext: StreamingContext,
                           zookeeperConfiguration: Map[String, String],
                           ephemeralTableName: String) extends Actor
with EphemeralTableStatusMapDAO {

  val ephemeralTMDao = new EphemeralTableMapDAO(Map(ZookeeperPrefixName -> zookeeperConfiguration))
  lazy val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)
  var ephemeralStatus: Option[EphemeralStatusModel] = getRepositoryStatusTable
  var cancellableCheckStatus: Option[Cancellable] = None

  def prefix:String = Try(memoryMap.get(ZookeeperPrefixName).get(PrefixStreamingCatalogsConfigForActors)+"_") getOrElse ("")


  override def receive: Receive = receive(listenerAdded = false)

  def receive(listenerAdded: Boolean): Receive = {
    case CheckStatus =>
      doCheckStatus()
    case GetStatus =>
      doGetStatus()
    case SetStatus(status) =>
      doSetStatus(status)
    case GetStreamingStatus =>
      doGetStreamingStatus()
    case AddListener if !listenerAdded =>
      doAddListener()
      context.become(receive(true))
  }

  override def preStart(): Unit = {
    super.preStart()

    val ephemeralTableModel = ephemeralTMDao.dao.get(ephemeralTableName)
    val delayMs = ephemeralTableModel match {
      case Some(tableModel) => tableModel.options.atomicWindow * 1000
      case None => EphemeralOptionsModel.DefaultAtomicWindow * 1000
    }
    cancellableCheckStatus = Option(cancellableCheckStatus.fold(
      context.system.scheduler.schedule(delayMs milliseconds, delayMs milliseconds, self, CheckStatus)(context.dispatcher)
    )(identity))
  }

  override def postStop(): Unit = {
    super.postStop()
    cancellableCheckStatus.foreach(_.cancel())
  }

  private[streaming] def doGetStatus(): Unit = {
    sender ! StatusResponse(getStatusFromTable(ephemeralStatus))
  }

  private[streaming] def doCheckStatus(): Unit =
  // TODO check if the status can be read from ephemeralStatus insteadof getRepository...; the listener should work
    getRepositoryStatusTable.foreach { statusModel =>
      if (statusModel.status == EphemeralExecutionStatus.Stopping) {
        // TODO add an actor containing status and query actor in order to exit gracefully
        synchronized {
          closeSparkContexts(stopGracefully = false)
          doSetStatus(EphemeralExecutionStatus.Stopped)
        }
      }
    }

  private[streaming] def doSetStatus(newStatus: EphemeralExecutionStatus.Value): Unit = {

    val startTime = if (newStatus == EphemeralExecutionStatus.Started) Option(DateTime.now.getMillis) else None
    val stopTime = if (newStatus == EphemeralExecutionStatus.Stopped) Option(DateTime.now.getMillis) else None
    val resultStatus = ephemeralStatus.fold(
      dao.create(ephemeralTableName, EphemeralStatusModel(ephemeralTableName, newStatus, startTime, stopTime))
    ) { ephStatus =>
      val newStatusModel = ephStatus.copy(
        status = newStatus,
        stoppedTime = stopTime,
        startedTime = startTime.orElse(ephStatus.startedTime)
      )
      dao.upsert(ephemeralTableName, newStatusModel)
    }

    ephemeralStatus = Option(resultStatus)

    sender ! StatusResponse(getStatusFromTable(ephemeralStatus))
  }

  private[streaming] def doAddListener(): Unit = {
    repository.addListener[EphemeralStatusModel](
      dao.entity,
      ephemeralTableName,
      (newEphemeralStatus: EphemeralStatusModel, _) => ephemeralStatus = Option(newEphemeralStatus)
    )

    sender ! ListenerResponse(true)
  }

  private[streaming] def getRepositoryStatusTable: Option[EphemeralStatusModel] = dao.get(ephemeralTableName)

  private[streaming] def getStatusFromTable(ephemeralTable: Option[EphemeralStatusModel])
  : EphemeralExecutionStatus.Value = {
    ephemeralTable.fold(EphemeralExecutionStatus.NotStarted) { tableStatus =>
      tableStatus.status
    }
  }

  // TODO callStatusHelper close => It might be nice to have a common parent actor
  private[streaming] def closeSparkContexts(stopGracefully: Boolean): Unit = {
    synchronized {
      try {
        if (streamingContext.getState() != StreamingContextState.STOPPED)
          streamingContext.stop(stopSparkContext = false, stopGracefully)

      } finally {
        // TODO stopSparkContext=true; otherwise, the application will exit before.
        streamingContext.sparkContext.stop()
      }
    }
  }

  private[streaming] def doGetStreamingStatus(): Unit = sender ! StreamingStatusResponse(streamingContext.getState())
}

object EphemeralStatusActor {

  case object GetStatus

  case object GetStreamingStatus

  case object AddListener

  case object CheckStatus

  case class ListenerResponse(added: Boolean)

  case class SetStatus(status: EphemeralExecutionStatus.Value)

  case class StatusResponse(status: EphemeralExecutionStatus.Value)

  case class StreamingStatusResponse(status: StreamingContextState)

}