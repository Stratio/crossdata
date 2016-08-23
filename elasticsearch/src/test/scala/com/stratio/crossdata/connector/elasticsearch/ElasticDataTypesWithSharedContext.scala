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
package com.stratio.crossdata.connector.elasticsearch

import java.util.{GregorianCalendar, UUID}

import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.mappings.{MappingDefinition, TypedFieldDefinition}
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import org.elasticsearch.common.settings.Settings
import org.scalatest.Suite
import org.joda.time.DateTime
import scala.util.Try


trait ElasticDataTypesWithSharedContext extends SharedXDContextWithDataTest with ElasticSearchDataTypesDefaultConstants with SparkLoggerComponent {
  this: Suite =>

  override type ClientParams = ElasticClient
  override val provider: String = SourceProvider

  override val defaultOptions = Map(
    "resource" -> s"$Index/$Type",
    "es.nodes" -> s"$ElasticHost",
    "es.port" -> s"$ElasticRestPort",
    "es.nativePort" -> s"$ElasticNativePort",
    "es.cluster" -> s"$ElasticClusterName"
  )

  case class ESColumnMetadata(fieldName: String, sparkSqlType: String, elasticType: TypedFieldDefinition, data: () => Any, typeValidation: Any => Unit)

  val dataTest = Seq(
    ESColumnMetadata("id", "INT", "id" typed IntegerType, () => 1, _ shouldBe a[java.lang.Integer]),
    ESColumnMetadata("age", "LONG", "age" typed LongType, () => 1, _ shouldBe a[java.lang.Long]),
    ESColumnMetadata("description", "STRING", "description" typed StringType, () => "1", _ shouldBe a[java.lang.String]),
    ESColumnMetadata("name", "STRING", "name" typed StringType index NotAnalyzed, () => "1", _ shouldBe a[java.lang.String]),
    ESColumnMetadata("enrolled", "BOOLEAN", "enrolled" typed BooleanType, () => false, _ shouldBe a[java.lang.Boolean]),
    ESColumnMetadata("birthday", "DATE", "birthday" typed DateType, () => DateTime.parse(1980 + "-01-01T10:00:00-00:00").toDate, _ shouldBe a [java.sql.Date]),
    ESColumnMetadata("salary", "DOUBLE", "salary" typed DoubleType, () => 0.15, _ shouldBe a[java.lang.Double]),
    ESColumnMetadata("timecol", "TIMESTAMP", "timecol" typed DateType, () => new java.sql.Timestamp(new GregorianCalendar(1970, 0, 1, 0, 0, 0).getTimeInMillis), _ shouldBe a[java.sql.Timestamp])
    //ESColumnMetadata("float", "FLOAT", "float" typed FloatType, () => 0.15, _ shouldBe a[java.lang.Float]), // TODO float not supported natively
    //ESColumnMetadata("binary", "BINARY", "binary" typed BinaryType, () => Array(Byte.MaxValue, Byte.MinValue), x => x.isInstanceOf[Array[Byte]] shouldBe true) // TODO native and spark ko
    //ESColumnMetadata("tinyint", "TINYINT", "tinyint" typed ByteType, () => Byte.MinValue, _ shouldBe a[java.lang.Byte]), // TODO native ko
    //ESColumnMetadata("smallint", "SMALLINT", "smallint" typed ShortType, () => Short.MaxValue, _ shouldBe a[java.lang.Short]) // TODO native ko
    // TODO study access to multi-field (example name with multiple fields: raw (not analyzed) and name spanish (analyzed with specific analyzer) ?
    //ESColumnMetadata("subdocument", "STRUCT<field1: INT>", "subdocument"  inner ("field1" typed IntegerType), () =>  Map( "field1" -> 15), _ shouldBe a [Row]) // TODO native ko
    // TODO study nested type => to enable flattening
    // TODO complex structures => [[SharedXDContextTypesTest]]

  )

  override protected def saveTestData: Unit = for (a <- 1 to 1) {
    client.get.execute {
      index into Index / Type fields( dataTest.map( colMetadata => (colMetadata.fieldName,colMetadata.data())): _*)
    }.await
    client.get.execute {
      flush index Index
    }.await
  }

  override protected def terminateClient: Unit = client.get.close()

  override protected def cleanTestData: Unit = cleanTestData(client.get, Index)

  //Template steps: Override them
  override protected def prepareClient: Option[ClientParams] = Try {
    logInfo(s"Connection to elastic search, ElasticHost: $ElasticHost, ElasticNativePort:$ElasticNativePort, ElasticClusterName $ElasticClusterName")
    val settings = Settings.settingsBuilder().put("cluster.name", ElasticClusterName).build()
    val elasticClient = ElasticClient.transport(settings, ElasticsearchClientUri(ElasticHost, ElasticNativePort))
    createIndex(elasticClient, Index, typeMapping())
    elasticClient
  } toOption


  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(
      s"CREATE TEMPORARY TABLE $Type ( ${fieldAndSparkType() mkString "," })"
    )


  override val runningError: String = "ElasticSearch and Spark must be up and running"

  def createIndex(elasticClient: ElasticClient, indexName:String, mappings:MappingDefinition): Unit ={
    val command = Option(mappings).fold(create index indexName)(create index indexName mappings _)
    elasticClient.execute {command}.await
  }

  def fieldAndSparkType(): Seq[String] = {
    Seq( dataTest.map(colMetadata => s"${colMetadata.fieldName}  ${colMetadata.sparkSqlType}") : _*)
  }

  def typeMapping(): MappingDefinition ={
    mapping(Type) fields (
      dataTest.map(_.elasticType) :_*
      )
  }

  def cleanTestData(elasticClient: ElasticClient, indexName:String): Unit = {
    elasticClient.execute {
      deleteIndex(indexName)
    }
  }

}


trait ElasticSearchDataTypesDefaultConstants extends ElasticSearchDefaultConstants{
  private lazy val config = ConfigFactory.load()
  override val Index = s"idxname${UUID.randomUUID.toString.replaceAll("-", "")}"
  override val Type = s"typename${UUID.randomUUID.toString.replaceAll("-", "")}"

}