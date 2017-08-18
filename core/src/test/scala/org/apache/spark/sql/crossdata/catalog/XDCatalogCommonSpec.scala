/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.{SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class XDCatalogCommonSpec extends BaseXDTest {

  "A Rich table identifier" should "normalize a table identifier based on catalyst conf" in {
    val tableName = "myTable"
    val dbName = "myDB"
    val tableIdentifier = TableIdentifier(tableName, Some(dbName))
    import XDCatalogCommon._

    tableIdentifier.normalize(new SimpleCatalystConf(true)) shouldBe TableIdentifierNormalized(tableName, Some(dbName))

    tableIdentifier.normalize(new SimpleCatalystConf(false)) shouldBe TableIdentifierNormalized(tableName.toLowerCase, Some(dbName.toLowerCase))

  }
}
