/**
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



import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.typesafe.config.ConfigFactory
import org.apache.spark.Logging
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import org.elasticsearch.common.joda.time.DateTime
import org.elasticsearch.common.settings.ImmutableSettings
import org.scalatest.Suite

import scala.util.Try


trait ElasticWithSharedContext extends SharedXDContextWithDataTest with ElasticSearchDefaultConstants with Logging {
  this: Suite =>

  override type ClientParams = ElasticClient
  override val provider: String = SourceProvider

  override protected def saveTestData: Unit = for (a <- 1 to 10) {
    client.get.execute {
      index into Index / Type fields(
        "id" -> a,
        "age" -> (10 + a),
        "description" -> s"A ${a}description about the Name$a",
        "enrolled" -> (if (a % 2 == 0) true else null),
        "name" -> s"Name $a",
        "birthday" -> DateTime.parse((1980+a)+"-01-01T10:00:00-00:00").toDate)
      }.await
    }

  override protected def terminateClient: Unit = client.get.close()

  override protected def cleanTestData: Unit = cleanTestData(client.get, Index)

  //Template steps: Override them
  override protected def prepareClient: Option[ClientParams] = Try {
    logInfo(s"Connection to elastic search, ElasticHost: $ElasticHost, ElasticNativePort:$ElasticNativePort, ElasticClusterName $ElasticClusterName")
    val settings = ImmutableSettings.settingsBuilder().put("cluster.name", ElasticClusterName).build()
    val elasticClient = ElasticClient.remote(settings, ElasticHost, ElasticNativePort)
    createIndex(elasticClient, Index, typeMapping())
    elasticClient
  } toOption

  override val sparkRegisterTableSQL: Seq[SparkTable] = s"""|CREATE TEMPORARY TABLE $Type
                                                            |(
                                                            |  id INT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN, birthday DATE)
                                                            |  USING $SourceProvider
                                                            |  OPTIONS (
                                                            |  resource '$Index/$Type',
                                                            |  es.nodes '$ElasticHost',
                                                            |  es.port '$ElasticRestPort',
                                                            |  es.nativePort '$ElasticNativePort',
                                                            |  es.cluster '$ElasticClusterName'
                                                            |)""".stripMargin.replaceAll("\n", " ")::Nil

  override val runningError: String = "ElasticSearch and Spark must be up and running"

  def createIndex(elasticClient: ElasticClient, indexName:String, mappings:MappingDefinition): Unit ={
    val command = Option(mappings).fold(create index indexName)(create index indexName mappings _)
    elasticClient.execute {command}.await
  }

  def typeMapping(): MappingDefinition ={
    Type as(
      "id" typed IntegerType,
      "age" typed IntegerType,
      "description" typed StringType,
      "enrolled" typed BooleanType,
      "name" typed StringType index NotAnalyzed,
      "birthday" typed DateType
      )
  }

  def cleanTestData(elasticClient: ElasticClient, indexName:String): Unit = {
    elasticClient.execute {
      deleteIndex(indexName)
    }
  }

}


trait ElasticSearchDefaultConstants {
  private lazy val config = ConfigFactory.load()
  val Index = "highschool"
  val Type = "students"
  val ElasticHost: String = Try(config.getStringList("elasticsearch.hosts")).map(_.get(0)).getOrElse("127.0.0.1")
  val ElasticRestPort = 9200
  val ElasticNativePort = 9300
  val SourceProvider = "com.stratio.crossdata.connector.elasticsearch"
  val ElasticClusterName: String = Try(config.getString("elasticsearch.cluster")).getOrElse("esCluster")

}