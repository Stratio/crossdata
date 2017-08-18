package com.stratio.crossdata.driver.globalindex

import com.mongodb.casbah.commons.MongoDBObject
import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class MongoCreateGlobalIndexIT extends MongoAndElasticWithSharedContext {

  val mongoTestDatabase = "globalIndexDb"
  val defaultIndexES = "gidx"


  protected override def beforeAll(): Unit = {
    super.beforeAll()

    //Create test tables
    val createTable1 =
      s"""|CREATE EXTERNAL TABLE $mongoTestDatabase.proofGlobalIndex (id Integer, name String, comments String, other Integer, another Integer)
      USING $MongoSourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$mongoTestDatabase',
          |collection 'proofGlobalIndex'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTable1)

    mongoClient(mongoTestDatabase)("proofGlobalIndex").insert(
      MongoDBObject("id" -> 11, "name" -> "prueba", "comments" -> "one comment", "other" -> 12, "another" -> 12)
    )

    mongoClient(mongoTestDatabase)("proofGlobalIndex").insert(
      MongoDBObject("id" -> 13, "name" -> "prueba2", "comments" -> "one comment fail", "other" -> 5, "another" -> 12)
    )

    val sentence =
      s"""|CREATE GLOBAL INDEX myIndex
          |ON globalIndexDb.proofGlobalIndex (other, another)
          |WITH PK id
          |USING com.stratio.crossdata.connector.elasticsearch
          |OPTIONS (
          | es.nodes '$ElasticHost',
          | es.port '$ElasticRestPort',
          | es.nativePort '$ElasticNativePort',
          | es.cluster '$ElasticClusterName',
          | es.nodes.wan.only 'true'
          |)""".stripMargin

    sql(sentence)

    elasticClient.execute {
      index into "gidx" / "myIndex" fields(
        "id" -> 11,
        "another" -> 12,
        "other"-> 12)
    }.await

    elasticClient.execute {
      index into "gidx" / "myIndex" fields(
        "id" -> 13,
        "another" -> 12,
        "other"-> 5)
    }.await

    elasticClient.execute {
      flush index "gidx"
    }.await

  }

  protected override def afterAll(): Unit = {

    mongoClient(mongoTestDatabase).dropDatabase()

    elasticClient.execute{
      deleteIndex(defaultIndexES)
    }.await

    super.afterAll()
  }

  it should "execute a select * where indexedFilter is greater than" in {

    val result = sql(s"select * from globalIndexDb.proofGlobalIndex WHERE other > 10").collect()

    result should have length 1
    result shouldBe Array(Row(11, "prueba", "one comment", 12, 12))
  }

  it should "execute a select * where indexedFilter equals to" in {
    val result = sql(s"select * from globalIndexDb.proofGlobalIndex WHERE other = 5").collect()

    result should have length 1
    result shouldBe Array(Row(13, "prueba2", "one comment fail", 5, 12))
  }

  it should "execute a select col where indexedFilter is greater than" in {
    val result = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other > 10").collect()

    result should have length 1
    result shouldBe Array(Row("prueba"))
  }

  it should "execute a select col where indexedFilter equals to" in {
    val result = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other = 5").collect()

    result should have length 1
    result shouldBe Array(Row("prueba2"))
  }

  it should "execute a select col where indexedFilter equals to using multiple projects via DDL" in {
    val result = xdContext.table("globalIndexDb.proofGlobalIndex").select("name", "other").where($"other" equalTo 5).select("name").collect()
    result should have length 1
    result shouldBe Array(Row("prueba2"))
  }

  it should "execute a select indexedCol where indexedFilter equals to" in {
    val result = sql(s"select other from globalIndexDb.proofGlobalIndex WHERE other = 5").collect()

    result should have length 1
    result shouldBe Array(Row(5))
  }

  it should "support filters mixed with indexedCols" in {
    val result = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other > 10 AND name LIKE '%prueba%'").collect()

    result should have length 1
    result shouldBe Array(Row("prueba"))
  }

  it should "support filters using equals in two indexed columns" in {
    val result = sql(s"select name from globalIndexDb.proofGlobalIndex WHERE other = another").collect()

    result should have length 1
    result shouldBe Array(Row("prueba"))
  }

}