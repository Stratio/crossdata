/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.catalog.temporary.HashmapCatalog
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.StringType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CatalogChainIT extends SharedXDContextTest {

  "A CatalogChain" should "allow to save a relation into previous temporary catalogs which does not contain the relation" in {

    val TableId = TableIdentifier("mytable")
    import XDCatalogCommon._
    val TableNormalized = TableId.normalize(_xdContext.catalog.conf)
    implicit val _ = _xdContext

    val prioritaryHashMapCatalog = new HashmapCatalog(xdContext.conf)
    val firstFallbackCatalog = new HashmapCatalog(xdContext.conf)
    val secondfallbackCatalog = new HashmapCatalog(xdContext.conf)

    val catalogChain = CatalogChain(prioritaryHashMapCatalog, firstFallbackCatalog, secondfallbackCatalog)(_xdContext)

    val localRelation: LocalRelation = {
      val attributes = AttributeReference("mystring", StringType)() :: Nil
      val rows = Row("chamartin -> aravaca") :: Nil
      LocalRelation.fromExternalRows(attributes, rows)
    }

    secondfallbackCatalog.saveTable(TableNormalized, localRelation)

    secondfallbackCatalog.relation(TableNormalized) should contain (localRelation)
    prioritaryHashMapCatalog.relation(TableNormalized) shouldBe None
    firstFallbackCatalog.relation(TableNormalized) shouldBe None

    // Once we lookup the relation, it should be stored in prioritary and firstFallback catalogs
    catalogChain.lookupRelation(TableId)

    prioritaryHashMapCatalog.relation(TableNormalized) should contain (localRelation)
    firstFallbackCatalog.relation(TableNormalized) should contain (localRelation)

  }
}
