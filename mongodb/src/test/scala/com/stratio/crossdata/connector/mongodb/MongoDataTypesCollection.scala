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

import java.util.{Calendar, GregorianCalendar}

import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

import scala.util.Try

trait MongoDataTypesCollection extends MongoWithSharedContext with SharedXDContextTypesTest {


  override val emptyTypesSetError: String = "Type test entries should have been already inserted"

  override def dataTypesSparkOptions: Map[String, String] = Map(
    "host" -> s"$MongoHost:$MongoPort",
    "database" -> s"$Database",
    "collection" -> s"$DataTypesCollection"
  )

  override def saveTypesData: Int = {
    val mongoClient = client.get
    val baseDate = new GregorianCalendar(1970, 0, 1, 0, 0, 0)
    Try {
      val dataTypesCollection = mongoClient(Database)(DataTypesCollection)
      for (a <- 1 to 10) {
        baseDate.set(Calendar.DAY_OF_MONTH, a)
        baseDate.set(Calendar.MILLISECOND, a)
        dataTypesCollection.insert {
          MongoDBObject(
            "int" -> (2000 + a),
            "bigint" -> (200000 + a).toLong,
            "long" -> (200000 + a).toLong,
            "string" -> s"String $a",
            "boolean" -> true,
            "double" -> (9.0 + (a.toDouble / 10)),
            "float" -> float,
            "decimalint" -> decimalInt,
            "decimallong" -> decimalLong,
            "decimaldouble" -> decimalDouble,
            "decimalfloat" -> decimalFloat,
            "date" -> new java.sql.Date(baseDate.getTimeInMillis),
            "timestamp" -> new java.sql.Timestamp(baseDate.getTimeInMillis),
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
    }.map(_ => 1).getOrElse(0)

  }
  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL

}
