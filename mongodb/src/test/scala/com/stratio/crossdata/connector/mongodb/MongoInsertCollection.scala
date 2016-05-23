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
package com.stratio.crossdata.connector.mongodb

import java.util.{Calendar, GregorianCalendar}

import com.mongodb.QueryBuilder
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

import scala.util.Try

trait MongoInsertCollection extends MongoWithSharedContext {

  override protected def saveTestData: Unit = {
    val client = this.client.get

    val collection = client(Database)(Collection)
    for (a <- 1 to 10) {
      collection.insert {
        MongoDBObject("id" -> a,
          "age" -> (10 + a),
          "description" -> s"description$a",
          "enrolled" -> (a % 2 == 0),
          "name" -> s"Name $a",
          "array_test" -> Seq(a toString, a+1 toString, a+2 toString),
          "map_test" -> Map(("x",a),("y",a+2),("c",a+3))
        )
      }
      collection.update(QueryBuilder.start("id").greaterThan(4).get, MongoDBObject(("$set", MongoDBObject(("optionalField", true)))), multi = true)
    }

  }

  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"""|CREATE TEMPORARY TABLE $Collection (id BIGINT, age INT, description STRING, enrolled BOOLEAN,
                           |name STRING, optionalField BOOLEAN, array_test ARRAY<STRING>, map_test MAP<STRING,STRING> )""".stripMargin)


  override val Collection = "studentsInsertTest"

  override val defaultOptions = Map(
    "host" -> s"$MongoHost:$MongoPort",
    "database" -> s"$Database",
    "collection" -> s"$Collection"
  )
}
