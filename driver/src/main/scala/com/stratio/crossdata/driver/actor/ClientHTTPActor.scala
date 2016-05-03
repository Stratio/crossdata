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
package com.stratio.crossdata.driver.actor

import java.io.File
import java.util.UUID

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.stream.scaladsl.{FileIO, ImplicitMaterializer, Source}
import akka.util.ByteString
import com.stratio.crossdata.common.result.{SuccessfulSQLResult, SQLResponse}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.{SQLReply, AddJARCommand, CommandEnvelope}
import com.stratio.crossdata.driver.config.DriverConf
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ClientHTTPActor {
  def props(config:DriverConf):Props = Props(new ClientHTTPActor(config))
}

class ClientHTTPActor(config:DriverConf) extends Actor
with ImplicitMaterializer
with ActorLogging {
  import akka.pattern.pipe
  import context.dispatcher
  val http = Http(context.system)
  var originalClient:ActorRef=self
  var originalRequester=UUID.randomUUID()

  override def preStart() = {
  }

  def sendJarToHTTPServer(path:String): Unit ={
    val host=config.getCrossdataServerHost.split(':').head
    val r=createRequest(s"http://$host:13422/upload", new File(path))
    r onComplete {
      case Success(req) => http.singleRequest(req).pipeTo(self)
      case Failure(f) => log.error("File Error:" + f.getMessage)
    }
  }

  def createEntity(file: File): Future[RequestEntity] = {
    require(file.exists())
    val fileIO=FileIO.fromFile(file)
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


  def receive = {
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8")).foreach{ str =>

      }
      val res:Array[Row]=Array()
      originalClient ! SQLReply(originalRequester,SuccessfulSQLResult(Array.empty, new StructType()))
      log.info("Got response, body: " + entity.dataBytes.runFold(ByteString(""))(_ ++ _))
    case HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)

    case secureSQLCommand @ CommandEnvelope(addjarcommand: AddJARCommand, session:Session) =>
      originalClient=sender()
      originalRequester=addjarcommand.requestId
      sendJarToHTTPServer(secureSQLCommand.cmd.asInstanceOf[AddJARCommand].path)
  }

}


