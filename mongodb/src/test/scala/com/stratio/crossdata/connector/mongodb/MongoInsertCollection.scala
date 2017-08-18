/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
          "map_test" -> Map(("x",a),("y",a+2),("c",a+3)),
          "array_map" -> Seq( Map("x" -> a), Map("y" -> (a+1)) ),
          "map_array" -> Map("x" -> Seq(1,2), "y" -> Seq(2,3))
        )
      }
      collection.update(QueryBuilder.start("id").greaterThan(4).get, MongoDBObject(("$set", MongoDBObject(("optionalField", true)))), multi = true)
    }

  }

  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"""|CREATE TEMPORARY TABLE $Collection (id BIGINT, age INT, description STRING, enrolled BOOLEAN,
                           |name STRING, optionalField BOOLEAN, array_test ARRAY<STRING>, map_test MAP<STRING,STRING>,
                           |array_map ARRAY<MAP<STRING,STRING>>, map_array MAP<STRING, ARRAY<STRING>>)""".stripMargin)


  override val Collection = "studentsInsertTest"

  override def defaultOptions = super.defaultOptions + ("collection" -> s"$Collection")
}
