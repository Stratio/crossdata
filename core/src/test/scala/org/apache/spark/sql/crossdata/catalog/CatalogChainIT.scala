package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.crossdata.catalog.temporary.HashmapCatalog
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.StringType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CatalogChainIT extends SharedXDContextTest {

  "A CatalogChain" should "allow to save a relation into previous temporary catalogs which does not contain the relation" in {

    val TableId = TableIdentifier("mytable")
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

    secondfallbackCatalog.saveTable(TableId, localRelation)

    secondfallbackCatalog.relation(TableId) should contain (localRelation)
    prioritaryHashMapCatalog.relation(TableId) shouldBe None
    firstFallbackCatalog.relation(TableId) shouldBe None

    // Once we lookup the relation, it should be stored in prioritary and firstFallback catalogs
    catalogChain.lookupRelation(TableId)

    prioritaryHashMapCatalog.relation(TableId) should contain (localRelation)
    firstFallbackCatalog.relation(TableId) should contain (localRelation)

  }
}
