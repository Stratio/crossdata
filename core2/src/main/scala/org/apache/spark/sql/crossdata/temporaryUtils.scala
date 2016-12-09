package org.apache.spark.sql.crossdata


trait XDLogging {
  // Log methods that take only a String
  protected def logInfo(msg: => String) = ()

  protected def logDebug(msg: => String) = ()

  protected def logTrace(msg: => String) = ()

  protected def logWarning(msg: => String) = ()

  protected def logError(msg: => String) = ()

  // Log methods that take Throwables (Exceptions/Errors) too
  protected def logInfo(msg: => String, throwable: Throwable) = ()

  protected def logDebug(msg: => String, throwable: Throwable) = ()

  protected def logTrace(msg: => String, throwable: Throwable) = ()

  protected def logWarning(msg: => String, throwable: Throwable) = ()

  protected def logError(msg: => String, throwable: Throwable) = ()
}
