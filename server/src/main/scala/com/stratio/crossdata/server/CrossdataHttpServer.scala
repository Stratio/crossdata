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
import akka.contrib.pattern.ClusterClient.Publish
import akka.contrib.pattern.DistributedPubSubExtension
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.{AddJARCommand, CommandEnvelope}
import com.stratio.crossdata.utils.HdfsUtils
import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.XDContext

import scala.concurrent.Future

class CrossdataHttpServer(config:Config, serverActor:ActorRef, httpSystem:ActorSystem) {

  implicit val system=httpSystem
  implicit val materializer = ActorMaterializer()
  import scala.concurrent.ExecutionContext.Implicits.global

  val addJarTopic: String = "newJAR"
  lazy val mediator = DistributedPubSubExtension(system).mediator

  def route =
    path("upload") {
      entity(as[Multipart.FormData]) {
        formData =>
          // collect all parts of the multipart as it arrives into a map
          var path=""
          val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

            case part: BodyPart if part.name == "fileChunk" =>
              // stream into a file as the chunks of it arrives and return a future file to where it got stored
              val file=new java.io.File(s"/tmp/${part.filename.getOrElse("uploadFile")}")
              path=file.getAbsolutePath
              println("Uploading file...")
              part.entity.dataBytes.runWith(FileIO.toFile(file)).map(_ =>
                (part.name -> file))

          }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

          val done = allPartsF.map { allParts =>
            println("Recieved file")
          }
          // when processing have finished create a response for the user
          onSuccess(allPartsF) { allParts =>
            complete {
              val hdfsConfig=XDContext.xdConfig.getConfig("hdfs")
              //Send a broadcast message to all servers
              val hdfsPath=getHdfsPath(hdfsConfig,path)
              mediator ! Publish(addJarTopic, CommandEnvelope(AddJARCommand(hdfsPath),new Session("HttpServer",serverActor)))
              hdfsPath
            }
          }
      }
    }~
      complete("Welcome to Crossdata HTTP Server")

  private def getHdfsPath(hdfsConfig:Config, jar:String):String={
    val user = hdfsConfig.getString("hadoopUserName")
    val hdfsMaster= hdfsConfig.getString("hdfsMaster")
    val destPath = s"/user/$user/externalJars/"

    val hdfsUtil = HdfsUtils(hdfsConfig)

    //send to HDFS if not exists
    val jarName = new File(jar).getName
    if (!hdfsUtil.fileExist(s"$destPath/$jarName")) {
      hdfsUtil.write(jar, destPath)
    }
    s"hdfs://$hdfsMaster/$destPath/$jarName"
  }



}
