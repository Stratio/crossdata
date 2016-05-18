package com.stratio.crossdata.driver.util

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, _}
import akka.stream.scaladsl.{FileIO, Source}
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.util.HttpClient.HttpClientContext
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.concurrent.duration._

object HttpClient {
  case class HttpClientContext(config: DriverConf, actorSystem: ActorSystem)

  def apply(implicit ctx: HttpClientContext): HttpClient = new HttpClient(ctx)

  def apply(config: DriverConf, actorSystem: ActorSystem): HttpClient =
    new HttpClient(HttpClientContext(config, actorSystem))
}

class HttpClient(ctx: HttpClientContext) {

  private implicit val actorSystem = ctx.actorSystem
  private val config = ctx.config
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val http = Http(actorSystem)

  def sendJarToHTTPServer(path: String): Future[String] = {
    val host = config.getCrossdataServerHost.split(':').head
    for(
      request <- createRequest(s"http://$host:13422/upload", new File(path));
      response <- http.singleRequest(request) map {
        case res @ HttpResponse(code, _, _, _) if(code != StatusCodes.OK) =>
          throw new RuntimeException(s"Request failed, response code: $code")
        case other => other
      };
      strictEntity <- response.entity.toStrict(5 seconds)
    ) yield strictEntity.data.decodeString("UTF-8")
  }

  private def createEntity(file: File): Future[RequestEntity] = {
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

  private def createRequest(target: Uri, file: File): Future[HttpRequest] =
    for {
      e ← createEntity(file)
    } yield HttpRequest(HttpMethods.POST, uri = target, entity = e)

}
