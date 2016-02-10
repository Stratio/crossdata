/**
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
package com.stratio.crossdata.server.actors

import akka.actor.{Props, ActorRef, Actor}
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import com.stratio.crossdata.server.actors.JobActor.Commands.GetJobStatus
import com.stratio.crossdata.server.actors.JobActor.Events.{JobFailed, JobCompleted}
import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}

import scala.concurrent.Future
import scala.util.{Success, Failure}

object JobActor {

  object JobStatus extends Enumeration {
    type JobStatus = Value
    val Starting, Running, Failed, Completed = Value
  }

  trait JobEvent


  private object InternalEvents {
    case class JobStarted() extends JobEvent
  }

  object Events {
    case class JobCompleted() extends JobEvent
    case class JobFailed(err: Throwable) extends JobEvent
  }

  object Commands {
    trait JobCommand
    case class GetJobStatus()
  }

  def props(xDContext: XDContext, command: SQLCommand, requester: ActorRef): Props =
    Props(new JobActor(xDContext, command, requester))

}

class JobActor(
                val xdContext: XDContext,
                val command: SQLCommand,
                val requester: ActorRef
              ) extends Actor {

  import JobActor.JobStatus._
  import JobActor.InternalEvents._

  override def preStart(): Unit = {
    super.preStart()

    import context.dispatcher

    Future {
      val df = xdContext.sql(command.query)
      self ! JobStarted()
      val rows = if(command.retrieveColumnNames)
        df.asInstanceOf[XDDataFrame].flattenedCollect() //TODO: Replace this cast by an implicit conversion
      else df.collect()

      // Sends the result to the requester
      requester ! SuccessfulQueryResult(command.queryId, rows, df.schema)

      // Complete the job lifecycle
      self ! JobCompleted()

    } onComplete {
      case Failure(e) =>
        self ! JobFailed(e)
      case Success(_) =>
        self ! JobCompleted
    }
  }

  override def receive: Receive = receive(Starting)

  private def receive(status: JobStatus): Receive = {
    case GetJobStatus() =>
      sender ! status
    case JobStarted() if status == Starting =>
      context.become(receive(Running))
    case event @ JobFailed(e) if sender == self && (Seq(Starting, Running) contains status) =>
      context.become(receive(Failed))
      context.parent ! event
      requester ! ErrorResult(command.queryId, e.getMessage, Some(new Exception(e.getMessage)))
      throw e //Let It Crash: It'll be managed by its supervisor
    case event: JobCompleted if sender == self && status == Running =>
      context.become(receive(Completed))
      context.parent ! JobCompleted
  }



}
