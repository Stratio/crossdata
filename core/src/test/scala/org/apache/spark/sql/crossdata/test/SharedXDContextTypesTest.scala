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
import org.apache.spark.sql.crossdata.{XDDataFrame, ExecutionType}
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

/* Mix this trait in a type test class to get most of the type test done.
 * Its based on SharedXDContextWithDataTest thus filling most of that template slots and generating new entry points
 * focused on type testing.
 */
trait SharedXDContextTypesTest extends SharedXDContextWithDataTest {
  this: BaseXDTest =>

  import SharedXDContextTypesTest._

  //Template steps: Override them

  val emptyTypesSetError: String                       /* Error message to be shown when the types test data have not
                                                        * been properly inserted in the data source */
  def saveTypesData: Int                               // Entry point for saving types examples into the data source
  def sparkAdditionalKeyColumns: Seq[SparkSQLColdDef]  /* There are data sources which require their tables to have a
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
  def dataTypesSparkOptions: Map[String, String]       /* Especial SparkSQL options for type tables, it is equivalent to
                                                        * `defaultOptions` but will only apply in the registration of
                                                        * the types test table.
                                                        */


  //Template: This is the template implementation and shouldn't be modified in any specific test

  def doTypesTest(datasourceName: String): Unit = {
    for(executionType <- ExecutionType.Spark::ExecutionType.Native::Nil)
      datasourceName should s"provide the right types for $executionType execution" in {
        val dframe = sql("SELECT " + typesSet.map(_.colname).mkString(", ") + s" FROM $dataTypesTableName")
        for(
          (tpe, i) <- typesSet zipWithIndex;
          typeCheck <- tpe.typeCheck
        ) typeCheck(dframe.collect(executionType).head(i))
      }

    //TODO: Remove Multi-level column flat test when a better alternative to PR#257 has been found
    //Multi-level column flat test
    if(typesSet.map(_.colname) contains "structofstruct")
      it should "provide flattened column names through the `annotatedCollect` method" in {
        val dataFrame = sql("SELECT structofstruct.struct1.structField1 FROM typesCheckTable")
        val (_, colNames) = dataFrame.asInstanceOf[XDDataFrame].annotatedCollect()
        colNames.head shouldBe "structofstruct.struct1.structField1"
      }
  }

  abstract override def saveTestData: Unit = {
    super.saveTestData
    require(saveTypesData > 0, emptyTypesSetError)
  }

  protected def typesSet: Seq[SparkSQLColdDef] = Seq(
    SparkSQLColdDef("int", "INT", _ shouldBe a[java.lang.Integer]),
    SparkSQLColdDef("bigint", "BIGINT", _ shouldBe a[java.lang.Long]),
    SparkSQLColdDef("long", "LONG", _ shouldBe a[java.lang.Long]),
    SparkSQLColdDef("string", "STRING", _ shouldBe a[java.lang.String]),
    SparkSQLColdDef("boolean", "BOOLEAN", _ shouldBe a[java.lang.Boolean]),
    SparkSQLColdDef("double", "DOUBLE", _ shouldBe a[java.lang.Double]),
    SparkSQLColdDef("float", "FLOAT", _ shouldBe a[java.lang.Float]),
    SparkSQLColdDef("decimalint", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColdDef("decimallong", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColdDef("decimaldouble", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColdDef("decimalfloat", "DECIMAL", _ shouldBe a[java.math.BigDecimal]),
    SparkSQLColdDef("date", "DATE", _ shouldBe a[java.sql.Date]),
    SparkSQLColdDef("timestamp", "TIMESTAMP", _ shouldBe a[java.sql.Timestamp]),
    SparkSQLColdDef("tinyint", "TINYINT", _ shouldBe a[java.lang.Byte]),
    SparkSQLColdDef("smallint", "SMALLINT", _ shouldBe a[java.lang.Short]),
    SparkSQLColdDef("binary", "BINARY", _.asInstanceOf[Array[Byte]]),
    SparkSQLColdDef("arrayint", "ARRAY<INT>", _ shouldBe a[Seq[_]]),
    SparkSQLColdDef("arraystring", "ARRAY<STRING>", _ shouldBe a[Seq[_]]),
    SparkSQLColdDef("mapintint", "MAP<INT, INT>", _ shouldBe a[Map[_, _]]),
    SparkSQLColdDef("mapstringint", "MAP<STRING, INT>", _ shouldBe a[Map[_, _]]),
    SparkSQLColdDef("mapstringstring", "MAP<STRING, STRING>", _ shouldBe a[Map[_, _]]),
    SparkSQLColdDef("struct", "STRUCT<field1: INT, field2: INT>", _ shouldBe a[Row]),
    SparkSQLColdDef("arraystruct", "ARRAY<STRUCT<field1: INT, field2: INT>>", _ shouldBe a[Seq[_]]),
    SparkSQLColdDef("arraystructwithdate", "ARRAY<STRUCT<field1: DATE, field2: INT>>", _ shouldBe a[Seq[_]]),
    SparkSQLColdDef("structofstruct", "STRUCT<field1: DATE, field2: INT, struct1: STRUCT<structField1: STRING, structField2: INT>>", _ shouldBe a[Row]),
    SparkSQLColdDef("mapstruct", "MAP<STRING, STRUCT<structField1: DATE, structField2: INT>>", _ shouldBe a[Map[_,_]])
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