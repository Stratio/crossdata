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

import java.io.File
import java.util.UUID

import akka.actor.{PoisonPill, Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.pattern.pipe
import akka.stream.scaladsl.{FileIO, ImplicitMaterializer, Source}
import com.stratio.crossdata.common.result.SuccessfulSQLResult
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.{AddJARCommand, CommandEnvelope, SQLReply}
import com.stratio.crossdata.driver.config.DriverConf
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ClientHTTPActor {
  def props(config: DriverConf): Props = Props(new ClientHTTPActor(config))
}

class ClientHTTPActor(config: DriverConf) extends Actor
with ImplicitMaterializer
with ActorLogging {


  import context.dispatcher

  val http = Http(context.system)

  def sendJarToHTTPServer(path: String): Unit = {
    val host = config.getCrossdataServerHost.split(':').head
    val r = createRequest(s"http://$host:13422/upload", new File(path))
    r onComplete {
      // TODO pipeTo proxyActor??
      case Success(req) => http.singleRequest(req).pipeTo(self)
      case Failure(f) => log.error("File Error:" + f.getMessage)
    }
  }

  def createEntity(file: File): Future[RequestEntity] = {
    require(file.exists())
    val fileIO = FileIO.fromFile(file)
    val formData =
      Multipart.FormData(
        Source.single(
          Multipart.FormData.BodyPart(
            "fileChunk",
            HttpEntity(ContentTypes.`application/octet-stream`, file.length(), fileIO),
            Map("filename" -> file.getName))))
    Marshal(formData).to[RequestEntity]
  }

  def createRequest(target: Uri, file: File): Future[HttpRequest] =
    for {
      e ← createEntity(file)
    } yield HttpRequest(HttpMethods.POST, uri = target, entity = e)


  override def receive: Actor.Receive = initial

  def initial: Receive = {
    case secureSQLCommand@CommandEnvelope(addjarcommand: AddJARCommand, session: Session) =>
      sendJarToHTTPServer(secureSQLCommand.cmd.asInstanceOf[AddJARCommand].path)
      context.become(waitingResponse(sender(), addjarcommand.requestId))

    case other =>
      log.error(s"Unexpected message $other")
  }

  def waitingResponse(proxyActor: ActorRef, requester: UUID): Receive = {
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8")).recover {
        case errorDecoding =>
          log.error(errorDecoding, s"Error decoding response ${errorDecoding.getMessage}")
          s"Cannot get the response: cause ${errorDecoding.getMessage}"
      } onSuccess {
        case response =>
          proxyActor ! SQLReply(requester,SuccessfulSQLResult(Array(Row(response)), StructType(StructField("filepath", StringType) :: Nil)))
          log.info("Got response, body: " + response)
          // TODO pfperez (how to kill)
          self ! PoisonPill
      }

    case HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      // TODO pfperez (how to kill)
      self ! PoisonPill

  }


}