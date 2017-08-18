package com.stratio.crossdata.server.actors


import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.server.actors.ServerActor.JobId
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.session.XDSessionProvider
import org.apache.spark.sql.types.StructType

import scala.util.{Failure, Success}

object ResourceManagerActor {
  val AddJarTopic: String = "newJAR"

  def props(cluster: Cluster, sessionProvider: XDSessionProvider): Props =
    Props(new ResourceManagerActor(cluster, sessionProvider))

  case class State(jobsById: Map[JobId, ActorRef])

}

class ResourceManagerActor(cluster: Cluster, sessionProvider: XDSessionProvider) extends Actor {

  import ResourceManagerActor._

  lazy val logger = Logger.getLogger(classOf[ServerActor])

  lazy val mediator = DistributedPubSub(context.system).mediator

  override def preStart(): Unit = {
    super.preStart()
    mediator ! Subscribe(AddJarTopic, self)
  }

  override def receive: Actor.Receive = initial(Set(AddJarTopic))

  private def initial(pendingTopics: Set[String]): Receive = {

    case SubscribeAck(Subscribe(AddJarTopic, None, self)) =>
      val newPendingTopics = pendingTopics - AddJarTopic
      checkSubscriptions(newPendingTopics)
  }

  private def checkSubscriptions(pendingTopics: Set[String]): Unit =
    if (pendingTopics.isEmpty)
      context.become(ready(State(Map.empty)))
    else
      context.become(initial(pendingTopics))

  // Function composition to build the finally applied receive-function
  private def ready(st: State): Receive =
    AddJarMessages(st)

  // Commands reception: Checks whether the command can be run at this Server passing it to the execution method if so
  def AddJarMessages(st: State): Receive = {
    case CommandEnvelope(addJarCommand: AddJARCommand, session@Session(id, requester)) =>
      logger.debug(s"Add JAR received ${addJarCommand.requestId}: ${addJarCommand.path}. Actor ${self.path.toStringWithoutAddress}")
      logger.debug(s"Session identifier $session")
      //TODO  Maybe include job controller if it is necessary as in sql command
      if (addJarCommand.path.toLowerCase.startsWith("hdfs://")) {
        sessionProvider.session(id) match {
          case Success(xdSession) =>
            xdSession.addJar(addJarCommand.path)
          case Failure(error) =>
            logger.warn(s"Received message with an unknown sessionId $id", error)
            sender ! ErrorSQLResult(s"Unable to recover the session ${session.id}. Cause: ${error.getMessage}")
        }

        // TODO addJar should not affect other sessions
        sender ! SQLReply(addJarCommand.requestId, SuccessfulSQLResult(Array.empty, new StructType()))
      } else {
        sender ! SQLReply(addJarCommand.requestId, ErrorSQLResult("File doesn't exist or is not a hdfs file", Some(new Exception("File doesn't exist or is not a hdfs file"))))
      }
    case _ =>
  }




}
