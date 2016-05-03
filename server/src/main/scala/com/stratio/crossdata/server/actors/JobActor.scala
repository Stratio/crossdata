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

import java.util.concurrent.{CancellationException, Executor}

import akka.actor.{Actor, ActorRef, Props}
import com.stratio.common.utils.concurrent.Cancellable
import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.{SQLCommand, SQLReply}
import com.stratio.crossdata.server.actors.JobActor.Commands.{CancelJob, GetJobStatus, StartJob}
import com.stratio.crossdata.server.actors.JobActor.Events.{JobCompleted, JobFailed}
import com.stratio.crossdata.server.actors.JobActor.{ProlificExecutor, Task}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


object JobActor {

  trait JobStatus
  object JobStatus {
    case object Idle extends JobStatus
    case object Running  extends JobStatus
    case object Completed extends JobStatus
    case object Cancelled extends JobStatus
    case class  Failed(reason: Throwable) extends JobStatus
  }

  trait JobEvent

  object Events {

    case object JobCompleted extends JobEvent

    case class JobFailed(err: Throwable) extends JobEvent

  }

  object Commands {

    trait JobCommand

    case object GetJobStatus

    case object CancelJob

    case object StartJob
  }

  case class Task(command: SQLCommand, requester: ActorRef, timeout: Option[FiniteDuration])


  /**
    * The [[JobActor]] state is directly given by the running task which can be: None (Idle st) or a Running, Completed,
    * Cancelled or Failed task.
    * @param runningTask [[Cancellable]] wrapping a [[scala.concurrent.Future]] which acts as a Spark driver.
    */
  case class State(runningTask: Option[Cancellable[SQLReply]]) {
    import JobStatus._
    def getStatus: JobStatus = runningTask map { task =>
      task.future.value map {
        case Success(_) => Completed
        case Failure(_: CancellationException) => Cancelled
        case Failure(err) => Failed(err)
      } getOrElse Running
    } getOrElse Idle
  }

  def props(xDContext: XDContext, command: SQLCommand, requester: ActorRef, timeout: Option[FiniteDuration]): Props =
    Props(new JobActor(xDContext, Task(command, requester, timeout)))

  /**
    * Executor class which runs each command in a brand new thread each time
    */
  class ProlificExecutor extends Executor { override def execute(command: Runnable): Unit = new Thread(command) start }

}

class JobActor(
                val xdContext: XDContext,
                val task: Task
              ) extends Actor {


  import JobActor.JobStatus._
  import JobActor.State

  import task._

  lazy val logger = Logger.getLogger(classOf[ServerActor])

  override def receive: Receive = receive(State(None))



  private def receive(st: State): Receive = {

    // Commands
    case StartJob if st.getStatus == Idle =>

      logger.debug(s"Starting Job under ${context.parent.path}")

      import context.dispatcher

      val runningTask = launchTask
      runningTask.future onComplete {
        case Success(queryRes) =>
          requester ! queryRes
          self ! JobCompleted
        case Failure(_: CancellationException) => self ! JobCompleted // Job cancellation
        case Failure(reason) => self ! JobFailed(reason) // Job failure
      }


      val isRunning = runningTask.future.value.isEmpty

      timeout.filter(_ => isRunning).foreach {
        context.system.scheduler.scheduleOnce(_, self, CancelJob)
      }

      context.become(receive(st.copy(runningTask = Some(runningTask))))

    case CancelJob =>
      st.runningTask.foreach{ tsk =>
        logger.debug(s"Cancelling ${self.path}'s task ")
        tsk.cancel()
      }

    case GetJobStatus =>
      sender ! st.getStatus

    // Events

    case event @ JobFailed(e) if sender == self =>
      logger.debug(s"Task failed at ${self.path}")
      context.parent ! event
      requester ! SQLReply(command.requestId, ErrorSQLResult(e.getMessage, Some(new Exception(e.getMessage))))
      throw e //Let It Crash: It'll be managed by its supervisor
    case JobCompleted if sender == self =>
      logger.debug(s"Completed or cancelled ${self.path} task")
      context.parent ! JobCompleted
  }

  private def launchTask: Cancellable[SQLReply] = {

    implicit val _: ExecutionContext = ExecutionContext.fromExecutor(new ProlificExecutor)

    Cancellable {
      val df = xdContext.sql(command.sql)
      val rows = if (command.flattenResults)
        df.asInstanceOf[XDDataFrame].flattenedCollect() //TODO: Replace this cast by an implicit conversion
      else df.collect()


      SQLReply(command.queryId, SuccessfulSQLResult(rows, df.schema))
    }
  }

}
