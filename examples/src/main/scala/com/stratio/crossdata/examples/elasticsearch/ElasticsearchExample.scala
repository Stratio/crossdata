/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.examples.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.mappings.FieldType._
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.common.settings.Settings

sealed trait ElasticsearchDefaultConstants {
  val Index = "highschool"
  val Type = "students"
  val ElasticHost = "127.0.0.1"
  val ElasticRestPort = 9200
  val ElasticNativePort = 9300
  val SourceProvider = "elasticsearch"
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
          |es.nodes '$ElasticHost',
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
    val settings = Settings.settingsBuilder().put("cluster.name", s"$ElasticClusterName").build
    ElasticClient.transport(settings, ElasticsearchClientUri(ElasticHost, ElasticNativePort))
  }

  private def buildTable(client: ElasticClient): Unit = {
    client.execute {
      create index s"$Index" mappings (
        mapping(s"$Type") fields(
          "id" typed IntegerType,
          "age" typed IntegerType,
          "description" typed StringType,
          "enrolled" typed BooleanType,
          "name" typed StringType
          )
        )
    }.await

    for (a <- 1 to 10) {
      client.execute {
        index into s"$Index" / s"$Type" fields(
          "id" -> a,
          "age" -> (10 + a),
          "description" -> s"Comment $a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a"
          )
      }.await
    }

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

