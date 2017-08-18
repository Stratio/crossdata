package com.stratio.crossdata.driver.exceptions

case class TLSInvalidAuthException(msg: String, e: Throwable = null) extends Exception(msg, e)
