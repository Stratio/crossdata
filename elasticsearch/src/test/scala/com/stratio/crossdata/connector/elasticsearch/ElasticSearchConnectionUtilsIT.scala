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


  "ElasticSearchConnectionUtils "
  it should "List ElasticSearch Tables" in {
    assumeEnvironmentIsUpAndRunning

    val options: Map[String, String] = Map(
      "es.node" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName",
      "es.index" -> s"$Index"
    )

    //Experimentation
    val client = ElasticSearchConnectionUtils.listTypes(options)

    //Expectations
    client should not be (null)
  }
}
