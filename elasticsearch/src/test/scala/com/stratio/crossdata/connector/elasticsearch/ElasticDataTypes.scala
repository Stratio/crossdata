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

import java.util.{GregorianCalendar, UUID}

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.mappings.{MappingDefinition, TypedFieldDefinition}
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest.SparkSQLColDef
import org.joda.time.DateTime

trait ElasticDataTypes extends ElasticWithSharedContext
  with SharedXDContextTypesTest
  with ElasticSearchDataTypesDefaultConstants
  with SparkLoggerComponent {

  override val dataTypesSparkOptions = Map(
    "resource" -> s"$Index/$Type",
    "es.nodes" -> s"$ElasticHost",
    "es.port" -> s"$ElasticRestPort",
    "es.nativePort" -> s"$ElasticNativePort",
    "es.cluster" -> s"$ElasticClusterName",
    "es.nodes.wan.only" -> "true"
  )

  protected case class ESColumnData(elasticType: TypedFieldDefinition, data: () => Any)

  protected val dataTest: Seq[(SparkSQLColDef, ESColumnData)] = Seq(
    (SparkSQLColDef("id", "INT",  _ shouldBe a[java.lang.Integer]), ESColumnData("id" typed IntegerType, () => 1)),
    (SparkSQLColDef("age", "LONG", _ shouldBe a[java.lang.Long]), ESColumnData("age" typed LongType, () => 1)),
    (
      SparkSQLColDef("description", "STRING", _ shouldBe a[java.lang.String]),
      ESColumnData("description" typed StringType, () => "1")
    ),
    (
      SparkSQLColDef("name", "STRING", _ shouldBe a[java.lang.String]),
      ESColumnData( "name" typed StringType index NotAnalyzed, () => "1")
    ),
    (
      SparkSQLColDef("enrolled", "BOOLEAN", _ shouldBe a[java.lang.Boolean]),
      ESColumnData("enrolled" typed BooleanType, () => false)
    ),
    (
      SparkSQLColDef("birthday", "DATE", _ shouldBe a [java.sql.Date]),
      ESColumnData("birthday" typed DateType, () => DateTime.parse(1980 + "-01-01T10:00:00-00:00").toDate)
    ),
    (
      SparkSQLColDef("salary", "DOUBLE", _ shouldBe a[java.lang.Double]),
      ESColumnData("salary" typed DoubleType, () => 0.15)
    ),
    (
      SparkSQLColDef("timecol", "TIMESTAMP", _ shouldBe a[java.sql.Timestamp]),
      ESColumnData(
        "timecol" typed DateType,
        () => new java.sql.Timestamp(new GregorianCalendar(1970, 0, 1, 0, 0, 0).getTimeInMillis)
      )
    ),
    (
      SparkSQLColDef("float", "FLOAT", _ shouldBe a[java.lang.Float]),
      ESColumnData("float" typed FloatType, () => 0.15)
    ),
    (
      SparkSQLColDef("binary", "BINARY", x => x.isInstanceOf[Array[Byte]] shouldBe true),
      ESColumnData("binary" typed BinaryType, () => "YWE=".getBytes)
    ),
    (
      SparkSQLColDef("tinyint", "TINYINT", _ shouldBe a[java.lang.Byte]),
      ESColumnData("tinyint" typed ByteType, () => Byte.MinValue)
    ),
    (
      SparkSQLColDef("smallint", "SMALLINT", _ shouldBe a[java.lang.Short]),
      ESColumnData("smallint" typed ShortType, () => Short.MaxValue)
    ),
    (
      SparkSQLColDef("subdocument", "STRUCT<field1: INT>", _ shouldBe a [Row]),
      ESColumnData("subdocument"  inner ("field1" typed IntegerType), () => Map( "field1" -> 15))
    ),
    (
      SparkSQLColDef(
        "structofstruct",
        "STRUCT<field1: INT, struct1: STRUCT<structField1: INT>>",
        { res =>
          res shouldBe a[GenericRowWithSchema]
          res.asInstanceOf[GenericRowWithSchema].get(1) shouldBe a[GenericRowWithSchema]
        }
      ),
      ESColumnData(
        "structofstruct" inner ("field1" typed IntegerType, "struct1" inner("structField1" typed IntegerType)),
        () => Map("field1" -> 15, "struct1" -> Map("structField1" -> 42))
      )
    ),
    (
      SparkSQLColDef(
        "arraystructarraystruct",
        "ARRAY<STRUCT<stringfield: STRING, arrayfield: ARRAY<STRUCT<field1: INT, field2: INT>>>>",
        { res =>
          res shouldBe a[Seq[_]]
          res.asInstanceOf[Seq[_]].head shouldBe a[Row]
          res.asInstanceOf[Seq[_]].head.asInstanceOf[Row].get(1) shouldBe a[Seq[_]]
          res.asInstanceOf[Seq[_]].head.asInstanceOf[Row].get(1).asInstanceOf[Seq[_]].head shouldBe a[Row]
        }
      ),
      ESColumnData(
        "arraystructarraystruct" multi (
          "structarraystruct" inner (
            "stringfield" typed StringType,
            "arrayfield" multi (
              "struct" inner (
                "field1" typed IntegerType,
                "field2" typed IntegerType
              )
            )
          )
        ),
        () => List(
          Map(
            "stringfield" -> "hello",
            "arrayfield" -> List(
              Map(
                "field1" -> 1,
                "field2" -> 2
              )
            )
          )
        )
      )
    )
  )


  override protected def typesSet: Seq[SparkSQLColDef] = dataTest.map(_._1)

  abstract override def saveTestData: Unit = {
    require(saveTypesData > 0, emptyTypesSetError)
  }

  override def saveTypesData: Int = {
    client.get.execute {
      val fieldsData = dataTest map {
        case (SparkSQLColDef(fieldName, _, _), ESColumnData(_, data)) => (fieldName, data())
      }
      index into Index / Type fields (fieldsData: _*)
    }.await
    client.get.execute {
      flush index Index
    }.await
    1
  }

  override def typeMapping(): MappingDefinition =
    Type fields (
      dataTest map {
        case (_, ESColumnData(mapping, _)) => mapping
      }: _*
    )

  override val emptyTypesSetError: String = "Couldn't insert Elasticsearch types test data"

}


trait ElasticSearchDataTypesDefaultConstants extends ElasticSearchDefaultConstants{
  private lazy val config = ConfigFactory.load()
  override val Index = s"idxname${UUID.randomUUID.toString.replaceAll("-", "")}"
  override val Type = s"typename${UUID.randomUUID.toString.replaceAll("-", "")}"

}