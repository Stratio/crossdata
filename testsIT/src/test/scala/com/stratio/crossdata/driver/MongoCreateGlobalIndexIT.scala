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
package com.stratio.crossdata.driver

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.sksamuel.elastic4s.ElasticDsl
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.ExecutionType


@RunWith(classOf[JUnitRunner])
class MongoCreateGlobalIndexIT extends MongoAndElasticWithSharedContext {

  val mongoTestDatabase = "globalIndexDb"
  val defaultIndexES = "gidx"


  protected override def beforeAll(): Unit = {
    super.beforeAll()

    //Create test tables
    val createTable1 =
      s"""|CREATE EXTERNAL TABLE $mongoTestDatabase.proofGlobalIndex (id Integer, name String, comments String, other Integer)
      USING $MongoSourceProvider
          |OPTIONS (
          |host '127.0.0.1:27017',
          |database 'globalIndexDb',
          |collection 'proofGlobalIndex'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTable1)

    mongoClient(mongoTestDatabase)("proofGlobalIndex").insert(
      MongoDBObject("id" -> 11, "name" -> "prueba", "comments" -> "one comment", "other" -> 12)
    )

    mongoClient(mongoTestDatabase)("proofGlobalIndex").insert(
      MongoDBObject("id" -> 13, "name" -> "prueba2", "comments" -> "one comment fail", "other" -> 5)
    )

  }

  protected override def afterAll(): Unit = {

    mongoClient(mongoTestDatabase).dropDatabase()

    elasticClient.execute{
      deleteIndex(defaultIndexES)
    }.await


    super.afterAll()
  }

  "The Mongo connector" should "execute a CREATE GLOBAL INDEX with select *" in {

    val sentence =
      s"""|CREATE GLOBAL INDEX myIndex
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

    val results = sql(s"select * from globalIndexDb.proofGlobalIndex WHERE other > 10").collect()

    results should have length 0 //No indexed data

    elasticClient.execute {
      index into "gidx" / "myIndex" fields(
        "id" -> 11,
        "other"-> 12)
    }.await

    elasticClient.execute {
      index into "gidx" / "myIndex" fields(
        "id" -> 13,
        "other"-> 5)
    }.await

    elasticClient.execute {
      flush index "gidx"
    }.await

    val resultsAfter = sql(s"select * from globalIndexDb.proofGlobalIndex WHERE other > 10").collect()

    resultsAfter should have length 1
    resultsAfter shouldBe Array(Row(11, "prueba", "one comment", 12))

    val resultsEquals = sql(s"select * from globalIndexDb.proofGlobalIndex WHERE other = 5").collect()

    resultsEquals should have length 1
    resultsEquals shouldBe Array(Row(13, "prueba2", "one comment fail", 5))

    val resultsAfter2 = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other > 10").collect()

    resultsAfter2 should have length 1
    resultsAfter2 shouldBe Array(Row("prueba"))

    val resultsEquals2 = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other = 5").collect()

    resultsEquals2 should have length 1
    resultsEquals2 shouldBe Array(Row("prueba2"))

    val resultsEquals3 = xdContext.table("globalIndexDb.proofGlobalIndex").select("name", "other").where($"other" equalTo 5).select("name").collect()
    resultsEquals3 should have length 1
    resultsEquals3 shouldBe Array(Row("prueba2"))

    //Mix
    //val resultsEquals3 = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other > 10 AND name LIKE '*prueba*'").collect()

    //resultsEquals3 should have length 1
    //resultsEquals3 shouldBe Array(Row("prueba2"))

  }

  //TODO: More tests and remove this properties from here!!!!!!!!!!!

"The insert in mongo doc with a global index" should "insert in ES too" in {



    val sentence =
      s"""|INSERT INTO globalIndexDb.proofGlobalIndex(id, name, comments)
          |VALUES( 1, "foo", "this is a proof of insert in a column that has a global index")
          |""".stripMargin

    sql(sentence)
  }
}