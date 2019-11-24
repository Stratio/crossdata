/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
