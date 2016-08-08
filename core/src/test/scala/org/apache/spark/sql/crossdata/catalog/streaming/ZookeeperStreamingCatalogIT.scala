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
package org.apache.spark.sql.crossdata.catalog.streaming

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.crossdata.catalog.CatalogConstants
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class ZookeeperStreamingCatalogIT
    extends SharedXDContextTest
    with CatalogConstants
    with ZookeeperStreamingDefaultTestConstants {

  override val coreConfig: Option[Config] = {
    val zkResourceConfig = Try(
        ConfigFactory.load("core-reference.conf").getConfig(CoreConfig.ParentConfigName)).toOption

    ZookeeperConnection.fold(zkResourceConfig) { connectionString =>
      zkResourceConfig.flatMap(
          resourceConfig =>
            Option(resourceConfig.withValue(ZookeeperStreamingConnectionKey,
                                            ConfigValueFactory.fromAnyRef(connectionString))))
    }
  }

  s"ZookeeperStreamingCatalogSpec" should "persist ephemeral tables" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.existsEphemeralTable(EphemeralTableName) shouldBe false
    streamCatalog.createEphemeralTable(EphemeralTable) shouldBe Right(EphemeralTable)
    streamCatalog.existsEphemeralTable(EphemeralTableName) shouldBe true
    streamCatalog.getEphemeralTable(EphemeralTableName) shouldBe Some(EphemeralTable)

    streamCatalog.dropEphemeralTable(EphemeralTableName)
    streamCatalog.existsEphemeralTable(EphemeralTableName) shouldBe false
  }

  it should "create the ephemeral table status when creating the table" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.createEphemeralTable(EphemeralTable) shouldBe Right(EphemeralTable)
    streamCatalog.existsEphemeralTable(EphemeralTableName) shouldBe true
    streamCatalog.getEphemeralStatus(EphemeralTableName).isDefined shouldBe true

    streamCatalog.dropEphemeralTable(EphemeralTableName)
  }

  it should "fail when persisting an ephemeral table twice" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.existsEphemeralTable(EphemeralTableName) shouldBe false
    streamCatalog.createEphemeralTable(EphemeralTable)
    streamCatalog.createEphemeralTable(EphemeralTable).isLeft shouldBe true

    streamCatalog.dropEphemeralTable(EphemeralTableName)
  }

  it should "not fail when trying to get a table which does not exist" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.getEphemeralTable("stronker").isEmpty shouldBe true
  }

  it should "fail when dropping a ephemeral table which does not exist" in {

    val streamCatalog = xdContext.catalog

    an[Exception] should be thrownBy streamCatalog.dropEphemeralTable("stronker")
  }

  it should "not fail when droppingAll ephemeral tables even though the catalog is empty" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.createEphemeralTable(EphemeralTable)
    streamCatalog.dropAllEphemeralTables()
    streamCatalog.dropAllEphemeralTables()
  }

  it should "fail when dropping an ephemeral table which status is started" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.createEphemeralTable(EphemeralTable)
    streamCatalog.updateEphemeralStatus(
        EphemeralTableName,
        EphemeralStatusModel(EphemeralTableName, EphemeralExecutionStatus.Started)
    )
    the[Exception] thrownBy {
      streamCatalog.dropEphemeralTable(EphemeralTableName)
    } should have message "The ephemeral is running. The process should be stopped first using 'Stop <tableIdentifier>'"

    streamCatalog.updateEphemeralStatus(
        EphemeralTableName,
        EphemeralStatusModel(EphemeralTableName, EphemeralExecutionStatus.Stopped)
    )
    streamCatalog.dropEphemeralTable(EphemeralTableName)
  }

  it should "manage ephemeral table status" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.createEphemeralTable(EphemeralTable)
    streamCatalog.getEphemeralStatus(EphemeralTableName).isDefined shouldBe true
    streamCatalog
      .getEphemeralStatus(EphemeralTableName)
      .get
      .status shouldBe EphemeralExecutionStatus.NotStarted

    streamCatalog.dropEphemeralTable(EphemeralTableName)
    streamCatalog.getEphemeralStatus(EphemeralTableName).isDefined shouldBe false
  }

  it should "create, get, and drop queries" in {

    val streamCatalog = xdContext.catalog

    streamCatalog.existsEphemeralQuery(QueryAlias) shouldBe false
    streamCatalog.createEphemeralQuery(EphemeralQuery) shouldBe Right(EphemeralQuery)
    streamCatalog.existsEphemeralQuery(QueryAlias) shouldBe true
    streamCatalog.getEphemeralQuery(QueryAlias) shouldBe Some(EphemeralQuery)

    streamCatalog.dropEphemeralQuery(QueryAlias)
    streamCatalog.existsEphemeralQuery(QueryAlias) shouldBe false

  }

  /**
    * Stop the underlying [[org.apache.spark.SparkContext]], if any.
    */
  protected override def afterAll(): Unit = {
    xdContext.catalog.dropAllEphemeralTables()
    super.afterAll()
  }
}

sealed trait ZookeeperStreamingDefaultTestConstants {

  val ZookeeperStreamingConnectionKey = "streaming.catalog.zookeeper.connectionString"
  val ZookeeperConnection: Option[String] = Try(
      ConfigFactory.load().getString(ZookeeperStreamingConnectionKey)).toOption

  // Ephemeral table
  val EphemeralTableName = "epheTable"
  val KafkaOptions = KafkaOptionsModel(
      ConnectionHostModel(Seq(ConnectionModel("zkHost", 2020)),
                          Seq(ConnectionModel("kafkaHost", 2125))),
      Seq(TopicModel("topic", 1)),
      "groupId",
      None,
      Map("key" -> "value"),
      "MEMORY_AND_DISK")
  val EphemeralTableOptions = EphemeralOptionsModel(KafkaOptions, 5)
  val EphemeralTable = EphemeralTableModel(EphemeralTableName, EphemeralTableOptions)

  //Queries
  val QueryAlias = "qalias"
  val Sql = "select * from epheTable"
  val EphemeralQuery = EphemeralQueryModel(EphemeralTableName, Sql, QueryAlias, 5, Map.empty)
}
