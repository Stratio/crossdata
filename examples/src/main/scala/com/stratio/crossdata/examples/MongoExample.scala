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
package com.stratio.crossdata.examples


import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}

sealed trait MongoDefaultConstants {
  val Database = "highschool"
  val Collection = "students"
  val MongoHost = "127.0.0.1"
  val MongoPort = 27017
  val SourceProvider = "mongodb"
  val MongoProvider = "com.stratio.provider.mongodb"
}

object MongoExample extends App with MongoDefaultConstants {

  val mongoClient = prepareEnvironment()

  withCrossdataContext { xdContext =>

    xdContext.sql(
      s"""|CREATE TEMPORARY TABLE $Collection
          |(id STRING, age INT, description STRING, enrolled BOOLEAN, name STRING)
          |USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database '$Database',
          |collection '$Collection'
          |)
       """.stripMargin.replaceAll("\n", " "))

    // Native
    //xdContext.sql(s"SELECT description as b FROM $Collection WHERE id = 1").show(5)
    //xdContext.sql(s"SELECT description as b FROM $Collection WHERE id IN(2,5,8) limit 2").show(5)
    //xdContext.sql(s"SELECT *  FROM $Collection ").show(5)
    //xdContext.sql(s"SELECT name as b, age FROM $Collection WHERE age > 12 limit 4").show(5)
    //xdContext.sql(s"SELECT id, age FROM $Collection WHERE age BETWEEN 13 AND 14 OR age = 15 OR (age = 11 AND id = '1')").show(5)
    //xdContext.sql(s"SELECT id, age FROM $Collection WHERE id LIKE '1%'").show(5)
    xdContext.sql(s"SELECT id, name FROM $Collection WHERE name LIKE '%ame%'").show(5)


    //Spark
    xdContext.sql(s"SELECT count(*), avg(age) FROM $Collection GROUP BY enrolled").show(5)

    /* TODO CREATE TABLE AS SELECT EXAMPLE
    xdContext.sql(
      s"""|CREATE TABLE newTable
          |USING $SourceProvider
          |OPTIONS (
          |host '$MongoHost:$MongoPort',
          |database 'any',
          |collection 'newTable'
          |)
          |AS SELECT * FROM $Collection
       """.stripMargin.replaceAll("\n", " "))*/
  }

  cleanEnvironment(mongoClient)

  private def withCrossdataContext(commands: XDContext => Unit) = {

    val sparkConf = new SparkConf().
      setAppName("MongoExample").
      setMaster("local[4]")

    val sc = new SparkContext(sparkConf)
    try {
      val xdContext = new XDContext(sc)
      commands(xdContext)
    } finally {
      sc.stop()
    }

  }

  def prepareEnvironment(): MongoClient = {
    val mongoClient = MongoClient(MongoHost,MongoPort)
    populateTable(mongoClient)
    mongoClient
  }

  def cleanEnvironment(mongoClient: MongoClient) = {
    cleanData(mongoClient)
    mongoClient.close()
  }


  private def populateTable(client: MongoClient): Unit = {

    val collection = client(Database)(Collection)
    for (a <- 1 to 10) {
      collection.insert{
        MongoDBObject("id" -> a.toString,
                      "age" -> (10+a),
                      "description" -> s"description $a",
                      "enrolled" -> (a % 2 == 0 ),
                      "name" -> s"Name $a"
        )
      }
    }
  }

  private def cleanData(client: MongoClient): Unit = {
    val collection = client(Database)(Collection)
    collection.dropCollection()
  }


}