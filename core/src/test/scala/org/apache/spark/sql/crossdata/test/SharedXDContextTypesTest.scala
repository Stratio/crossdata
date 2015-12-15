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
package org.apache.spark.sql.crossdata.test

import com.stratio.crossdata.test.BaseXDTest

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.ExecutionType
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

trait SharedXDContextTypesTest extends SharedXDContextWithDataTest {
  this: BaseXDTest =>

  import SharedXDContextTypesTest._

  //Template steps: Override them
  val emptyTypesSetError: String
  def saveTypesData: Int
  def sparkAdditionalKeyColumns: Seq[SparkSQLColdDef]
  def dataTypesSparkOptions: Map[String, String]

  //Template
  def doTypesTest(datasourceName: String): Unit = {

    for(executionType <- ExecutionType.Spark::ExecutionType.Native::Nil)
      datasourceName should s"provide the right types for $executionType execution" in {
        val q = "SELECT " + typesSet.map(_.colname).mkString(", ") + s" FROM $dataTypesTableName"
        val dframe = sql("SELECT " + typesSet.map(_.colname).mkString(", ") + s" FROM $dataTypesTableName")
        for((tpe, i) <- typesSet zipWithIndex; typeCheck <- tpe.typeCheck) {
          println(s">> ${tpe.colname}")
          typeCheck(dframe.collect(executionType).head(i))
        }
      }


  }

  abstract override def saveTestData: Unit = {
    super.saveTestData
    require(saveTypesData > 0, emptyTypesSetError)
  }

  protected def typesSet: Seq[SparkSQLColdDef] = Seq(
    SparkSQLColdDef("int", "INT", { o =>
      println("INT TRANSFORMER IN ACTION")
      o.asInstanceOf[java.lang.Integer];
      println("And finishing")
    }
    ),
    SparkSQLColdDef("bigint", "BIGINT", _.asInstanceOf[java.lang.Long]),
    SparkSQLColdDef("long", "LONG", _.asInstanceOf[java.lang.Long]),
    SparkSQLColdDef("string", "STRING", _.asInstanceOf[java.lang.String]),
    SparkSQLColdDef("boolean", "BOOLEAN", _.asInstanceOf[java.lang.Boolean]),
    SparkSQLColdDef("double", "DOUBLE", _.asInstanceOf[java.lang.Double]),
    SparkSQLColdDef("float", "FLOAT", _.asInstanceOf[java.lang.Float]),
    SparkSQLColdDef("decimalint", "DECIMAL", _.asInstanceOf[java.math.BigDecimal]),
    SparkSQLColdDef("decimallong", "DECIMAL", _.asInstanceOf[java.math.BigDecimal]),
    SparkSQLColdDef("decimaldouble", "DECIMAL", _.asInstanceOf[java.math.BigDecimal]),
    SparkSQLColdDef("decimalfloat", "DECIMAL", _.asInstanceOf[java.math.BigDecimal]),
    SparkSQLColdDef("date", "DATE", _.asInstanceOf[java.sql.Date]),
    SparkSQLColdDef("timestamp", "TIMESTAMP", _.asInstanceOf[java.sql.Timestamp]),
    SparkSQLColdDef("tinyint", "TINYINT", _.asInstanceOf[java.lang.Byte]),
    SparkSQLColdDef("smallint", "SMALLINT", _.asInstanceOf[java.lang.Short]),
    SparkSQLColdDef("binary", "BINARY", _.asInstanceOf[Array[Byte]]),
    SparkSQLColdDef("arrayint", "ARRAY<INT>", _.asInstanceOf[Seq[_]]),
    SparkSQLColdDef("arraystring", "ARRAY<STRING>", _.asInstanceOf[Seq[_]]),
    SparkSQLColdDef("mapintint", "MAP<INT, INT>", _.asInstanceOf[Map[_, _]]),
    SparkSQLColdDef("mapstringint", "MAP<STRING, INT>", _.asInstanceOf[Map[_, _]]),
    SparkSQLColdDef("mapstringstring", "MAP<STRING, STRING>", _.asInstanceOf[Map[_, _]]),
    SparkSQLColdDef("struct", "STRUCT<field1: DATE, field2: INT>", _.asInstanceOf[Row]),
    SparkSQLColdDef("arraystruct", "ARRAY<STRUCT<field1: INT, field2: INT>>", _.asInstanceOf[Seq[_]]),
    SparkSQLColdDef("arraystructwithdate", "ARRAY<STRUCT<field1: DATE, field2: INT>>", _.asInstanceOf[Seq[_]]),
    SparkSQLColdDef("structofstruct", "STRUCT<field1: DATE, field2: INT, struct1: STRUCT<structField1: STRING, structField2: INT>>", _.asInstanceOf[Row]),
    SparkSQLColdDef("mapstruct", "MAP<STRING, STRUCT<structField1: DATE, structField2: INT>>", _.asInstanceOf[Map[_,_]])
  )

  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+ {
    val fields = (sparkAdditionalKeyColumns ++ typesSet) map {
      case SparkSQLColdDef(name, tpe, _) => s"$name $tpe"
    } mkString ", "
    SparkTable(s"CREATE TEMPORARY TABLE $dataTypesTableName ( $fields )", dataTypesSparkOptions)
  }

}

object SharedXDContextTypesTest {
  val dataTypesTableName = "typesCheckTable"
  case class SparkSQLColdDef(colname: String, sqlType: String, typeCheck: Option[Any => Unit] = None)
  object SparkSQLColdDef {
    def apply(colname: String, sqlType: String, typeCheck: Any => Unit): SparkSQLColdDef =
    SparkSQLColdDef(colname, sqlType, Some(typeCheck))
  }
}