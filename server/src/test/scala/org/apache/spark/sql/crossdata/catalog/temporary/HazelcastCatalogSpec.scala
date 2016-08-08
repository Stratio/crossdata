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
    val tables =
      hInstance.getMap[TableIdentifierNormalized, CrossdataTable](UUID.randomUUID().toString)
    val views = hInstance.getMap[TableIdentifierNormalized, String](UUID.randomUUID().toString)

    new HazelcastCatalog(tables, views)(xdContext.conf)
  }
}
