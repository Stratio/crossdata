package com.stratio.crossdata.driver.utils

import com.stratio.crossdata.common.result.IDriverResultHandler

class QueryData(val resultHandler: IDriverResultHandler, val query: String, val sessionId: String) {

  val init = System.currentTimeMillis()
  
  private def getExecutionTime(): java.lang.Long = {
    (System.currentTimeMillis() - init) / 1000
  }

  def getExecutionInfo(): String = {
    val sb = new StringBuilder
    sb.append(System.lineSeparator())
    sb.append("Session Id: " + sessionId + System.lineSeparator())
    sb.append("Query: " + query + System.lineSeparator())
    sb.append("Execution time: " + getExecutionTime() + " seconds" + System.lineSeparator())
    sb.append(System.lineSeparator())
    sb.toString
  }

}
