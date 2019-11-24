/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.daos

import java.util.UUID

object DAOConstants {

  val PrefixPermantCatalogsConfig = "prefix" //crossdata-core.catalog.prefix
  val PrefixStreamingCatalogsConfigForActors = "prefix" //crossdata-core.streaming.catalog.zookeeper.prefix
  val PrefixStreamingCatalogsConfig = "zookeeper.prefix" //crossdata-core.streaming.catalog.zookeeper.prefix

  val BaseZKPath = "stratio/crossdata"
  val TablesPath = "tables"
  val ViewsPath = "views"
  val AppsPath = "apps"
  val IndexesPath = "indexes"
  val EphemeralTablesPath = "ephemeraltables"
  val EphemeralTableStatusPath = "ephemeraltablestatus"
  val EphemeralQueriesPath = "ephemeralqueries"

  def createId: String = UUID.randomUUID.toString
}
