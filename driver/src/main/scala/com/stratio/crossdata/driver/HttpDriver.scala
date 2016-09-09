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
package com.stratio.crossdata.driver

import java.util.UUID

import akka.actor.ActorRef
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model.{HttpMethod, HttpRequest, RequestEntity, ResponseEntity}
import akka.stream.ActorMaterializer
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.session.Authentication
import org.slf4j.{Logger, LoggerFactory}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import com.stratio.crossdata.common.{OpenSessionCommand, OpenSessionReply, SQLReply}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Try

class HttpDriver private[driver](driverConf: DriverConf,
                                 auth: Authentication) extends Driver(driverConf, auth) {

  import Driver._

  lazy val logger: Logger = LoggerFactory.getLogger(classOf[HttpDriver])

  lazy val driverSession: Session = ???

  private var sessionBeacon: Option[ActorRef] = ???

  private implicit lazy val _ = system
  private implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  private lazy val http = Http(system)
  private val serverHttp = driverConf.getCrossdataServerHttp
  private val protocol = "http" //TODO


/*  override protected[driver] def openSession(): Try[Boolean] = {

    val isOpenReply =
      for {
        requestEntity <- Marshal(securitizeCommand(OpenSessionCommand())).to[RequestEntity] //TODO commandEnvelope??
        httpResponse <- http.singleRequest(HttpRequest(POST, s"$protocol://$serverHttp/opensession", entity = requestEntity))
        reply <- Unmarshal(httpResponse.entity).to[OpenSessionReply]
        isOpen <- reply.isOpen
      } yield isOpen

    val recovered =
      isOpenReply.recover {
        case exception =>
          logger.error(exception.getMessage, exception)
          false
      }

    Try {
      // TODO onComplete // remove try
      Await.result(recovered, ClusterClientDriver.InitializationTimeout)
    }
  }*/

  private def simpleRequest[A, E, R](
                                      toMarshalCommand: A,
                                      path: String,
                                      replyToResult: E => R,
                                      defaultValue: Option[R] = None,
                                      httpMethod: HttpMethod = POST
                                    )(implicit m: Marshaller[A, RequestEntity], u: Unmarshaller[ResponseEntity, E]): Future[R] = {


    val result =
      for {
        requestEntity <- Marshal(toMarshalCommand).to[RequestEntity]
        httpResponse <- http.singleRequest(HttpRequest(httpMethod, s"$protocol://$serverHttp/$path", entity = requestEntity))
        reply <- Unmarshal(httpResponse.entity).to[E]
        desiredResult = replyToResult(reply)
      } yield desiredResult

    defaultValue.map { dValue =>
      result.recover {
        case exception =>
          logger.error(exception.getMessage, exception)
          dValue
      }
    }.getOrElse(result)

  }

  protected[driver] def openSession(): Try[Boolean] = {

    val result = simpleRequest(
      securitizeCommand(OpenSessionCommand()),
      "openSession",
      (opSessionReply: OpenSessionReply) => opSessionReply.isOpen
    )

    Try {
      // TODO onComplete // remove try
      Await.result(result, InitializationTimeout)
    }
  }

  override def sql(query: String): SQLResponse = ???

  override def addJar(path: String, toClassPath: Option[Boolean] = None): SQLResponse =
    apiNotSupported("addJar")

  override def addAppCommand(path: String, clss: String, alias: Option[String]): SQLResponse =
    apiNotSupported("addAppCommand")

  override def clusterState(): Future[CurrentClusterState] = ???

  override def closeSession(): Unit = ???

  private def apiNotSupported(command: String): SQLResponse =
    new SQLResponse(
      UUID.randomUUID(),
      Future.successful(ErrorSQLResult(s"HttpDriver does not support $command; please, use a ClusterClientDriver instead"))
    )


  /*{
    proxyActor ! securitizeCommand(CloseSessionCommand())
    sessionBeacon.foreach(system.stop)
  }*/


 /* val tablesURI = {
    Uri(s"http://$serverHttp/tables")//.withQuery(Query("sessionID" -> session.id.toString))
  }
  val requestEntity = Marshal(commandEnvelope).to[RequestEntity]

  requestEntity.flatMap { reqEntity =>
    //TODO flatMap
    http.singleRequest(HttpRequest(HttpMethods.POST, uri = tablesURI, entity = reqEntity)).flatMap { //TODO pipeTo an actor vs no onSuccess
      case resp @ HttpResponse(StatusCodes.OK, _, _, _ ) =>
        // TODO transform resp to XXX
        /*for (
          strictEntity <- response.entity.toStrict(5 seconds)
        ) yield strictEntity.data.decodeString("UTF-8")
        */

        val unmarshalStr = Unmarshal(resp.entity).to[SQLReply]

        unmarshalStr.onSuccess{
          case sqlReply => sqlReply.sqlResult.resultSet.foreach(println)
        }

        unmarshalStr

      case HttpResponse(code, _, _, _) =>
        throw new RuntimeException(s"Request failed, response code: $code")
    }

  } recover  {
    case throwa => SQLReply(commandEnvelope.cmd.requestId, ErrorSQLResult("Failed while marshalling")) // TODO replace
  }*/

}