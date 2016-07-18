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
package com.stratio.crossdata.connector.elasticsearch

import java.util.UUID

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import org.elasticsearch.common.settings.Settings
import org.joda.time.DateTime
import org.scalatest.Suite

import scala.util.Try


trait ElasticInsertCollection extends ElasticWithSharedContext {

  override protected def saveTestData: Unit = for (a <- 1 to 10) {
    client.get.execute {
      index into Index / Type fields(
        "id" -> a,
        "age" -> (10 + a),
        "description" -> s"A ${a}description about the Name$a",
        "enrolled" -> (if (a % 2 == 0) true else null),
        "name" -> s"Name $a",
        "birthday" -> DateTime.parse((1980 + a) + "-01-01T10:00:00-00:00").toDate,
        "salary" -> a * 1000.5,
        "ageInMilis" -> DateTime.parse((1980 + a) + "-01-01T10:00:00-00:00").getMillis,
        "array_test" -> List(a, a+1, a+2),
        "map_test" -> Map("x" -> a, "y" -> (a+1)),
        "array_map" -> Seq( Map("x" -> a), Map("y" -> (a+1)) ),
        "map_array" -> Map("x" -> Seq(1,2), "y" -> Seq(2,3))
        )
    }.await
    client.get.execute {
      flush index Index
    }.await
  }

  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"""|CREATE TEMPORARY TABLE $Type (id INT, age INT, description STRING, enrolled BOOLEAN,
                           |name STRING, optionalField BOOLEAN, birthday DATE, salary DOUBLE, ageInMilis LONG,
                           |array_test ARRAY<STRING>, map_test MAP<STRING,STRING>,
                           |array_map ARRAY<MAP<STRING,STRING>>, map_array MAP<STRING, ARRAY<STRING>>)""".stripMargin)


  override def typeMapping(): MappingDefinition ={
    Type as(
      "id" typed IntegerType,
      "age" typed IntegerType,
      "description" typed StringType,
      "enrolled" typed BooleanType,
      "name" typed StringType index NotAnalyzed,
      "birthday" typed DateType,
      "salary" typed DoubleType,
      "ageInMilis" typed LongType,
      "array_test" typed StringType,
      "map_test" typed ObjectType
      )
  }

  override val Type = s"students_test_insert"

  override val defaultOptions = Map(
    "resource" -> s"$Index/$Type",
    "es.nodes" -> s"$ElasticHost",
    "es.port" -> s"$ElasticRestPort",
    "es.nativePort" -> s"$ElasticNativePort",
    "es.cluster" -> s"$ElasticClusterName"
  )


}