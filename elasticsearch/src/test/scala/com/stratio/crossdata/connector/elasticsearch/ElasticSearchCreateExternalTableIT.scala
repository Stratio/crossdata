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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchCreateExternalTableIT extends ElasticWithSharedContext {

  "The ElasticSearch Connector" should "Create an external table" in {
    assumeEnvironmentIsUpAndRunning

    val createTableQueryString =
      s"""|CREATE EXTERNAL TABLE $Index.newtable (id Integer, title String)
          |USING $SourceProvider
          |OPTIONS (
          |es.resource '$Index/newtable',
          |es.nodes '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
      """.stripMargin.replaceAll("\n", " ")
    //Experimentation
    sql(createTableQueryString).collect()

    //Expectations
    val table = xdContext.table(s"$Index.newtable")
    table should not be null
    table.schema.fieldNames should contain("title")

    client.get.admin.indices
      .prepareTypesExists(Index)
      .setTypes(Type)
      .get
      .isExists shouldBe true
  }

  it should "create an external table without es.resource" in {
    assumeEnvironmentIsUpAndRunning
    val createTableQUeryString =
      s"""|CREATE EXTERNAL TABLE $Index.newtable2 (id Integer, city String)
          |USING $SourceProvider
          |OPTIONS (
          |es.nodes '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
      """.stripMargin.replaceAll("\n", " ")

    sql(createTableQUeryString).collect()

    //Expectations
    val table = xdContext.table(s"$Index.newtable2")
    table should not be null
    table.schema.fieldNames should contain("city")

    client.get.admin.indices
      .prepareTypesExists(Index)
      .setTypes("newtable2")
      .get
      .isExists shouldBe true

  }

}
