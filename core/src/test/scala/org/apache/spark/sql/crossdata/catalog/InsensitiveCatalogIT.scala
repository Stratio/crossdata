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

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, OneRowRelation}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.catalog.persistent.DerbyCatalogIT
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InsensitiveCatalogIT extends DerbyCatalogIT {

  override val coreConfig: Option[Config] = Some(
      ConfigFactory
        .empty()
        .withValue(s"config.${SQLConf.CASE_SENSITIVE.key}", ConfigValueFactory.fromAnyRef(false)))

  it should s"persist a table and retrieve it changing some letters to upper case in $catalogName" in {

    val tableNameOriginal = "TableNameInsensitive"
    import XDCatalogCommon._
    val tableIdentifier = TableIdentifier(tableNameOriginal, Some(Database))
    val tableNormalized = tableIdentifier.normalize
    val crossdataTable = CrossdataTable(tableNormalized,
                                        Some(Columns),
                                        SourceDatasource,
                                        Array[String](Field1Name),
                                        OptsJSON)

    xdContext.catalog.persistTable(crossdataTable, OneRowRelation)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true

    val tableNameOriginal2 = "tablenameinsensitive"
    val tableIdentifier2 = TableIdentifier(tableNameOriginal2, Some(Database))
    xdContext.catalog.tableExists(tableIdentifier2) shouldBe true

    val tableNameOriginal3 = "TABLENAMEINSENSITIVE"
    val tableIdentifier3 = TableIdentifier(tableNameOriginal2, Some(Database))
    xdContext.catalog.tableExists(tableIdentifier3) shouldBe true

  }

}
