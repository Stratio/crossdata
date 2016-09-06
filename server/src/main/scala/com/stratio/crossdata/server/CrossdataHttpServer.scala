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
package com.stratio.crossdata.server

import java.io.File

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import akka.http.javadsl.model.RequestEntity
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directive
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.{ByteString, Timeout}
import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.{AddJARCommand, CommandEnvelope, SQLCommand, SQLReply}
import com.stratio.crossdata.server.actors.ResourceManagerActor
import com.stratio.crossdata.util.HdfsUtils
import com.typesafe.config.Config
import org.apache.log4j.Logger
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.{DefaultFormats, jackson}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


class CrossdataHttpServer(config: Config, serverActor: ActorRef, implicit val system: ActorSystem) extends
CrossdataSerializer {

  import ResourceManagerActor._

  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
  lazy val logger = Logger.getLogger(classOf[CrossdataHttpServer])
  lazy val mediator = DistributedPubSub(system).mediator

  type SessionDirective[Session] = Directive[Tuple1[Session]]

  lazy val route =
    path("upload" / JavaUUID) { sessionUUID =>
      entity(as[Multipart.FormData]) { formData =>
        // collect all parts of the multipart as it arrives into a map
        var path = ""
        val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

          case part: BodyPart if part.name == "fileChunk" =>
            // stream into a file as the chunks of it arrives and return a future file to where it got stored
            val file = new java.io.File(s"/tmp/${part.filename.getOrElse("uploadFile")}")
            path = file.getAbsolutePath
            logger.info("Uploading file...")
            // TODO map is not used
            part.entity.dataBytes.runWith(FileIO.toFile(file)).map(_ => part.name -> file)

        }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)


        // when processing have finished create a response for the user
        onSuccess(allPartsF) { allParts =>

          logger.info("Recieved file")
          complete {
            val hdfsConfig = XDContext.xdConfig.getConfig("hdfs")
            val hdfsPath = writeJarToHdfs(hdfsConfig, path)
            val session = Session(sessionUUID, null)
            val user = "fileupload"
            allParts.values.toSeq.foreach{
              case file: File =>
                file.delete
                logger.info("Tmp file deleted")
              case _ => logger.error("Problem deleting the temporary file.")
            }
            //Send a broadcast message to all servers
            mediator ! Publish(AddJarTopic, CommandEnvelope(AddJARCommand(hdfsPath, hdfsConfig = Option(hdfsConfig)), session, user))
            hdfsPath
          }
        }
      }

    } ~ path("query") {
      //TODO: REFACTOR
      import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
      implicit val serialization = jackson.Serialization

      post {
        entity(as[CommandEnvelope]) { rq: CommandEnvelope =>

          val queryTo = 30 minutes
          implicit val _ = Timeout(queryTo)

          //TODO: use 'onComplete' directive.
          Await.result(serverActor ? rq, queryTo) match {
            case SQLReply(requestId, _) if requestId != rq.cmd.requestId =>
              complete(StatusCodes.ServerError, s"Request ids do not match: (${rq.cmd.requestId}, $requestId)")

            case reply @ SQLReply(_, SuccessfulSQLResult(rset, _)) =>

              complete(reply)

              /*val txt = ("" /: rset) {
                case (acc, row: Row) => acc + row.toString() + "\n"
              }

              complete(StatusCodes.OK, txt)*/

             /* val resStream = Source.fromIterator(() => rset.iterator)

              complete(
                HttpEntity(
                  ContentTypes.`text/plain(UTF-8)`,
                  resStream.map(row => ByteString(row.toString + "\n"))
                )
              )*/

            case SQLReply(_, ErrorSQLResult(message, _)) =>
              complete(StatusCodes.ServerError, message)

            case other => complete(StatusCodes.ServerError, s"Bad internal reply: $other")

          }

        }
      }
    } ~ complete("Welcome to Crossdata HTTP Server")


  val getRqEnt = extract[HttpRequest] { rqCtx =>
    rqCtx.request
  }

  private def writeJarToHdfs(hdfsConfig: Config, jar: String): String = {
    val user = hdfsConfig.getString("user")
    val hdfsMaster = hdfsConfig.getString("namenode")
    val destPath = s"/user/$user/externalJars/"

    val hdfsUtil = HdfsUtils(hdfsConfig)

    //send to HDFS if not exists
    val jarName = new File(jar).getName
    if (!hdfsUtil.fileExist(s"$destPath/$jarName")) {
      hdfsUtil.write(jar, destPath)
    }
    s"$hdfsMaster/$destPath/$jarName"
  }

}
