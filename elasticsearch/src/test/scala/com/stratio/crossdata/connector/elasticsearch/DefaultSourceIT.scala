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

import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.StructType
import org.elasticsearch.hadoop.cfg.ConfigurationOptions
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class DefaultSourceIT extends ElasticWithSharedContext with ElasticSearchDefaultConstants with MockitoSugar {


  "A DefaultSource " should "return the list of Tables" in {
    //Fixture
    assumeEnvironmentIsUpAndRunning
    val defaultDatasource = new DefaultSource()
    val sqlContext = mock[SQLContext]

    val options: Map[String, String] = Map(
      "es.node" -> s"$ElasticHost",
      "es.port" -> s"$ElasticRestPort",
      "es.nativePort" -> s"$ElasticNativePort",
      "es.cluster" -> s"$ElasticClusterName",
      "es.index" -> s"$Index"
    )

    //Experimentation
    val result = defaultDatasource.listTables(sqlContext, options)

    //Expectations
    result should not be null
  }
}
