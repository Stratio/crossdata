/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
    table.schema.fieldNames should contain ("title")

    client.get.admin.indices.prepareTypesExists(Index).setTypes(Type).get.isExists shouldBe true
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
    table.schema.fieldNames should contain ("city")

    client.get.admin.indices.prepareTypesExists(Index).setTypes("newtable2").get.isExists shouldBe true

  }

}
