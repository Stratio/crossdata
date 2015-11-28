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

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.ExecutionType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongodbDataTypesIT extends MongoWithSharedContext {


  "MongoConnector" should "support _id" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT _id FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT _id FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.String]
    nativeRow shouldBe a[java.lang.String]
  }

  it should "support Int type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT int FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT int FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Integer]
    nativeRow shouldBe a[java.lang.Integer]
  }

  it should "support BigInt type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT bigint FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT bigint FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Long]
    nativeRow shouldBe a[java.lang.Long]
  }

  it should "support Long type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT long FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT long FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Long]
    nativeRow shouldBe a[java.lang.Long]
  }

  it should "support String type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT string FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT string FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.String]
    nativeRow shouldBe a[java.lang.String]
  }

  it should "support Boolean type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT boolean FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT boolean FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Boolean]
    nativeRow shouldBe a[java.lang.Boolean]
  }

  it should "support Double type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT double FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT double FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Double]
    nativeRow shouldBe a[java.lang.Double]
  }

  it should "support Decimal type writting a Int in mongodb" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT decimalInt FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT decimalInt FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.math.BigDecimal]
    nativeRow shouldBe a[java.math.BigDecimal]
  }

  it should "support Decimal type writting a Long in mongodb" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT decimalLong FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT decimalLong FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.math.BigDecimal]
    nativeRow shouldBe a[java.math.BigDecimal]
  }

  it should "support Decimal type writting a Double in mongodb" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT decimalDouble FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT decimalDouble FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.math.BigDecimal]
    nativeRow shouldBe a[java.math.BigDecimal]
  }

  it should "support Decimal type writting a Float in mongodb" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT decimalFloat FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT decimalFloat FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.math.BigDecimal]
    nativeRow shouldBe a[java.math.BigDecimal]
  }

  it should "support Date type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT date FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT date FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.sql.Date]
    nativeRow shouldBe a[java.sql.Date]
  }

  it should "support Timestamp type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT timestamp FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT timestamp FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.sql.Timestamp]
    nativeRow shouldBe a[java.sql.Timestamp]
  }

  it should "support Float type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT float FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT float FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Float]
    nativeRow shouldBe a[java.lang.Float]
  }

  it should "support TinyInt type: 8 bits integer precision" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT tinyint FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT tinyint FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Byte]
    nativeRow shouldBe a[java.lang.Byte]
  }

  it should "support SmallInt type: 16 bits integer precision" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT smallint FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT smallint FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[java.lang.Short]
    nativeRow shouldBe a[java.lang.Short]
  }

  it should "support Binary type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT binary FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT binary FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Array[Byte]]
    nativeRow shouldBe a[Array[Byte]]
  }

  it should "support simple Array<INT> type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT arrayint FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT arrayint FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Seq[_]]
    nativeRow shouldBe a[Seq[_]]

    sparkRow.asInstanceOf[Seq[_]](0) shouldBe a [java.lang.Integer]
    nativeRow.asInstanceOf[Seq[_]](0) shouldBe a [java.lang.Integer]
  }

  it should "support simple Array<STRING> type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT arraystring FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT arraystring FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Seq[_]]
    nativeRow shouldBe a[Seq[_]]
  }

  it should "support simple Map<INT,INT> type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT mapintint FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT mapintint FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Map[_, _]]
    nativeRow shouldBe a[Map[_, _]]
  }

  it should "support simple Map<STRING,INT> type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT mapstringint FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT mapstringint FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Map[_, _]]
    nativeRow shouldBe a[Map[_, _]]
  }

  it should "support simple Map<STRING,STRING> type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT mapstringstring FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT mapstringstring FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Map[_, _]]
    nativeRow shouldBe a[Map[_, _]]
  }

  it should "support struct type" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT struct FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT struct FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a[Row]
    nativeRow shouldBe a[Row]
  }

  it should "support access to struct fields" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRowStructField = sql(s"SELECT struct.field1 FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    sparkRowStructField shouldBe a[java.sql.Date]
  }

  it should "support array of structs" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT arraystruct FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT arraystruct FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a [Seq[_]]
    nativeRow shouldBe a [Seq[_]]
  }

  it should "support Array of structs with Date inside the struct" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT arraystructwithdate FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT arraystructwithdate FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a [Seq[_]]
    nativeRow shouldBe a [Seq[_]]
  }

  it should "support structs of structs" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT structofstruct FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    val nativeRow = sql(s"SELECT structofstruct FROM $DataTypesCollection").collect(ExecutionType.Native).head(0)

    sparkRow shouldBe a [Row]
    nativeRow shouldBe a [Row]

  }

  it should "support nested structs" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRowsStruct = sql(s"SELECT structofstruct.struct1 FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    sparkRowsStruct shouldBe a [Row]

  }

  it should "support access to fields in nested structs" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRowsStructField = sql(s"SELECT structofstruct.struct1.structField1 FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    sparkRowsStructField shouldBe a [java.lang.String]
  }


  it should "support MAP type with STRUCT inside" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT mapstruct FROM $DataTypesCollection").collect(ExecutionType.Spark).head(0)
    sparkRow shouldBe a [Map[_,_]]
  }

}
