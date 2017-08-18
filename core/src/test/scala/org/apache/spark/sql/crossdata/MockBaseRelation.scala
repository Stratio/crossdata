package org.apache.spark.sql.crossdata

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.BaseRelation
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}

class MockBaseRelation extends BaseRelation with Serializable {

  override def sqlContext: SQLContext = ???

  override def schema: StructType = StructType(List(StructField("id", IntegerType)))
}
