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
package com.stratio.crossdata.server.actors

import java.io.{File, InputStream}
import java.lang.reflect.Method
import java.net.{URL, URLClassLoader}

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.contrib.pattern.DistributedPubSubExtension
import akka.contrib.pattern.DistributedPubSubMediator.{Subscribe, SubscribeAck}
import com.google.common.io.Files
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.server.actors.ServerActor.State
import com.stratio.crossdata.server.config.ServerConfig
import com.stratio.crossdata.utils.HdfsUtils
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.types.StructType

object ResourceManagerActor {
  val AddJarTopic: String = "newJAR"

  def props(cluster: Cluster, xdContext: XDContext): Props =
    Props(new ResourceManagerActor(cluster, xdContext))
}

class ResourceManagerActor(cluster: Cluster, xdContext: XDContext) extends Actor with ServerConfig {

  import ResourceManagerActor._

  lazy val logger = Logger.getLogger(classOf[ServerActor])

  lazy val mediator = DistributedPubSubExtension(context.system).mediator

  override def preStart(): Unit = {
    super.preStart()
    mediator ! Subscribe(AddJarTopic, self)
  }

  override def receive: Actor.Receive = initial(Set(AddJarTopic))

  private def initial(pendingTopics: Set[String]): Receive = {

    case SubscribeAck(Subscribe(AddJarTopic, None, self)) =>
      val newPendingTopics = pendingTopics - AddJarTopic
      checkSubscriptions(newPendingTopics)
  }

  private def checkSubscriptions(pendingTopics: Set[String]): Unit =
    if (pendingTopics.isEmpty)
      context.become(ready(State(Map.empty)))
    else
      context.become(initial(pendingTopics))

  // Function composition to build the finally applied receive-function
  private def ready(st: State): Receive =
    AddJarMessages(st)

  // Commands reception: Checks whether the command can be run at this Server passing it to the execution method if so
  def AddJarMessages(st: State): Receive = {
    case CommandEnvelope(addJarCommand: AddJARCommand, session@Session(id, requester)) =>
      logger.debug(s"Add JAR received ${addJarCommand.requestId}: ${addJarCommand.path}. Actor ${self.path.toStringWithoutAddress}")
      logger.debug(s"Session identifier $session")
      //TODO  Maybe include job controller if it is necessary as in sql command
      if (addJarCommand.path.toLowerCase.startsWith("hdfs://")) {
        xdContext.addJar(addJarCommand.path)
        //add to runtime
        val fileHdfsPath = addJarCommand.path
        val hdfsIS: InputStream = HdfsUtils(addJarCommand.hdfsConfig.get).getFile(fileHdfsPath)
        val file: File = createFile(hdfsIS, s"${config.getString(ServerConfig.repoJars)}/${fileHdfsPath.split("/").last}")
        addToClasspath(file)
        sender ! SQLReply(addJarCommand.requestId, SuccessfulSQLResult(Array.empty, new StructType()))
      } else {
        sender ! SQLReply(addJarCommand.requestId, ErrorSQLResult("File doesn't exists or is not a hdfs file", Some(new Exception("File doesn't exists or is not a hdfs file"))))
      }
    case _ =>
  }

  private def addToClasspath(file: File): Unit = {
    if (file.exists) {
      val method: Method = classOf[URLClassLoader].getDeclaredMethod("addURL", classOf[URL])
      method.setAccessible(true)
      method.invoke(ClassLoader.getSystemClassLoader, file.toURI.toURL)
      method.setAccessible(false)
    } else {
      logger.warn(s"The file ${file.getName} not exists.")
    }
  }

  private def createFile(hdfsIS: InputStream, path: String): File = {
    val targetFile = new File(path)

    val arrayBuffer = new Array[Byte](hdfsIS.available)
    hdfsIS.read(arrayBuffer)

    Files.write(arrayBuffer, targetFile)
    targetFile
  }


}
