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

class ElasticSearchImportTablesIT extends ElasticWithSharedContext {


  // IMPORT OPERATIONS

  it should "import all tables from a keyspace" in {
    assumeEnvironmentIsUpAndRunning
    def tableCountInHighschool: Long = sql("SHOW TABLES").count
    val initialLength = tableCountInHighschool

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
          |OPTIONS (
          |es.node '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName',
          |es.index '$Index'
          |)
      """.stripMargin

    //Experimentation
    sql(importQuery)

    //Expectations
    tableCountInHighschool should be > initialLength
  }

  it should "infer schema after import all tables from a keyspace" in {
    assumeEnvironmentIsUpAndRunning

    val importQuery =
      s"""
         |IMPORT TABLES
         |USING $SourceProvider
          |OPTIONS (
          |es.node '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName',
          |es.index '$Index'
          |)
      """.stripMargin

    //Experimentation
    sql(importQuery)

    //Expectations
    ctx.table(s"$Index.$Type").schema should have length 6
  }

  it should "infer schema after import all tables from a Cluster" in {
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
      val importQuery =
        s"""
           |IMPORT TABLES
           |USING $SourceProvider
            |OPTIONS (
            |es.node '$ElasticHost',
            |es.port '$ElasticRestPort',
            |es.nativePort '$ElasticNativePort',
            |es.cluster '$ElasticClusterName'
            |)
      """.stripMargin

      //Experimentation:
      sql(importQuery)

      //Expectations
      sql("SHOW TABLES").count should be > 1l

    }finally {
      cleanTestData(client, "index_test")
    }
  }
}
