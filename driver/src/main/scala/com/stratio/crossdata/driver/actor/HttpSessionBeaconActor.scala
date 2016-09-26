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
