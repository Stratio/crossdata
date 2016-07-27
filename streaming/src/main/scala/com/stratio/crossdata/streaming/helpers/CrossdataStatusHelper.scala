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
package com.stratio.crossdata.streaming.helpers

import akka.actor.{PoisonPill, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.streaming.actors.EphemeralQueryActor.{AddListener, EphemeralQueriesResponse, GetQueries}
import com.stratio.crossdata.streaming.actors.EphemeralStatusActor.SetStatus
import com.stratio.crossdata.streaming.actors.{EphemeralQueryActor, EphemeralStatusActor}
import com.stratio.crossdata.streaming.constants.AkkaConstants._
import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import org.apache.spark.sql.crossdata.models.{EphemeralExecutionStatus, EphemeralQueryModel}
import org.apache.spark.streaming.StreamingContext

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object CrossdataStatusHelper extends SparkLoggerComponent {

  implicit val actorSystem = ActorSystem(ParentPrefixName)
  implicit val timeout: Timeout = Timeout(2.seconds)

  private var ephemeralQueryActor: Option[ActorRef] = None
  private var ephemeralStatusActor: Option[ActorRef] = None

  def initStatusActor(streamingContext: StreamingContext,
                      zookeeperConfiguration: Map[String, String],
                      ephemeralTableName: String): Option[ActorRef] = {
    if (ephemeralStatusActor.isEmpty) {
      Try(
          actorSystem.actorOf(
              Props(
                  new EphemeralStatusActor(streamingContext,
                                           zookeeperConfiguration,
                                           ephemeralTableName)),
              EphemeralStatusActorName
          )
      ) match {
        case Success(actorRef) =>
          ephemeralStatusActor = Option(actorRef)
          actorRef ! AddListener
        case Failure(e) =>
          logger
            .error("Error creating streaming status actor with listener: ", e)
      }
    }
    ephemeralStatusActor
  }

  def queriesFromEphemeralTable(
      zookeeperConfiguration: Map[String, String],
      ephemeralTableName: String): Seq[EphemeralQueryModel] = {

    createEphemeralQueryActor(zookeeperConfiguration)

    ephemeralQueryActor.fold(Seq.empty[EphemeralQueryModel]) { queryActorRef =>
      val futureResult = queryActorRef ? GetQueries
      Await.result(futureResult, timeout.duration) match {
        case EphemeralQueriesResponse(queries) =>
          queries.filter(streamingQueryModel =>
                streamingQueryModel.ephemeralTableName == ephemeralTableName)
        case _ =>
          Seq.empty
      }
    }
  }

  def setEphemeralStatus(status: EphemeralExecutionStatus.Value,
                         zookeeperConfiguration: Map[String, String],
                         ephemeralTableName: String): Unit = {

    ephemeralStatusActor.foreach { statusActorRef =>
      statusActorRef ! SetStatus(status)
    }
  }

  def close(): Unit = {
    ephemeralQueryActor.foreach(_ ! PoisonPill)
    ephemeralStatusActor.foreach(_ ! PoisonPill)
    if (!actorSystem.isTerminated) {
      actorSystem.shutdown()
      actorSystem.awaitTermination(5 seconds)
    }
  }

  private[streaming] def createEphemeralQueryActor(
      zookeeperConfiguration: Map[String, String]): Option[ActorRef] = {
    synchronized {
      if (ephemeralQueryActor.isEmpty) {
        Try(
            actorSystem.actorOf(
                Props(new EphemeralQueryActor(zookeeperConfiguration)),
                EphemeralQueryActorName)
        ) match {
          case Success(actorRef) =>
            ephemeralQueryActor = Option(actorRef)
            actorRef ! AddListener
          case Failure(e) =>
            logger.error("Error creating streaming actor with listener: ", e)
        }
      }
      ephemeralQueryActor
    }
  }

}
