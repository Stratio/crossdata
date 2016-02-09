package com.stratio.crossdata.server.actors

import akka.actor.{ActorRef, Actor}
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import com.stratio.crossdata.server.actors.JobActor.Commands.GetJobStatus
import com.stratio.crossdata.server.actors.JobActor.Events.{JobCompleted}
import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}

import scala.concurrent.Future
import scala.util.{Success, Failure}

object JobActor {

  object JobStatus extends Enumeration {
    type JobStatus = Value
    val Starting, Running, Failed, Completed = Value
  }

  private trait JobEvent


  private object InternalEvents {
    case class JobStarted() extends JobEvent
    case class JobFailed(err: Throwable) extends JobEvent
  }

  object Events {
    case class JobCompleted() extends JobEvent
  }

  object Commands {
    trait JobCommand
    case class GetJobStatus()
  }

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
      case Failure(e @ Throwable) =>
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
    case JobFailed(e) if sender == self && (Seq(Starting, Running) contains status) =>
      context.become(receive(Failed))
      requester ! ErrorResult(command.queryId, e.getMessage, Some(new Exception(e.getMessage)))
      throw e //Let It Crash: It'll be managed by its supervisor
    case event: JobCompleted if sender == self && status == Running =>
      context.become(receive(Completed))
      context.parent ! JobCompleted
  }



}
