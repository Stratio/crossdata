package com.stratio.crossdata.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity}
import akka.stream.ActorMaterializer
import com.stratio.crossdata.common.{CommandEnvelope, SQLCommand}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.UUID

import com.stratio.crossdata.common.security.Session
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.jackson

object HTTPClientPrototype extends App  with CrossdataSerializer {

  implicit val system = ActorSystem("ClientPrototype")
  implicit val materializer = ActorMaterializer()

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  import akka.http.scaladsl.marshalling._
  implicit val serialization = jackson.Serialization
  //implicit val formats = CrossdataSerializer


  val sessionId = UUID.fromString("69776ca1-f9eb-415e-a0f9-77e1d331f843")
  //val queryId = UUID.fromString("d44697a3-ff8a-46c2-80ff-b330f5d58c77")

  val query = CommandEnvelope(
    SQLCommand("show tables"),
    Session(sessionId, null),
    "pablo"
  )


  val res = for(
    requestEntity <- Marshal(query).to[RequestEntity];
    request = HttpRequest(
      method = HttpMethods.POST,
      uri = "http://127.0.0.1:13422/query",
      entity = requestEntity
    );
    response <- Http().singleRequest(request)//;
    //responseEntityStr <- Unmarshal(response.entity).to[String]
  ) yield response //responseEntityStr


  print(Await.result(res, 5 seconds))




}
