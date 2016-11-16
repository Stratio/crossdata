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

import java.security.SecureRandom
import java.util.UUID
import javax.net.ssl.{KeyManagerFactory, SSLContext, SSLException, TrustManagerFactory}

import akka.NotUsed
import akka.actor.ActorRef
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.http.scaladsl.{Http, HttpExt, HttpsConnectionContext}
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, TLSClientAuth}
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.security.{KeyStoreUtils, Session}
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.session.{Authentication, SessionManager}
import org.slf4j.{Logger, LoggerFactory}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.unmarshalling.{Unmarshaller, _}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.util.ByteString
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.serializers.{CrossdataCommonSerializer, StreamedRowSerializer}
import com.stratio.crossdata.driver.actor.HttpSessionBeaconActor
import com.stratio.crossdata.driver.exceptions.TLSInvalidAuthException
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.json4s.jackson

import scala.collection.generic.SeqFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Try}


class HttpDriver private[driver](driverConf: DriverConf,
                                 auth: Authentication
                                ) extends Driver(driverConf) with CrossdataCommonSerializer {

  import Driver._

  lazy val logger: Logger = LoggerFactory.getLogger(classOf[HttpDriver])

  override lazy val driverSession: Session = SessionManager.createSession(auth)

  private var sessionBeacon: Option[ActorRef] = None

  private implicit lazy val _ = system
  private implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  private implicit lazy val http = obtainHttpContext
  private val serverHttp: String = driverConf.getCrossdataServerHttp
  private def protocol = if(driverConf.httpTlsEnable) "https" else "http"
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
    if(driverConf.httpTlsEnable){ //Set for all the requests the Https configured context with key stores
      ext.setDefaultClientHttpsContext(getTlsContext)
    }
    ext
  }

  private def getTlsContext: HttpsConnectionContext = {
    val sslContext: SSLContext = SSLContext.getInstance("TLS")

    val keystorePath = driverConf.httpTlsKeyStore
    val keyStorePwd = driverConf.httpTlsKeyStorePwd
    val keyManagerFactory: KeyManagerFactory = KeyStoreUtils.getKeyManagerFactory(keystorePath, keyStorePwd)

    val trustStorePath = driverConf.httpTlsTrustStore
    val trustStorePwd = driverConf.httpTlsTrustStorePwd
    val trustManagerFactory: TrustManagerFactory = KeyStoreUtils.getTrustManagerFactory(trustStorePath, trustStorePwd)

    sslContext.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom())
    new HttpsConnectionContext(sslContext, clientAuth = Some(TLSClientAuth.Need))
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
      case e: SSLException if driverConf.httpTlsEnable =>
        throw TLSInvalidAuthException("Possible invalid authentication (check if you have a valid TLS certificate configured in your driver).", e)

      case exception if defaultValue.isDefined =>
        logger.error(exception.getMessage, exception)
        defaultValue.get
    }

  }

  protected[driver] def openSession(user: String): Try[Boolean] = {
    val command = OpenSessionCommand(user)
    val response = simpleRequest(
      securitizeCommand(command),
      s"query/${command.requestId}",
      { reply: OpenSessionReply => reply.isOpen }
    )

    val res = Try(Await.result(response, InitializationTimeout))
    sessionBeacon = res.toOption collect { case true => system.actorOf(sessionBeaconProps) }
    res

  }


  override def sql(query: String): SQLResponse = {

    val sqlCommand = new SQLCommand(query, retrieveColNames = driverConf.getFlattenTables)

    // Performs the request to server
    val response = Marshal(securitizeCommand(sqlCommand)).to[RequestEntity] flatMap { requestEntity =>
      val request = HttpRequest(POST, s"$protocol://$serverHttp/query/${sqlCommand.requestId}", entity = requestEntity)
       http.singleRequest(request) flatMap { httpResponse =>

         if(httpResponse.status == StatusCodes.OK) { // OK Responses will be served through streaming

           deserializeSchemaAndRows(httpResponse.entity.dataBytes).flatMap { case (schema, streamedRowSource) =>
             val rows = streamedRowSource.runFold(List.empty[Row]) {
               case (acc: List[Row], StreamedRow(row, None)) => row::acc
               case _ => Nil
             }
             rows.map{ rowList =>
               /* TODO: Performance could be increased if `SuccessfulSQLResult`#resultSet were of type `Seq[Row]`*/
               SuccessfulSQLResult(rowList.reverse toArray, schema)
             }
           }
         } else {
             Unmarshal(httpResponse.entity).to[SQLReply] map {
               case SQLReply(_, result: SQLResult) => result
             }
         }
       }
    }

    new SQLResponse(sqlCommand.requestId, response) {
      override def cancelCommand(): Future[QueryCancelledReply] = {
        val command = CancelQueryExecution(sqlCommand.queryId)
        simpleRequest(
          securitizeCommand(command),
          s"query/${command.requestId}", {
            reply: QueryCancelledReply => reply
          }
        )
      }
    }

  }


  override def sqlStreamSource(query: String): Future[StreamedSQLResult] = {

    val sqlCommand = new SQLCommand(query, retrieveColNames = driverConf.getFlattenTables)
    Marshal(securitizeCommand(sqlCommand)).to[RequestEntity] flatMap { requestEntity =>

      val request = HttpRequest(POST, s"$protocol://$serverHttp/query/${sqlCommand.requestId}", entity = requestEntity)
      http.singleRequest(request) flatMap { httpResponse =>

        if (httpResponse.status == StatusCodes.OK) {  // OK Responses will be served through streaming
          deserializeSchemaAndRows(httpResponse.entity.dataBytes).map { case (schema, streamedRowSource) =>
            val rows = streamedRowSource.map { case streamedRow: StreamedRow => streamedRow.row }
            StreamedSuccessfulSQLResult(rows, schema)
          }
        } else {
          Unmarshal(httpResponse.entity).to[SQLReply].map {
            case SQLReply(_, ErrorSQLResult(message, cause)) => StreamedErrorSQLResult(message, cause)
          }
        }
      }
    }
  }

  private def deserializeSchemaAndRows(bytesSource: Source[ByteString, Any]): Future[(StructType, Source[InternalStreamedSuccessfulSQLResult, NotUsed])] = {
    val framesSource = bytesSource.filterNot(bs => bs.isEmpty || bs == ByteString("\n")) //...empty lines get removed...
    val rawSchemaAndRawRowsSource = framesSource.prefixAndTail[ByteString](1) //remaining get transformed to ByteStrings.

    // From the raw lines stream, a new stream providing the first one and a stream of the remaining ones is created
    val sink = Sink.head[(Seq[ByteString], Source[ByteString, NotUsed])] //Its single elements get extracted by future...

    rawSchemaAndRawRowsSource.toMat(sink)(Keep.right).run.flatMap { case (Seq(rawSchema), rawRows) =>
      Unmarshal(HttpEntity(ContentTypes.`application/json`, rawSchema)).to[InternalStreamedSuccessfulSQLResult].map {
        case StreamedSchema(schema) => // Having de-serialized the schema, it can be used to deserialize each row at the un-marshalling phase
          (schema, deserializeRows(schema, rawRows))
      }
    }
  }

  private def deserializeRows(schema: StructType, rawRows: Source[ByteString, NotUsed]): Source[InternalStreamedSuccessfulSQLResult, NotUsed] = {
    implicit val json4sJacksonFormats = this.json4sJacksonFormats + new StreamedRowSerializer(schema)
    val um: Unmarshaller[ResponseEntity, InternalStreamedSuccessfulSQLResult] = json4sUnmarshaller

    rawRows.mapAsync(1) { bs =>
      val entity = HttpEntity(ContentTypes.`application/json`, bs)
      um(entity)
    }
  }

  override def addJar(path: String, toClassPath: Option[Boolean] = None): SQLResponse =
    apiNotSupported("addJar")

  override def addAppCommand(path: String, clss: String, alias: Option[String]): SQLResponse =
    apiNotSupported("addAppCommand")

  override def clusterState(): Future[CurrentClusterState] = {
    val command = ClusterStateCommand()
    simpleRequest(
      securitizeCommand(command),
      s"query/${command.requestId}",
      { reply: ClusterStateReply => reply.clusterState }
    )
  }

  private[driver] def sessionProviderState(): Future[scala.collection.Set[String]] =
    simpleRequest(
      securitizeCommand(ClusterStateCommand()),
      "query",
      { reply: ClusterStateReply => reply.sessionCluster }
    )

  override def closeSession(): Unit = {
    val command = CloseSessionCommand()
    val response = Marshal(securitizeCommand(command)).to[RequestEntity] flatMap { requestEntity =>
      http.singleRequest(HttpRequest(POST, s"$protocol://$serverHttp/query/${command.requestId}", entity = requestEntity))
    }
    Try(Await.ready(response, requestTimeout)) recoverWith {
      case err =>
        sessionBeacon.foreach(system.stop)
        Failure(err)
    } get
  }

  private def apiNotSupported(command: String): SQLResponse =
    new SQLResponse(
      UUID.randomUUID(),
      Future.successful(ErrorSQLResult(s"HttpDriver does not support $command; please, use a ClusterClientDriver instead"))
    )

}
