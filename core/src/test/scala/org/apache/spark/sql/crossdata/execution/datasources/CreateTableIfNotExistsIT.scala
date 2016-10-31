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
package org.apache.spark.sql.crossdata.execution.datasources

import java.nio.file.Paths

import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._
import org.apache.spark.sql.crossdata.catalog.persistent.PersistentCatalogWithCache
import org.apache.spark.sql.crossdata.catalog.{CatalogChain, XDCatalog}
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CreateTableIfNotExistsIT extends SharedXDContextTest {


  "CreateTableIfNotExists command" should "allow multiple creations of the table persisting just the first" in {
    _xdContext.sql(s"CREATE TABLE tabNameIfExists USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/foo.json").toURI).toString}')")

    an[Exception] should be thrownBy _xdContext.sql(s"CREATE TABLE tabNameIfExists USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/foo.json").toURI).toString}')")

    _xdContext.sql(s"CREATE TABLE IF NOT EXISTS tabNameIfExists USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/foo.json").toURI).toString}')")
    _xdContext.sql(s"CREATE TABLE IF NOT EXISTS tabNameIfExists USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/foo.json").toURI).toString}')")

    _xdContext.sql("DROP TABLE tabNameIfExists")
  }
  
}
