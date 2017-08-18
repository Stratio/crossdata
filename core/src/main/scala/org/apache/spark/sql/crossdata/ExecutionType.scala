package org.apache.spark.sql.crossdata

object ExecutionType extends Enumeration{
  type ExecutionType = Value
  val Default, Spark, Native = Value
}