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
import org.apache.spark.sql.crossdata.execution.datasources.CreateGlobalIndex
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InsertGlobalIndexIT extends MongoAndElasticWithSharedContext {

  val mongoDB = "globalIndexDb"
  val mongoCollection = "collection"
  val mongoTableIdentifier = s"$mongoDB.$mongoCollection"
  val defaultIndexES = CreateGlobalIndex.DefaultDatabaseName
  val indexName = "myIndex"

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    //Create test tables
    val createTable1 =
      s"""|CREATE EXTERNAL TABLE $mongoTableIdentifier (id Integer, name String, comments String, other Integer)
          |USING $MongoSourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$mongoDB',
          |collection '$mongoCollection'
          |)
      """.stripMargin.replaceAll("\n", " ")

    sql(createTable1)

  }

  protected override def afterAll(): Unit = {

    mongoClient(mongoDB).dropDatabase()

    elasticClient.execute{
      deleteIndex(defaultIndexES)
    }.await


    super.afterAll()
  }


  "Insertion" should "insert indexed columns into elasticsearch index" in {

    val sentence =
      s"""|CREATE GLOBAL INDEX $indexName
          |ON $mongoTableIdentifier (other)
          |WITH PK id
          |USING com.stratio.crossdata.connector.elasticsearch
          |OPTIONS (
          | es.nodes '$ElasticHost',
          | es.port '$ElasticRestPort',
          | es.nativePort '$ElasticNativePort',
          | es.cluster '$ElasticClusterName'
          |)""".stripMargin

    sql(sentence)

    val typeExistResponse = elasticClient.execute{
      typesExist(indexName).in(defaultIndexES)
    }.await

    typeExistResponse.isExists shouldBe true


    sql(s"INSERT INTO $mongoTableIdentifier VALUES ( 50, 'Samantha', 'Fox', 4),( 1, 'Charlie', 'Green', 5)")

    mongoClient(mongoDB)(mongoCollection).count() shouldBe 2
    elasticClient.execute(search in defaultIndexES / indexName).await.getHits.totalHits() shouldBe 2

  }

}