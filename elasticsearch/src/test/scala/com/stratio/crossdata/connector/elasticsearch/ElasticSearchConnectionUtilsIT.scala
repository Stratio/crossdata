package com.stratio.crossdata.connector.elasticsearch

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchConnectionUtilsIT extends ElasticWithSharedContext with ElasticSearchDefaultConstants {

  "ElasticSearchConnectionUtils "
  it should "Build a native ES Connection" in {

    val options: Map[String, String] = Map(
      "es.node" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName"
    )

    //Experimentation
    val client = ElasticSearchConnectionUtils.buildClient(options)

    //Expectations
    client should not be (null)
  }

  it should "List ElasticSearch Tables in One Index" in {
    assumeEnvironmentIsUpAndRunning

    val options: Map[String, String] = Map(
      "es.node" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName",
      "es.index" -> s"$Index"
    )

    //Experimentation
    val types = ElasticSearchConnectionUtils.listTypes(options)

    //Expectations
    types should not be (null)
    types.size should be (1)
    types(0).schema.get.size should be (6)

  }

  it should "List All ElasticSearch Tables" in {
    assumeEnvironmentIsUpAndRunning

    val options: Map[String, String] = Map(
      "es.node" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName"
    )

    val client = ElasticSearchConnectionUtils.buildClient(options)
    createIndex(client,"index_test")
    try {
      //Experimentation
      val types = ElasticSearchConnectionUtils.listTypes(options)

      //Expectations
      types should not be (null)
      types.size should be > 1

    }finally {
      cleanTestData(client, "index_test")
    }
  }
}
