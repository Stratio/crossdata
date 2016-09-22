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
import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, SendToAll}
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directive
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import akka.util.Timeout
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.util.akka.keepalive.LiveMan.HeartBeat
import com.stratio.crossdata.common.{AddJARCommand, CommandEnvelope, SQLReply, ServerReply}
import com.stratio.crossdata.server.actors.ResourceManagerActor
import com.stratio.crossdata.util.HdfsUtils
import com.typesafe.config.Config
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.jackson

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success


class CrossdataHttpServer(config: Config, serverActor: ActorRef, implicit val system: ActorSystem) extends CrossdataSerializer {

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  implicit val serialization = jackson.Serialization

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

      post {
        entity(as[CommandEnvelope]) { rq: CommandEnvelope =>

          implicit val _ = Timeout(1 hour) //TODO Make this configurable

          onComplete(serverActor ? rq) {
            case Success(SQLReply(requestId, _)) if requestId != rq.cmd.requestId =>
              complete(StatusCodes.ServerError, s"Request ids do not match: (${rq.cmd.requestId}, $requestId)")
            case Success(reply: ServerReply) =>
              complete(reply)
            case other => complete(StatusCodes.ServerError, s"Internal XD server error: $other")
          }

        } /*~ getRqEnt { rq: HttpRequest =>
          onComplete(rq.entity.toStrict(5 seconds)) {
            case Success(s: HttpEntity.Strict) =>
              import org.json4s.jackson.JsonMethods._
              val bs = s.data.toIterator.toArray
              val parsed = parse(new String(bs))
              println("\n\n\n" + parsed.toString)
              val extracted = parsed.extract[CommandEnvelope]
              complete(parsed.toString)
          }
        }*/
      }

    } ~ path("sessions") {

      post {  //Session life proof is not a PUT to /session/idSession for security reasons.
        entity(as[HeartBeat[UUID]]) { heartBeat =>
          mediator ! SendToAll("/user/client-monitor", heartBeat) //TODO: Hardcoded path
          complete(StatusCodes.Success) //Doesn't give clues on active sessions...
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
