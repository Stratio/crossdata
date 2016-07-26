/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    tableIdentifier
      .normalize(new SimpleCatalystConf(true)) shouldBe TableIdentifierNormalized(
        tableName,
        Some(dbName))

    tableIdentifier
      .normalize(new SimpleCatalystConf(false)) shouldBe TableIdentifierNormalized(
        tableName.toLowerCase,
        Some(dbName.toLowerCase))

  }
}
