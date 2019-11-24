/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog.temporary

import java.util.UUID

import com.hazelcast.core.Hazelcast
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.catalog.TableIdentifierNormalized
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HazelcastCatalogSpec extends {
  val catalogName = "Hazelcast"
} with XDTemporaryCatalogTests {

  override lazy val temporaryCatalog: XDTemporaryCatalog = {
    val hInstance = Hazelcast.newHazelcastInstance
    val tables = hInstance.getMap[TableIdentifierNormalized, CrossdataTable](UUID.randomUUID().toString)
    val views = hInstance.getMap[TableIdentifierNormalized, String](UUID.randomUUID().toString)

    new HazelcastCatalog(tables, views)(xdContext.conf)
  }
}