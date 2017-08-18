package com.stratio.crossdata.driver.metadata

import org.apache.spark.sql.types.DataType

case class FieldMetadata(name: String, _type: DataType)