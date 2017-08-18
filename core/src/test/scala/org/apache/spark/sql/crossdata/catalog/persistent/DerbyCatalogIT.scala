package org.apache.spark.sql.crossdata.catalog.persistent

import org.apache.spark.sql.crossdata.catalog.CatalogConstants
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class DerbyCatalogIT extends {
  val catalogName = "Derby"
} with SharedXDContextTest with CatalogConstants with GenericCatalogTests