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
package com.stratio.crossdata.driver.globalindex

import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.spark.sql.crossdata.catalog.XDCatalog.IndexIdentifier
import org.apache.spark.sql.crossdata.catalyst.execution.CreateGlobalIndex
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CreateGlobalIndexIT extends MongoAndElasticWithSharedContext {

  val mongoTestDatabase = "globalIndexDb"
  val defaultIndexES = CreateGlobalIndex.DefaultDatabaseName
  val indexName = "myIndex"

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    //Create test tables
    val createTable1 =
      s"""|CREATE EXTERNAL TABLE $mongoTestDatabase.proofGlobalIndex (id Integer, name String, comments String, other Integer)
          |USING $MongoSourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database 'globalIndexDb',
          |collection 'proofGlobalIndex'
          |)
      """.stripMargin.replaceAll("\n", " ")

    sql(createTable1)

  }

  protected override def afterAll(): Unit = {

    mongoClient(mongoTestDatabase).dropDatabase()

    elasticClient.execute {
      deleteIndex(defaultIndexES)
    }.await

    super.afterAll()
  }

  "Create global index" should "create an associated index in Elasticsearch" in {

    val sentence = s"""|CREATE GLOBAL INDEX $indexName
          |ON globalIndexDb.proofGlobalIndex (other)
          |WITH PK id
          |USING com.stratio.crossdata.connector.elasticsearch
          |OPTIONS (
          | es.nodes '$ElasticHost',
          | es.port '$ElasticRestPort',
          | es.nativePort '$ElasticNativePort',
          | es.cluster '$ElasticClusterName'
          |)""".stripMargin

    sql(sentence)

    val typeExistResponse = elasticClient.execute {
      typesExist(indexName).in(defaultIndexES)
    }.await

    typeExistResponse.isExists shouldBe true

    xdContext.dropGlobalIndex(IndexIdentifier(indexName, CreateGlobalIndex.DefaultDatabaseName))

  }

  it should "fail if the target table is temporary" in {

    val tempTableId = "tempTable"

    val createTable1 =
      s"""|CREATE TEMPORARY TABLE $tempTableId (id Integer, name String, comments String, other Integer)
          |USING $MongoSourceProvider
          |OPTIONS (
          |host '127.0.0.1:27017',
          |database 'globalIndexDb',
          |collection 'proofGlobalIndex'
          |)
      """.stripMargin.replaceAll("\n", " ")

    sql(createTable1)

    val sentence = s"""|CREATE GLOBAL INDEX fail_index
          |ON $tempTableId (other)
          |WITH PK id
          |USING com.stratio.crossdata.connector.elasticsearch
          |OPTIONS (
          | es.nodes '$ElasticHost',
          | es.port '$ElasticRestPort',
          | es.nativePort '$ElasticNativePort',
          | es.cluster '$ElasticClusterName'
          |)""".stripMargin

    the[RuntimeException] thrownBy {
      sql(sentence)
    } should have message s"Cannot create the index. Table `$tempTableId` doesn't exist or is temporary"

  }
}
