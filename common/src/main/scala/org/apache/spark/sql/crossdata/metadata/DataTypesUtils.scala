package org.apache.spark.sql.crossdata.metadata

import org.apache.spark.sql.types.{DataType, DataTypeParser}


object DataTypesUtils {
  def toDataType(stringType: String): DataType = DataTypeParser.parse(stringType)
}
