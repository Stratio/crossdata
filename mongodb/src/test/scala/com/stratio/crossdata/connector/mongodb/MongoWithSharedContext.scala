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
package com.stratio.crossdata.connector.mongodb

import com.mongodb.{BasicDBObject, QueryBuilder}
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import com.typesafe.config.ConfigFactory
import org.apache.spark.Logging
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.scalatest.Suite

import scala.util.Try

trait MongoWithSharedContext extends SharedXDContextTest with MongoDefaultConstants with Logging {
  this: Suite =>

  var mongoClient: Option[MongoClient] = None
  var isEnvironmentReady = false
  val UnregisteredCollection = "unregistered"

  override protected def beforeAll() = {
    super.beforeAll()

    try {
      mongoClient = Some(prepareEnvironment())

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE $Collection
            |(id BIGINT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)
            |USING $SourceProvider
            |OPTIONS (
            |host '$MongoHost:$MongoPort',
            |database '$Database',
            |collection '$Collection'
            |)
         """.stripMargin.replaceAll("\n", " "))

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE $DataTypesCollection
            |(
            |_id STRING,
            |int INT,
            |bigint BIGINT,
            |long LONG,
            |string STRING,
            |boolean BOOLEAN,
            |double DOUBLE,
            |float FLOAT,
            |decimalInt DECIMAL,
            |decimalLong DECIMAL,
            |decimalDouble DECIMAL,
            |decimalFloat DECIMAL,
            |date DATE,
            |timestamp TIMESTAMP,
            |tinyint TINYINT,
            |smallint SMALLINT,
            |binary BINARY,
            |arrayint ARRAY<INT>,
            |arraystring ARRAY<STRING>,
            |mapintint MAP<INT, INT>,
            |mapstringint MAP<STRING, INT>,
            |mapstringstring MAP<STRING, STRING>,
            |struct STRUCT<field1: DATE, field2: INT>,
            |arraystruct ARRAY<STRUCT<field1: INT, field2: INT>>,
            |arraystructwithdate ARRAY<STRUCT<field1: DATE, field2: INT>>,
            |structofstruct STRUCT<field1: DATE, field2: INT, struct1: STRUCT<structField1: STRING, structField2: INT>>,
            |mapstruct MAP<STRING, STRUCT<structField1: DATE, structField2: INT>>
            |)
            |USING $SourceProvider
            |OPTIONS (
            |host '$MongoHost:$MongoPort',
            |database '$Database',
            |collection '$DataTypesCollection'
            |)
         """.stripMargin.replaceAll("\n", " "))

    } catch {
      case e: Throwable => logError(e.getMessage)
    }

    isEnvironmentReady = mongoClient.isDefined
  }

  override protected def afterAll() = {
    _xdContext.dropAllTables()
    super.afterAll()
    mongoClient.foreach(cleanEnvironment)
  }

  def prepareEnvironment(): MongoClient = {
    val mongoClient = MongoClient(MongoHost, MongoPort)
    saveTestData(mongoClient)
    mongoClient
  }

  def cleanEnvironment(mongoClient: MongoClient) = {
    cleanTestData(mongoClient)
    mongoClient.close()
  }


  private def saveTestData(client: MongoClient): Unit = {

    val collection = client(Database)(Collection)
    for (a <- 1 to 10) {
      collection.insert {
        MongoDBObject("id" -> a,
          "age" -> (10 + a),
          "description" -> s"description$a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a"
        )
      }
      collection.update(QueryBuilder.start("id").greaterThan(4).get, MongoDBObject(("$set", MongoDBObject(("optionalField", true)))), multi = true)
    }

    val dataTypesCollection = client(Database)(DataTypesCollection)
    for (a <- 1 to 10) {
      dataTypesCollection.insert {
        MongoDBObject(
          "int" -> (2000 + a),
          "bigint" -> (200000+a).toLong,
          "long" -> (200000+a).toLong,
          "string" -> s"String $a",
          "boolean" -> true,
          "double" -> (9.0+(a.toDouble/10)),
          "float" -> float,
          "decimalInt" -> decimalInt,
          "decimalLong" -> decimalLong,
          "decimalDouble" -> decimalDouble,
          "decimalFloat" -> decimalFloat,
          "date" -> date,
          "timestamp" -> timestamp,
          "tinyint" -> tinyint,
          "smallint" -> smallint,
          "binary" -> binary,
          "arrayint" -> arrayint,
          "arraystring" -> arraystring,
          "mapintint" -> mapintint,
          "mapstringint" -> mapstringint,
          "mapstringstring" -> mapstringstring,
          "struct" -> struct,
          "arraystruct" -> arraystruct,
          "arraystructwithdate" -> arraystructwithdate,
          "structofstruct" -> structofstruct,
          "mapstruct" -> mapstruct
        )
      }
    }

  }

  private def cleanTestData(client: MongoClient): Unit = {
    val collection = client(Database)(Collection)
    collection.dropCollection()

    val dataTypesCollection = client(Database)(DataTypesCollection)
    dataTypesCollection.dropCollection()
  }

  lazy val assumeEnvironmentIsUpAndRunning = {
    assume(isEnvironmentReady, "MongoDB and Spark must be up and running")
  }
}

sealed trait MongoDefaultConstants {
  val Database = "highschool"

  //Collections
  val Collection = "students"
  val DataTypesCollection = "studentsTestDataTypes"

  //Config
  val MongoHost: String = {
    Try(ConfigFactory.load().getStringList("mongo.hosts")).map(_.get(0)).getOrElse("127.0.0.1")
  }
  val MongoPort = 27017
  val SourceProvider = "com.stratio.crossdata.connector.mongodb"

  // Date types
  val date = new java.sql.Date(2015)
  val dt = new java.util.Date().getTime
  val timestamp =  new java.sql.Timestamp(dt)

  // Types supported in MongoDB for write decimal
  val decimalInt = 10
  val decimalLong = 10l
  val decimalDouble = 10.0
  val decimalFloat = 10f

  // Numeric types
  val float = 1.5f
  val tinyint= 127 // Mongo store it like Byte
  val smallint = 32767

  val byte = Byte.MaxValue
  val binary = Array(byte, byte)

  // Arrays
  val arrayint = Seq(1,2,3)
  val arraystring = Seq("a","b","c")
  val arraystruct = Seq(MongoDBObject("field1" -> 1, "field2" -> 2))
  val arraystructwithdate = Seq(MongoDBObject("field1" -> date ,"field2" -> 3))

  // Map
  val mapintint = new BasicDBObject("1",1).append("2",2)
  val mapstringint = new BasicDBObject("1",1).append("2",2)
  val mapstringstring = new BasicDBObject("1","1").append("2","2")
  val mapstruct = new BasicDBObject("mapstruct", MongoDBObject("structField1" -> date ,"structField2" -> 3))

  // Struct
  val struct = MongoDBObject("field1" -> date ,"field2" -> 3)
  val structofstruct = MongoDBObject("field1" -> date ,"field2" -> 3, "struct1" -> MongoDBObject("structField1"-> "structfield1", "structField2" -> 2))

}