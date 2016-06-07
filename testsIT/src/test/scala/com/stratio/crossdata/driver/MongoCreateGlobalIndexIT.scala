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

import com.sksamuel.elastic4s.ElasticDsl
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.sksamuel.elastic4s.ElasticDsl._


@RunWith(classOf[JUnitRunner])
class MongoCreateGlobalIndexIT extends MongoAndElasticWithSharedContext {

  val mongoTestDatabase = "globalIndexDb"
  val defaultIndexES = "gidx"


  protected override def beforeAll(): Unit = {
    super.beforeAll()

    //Create test tables
    val createTable1 =
      s"""|CREATE EXTERNAL TABLE $mongoTestDatabase.proofGlobalIndex (id Integer, name String, comments String)
      USING $MongoSourceProvider
          |OPTIONS (
          |host '127.0.0.1:27017',
          |database 'globalIndexDb',
          |collection 'proofGlobalIndex'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTable1)

  }

  protected override def afterAll(): Unit = {

    mongoClient(mongoTestDatabase).dropDatabase()

    elasticClient.execute{
      deleteIndex(defaultIndexES)
    }.await


    super.afterAll()
  }

  "The Mongo connector" should "execute a CREATE GLOBAL INDEX" in {

    val ElasticHost: String = "127.0.0.1"
    val ElasticRestPort = 9200
    val ElasticNativePort = 9300
    val ElasticClusterName: String = "esCluster"


    val sentence =
      s"""|CREATE GLOBAL INDEX myIndex
         |ON globalIndexDb.proofGlobalIndex (comments)
         |WITH PK (id)
         |USING com.stratio.crossdata.connector.elasticsearch
         |OPTIONS (
         | es.nodes '$ElasticHost',
         | es.port '$ElasticRestPort',
         | es.nativePort '$ElasticNativePort',
         | es.cluster '$ElasticClusterName'
         |)""".stripMargin

    sql(sentence)
  }

}