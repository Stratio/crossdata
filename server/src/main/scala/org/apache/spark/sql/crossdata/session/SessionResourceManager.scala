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
package org.apache.spark.sql.crossdata.session

import java.util.UUID

import com.hazelcast.core.{HazelcastInstance, IMap, Message, MessageListener}
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog
import org.apache.spark.sql.crossdata.catalog.persistent.HazelcastCacheInvalidator.{CacheInvalidationEvent, ResourceInvalidation, ResourceInvalidationForAllSessions}
import org.apache.spark.sql.crossdata.catalog.temporary.{HashmapCatalog, HazelcastCatalog, MapCatalog}

import scala.collection.mutable
import scala.util.Try

trait SessionResourceManager[V] {

  //NOTE: THIS METHOD SHOULD NEVER BE CALLED TWICE WITH THE SAME ID
  def newResource(key: SessionID): V

  def getResource(key: SessionID): Try[V]

  def deleteSessionResource(key: SessionID): Try[Unit]

  def clearAllSessionsResources(): Unit

  def invalidateLocalCaches(key: SessionID): Unit

  def invalidateAllLocalCaches: Unit

}


trait HazelcastSessionResourceManager[V] extends MessageListener[CacheInvalidationEvent]
  with SessionResourceManager[V] {

  protected val topicName: String
  protected val hInstance: HazelcastInstance

  lazy val invalidationTopic = {
    val topic = hInstance.getTopic[CacheInvalidationEvent](topicName)
    topic.addMessageListener(this)
    topic
  }



  override def onMessage(message: Message[CacheInvalidationEvent]): Unit =
    Option(message.getMessageObject).filterNot(
      _ => message.getPublishingMember equals hInstance.getCluster.getLocalMember
    ) foreach {
      case ResourceInvalidation(sessionId) => invalidateLocalCaches(sessionId)
      case ResourceInvalidationForAllSessions => invalidateAllLocalCaches
    }

}

// TODO add listeners to propagate hazelcast changes (delete/updates) to the localCatalog
class HazelcastSessionCatalogResourceManager(
                                              override protected val hInstance: HazelcastInstance,
                                              catalystConf: CatalystConf
                                            ) extends HazelcastSessionResourceManager[Seq[XDTemporaryCatalog]] {

  import HazelcastSessionProvider._

  type TableMapUUID = UUID
  type ViewMapUUID = UUID

  override protected val topicName: String = "session-rec-catalog"

  private val sessionIDToMapCatalog: mutable.Map[SessionID, MapCatalog] = mutable.Map.empty
  private val sessionIDToTableViewID: IMap[SessionID, (TableMapUUID, ViewMapUUID)] = hInstance.getMap(HazelcastCatalogMapId)

  // Returns the seq of XDTempCatalog for the new session
  override def newResource(key: SessionID): Seq[XDTemporaryCatalog] = {
    // TODO try // TODO check if the session already exists?? and use it or it hsould not happen??
    //NO! IT SHOULDN'T HAPPEN BUT SOME PROTECTION IS STILL TODO
    // AddMapCatalog for local/cache interaction
    val localCatalog = addNewMapCatalog(key)

    publishInvalidation(key)

    // Add hazCatalog for detect metadata from other servers
    val (tableMap, tableMapUUID) = createRandomMap[TableIdentifier, CrossdataTable]
    val (viewMap, viewMapUUID) = createRandomMap[ViewIdentifier, String]
    val hazelcastCatalog = new HazelcastCatalog(tableMap, viewMap)(catalystConf)
    sessionIDToTableViewID.set(key, (tableMapUUID, viewMapUUID))

    Seq(localCatalog, hazelcastCatalog)
  }

  // TODO refactor
  override def getResource(key: SessionID): Try[Seq[XDTemporaryCatalog]] =
    for {
      (tableUUID, viewUUID) <- checkNotNull(sessionIDToTableViewID.get(key))
      hazelcastTables <- checkNotNull(hInstance.getMap[TableIdentifier, CrossdataTable](tableUUID.toString))
      hazelcastViews <- checkNotNull(hInstance.getMap[ViewIdentifier, String](viewUUID.toString))
    } yield {
      val hazelcastCatalog = new HazelcastCatalog(hazelcastTables, hazelcastViews)(catalystConf)
      val mapCatalog = sessionIDToMapCatalog.getOrElse(key, addNewMapCatalog(key)) // local catalog could not exist
      Seq(mapCatalog, hazelcastCatalog)
    }

  override def deleteSessionResource(key: SessionID): Try[Unit] =
    for {
      (tableUUID, viewUUID) <- checkNotNull(sessionIDToTableViewID.get(key))
      hazelcastTables <- checkNotNull(hInstance.getMap(tableUUID.toString))
      hazelcastViews <- checkNotNull(hInstance.getMap(viewUUID.toString))
    } yield {
      hazelcastViews clear()
      hazelcastTables clear()
      sessionIDToTableViewID remove key
      sessionIDToMapCatalog remove key
      publishInvalidation(key)
    }

  override def clearAllSessionsResources(): Unit = {
    import scala.collection.JavaConversions._
    sessionIDToTableViewID.values().foreach { case (tableUUID, viewUUID) =>
      hInstance.getMap(tableUUID.toString).clear()
      hInstance.getMap(viewUUID.toString).clear()
    }
    sessionIDToMapCatalog.clear()
    sessionIDToTableViewID.clear()
    publishInvalidation()
  }

  private def createRandomMap[K, V]: (IMap[K, V], UUID) = {
    val randomUUID = UUID.randomUUID()
    (hInstance.getMap[K, V](randomUUID.toString), randomUUID)
  }

  private def addNewMapCatalog(sessionID: SessionID): HashmapCatalog = {
    val localCatalog = new HashmapCatalog(catalystConf)
    sessionIDToMapCatalog.put(sessionID, localCatalog)
    localCatalog
  }

  override def invalidateLocalCaches(key: SessionID): Unit = {
    println(">"*10 +  "INVALIDATING CATALOG CACHE FOR SESSION: " + key) //TODO: remove this
    sessionIDToMapCatalog remove key
  }

  override def invalidateAllLocalCaches: Unit = {
    println(">"*10 +  "INVALIDATING CATALOG CACHE FOR ALL SESSIONS") //TODO: remove this
    sessionIDToMapCatalog clear
  }


  private def publishInvalidation(sessionID: Option[SessionID] = None): Unit =
    invalidationTopic publish {
      sessionID.map(ResourceInvalidation(_)) getOrElse ResourceInvalidationForAllSessions
    }

  private def publishInvalidation(sessionID: SessionID): Unit = publishInvalidation(Some(sessionID))

}
