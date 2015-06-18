package com.stratio.crossdata.driver.utils

import com.stratio.crossdata.common.result.IDriverResultHandler

class QueryData(val resultHandler: IDriverResultHandler, val queryId: String , val query: String, val sessionId: String,
                var startTime:Long, var endTime:Long, var status: String) {

  val init = System.currentTimeMillis()

  
  def getExecutionTime(): java.lang.Long = {
    (System.currentTimeMillis() - init) / 1000
  }

  def setStatus(newStatus :String)={
    status=newStatus
  }

  def setEndTime(end :Long)= endTime=end

  def getQueryDuration():Long= {
    if (endTime!=0)
      (endTime - startTime)/1000
    else
      getExecutionTime()
  }

  def getCompleteExecutionInfo(): String = {
    val sb = new StringBuilder
    sb.append(System.lineSeparator())
    sb.append("Query Id: " + queryId + System.lineSeparator())
    sb.append("Query: " + query + System.lineSeparator())
    sb.append("Execution time: " + getQueryDuration() + " seconds" + System.lineSeparator())
    sb.append("Status: ").append(status).append(System.lineSeparator())
    sb.toString
  }

  def getExecutionInfo(): String = {
    val sb = new StringBuilder
    sb.append(System.lineSeparator())
    sb.append("Query Id: " + queryId + System.lineSeparator())
    sb.append("Status: ").append(status).append(System.lineSeparator())
    sb.toString
  }

}
