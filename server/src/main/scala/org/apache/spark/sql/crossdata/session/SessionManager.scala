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

import com.hazelcast.core.{HazelcastInstance, IMap}
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog
import org.apache.spark.sql.crossdata.catalog.temporary.{HashmapCatalog, HazelcastCatalog, MapCatalog}

import scala.collection.mutable
import scala.util.Try


trait SessionManager[K, V] {
  
  def addSession(key: K): V

  def getSession(key: K): Try[V]

  def removeSession(key: K): Try[Unit]

  def clearAllSessions(): Unit
}

// TODO add listeners to propagate hazelcast changes (delete/updates) to the localCatalog
class HazelcastSessionCatalogManager(hInstance: HazelcastInstance, catalystConf: CatalystConf) extends SessionManager[SessionID, Seq[XDTemporaryCatalog]]{

  import HazelcastSessionProvider._

  type TableMapUUID = UUID
  type ViewMapUUID = UUID

  private val sessionIDToMapCatalog: mutable.Map[SessionID, MapCatalog] = mutable.Map.empty
  private val sessionIDToTableViewID: IMap[SessionID, (TableMapUUID, ViewMapUUID)] = hInstance.getMap(HazelcastCatalogMapId)

  // Returns the seq of XDTempCatalog for the new session
  override def addSession(key: SessionID): Seq[XDTemporaryCatalog] = { // TODO try

    // AddMapCatalog for local/cache interaction
    val localCatalog = addNewMapCatalog(key)

    // Add hazCatalog for detect metadata from other servers
    val (tableMap, tableMapUUID) = createRandomMap[TableIdentifier, CrossdataTable]
    val (viewMap, viewMapUUID) = createRandomMap[ViewIdentifier, String]
    val hazelcastCatalog = new HazelcastCatalog(tableMap, viewMap)(catalystConf)
    sessionIDToTableViewID.set(key, (tableMapUUID, viewMapUUID))

    Seq(localCatalog, hazelcastCatalog)
  }

  override def getSession(key: SessionID): Try[Seq[XDTemporaryCatalog]] =
    for {
      (tableUUID, viewUUID) <- checkNotNull(sessionIDToTableViewID.get(key))
      hazelcastTables <- checkNotNull(hInstance.getMap[TableIdentifier, CrossdataTable](tableUUID.toString))
      hazelcastViews <- checkNotNull(hInstance.getMap[ViewIdentifier, String](viewUUID.toString))
    } yield {
      val hazelcastCatalog = new HazelcastCatalog(hazelcastTables, hazelcastViews)(catalystConf)
      val mapCatalog = sessionIDToMapCatalog.getOrElse(key, addNewMapCatalog(key)) // local catalog could not exist
      Seq(mapCatalog, hazelcastCatalog)
    }

  override def removeSession(key: SessionID): Try[Unit] =
    for {
      (tableUUID, viewUUID) <- checkNotNull(sessionIDToTableViewID.get(key))
      hazelcastTables <- checkNotNull(hInstance.getMap(tableUUID.toString))
      hazelcastViews <- checkNotNull(hInstance.getMap(viewUUID.toString))
    } yield {
      hazelcastViews clear()
      hazelcastTables clear()
      sessionIDToTableViewID remove key
      sessionIDToMapCatalog remove key
    }

  override def clearAllSessions(): Unit = {
    import scala.collection.JavaConversions._
    sessionIDToTableViewID.values().foreach { case (tableUUID, viewUUID) =>
      hInstance.getMap(tableUUID.toString).clear()
      hInstance.getMap(viewUUID.toString).clear()
    }
    sessionIDToMapCatalog.clear()
    sessionIDToTableViewID.clear()
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

}
