package com.stratio.crossdata.driver.session

import java.util.UUID

import akka.actor.ActorRef
import com.stratio.crossdata.common.security.Session

object SessionManager {

  def createSession(auth: Authentication, clientRef: ActorRef): Session =
    Session(newUUID, Option(clientRef))

  def createSession(auth: Authentication): Session =
    Session(newUUID, None)

  private def newUUID = UUID.randomUUID()


}

private[crossdata] case class Authentication(user: String, password: Option[String] = None)
