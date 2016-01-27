/**
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
import com.stratio.crossdata.streaming.constants.ApplicationConstants
import EphemeralQueryActor._
import ApplicationConstants._
import org.apache.curator.framework.recipes.cache.PathChildrenCache
import org.apache.spark.sql.crossdata.daos.EphemeralQueriesMapDAO
import org.apache.spark.sql.crossdata.models.EphemeralQueryModel

class EphemeralQueryActor(zookeeperConfiguration: Map[String, String]) extends Actor
with EphemeralQueriesMapDAO {

  val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)
  var streamingQueries : List[EphemeralQueryModel] = dao.getAll()
  var listenerAdded : Boolean = false

  override def receive: Receive = {
    case GetQueries => doGetQueries()
    case AddListener => doAddListener()
  }

  private def doGetQueries() : Unit = {
    sender ! EphemeralQueriesResponse(streamingQueries.toSeq)
  }

  private def doAddListener() : Unit = {
    if(!listenerAdded){
      repository.addEntityListener(dao.entity, (nodeCache: PathChildrenCache) => {
        streamingQueries = dao.getAll()
      })
      listenerAdded = true
    }
  }
}

object EphemeralQueryActor {

  case object GetQueries

  case object AddListener

  case class EphemeralQueriesResponse(streamingQueries: Seq[EphemeralQueryModel])

}
