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

package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.types._

trait CatalogConstants {
  val Database = "database"
  val TableName = "tableName"
  val AnotherTable = "anotherTable"
  val Field1Name = "column1"
  val Field2Name = "column2"
  val FieldWitStrangeChars = "1+x"
  val SubField1Name = "subcolumn1"
  val SubField2Name = "subcolumn2"
  val FieldWithSubcolumnsName = "columnWithSubcolumns"
  val Field1 = StructField(Field1Name, StringType, nullable = true)
  val Field2 = StructField(Field2Name, StringType, nullable = true)
  val SubField = StructField(SubField1Name, StringType, nullable = true)
  val SubField2 = StructField(SubField2Name, StringType, nullable = true)
  val arrayField = StructField(SubField2Name, ArrayType(StringType), nullable = true)
  val arrayFieldIntegers = StructField(SubField2Name, ArrayType(IntegerType), nullable = true)
  val arrayFieldWithSubDocs = StructField(FieldWitStrangeChars, ArrayType(StructType(Seq(Field1, Field2))))
  val SourceDatasource = "org.apache.spark.sql.json"
  val Fields = Seq[StructField](Field1, Field2)
  val SubFields = Seq(SubField, SubField2)
  val Columns = StructType(Fields)
  val ColumnsWithSubColumns = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true)) )
  val ColumnsWithArrayString = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true), arrayField) )
  val ColumnsWithArrayInteger = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true), arrayFieldIntegers) )
  val ColumnsWithArrayWithSubdocuments = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true), arrayFieldWithSubDocs) )
  val ColumnsWithMapWithArrayWithSubdocuments = StructType(Seq(StructField(Field1Name, MapType(ColumnsWithSubColumns, ColumnsWithArrayWithSubdocuments))))
  val OptsJSON = Map("path" -> "/fake_path")
}
