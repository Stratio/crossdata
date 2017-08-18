package org.apache.spark.sql.crossdata.session

import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID

import scala.util.Try

trait SessionResourceManager[V] {

  //NOTE: THIS METHOD SHOULD NEVER BE CALLED TWICE WITH THE SAME ID
  def newResource(key: SessionID, from: Option[V]): V

  def getResource(key: SessionID): Try[V]

  def deleteSessionResource(key: SessionID): Try[Unit]

  def clearAllSessionsResources(): Unit

  def invalidateLocalCaches(key: SessionID): Unit

  def invalidateAllLocalCaches: Unit

}
