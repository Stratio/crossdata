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
package com.stratio.crossdata.driver.util

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, _}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Source}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.serializers.CrossdataCommonSerializer
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.util.HttpClient.HttpClientContext
import org.json4s.jackson

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object HttpClient {

  case class HttpClientContext(config: DriverConf, actorSystem: ActorSystem)

  def apply(implicit ctx: HttpClientContext): HttpClient = new HttpClient(ctx)

  def apply(config: DriverConf, actorSystem: ActorSystem): HttpClient =
    new HttpClient(HttpClientContext(config, actorSystem))
}

class HttpClient(ctx: HttpClientContext) extends CrossdataCommonSerializer{

  private implicit val actorSystem = ctx.actorSystem
  private val config = ctx.config
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  import akka.http.scaladsl.marshalling._
  implicit val serialization = jackson.Serialization

  private val http = Http(actorSystem)
  private val serverHttp = config.getCrossdataServerHttp
  private val protocol = "http"


  def sendJarToHTTPServer(path: String, session: Session): Future[String] = {
    val sessionUUID = session.id

    for (
      request <- createSendFileRequest(s"$protocol://$serverHttp/upload/$sessionUUID", new File(path));
      response <- http.singleRequest(request) map {
        case resp@HttpResponse(StatusCodes.OK, _, entity, _) => resp
        case HttpResponse(code, _, _, _) => throw new RuntimeException(s"Request failed, response code: $code")
      };
      strictEntity <- response.entity.toStrict(5 seconds)
    ) yield strictEntity.data.decodeString("UTF-8")
  }

  private def createJarEntity(file: File): Future[RequestEntity] = {
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

  private def createSendFileRequest(target: Uri, file: File): Future[HttpRequest] =
    for {
      e ← createJarEntity(file)
    } yield HttpRequest(HttpMethods.POST, uri = target, entity = e)

}
