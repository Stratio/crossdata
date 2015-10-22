package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import org.apache.spark.Logging
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.elasticsearch.common.settings.ImmutableSettings
import org.scalatest.Suite


trait ElasticWithSharedContext extends SharedXDContextTest with ElasticSearchDefaultConstants with Logging {
  this: Suite =>

  var elasticClient: Option[ElasticClient] = None
  var isEnvironmentReady = false

  override protected def beforeAll() = {
    super.beforeAll()

    try {
      elasticClient = Some(prepareEnvironment())

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE $Type
            |(_id String, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)
            |USING $SourceProvider
            |OPTIONS (
            |resource '$Index/$Type',
            |es.node '$ElasticHost',
            |es.port '$ElasticRestPort',
            |es.nativePort '$ElasticNativePort',
            |es.cluster '$ElasticClusterName'
            |)
         """.stripMargin.replaceAll("\n", " "))

    } catch {
      case e: Throwable => logError(e.getMessage)
    }

    isEnvironmentReady = elasticClient.isDefined
  }

  override protected def afterAll() = {
    super.afterAll()
    elasticClient.foreach(cleanEnvironment)
  }


  def prepareEnvironment(): ElasticClient = {
    val settings = ImmutableSettings.settingsBuilder().put("cluster.name", ElasticClusterName).build()
    val elasticClient = ElasticClient.remote(settings, ElasticHost, ElasticNativePort)

    elasticClient.execute {
      create index Index mappings (
        Type as(
          "age" typed IntegerType,
          "description" typed StringType,
          "enrolled" typed BooleanType,
          "name" typed StringType
          ))
    }.await

    saveTestData(elasticClient)
    elasticClient
  }

  def cleanEnvironment(elasticClient: ElasticClient) = {
    cleanTestData(elasticClient)
    elasticClient.close()
  }

  private def saveTestData(elasticClient: ElasticClient): Unit = {

    for (a <- 1 to 10) {
      elasticClient.execute {
        index into Index / Type fields(
          "age" -> (10 + a),
          "description" -> s"description $a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a")
      }.await

    }
  }

  private def cleanTestData(elasticClient: ElasticClient): Unit = {
    elasticClient.execute {
      deleteIndex(Index)
    }
  }

  lazy val assumeEnvironmentIsUpAndRunning = {
    assume(isEnvironmentReady, "ElasticSearch and Spark must be up and running")
  }
}


sealed trait ElasticSearchDefaultConstants {
  val Index = "highschool"
  val Type = "students"
  val ElasticHost = "127.0.0.1"
  val ElasticRestPort = 9200
  val ElasticNativePort = 9300
  val SourceProvider = "com.stratio.crossdata.connector.elasticsearch"
  val ElasticClusterName = "esCluster"
}