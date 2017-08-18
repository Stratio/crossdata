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
