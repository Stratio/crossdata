/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.driver

import com.stratio.crossdata.common.utils.Constants
import com.stratio.crossdata.driver.mocks.DriverConnectionFake
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.immutable.ListMap

class DriverConnectionAPITest extends FunSuite with BeforeAndAfter{


  val INMEMORY_CLUSTER: String = "InMemoryCluster"
  val INMEMORY_DATASTORE: String = "InMemoryDatastore"
  val INMEMORY_CONNECTOR = "InmemoryConnector"
  val NUMBER_OF_ROWS: Int = 100
  val USER_NAME: String = "stratio"
  val PASSWORD: String = "stratio"
  val CATALOG_NAME: String = "customers"
  val TABLE_NAME: String = "products"
  val PARTITION_KEY: List[String] = List("id")
  val CLUSTER_KEY: List[String] = List("email")
  val COLUMN_TYPE: Map[String, String] = ListMap(
    "id" -> "INT",
    "serial" -> "INT",
    "name" -> "TEXT",
    "rating" -> "DOUBLE",
    "email" -> "TEXT"
  )

  val COLUMN_VALUE: Map[String, Any] = ListMap(
    "id" -> 1,
    "email" -> "ro@mail.com"
  )

  val OPTIONS: Map[String, Any] = Map("strkey" -> "value", "intkey" -> 1)

  val xdConnection = new DriverConnectionFake

  test("DriverAPI doesn't build create catalog statement properly"){
      xdConnection.createCatalog(CATALOG_NAME, true, OPTIONS )
      assert(xdConnection.assertEquals("CREATE CATALOG IF NOT EXISTS "+CATALOG_NAME+" WITH {\"strkey\" : \"value\", \"intkey\" : 1};"))
  }

  test("DriverAPI doesn't build attach cluster statement properly"){
    xdConnection.attachCluster(INMEMORY_CLUSTER, INMEMORY_DATASTORE, true, OPTIONS )
    assert(xdConnection.assertEquals("ATTACH CLUSTER IF NOT EXISTS "+INMEMORY_CLUSTER+" ON DATASTORE "+INMEMORY_DATASTORE+" WITH OPTIONS {\"strkey\" : \"value\", \"intkey\" : 1};"))
  }

  test("DriverAPI doesn't build attach connector statement properly"){
    xdConnection.attachConnector(INMEMORY_CONNECTOR, INMEMORY_CLUSTER, OPTIONS, 100, 1)
    assert(xdConnection.assertEquals("ATTACH CONNECTOR "+INMEMORY_CONNECTOR+" TO "+INMEMORY_CLUSTER+" WITH OPTIONS {\"strkey\" : \"value\", \"intkey\" : 1} AND PRIORITY = 1 AND PAGINATION = 100;"))
  }

  test("DriverAPI doesn't build attach connector without pagination and priority properly"){
    xdConnection.attachConnector(INMEMORY_CONNECTOR, INMEMORY_CLUSTER, OPTIONS)
    assert(xdConnection.assertEquals("ATTACH CONNECTOR "+INMEMORY_CONNECTOR+" TO "+INMEMORY_CLUSTER+" WITH OPTIONS {\"strkey\" : \"value\", \"intkey\" : 1} AND PRIORITY = "+Constants.DEFAULT_PRIORITY+" AND PAGINATION = "+Constants.DEFAULT_PAGINATION+";"))
  }

  test("DriverAPI doesn't build create catalog properly"){
    xdConnection.createCatalog(CATALOG_NAME, true, OPTIONS)
    assert(xdConnection.assertEquals("CREATE CATALOG IF NOT EXISTS "+CATALOG_NAME+" WITH {\"strkey\" : \"value\", \"intkey\" : 1};"))
  }

  test("DriverAPI doesn't build create table properly"){
    xdConnection.createTable(CATALOG_NAME, TABLE_NAME, INMEMORY_CLUSTER, COLUMN_TYPE, PARTITION_KEY, CLUSTER_KEY, true, false, OPTIONS)
    assert(xdConnection.assertEquals("CREATE TABLE IF NOT EXISTS "+CATALOG_NAME+"."+TABLE_NAME+" ON CLUSTER "+INMEMORY_CLUSTER+" (id  INT, serial  INT, name  TEXT, rating  DOUBLE, email  TEXT, PRIMARY KEY ( (id) , email ) ) WITH {\"strkey\" : \"value\", \"intkey\" : 1};"))
  }

  test("DriverAPI doesn't build register table statement properly"){
    xdConnection.createTable(CATALOG_NAME, TABLE_NAME, INMEMORY_CLUSTER, COLUMN_TYPE, PARTITION_KEY, CLUSTER_KEY, false, true, OPTIONS)
    assert(xdConnection.assertEquals("REGISTER TABLE "+CATALOG_NAME+"."+TABLE_NAME+" ON CLUSTER "+INMEMORY_CLUSTER+" (id  INT, serial  INT, name  TEXT, rating  DOUBLE, email  TEXT, PRIMARY KEY ( (id) , email ) ) WITH {\"strkey\" : \"value\", \"intkey\" : 1};"))
  }

  test("DriverAPI doesn't build insert into statement properly"){
    xdConnection.insert(CATALOG_NAME, TABLE_NAME, COLUMN_VALUE, true, OPTIONS)
    assert(xdConnection.assertEquals("INSERT INTO "+CATALOG_NAME+"."+TABLE_NAME+" (id, email) VALUES ( 1, 'ro@mail.com') IF NOT EXISTS WITH {\"strkey\" : \"value\", \"intkey\" : 1};"))
  }

  test("DriverAPI doesn't build drop table statement properly"){
    xdConnection.dropTable(CATALOG_NAME, TABLE_NAME, true, false)
    assert(xdConnection.assertEquals("DROP TABLE IF EXISTS "+CATALOG_NAME+"."+TABLE_NAME+";"))
  }

  test("DriverAPI doesn't build unregister table statement properly"){
    xdConnection.dropTable(CATALOG_NAME, TABLE_NAME, false, true)
    assert(xdConnection.assertEquals("UNREGISTER TABLE "+CATALOG_NAME+"."+TABLE_NAME+";"))
  }

  test("DriverAPI doesn't build drop catalog statement properly "){
    xdConnection.dropCatalog(CATALOG_NAME, true)
    assert(xdConnection.assertEquals("DROP CATALOG IF EXISTS "+CATALOG_NAME+";"))
  }

}
