package com.stratio.crossdata.connector.elasticsearch

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchConnectionUtilsIT extends ElasticWithSharedContext with ElasticSearchDefaultConstants {
  
  "ElasticSearchConnectionUtils" should "build a native ES Connection" in {
    assumeEnvironmentIsUpAndRunning
    val options: Map[String, String] = Map(
      "es.nodes" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName"
    )

    //Experimentation
    ElasticSearchConnectionUtils.withClientDo(options){ client =>

      //Expectations
      client should not be (null)
    }

  }


  it should "list ElasticSearch Tables in One Index" in {
    assumeEnvironmentIsUpAndRunning

    val options: Map[String, String] = Map(
      "es.nodes" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName",
      "es.index" -> s"$Index"
    )

    //Experimentation
    val types = ElasticSearchConnectionUtils.listTypes(options)

    //Expectations
    types should not be null
    types.size should be (1)
    types.head.schema shouldBe empty

  }

  it should "list All ElasticSearch Tables" in {
    assumeEnvironmentIsUpAndRunning

    val options: Map[String, String] = Map(
      "es.nodes" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName"
    )

    ElasticSearchConnectionUtils.withClientDo(options){ client =>
      createIndex(client,"index_test",  typeMapping())
      try {
        //Experimentation
        val types = ElasticSearchConnectionUtils.listTypes(options)

        //Expectations
        types should not be (null)
        types.size should be > 1
      } finally {
        cleanTestData(client, "index_test")
      }
    }

  }


  it should "list tables on an empty index" in {
    assumeEnvironmentIsUpAndRunning

    val options: Map[String, String] = Map(
      "es.nodes" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName",
      "es.index" -> "empty_index"
    )

    ElasticSearchConnectionUtils.withClientDo(options){ client =>
      createIndex(client,"empty_index",  null)

      try {
        //Experimentation
        val types = ElasticSearchConnectionUtils.listTypes(options)

        //Expectations
        types should not be null
        types.size should be (0)
      } finally {
        cleanTestData(client, "empty_index")
      }
    }

  }

}
