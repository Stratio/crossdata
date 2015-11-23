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
;

import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.junit.runner.RunWith
import org.mockito.Mockito._

@RunWith(classOf[JUnitRunner])
class DefaultSourceESSpec extends BaseXDTest with MockitoSugar {


  "A DefaultSource " should "build a ElasticSearchXDRelation without schema" in {
    //Fixture
    val defaultDatasource = new DefaultSource()
    val sqlContext = mock[SQLContext]
    val parameters = Map[String, String] {ConfigurationOptions.ES_RESOURCE -> "index/type"}

    //Experimentation
    val result = defaultDatasource.createRelation(sqlContext, parameters)

    //Expectations
    result should not be null
  }

  it should "build a ElasticSearchXDRelation with schema" in {
    //Fixture
    val defaultDatasource = new DefaultSource()
    val sqlContext = mock[SQLContext]
    val schema = mock[StructType]
    val parameters = Map[String, String] {ConfigurationOptions.ES_RESOURCE -> "index/type"}

    //Experimentation
    val result = defaultDatasource.createRelation(sqlContext, parameters, schema)

    //Expectations
    result should not be null
  }

  it should "Build a Map with the default opts" in {

    val defaultDatasource = new DefaultSource()
    val item: Table = mock[Table]
    when(item.database).thenReturn(Some("index"))
    when(item.tableName).thenReturn("type")
    val userOpts: Map[String, String] = Map(ES_HOST -> "localhost")

    //Experimentation
    val result:Map[String, String] = defaultDatasource.generateConnectorOpts(item, userOpts)

    //Expectations
    result should not be null
    result.get(ES_RESOURCE).get should be ("index/type")
    result.get(ES_HOST).get should be ("localhost")

  }
}
