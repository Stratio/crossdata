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
package com.stratio.crossdata.streaming.actors

import akka.actor.Actor
import com.stratio.crossdata.streaming.actors.EphemeralQueryActor._
import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.EphemeralQueriesMapDAO
import org.apache.spark.sql.crossdata.models.EphemeralQueryModel

import scala.util.Try

class EphemeralQueryActor(zookeeperConfiguration: Map[String, String]) extends Actor
with EphemeralQueriesMapDAO {

  lazy val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)
  var streamingQueries: List[EphemeralQueryModel] = dao.getAll()

  def prefix:String = Try(memoryMap.get(ZookeeperPrefixName).get(PrefixStreamingCatalogsConfigForActors)+"_") getOrElse ("")

  import context.become

  def receive: Receive = receive(listenerAdded = false)

  def receive(listenerAdded: Boolean): Receive = {
    case GetQueries if listenerAdded =>
      doGetQueries()
    case AddListener if !listenerAdded =>
      doAddListener()
      become(receive(listenerAdded = true))
  }

  private def doGetQueries(): Unit = {
    sender ! EphemeralQueriesResponse(streamingQueries.toSeq)
  }

  private def doAddListener(): Unit = {
    repository.addEntityListener(dao.entity, _ => streamingQueries = dao.getAll())
    sender ! ListenerResponse(true)
  }
}

object EphemeralQueryActor {

  case object GetQueries

  case object AddListener

  case class ListenerResponse(added : Boolean)

  case class EphemeralQueriesResponse(streamingQueries: Seq[EphemeralQueryModel])

}
