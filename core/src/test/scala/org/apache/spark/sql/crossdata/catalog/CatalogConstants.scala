package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.types._

trait CatalogConstants {
  val Database = "database"
  val TableName = "tableName"
  val ViewName = "viewName"
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
  val sqlView = s"select $Field1Name from $Database.$TableName"
}
