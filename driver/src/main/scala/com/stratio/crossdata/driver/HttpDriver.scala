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

import java.io.{FileInputStream, InputStream}
import java.security.{KeyStore, SecureRandom}
import java.util.UUID
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import akka.actor.ActorRef
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.http.scaladsl.{Http, HttpExt, HttpsConnectionContext}
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model.{HttpMethod, HttpRequest, RequestEntity, ResponseEntity}
import akka.stream.{ActorMaterializer, StreamTcpException, TLSClientAuth}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.session.{Authentication, SessionManager}
import org.slf4j.{Logger, LoggerFactory}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.serializers.CrossdataCommonSerializer
import com.stratio.crossdata.driver.actor.HttpSessionBeaconActor
import com.stratio.crossdata.driver.error.TLSInvalidAuthException
import org.json4s.jackson

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Success, Try}


class HttpDriver private[driver](driverConf: DriverConf,
                                 auth: Authentication
                                ) extends Driver(driverConf, auth) with CrossdataCommonSerializer {

  import Driver._

  lazy val logger: Logger = LoggerFactory.getLogger(classOf[HttpDriver])

  override lazy val driverSession: Session = SessionManager.createSession(auth)

  private var sessionBeacon: Option[ActorRef] = None

  private implicit lazy val _ = system
  private implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  private implicit lazy val http = obtainHttpContext
  private val serverHttp = driverConf.getCrossdataServerHttp
  private val protocol = "http" //TODO
  private val requestTimeout: Duration = Duration.Inf //TODO

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  implicit val serialization = jackson.Serialization


  private lazy val sessionBeaconProps = HttpSessionBeaconActor.props(
    driverSession.id,
    5 seconds, /* This ins't configurable since it's simpler for the user
                  to play just with alert period time at server side. */
    s"$protocol://$serverHttp/sessions"
  )

  private def obtainHttpContext: HttpExt = {
    val ext = Http(system)
    if(driverConf.httpTlsEnable){ //Set for all the requests the Https configurated context with keystores
      ext.setDefaultClientHttpsContext(getTlsContext)
    }
    ext
  }

  private def getTlsContext: HttpsConnectionContext = {
    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(getKeyManagerFactory.getKeyManagers, getTrustManagerFactory.getTrustManagers, new SecureRandom())
    new HttpsConnectionContext(sslContext, clientAuth = Some(TLSClientAuth.Need))
  }

  private def getKeyManagerFactory: KeyManagerFactory = {
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val keyStorePwd = driverConf.httpTlsKeyStorePwd
    keyManagerFactory.init(getKeyStore, keyStorePwd.toCharArray)
    keyManagerFactory
  }

  private def getKeyStore: KeyStore = {
    val ks: KeyStore = KeyStore.getInstance("JKS")
    val path = driverConf.httpTlsKeyStore
    val pwd = driverConf.httpTlsKeyStorePwd
    val keystore: InputStream = new FileInputStream(path)
    require(keystore != null, "Keystore required!")
    ks.load(keystore, pwd.toCharArray)
    ks
  }

  private def getTrustManagerFactory: TrustManagerFactory = {
    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    tmf.init(getTrustStore)
    tmf
  }

  private def getTrustStore: KeyStore = {
    val ts: KeyStore = KeyStore.getInstance("JKS")
    val path = driverConf.httpTlsTrustStore
    val pwd = driverConf.httpTlsTrustStorePwd
    val truststore: InputStream = new FileInputStream(path)
    require(truststore != null, "TrustStore required!")
    ts.load(truststore, pwd.toCharArray)
    ts
  }


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

    result.recover {
      case e: StreamTcpException if driverConf.httpTlsEnable =>
        throw TLSInvalidAuthException("Possible invalid authentication (check if you have a valid TLS certificate configured in your driver or if your server is under a lot of requests).",e)

      case exception if defaultValue.isDefined =>
        logger.error(exception.getMessage, exception)
        defaultValue.get
    }

  }

  protected[driver] def openSession(): Try[Boolean] = {

    val response = simpleRequest(
      securitizeCommand(OpenSessionCommand()),
      "query",
      { reply: OpenSessionReply => reply.isOpen }
    )

    val res = Try(Await.result(response, InitializationTimeout))
    if(res.getOrElse(false)) sessionBeacon = Some(system.actorOf(sessionBeaconProps))
    res

  }


  override def sql(query: String): SQLResponse = {

    val sqlCommand = new SQLCommand(query, retrieveColNames = driverConf.getFlattenTables)

    val response = simpleRequest(
      securitizeCommand(sqlCommand),
      "query",
      {
        case SQLReply(_, result: SQLResult) =>
          result
      } : PartialFunction[SQLReply, SQLResult]
    )

    SQLResponse(sqlCommand.requestId, response) //TODO: Cancellable

  }

  override def addJar(path: String, toClassPath: Option[Boolean] = None): SQLResponse =
    apiNotSupported("addJar")

  override def addAppCommand(path: String, clss: String, alias: Option[String]): SQLResponse =
    apiNotSupported("addAppCommand")

  override def clusterState(): Future[CurrentClusterState] =
    simpleRequest(
      securitizeCommand(ClusterStateCommand()),
      "query",
      { reply: ClusterStateReply => reply.clusterState }
    )

  override def closeSession(): Unit = {
    val response = Marshal(securitizeCommand(CloseSessionCommand())).to[RequestEntity] flatMap { requestEntity =>
      http.singleRequest(HttpRequest(POST, s"$protocol://$serverHttp/query", entity = requestEntity))
    }
    Await.ready(response, requestTimeout)
    sessionBeacon.foreach(system.stop)
  }

  private def apiNotSupported(command: String): SQLResponse =
    new SQLResponse(
      UUID.randomUUID(),
      Future.successful(ErrorSQLResult(s"HttpDriver does not support $command; please, use a ClusterClientDriver instead"))
    )

}
