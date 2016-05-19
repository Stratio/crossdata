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
package org.apache.spark.sql.crossdata.test

import com.stratio.crossdata.test.BaseXDTest

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.{XDDataFrame, ExecutionType}
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import org.apache.spark.sql.types.{StructType, ArrayType, StructField}

/* Mix this trait in a type test class to get most of the type test done.
 * Its based on SharedXDContextWithDataTest thus filling most of that template slots and generating new entry points
 * focused on type testing.
 */
trait SharedXDContextTypesTest extends SharedXDContextWithDataTest {
  this: BaseXDTest =>

  import SharedXDContextTypesTest._

  //Template steps: Override them

  val emptyTypesSetError: String                                /* Error message to be shown when the types test data have not
                                                                 * been properly inserted in the data source */
  def saveTypesData: Int                                        // Entry point for saving types examples into the data source
  def sparkAdditionalKeyColumns: Seq[SparkSQLColDef] = Seq()   /* There are data sources which require their tables to have a
                                                                 * primary key. This entry point allows specifying primary keys
                                                                 * columns.
                                                                 * NOTE that these `SparkSQLColdDef`s shouldn't have type checker
                                                                 * since the column type does not form part of the test.
                                                                 * e.g:
                                                                 *   override def sparkAdditionalKeyColumns(
                                                                 *                                           "k",
                                                                 *                                           "INT PRIMARY KEY"
                                                                 *                                         )
                                                                 */
  def dataTypesSparkOptions: Map[String, String]                /* Especial SparkSQL options for type tables, it is equivalent to
                                                                 * `defaultOptions` but will only apply in the registration of
                                                                 * the types test table.
                                                                 */


  //Template: This is the template implementation and shouldn't be modified in any specific test

  def doTypesTest(datasourceName: String): Unit = {
    for(executionType <- ExecutionType.Spark::ExecutionType.Native::Nil)
      datasourceName should s"provide the right types for $executionType execution" in {
        assumeEnvironmentIsUpAndRunning
        val dframe = sql("SELECT " + typesSet.map(_.colname).mkString(", ") + s" FROM $dataTypesTableName")
        for(
          (tpe, i) <- typesSet zipWithIndex;
          typeCheck <- tpe.typeCheck
        ) typeCheck(dframe.collect(executionType).head(i))
      }

    //Multi-level column flat test

    it should "provide flattened column names through the `annotatedCollect` method" in {
      val dataFrame = sql("SELECT structofstruct.struct1.structField1 FROM typesCheckTable")
      val rows = dataFrame.flattenedCollect()
      rows.head.schema.head.name shouldBe "structofstruct.struct1.structField1"
    }

    it should "be able to flatten results for LIMIT queries" in {
      val dataFrame = sql("SELECT structofstruct FROM typesCheckTable LIMIT 1")
      val rows = dataFrame.flattenedCollect()
      rows.head.schema.head.name shouldBe "structofstruct.field1"
      rows.length shouldBe 1
    }

    it should "be able to vertically flatten results for array columns" in {
      val dataFrame = sql(s"SELECT arraystructarraystruct FROM typesCheckTable")
      val res = dataFrame.flattenedCollect()

      // No array columns should be found in the result schema
      res.head.schema filter {
        case StructField(_, _: ArrayType, _, _) => true
        case _ => false
      } shouldBe empty

      // No struct columns should be found in the result schema
      res.head.schema filter {
        case StructField(_, _: StructType, _, _) => true
        case _ => false
      } shouldBe empty

    }

    it should "correctly apply user limits to a vertically flattened array column" in {
      val dataFrame = sql(s"SELECT arraystructarraystruct FROM typesCheckTable LIMIT 1")
      val res = dataFrame.flattenedCollect()
      res.length shouldBe 1
    }

    it should "correctly apply user limits to project-less queries where arrays are getting flattened" in {
      val dataFrame = sql(s"SELECT * FROM typesCheckTable LIMIT 1")
      val res = dataFrame.flattenedCollect()
      res.length shouldBe 1
    }

  }

  abstract override def saveTestData: Unit = {
    super.saveTestData
    require(saveTypesData > 0, emptyTypesSetError)
  }

  protected def typesSet: Seq[SparkSQLColDef] = Seq(
    SparkSQLColDef("int", "INT", _ shouldBe a[java.lang.Integer]),
    SparkSQLColDef("bigint", "BIGINT", _ shouldBe a[java.lang.Long]),
    SparkSQLColDef("long", "LONG", _ shouldBe a[java.lang.Long]),
    SparkSQLColDef("string", "STRING", _ shouldBe a[java.lang.String]),
    SparkSQLColDef("boolean", "BOOLEAN", _ shouldBe a[java.lang.Boolean]),
    SparkSQLColDef("double", "DOUBLE", _ shouldBe a[java.lang.Double]),
    SparkSQLColDef("float", "FLOAT", _ shouldBe a[java.lang.Float]),
    SparkSQLColDef("decimalint", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColDef("decimallong", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColDef("decimaldouble", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColDef("decimalfloat", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColDef("date", "DATE", _ shouldBe a[java.sql.Date]),
    SparkSQLColDef("timestamp", "TIMESTAMP", _ shouldBe a[java.sql.Timestamp]),
    SparkSQLColDef("tinyint", "TINYINT", _ shouldBe a[java.lang.Byte]),
    SparkSQLColDef("smallint", "SMALLINT", _ shouldBe a[java.lang.Short]),
    SparkSQLColDef("binary", "BINARY", _.asInstanceOf[Array[Byte]]),
    SparkSQLColDef("arrayint", "ARRAY<INT>", _ shouldBe a[Seq[_]]),
    SparkSQLColDef("arraystring", "ARRAY<STRING>", _ shouldBe a[Seq[_]]),
    SparkSQLColDef("mapintint", "MAP<INT, INT>", _ shouldBe a[Map[_, _]]),
    SparkSQLColDef("mapstringint", "MAP<STRING, INT>", _ shouldBe a[Map[_, _]]),
    SparkSQLColDef("mapstringstring", "MAP<STRING, STRING>", _ shouldBe a[Map[_, _]]),
    SparkSQLColDef("struct", "STRUCT<field1: INT, field2: INT>", _ shouldBe a[Row]),
    SparkSQLColDef("arraystruct", "ARRAY<STRUCT<field1: INT, field2: INT>>", _ shouldBe a[Seq[_]]),
    SparkSQLColDef("arraystructwithdate", "ARRAY<STRUCT<field1: DATE, field2: INT>>", _ shouldBe a[Seq[_]]),
    SparkSQLColDef("structofstruct", "STRUCT<field1: DATE, field2: INT, struct1: STRUCT<structField1: STRING, structField2: INT>>", _ shouldBe a[Row]),
    SparkSQLColDef("mapstruct", "MAP<STRING, STRUCT<structField1: DATE, structField2: INT>>", _ shouldBe a[Map[_,_]]),
    SparkSQLColDef(
      "arraystructarraystruct",
      "ARRAY<STRUCT<stringfield: STRING, arrayfield: ARRAY<STRUCT<field1: INT, field2: INT>>>>",
      { res =>
        res shouldBe a[Seq[_]]
        res.asInstanceOf[Seq[_]].head shouldBe a[Row]
        res.asInstanceOf[Seq[_]].head.asInstanceOf[Row].get(1) shouldBe a[Seq[_]]
        res.asInstanceOf[Seq[_]].head.asInstanceOf[Row].get(1).asInstanceOf[Seq[_]].head shouldBe a[Row]
      }
    )
  )

  override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+ {
    val fields = (sparkAdditionalKeyColumns ++ typesSet) map {
      case SparkSQLColDef(name, tpe, _) => s"$name $tpe"
    } mkString ", "
    SparkTable(s"CREATE TEMPORARY TABLE $dataTypesTableName ( $fields )", dataTypesSparkOptions)
  }

}

object SharedXDContextTypesTest {
  val dataTypesTableName = "typesCheckTable"
  case class SparkSQLColDef(colname: String, sqlType: String, typeCheck: Option[Any => Unit] = None)
  object SparkSQLColDef {
    def apply(colname: String, sqlType: String, typeCheck: Any => Unit): SparkSQLColDef =
    SparkSQLColDef(colname, sqlType, Some(typeCheck))
  }
}
