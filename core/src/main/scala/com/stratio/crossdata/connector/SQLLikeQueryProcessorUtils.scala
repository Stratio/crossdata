package com.stratio.crossdata.connector

trait SQLLikeQueryProcessorUtils {
  type ProcessingContext
  def quoteString(in: Any)(implicit context: ProcessingContext): String
}
