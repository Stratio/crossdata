package org.apache.spark.sql.crossdata.models

object EphemeralExecutionStatus extends Enumeration {

  type Status = Value
  val NotStarted, Starting, Started, Stopping, Stopped, Error = Value
}
