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

package com.stratio.crossdata.connectors

import java.util
import java.util.UUID

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import com.stratio.crossdata.common.connector.{IConnector, IMetadataEngine, IStorageEngine}
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.metadata._
import com.stratio.crossdata.common.result.{MetadataResult, QueryResult, StorageResult}
import com.stratio.crossdata.common.statements.structures.{Selector, StringSelector}
import com.stratio.crossdata.communication._
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Suite}
import org.testng.Assert.assertNotNull

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class ConnectorWorkerActorAppTest extends TestKit(ActorSystem()) with FunSuite with MockFactory with ImplicitSender {
  this:Suite =>

  lazy val logger = Logger.getLogger(classOf[ConnectorWorkerActorAppTest])
  implicit val timeout = Timeout(3 seconds)

  val connector: String = "MyConnector"

  //val to initialize and don't use null
  val options: Option[java.util.Map[Selector, Selector]] = Some(new util.HashMap[Selector, Selector]())
  val columns: Option[java.util.LinkedHashMap[ColumnName, ColumnMetadata]] = Some(new util.LinkedHashMap[ColumnName, ColumnMetadata]())
  val indexes: Option[java.util.Map[IndexName, IndexMetadata]] = Some(new util.HashMap[IndexName, IndexMetadata]())
  val clusterRef: Option[ClusterName] = Some(new ClusterName("myCluster"))
  val partitionKey: Option[java.util.LinkedList[ColumnName]] = Some(new java.util.LinkedList[ColumnName]())

  test("Basic Connector Mock") {
    val m = mock[IConnector]
    assert(true)
  }

  //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE

  test("Create catalog Message") {

    val catalog_options = new util.HashMap[Selector, Selector]()
    catalog_options.put(new StringSelector("Description"), new StringSelector("Example"))
    val tableMetadata = new TableMetadata(new TableName("catalog", "mytable"), options.get, columns.get, indexes.get, clusterRef.get, partitionKey.get, partitionKey.get)
    val tables = new util.HashMap[TableName, TableMetadata]()
    tables.put(new TableName("catalog", "mytable"), tableMetadata)
    val catalogMetadata = new CatalogMetadata(new CatalogName("catalog"), catalog_options, tables);

    val message = CreateCatalog("query", new ClusterName("cluster"), catalogMetadata)
    assert(message.catalogMetadata.getName.getName == "catalog")

    logger.info("\n\nsending create catalog message  \n\n")
  }

  test("Alter catalog Message") {

    val catalog_options = new util.HashMap[Selector, Selector]()
    catalog_options.put(new StringSelector("Description"), new StringSelector("Example"))
    val tableMetadata = new TableMetadata(new TableName("catalog", "mytable"), options.get, columns.get, indexes.get, clusterRef.get, partitionKey.get, partitionKey.get)
    val tables = new util.HashMap[TableName, TableMetadata]()
    tables.put(new TableName("catalog", "mytable2"), tableMetadata)
    val catalogMetadata = new CatalogMetadata(new CatalogName("catalog"), catalog_options, tables);

    val message = AlterCatalog("query", new ClusterName("cluster"), catalogMetadata)

    assert(message.catalogMetadata.getName.getName == "catalog")

    logger.info(s"${System.lineSeparator}sending alter catalog message${System.lineSeparator}")
  }

  test("create index message") {
    val columns = new util.HashMap[ColumnName, ColumnMetadata]
    val columnMetadata = new ColumnMetadata(new ColumnName("catalog", "table", "column"), null, new ColumnType(DataType.INT))
    columns.put(new ColumnName("catalog", "table", "column"), columnMetadata)
    val indexMetadata = new IndexMetadata(new IndexName("catalog", "table", "myIndex"), columns, IndexType.DEFAULT,
      new util.HashMap[Selector, Selector]())
    val message = CreateIndex("query", new ClusterName("cluster"), indexMetadata)

    assert(message.indexMetadata.getName.getName.equals("myIndex"))
  }

  test("drop catalog") {
    val message = DropCatalog("query", new ClusterName("cluster"), new CatalogName("catalog"))
    assert(message.catalogName.getName.equals("catalog"))
  }

  test("drop table") {
    val message = DropTable("query", new ClusterName("cluster"), new TableName("catalog", "table"))
    assert(message.tableName.getName.equals("table"))
  }

  test("alter table") {
    val alterOptionsProps = new util.HashMap[Selector, Selector]()
    alterOptionsProps.put(new StringSelector("Description"), new StringSelector("Example"))
    val alterOptions = new AlterOptions(AlterOperation.ALTER_OPTIONS, alterOptionsProps, null)
    val message = AlterTable("query", new ClusterName("cluster"), new TableName("catalog", "table"), alterOptions)
    assert(message.alterOptions.getProperties.containsKey(new StringSelector("Description")))
  }

  test("create table and catalog") {
    val catalog_options = new util.HashMap[Selector, Selector]()
    catalog_options.put(new StringSelector("Description"), new StringSelector("Example"))
    val tableMetadata = new TableMetadata(new TableName("catalog", "mytable"), options.get, columns.get, indexes.get, clusterRef.get, partitionKey.get, partitionKey.get)
    val tables = new util.HashMap[TableName, TableMetadata]()
    tables.put(new TableName("catalog", "mytable"), tableMetadata)
    val catalogMetadata = new CatalogMetadata(new CatalogName("catalog"), catalog_options, tables);

    val message = CreateTableAndCatalog("query", new ClusterName("cluster"), catalogMetadata, tableMetadata)
    assert(message.catalogMetadata.getName.getName.equals("catalog"))
    assert(message.tableMetadata.getName.getName.equals("mytable"))
  }


  test("provide metadata") {
    val message = ProvideMetadata("query", new ClusterName("cluster"))
    assert(message.targetCluster.getName.equals("cluster"))
  }

  test("provides catalogs metadata") {
    val message = ProvideCatalogsMetadata("query", new ClusterName("cluster"))
    assert(message.targetCluster.getName.equals("cluster"))
  }

  test("provides catalog metadata") {
    val message = ProvideCatalogMetadata("query", new ClusterName("cluster"), new CatalogName("catalog"))
    assert(message.catalogName.getName.equals("catalog"))
  }

  test("provides table metadata") {
    val message = ProvideTableMetadata("query", new ClusterName("cluster"), new TableName("catalog", "table"))
    assert(message.tableName.getName.equals("table"))
  }

}

