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

import java.util.UUID

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.Seq

@RunWith(classOf[JUnitRunner])
class ElasticSearchDropExternalTableIT extends ElasticWithSharedContext {

  val Index1 = s"droptest${UUID.randomUUID.toString.replace("-", "")}"

  val Index2 = s"droptest${UUID.randomUUID.toString.replace("-", "")}"

  val Index3 = s"droptest${UUID.randomUUID.toString.replace("-", "")}"

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    val createTableQueryString1 =
      s"""|CREATE EXTERNAL TABLE $Index1.testDrop1 (id Integer, name String)
          |USING $SourceProvider
          |OPTIONS (
          |es.resource '$Index1/testDrop1',
          |es.nodes '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString1).collect()

    val createTableQueryString2 =
      s"""|CREATE EXTERNAL TABLE testDrop2 (id Integer, name String)
          |USING $SourceProvider
          |OPTIONS (
          |es.resource '$Index2/drop_table_example',
          |es.nodes '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString2).collect()

    val createTableQueryString3 =
      s"""|CREATE EXTERNAL TABLE testDrop3 (id Integer, name String)
          |USING $SourceProvider
          |OPTIONS (
          |es.resource '$Index3/drop_table_example',
          |es.nodes '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString3).collect()

    val createTableQueryString4 =
      s"""|CREATE EXTERNAL TABLE testDrop4 (id Integer, name String)
          |USING $SourceProvider
          |OPTIONS (
          |es.resource '$Index3/drop_table_example2',
          |es.nodes '$ElasticHost',
          |es.port '$ElasticRestPort',
          |es.nativePort '$ElasticNativePort',
          |es.cluster '$ElasticClusterName'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString4).collect()

  }

  "The ElasticSearch connector" should "execute a DROP EXTERNAL TABLE" in {

    //Precondition
    xdContext.table(s"$Index1.testDrop1") should not be null

    val mappingName = "testDrop1"

    //DROP
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $Index1.$mappingName"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$Index1.testDrop1"))
    client.get.admin.indices.prepareExists(Index1).get.isExists shouldBe false
  }

  "The ElasticSearch connector" should "execute a DROP EXTERNAL TABLE without specify database" in {

    //Precondition
    xdContext.table("testDrop2") should not be null

    val mappingName = "drop_table_example"

    //DROP
    val dropExternalTableQuery = "DROP EXTERNAL TABLE testDrop2"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table("testDrop2"))
    client.get.admin.indices.prepareExists(Index2).get.isExists shouldBe false
  }

  it should "be unable to drop one table if there are more than one table living in the same index" in {

    //Precondition
    xdContext.table("testDrop3") should not be null
    xdContext.table("testDrop4") should not be null

    //DROP
    val dropExternalTableQuery = "DROP EXTERNAL TABLE testDrop3"
    an[Exception] shouldBe thrownBy(sql(dropExternalTableQuery))

    val dropExternalTableQuery2 = "DROP EXTERNAL TABLE testDrop4"
    an[Exception] shouldBe thrownBy(sql(dropExternalTableQuery2))

    //Expectations
    client.get.admin.indices.prepareExists(Index3).get.isExists shouldBe true
    xdContext.table("testDrop3") should not be null
    xdContext.table("testDrop4") should not be null

  }

}
