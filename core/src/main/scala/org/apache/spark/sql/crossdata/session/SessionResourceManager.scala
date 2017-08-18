/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
