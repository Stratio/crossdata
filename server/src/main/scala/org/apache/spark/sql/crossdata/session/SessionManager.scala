package org.apache.spark.sql.crossdata.session

import java.util.UUID

import com.hazelcast.core.{HazelcastInstance, IMap}
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.temporary.{HashmapCatalog, HazelcastCatalog, MapCatalog}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog

import scala.util.Try


trait SessionManager[K, V] {

  //def put(key: K, value: V): Try[V]

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

  private val sessionIDToMapCatalog: java.util.Map[SessionID, MapCatalog] = new java.util.HashMap()
  private val sessionIDToTableViewID: IMap[SessionID, (TableMapUUID, ViewMapUUID)] = hInstance.getMap(HazelcastCatalogMapId)

  // Returns the seq of XDTempCatalog for the new session
  override def addSession(key: SessionID): Seq[XDTemporaryCatalog] = {
    // TODO try // TODO check if the session already exists?? and use it or it hsould not happen??
    // AddMapCatalog for local/cache interaction
    val localCatalog = new HashmapCatalog(catalystConf)
    sessionIDToMapCatalog.put(key, localCatalog)

    // Add hazCatalog for detect metadata from other servers
    val (tableMap, tableMapUUID) = createRandomMap[TableIdentifier, CrossdataTable]
    val (viewMap, viewMapUUID) = createRandomMap[ViewIdentifier, String]
    val hazelcastCatalog = new HazelcastCatalog(tableMap, viewMap)(catalystConf)
    sessionIDToTableViewID.set(key, (tableMapUUID, viewMapUUID))

    Seq(localCatalog, hazelcastCatalog)
  }

  // TODO refactor
  override def getSession(key: SessionID): Try[Seq[XDTemporaryCatalog]] =
    for {
      mapCatalog <- checkNotNull(sessionIDToMapCatalog.get(key))
      (tableUUID, viewUUID) <- checkNotNull(sessionIDToTableViewID.get(key))
      hazelcastTables <- checkNotNull(hInstance.getMap[TableIdentifier, CrossdataTable](tableUUID.toString))
      hazelcastViews <- checkNotNull(hInstance.getMap[ViewIdentifier, String](viewUUID.toString))
    } yield {
      val hazelcastCatalog = new HazelcastCatalog(hazelcastTables, hazelcastViews)(catalystConf)
      Seq(mapCatalog, hazelcastCatalog)
    }


  override def removeSession(key: SessionID): Try[Unit] =
    for {
      mapCatalog <- checkNotNull(sessionIDToMapCatalog.get(key))
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
    // TODO clear underlying maps
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

}
