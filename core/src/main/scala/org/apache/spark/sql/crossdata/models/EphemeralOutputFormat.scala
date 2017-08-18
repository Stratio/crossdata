package org.apache.spark.sql.crossdata.models

object EphemeralOutputFormat extends Enumeration {

  type Status = Value
  val ROW, JSON = Value
}

