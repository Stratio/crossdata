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

import akka.actor.{Actor, ActorRef, Props}
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import com.stratio.crossdata.common.{AddJARCommand, Command, SQLCommand}
import com.stratio.crossdata.server.actors.JobActor.Commands.{CancelJob, GetJobStatus}
import com.stratio.crossdata.server.actors.JobActor.Events.{JobCompleted, JobFailed}
import com.stratio.crossdata.server.actors.JobActor.Task
import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.reflect.io.File
import scala.util.{Failure, Success}

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

    case object JobCompleted extends JobEvent

    case class JobFailed(err: Throwable) extends JobEvent

  }

  object Commands {

    trait JobCommand

    case object GetJobStatus

    case object CancelJob

  }

  case class Task(command: Command, requester: ActorRef, timeout: Option[FiniteDuration])

  def props(xDContext: XDContext, SqlCommand: SQLCommand, requester: ActorRef, timeout: Option[FiniteDuration]): Props =
    Props(new JobActor(xDContext, Task(SqlCommand, requester, timeout)))

  def props(xDContext: XDContext, addJarCommand: AddJARCommand, requester: ActorRef, timeout: Option[FiniteDuration]): Props =
    Props(new JobActor(xDContext, Task(addJarCommand, requester, timeout)))

}

class JobActor(
                val xdContext: XDContext,
                val task: Task
              ) extends Actor {

  import JobActor.InternalEvents._
  import JobActor.JobStatus._
  import task._

  override def preStart(): Unit = {
    super.preStart()

    //import context.dispatcher
    import scala.concurrent.ExecutionContext.Implicits.global

    Future {
      command match {
        case sqlCommand: SQLCommand => executeSQLCommand(sqlCommand)
        case addJarCommand: AddJARCommand =>
      }
    } onComplete {
      case Failure(e) =>
        self ! JobFailed(e)
      case Success(_) =>
        self ! JobCompleted
    }

    timeout.foreach(context.system.scheduler.scheduleOnce(_, self, CancelJob))

  }

  private def executeSQLCommand(command: SQLCommand) = {
    xdContext.sparkContext.setJobGroup(command.commandId.toString, command.query, true)
    val df = xdContext.sql(command.query)
    self ! JobStarted()
    val rows = if (command.retrieveColumnNames)
      df.asInstanceOf[XDDataFrame].flattenedCollect() //TODO: Replace this cast by an implicit conversion
    else df.collect()
    requester ! SuccessfulQueryResult(command.commandId, rows, df.schema)
  }

  private def executeAddJar(command: AddJARCommand) = {
    xdContext.sparkContext.setJobGroup(command.commandId.toString, command.path, true)
    //Check the path to ensure that the file id an HDFS file or Local parth
    if ((command.path.toLowerCase.startsWith("hdfs://")) || (File(command.path).exists)) {
      xdContext.addJar(command.path)
      self ! JobStarted()
      requester ! SuccessfulQueryResult(command.commandId, Array.empty, null)
    } else {
      requester ! ErrorResult(command.commandId, "File doesn't exists or is not a hdfs file")
    }
  }

  override def receive: Receive = receive(Starting)

  private def receive(status: JobStatus): Receive = {
    // Commands
    case GetJobStatus =>
      sender ! status
    case JobStarted() if status == Starting =>
      context.become(receive(Running))
    case CancelJob => xdContext.sparkContext.cancelJobGroup(command.commandId.toString)

    // Events
    /* TODO: Jobs cancellations will be treated as errors.
        I'd be nice (and it'll be done) to discriminate among errors and cancellations
     */
    case event@JobFailed(e) if sender == self && (Seq(Starting, Running) contains status) =>
      context.become(receive(Failed))
      context.parent ! event
      requester ! ErrorResult(command.commandId, e.getMessage, Some(new Exception(e.getMessage)))
      throw e //Let It Crash: It'll be managed by its supervisor
    case JobCompleted if sender == self && status == Running =>
      context.become(receive(Completed))
      context.parent ! JobCompleted
  }


}
