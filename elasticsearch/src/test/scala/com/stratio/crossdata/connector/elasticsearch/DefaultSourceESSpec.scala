/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
    val userOpts: Map[String, String] = Map(ES_NODES -> "localhost")

    //Experimentation
    val result:Map[String, String] = defaultDatasource.generateConnectorOpts(item, userOpts)

    //Expectations
    result should not be null
    result.get(ES_RESOURCE).get should be ("index/type")
    result.get(ES_NODES).get should be ("localhost")

  }
}
