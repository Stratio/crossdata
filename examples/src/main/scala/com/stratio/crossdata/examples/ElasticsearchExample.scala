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
package com.stratio.crossdata.examples

import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.common.settings.ImmutableSettings

sealed trait ElasticsearchDefaultConstants {
  val Index = "highschool"
  val Type = "students"
  val ElasticHost = "127.0.0.1"
  val ElasticRestPort = 9200
  val ElasticNativePort = 9300
  val SourceProvider = "com.stratio.crossdata.connector.elasticsearch"
  val ElasticClusterName = "esCluster"
}

object ElasticsearchExample extends App with ElasticsearchDefaultConstants {

  val client = prepareEnvironment()

  withCrossdataContext { xdContext =>

    xdContext.sql(
      s"""|CREATE TEMPORARY TABLE $Type
          |(id INT, age INT, description STRING, enrolled BOOLEAN, name STRING)
          |USING $SourceProvider
          |OPTIONS (
          |resource '$Index/$Type',
          |es.node '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
         """.stripMargin.replaceAll("\n", " "))

    // Native
    xdContext.sql(s"SELECT description as b FROM $Type WHERE id = 1").show(5)
    xdContext.sql(s"SELECT * FROM $Type").show(5)

    // Spark
    xdContext.sql(s"SELECT name as b FROM $Type WHERE age > 1 limit 7").show(5)
    xdContext.sql(s"SELECT description as b FROM $Type WHERE description = 'Comment 4'").show(5)
    xdContext.sql(s"SELECT description as b FROM $Type WHERE description = 'Comment 2' AND id = 2").show(5)

  }

  cleanEnvironment(client)

  private def withCrossdataContext(commands: XDContext => Unit) = {

    val sparkConf = new SparkConf().
      setAppName("ElasticsearchExample").
      setMaster("local[4]")

    val sc = new SparkContext(sparkConf)
    try {
      val xdContext = new XDContext(sc)
      commands(xdContext)
    } finally {
      sc.stop()
    }

  }

  def prepareEnvironment(): ElasticClient = {
    val client = createClient()
    buildTable(client)
    client
  }

  def cleanEnvironment(client: ElasticClient) = {
    cleanData(client)
    closeClient(client)
  }

  private def createClient(): ElasticClient = {
    val settings = ImmutableSettings.settingsBuilder().put("cluster.name", s"$ElasticClusterName").build
    ElasticClient.remote(settings, ElasticHost, ElasticNativePort)
  }

  private def buildTable(client: ElasticClient): Unit = {

    client.execute {
      create index s"$Index" mappings (
        s"$Type" as (
          "id" typed IntegerType,
          "age" typed IntegerType,
          "description" typed StringType,
          "enrolled" typed BooleanType,
          "name" typed StringType
          )
        )
    }.await

    client.execute {
      search in s"$Index" / s"$Type"
    }.await

    for (a <- 1 to 10) {
      client.execute {
        index into s"$Index" / s"$Type" fields (
          "id" -> a,
          "age" -> (10 + a),
          "description" -> s"Comment $a",
          "enrolled" -> (a % 2 ==0),
          "name" -> s"Name $a"
        )
      }.await
    }

    Thread.sleep(2000)

    client.execute {
      search in s"$Index" / s"$Type"
    }.await

  }

  private def cleanData(client: ElasticClient): Unit = {
    client.execute {
      deleteIndex(s"$Index")
    }.await
  }

  private def closeClient(client: ElasticClient): Unit = {
    client.client.close
  }

}

