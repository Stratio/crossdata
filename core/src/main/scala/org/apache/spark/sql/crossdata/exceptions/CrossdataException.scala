package org.apache.spark.sql.crossdata.exceptions

class CrossdataException(message: String, cause: Throwable)
  extends Exception(message, cause) {

  def this(message: String) = this(message, null)
}

/**
 * Exception thrown when a Native [[org.apache.spark.sql.crossdata.ExecutionType]] fails.
 */
private[spark] class NativeExecutionException
  extends CrossdataException("The operation cannot be executed without Spark")
