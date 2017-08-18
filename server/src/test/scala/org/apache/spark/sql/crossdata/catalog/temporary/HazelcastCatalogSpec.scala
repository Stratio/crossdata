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