/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.actor

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.{HttpRequest, RequestEntity}
import akka.stream.ActorMaterializer
import com.stratio.crossdata.common.util.akka.keepalive.LiveMan
import com.stratio.crossdata.common.util.akka.keepalive.LiveMan.HeartBeat

import scala.concurrent.duration.FiniteDuration

object HttpSessionBeaconActor {

  def props(
             sessionId: UUID,
             period: FiniteDuration,
             url: String)(
    implicit httpClient: HttpExt, m: Marshaller[HeartBeat[UUID], RequestEntity]): Props =
    Props(new HttpSessionBeaconActor(sessionId, period, url, httpClient))

  class RequesterActor(url: String, httpClient: HttpExt)(
    implicit m: Marshaller[HeartBeat[UUID], RequestEntity]
  ) extends Actor {

    import context.dispatcher
    implicit val _: ActorMaterializer = ActorMaterializer()

    override def receive: Receive = {
      case tick: HeartBeat[UUID @unchecked] =>
        Marshal(tick).to[RequestEntity] flatMap { requestEntity =>
          httpClient.singleRequest(HttpRequest(POST, url, entity = requestEntity))
        }
    }

  }

}

class HttpSessionBeaconActor private(
                                      override val keepAliveId: UUID,
                                      override val period: FiniteDuration,
                                      url: String,
                                      client: HttpExt)(implicit
  m: Marshaller[HeartBeat[UUID], RequestEntity]
) extends Actor with LiveMan[UUID] {

  import HttpSessionBeaconActor.RequesterActor

  override def receive: Receive = PartialFunction.empty

  override val master: ActorRef = context.actorOf(Props(new RequesterActor(url, client)))

}
