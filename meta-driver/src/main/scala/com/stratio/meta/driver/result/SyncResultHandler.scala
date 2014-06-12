/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.driver.result

import com.stratio.meta.common.result._
import com.stratio.meta.common.exceptions.{ExecutionException, ValidationException, UnsupportedException, ParsingException}
import com.stratio.meta.common.data.{ResultSet, CassandraResultSet}

/**
 * Synchronous result handler.
 */
class SyncResultHandler extends IResultHandler{

  var errorFound : Boolean = false

  var exception : Exception = null

  var allResults : Boolean = false

  var lastStatus : QueryStatus = QueryStatus.NONE

  var queryResult : QueryResult = null

  var nonQueryResult : Result = null

  override def processAck(queryId: String, status: QueryStatus): Unit = {
    lastStatus = status
    //println("ACK: " + lastStatus)
  }

  override def processResult(result: Result): Unit = synchronized {
    //println("Results received: " + result.getClass.toString)
    if(result.isInstanceOf[QueryResult]){
      var r = result.asInstanceOf[QueryResult]
      if(queryResult == null){
        queryResult = r
      }else{
        queryResult.getResultSet.asInstanceOf[CassandraResultSet].getRows.addAll(
          r.getResultSet.asInstanceOf[CassandraResultSet].getRows)
      }
    }else{
      nonQueryResult = result
      allResults = true;
    }
    allResults = true;
    notify()
  }

  override def processError(errorResult: Result): Unit = synchronized {
    //println("Error found! " + errorResult.asInstanceOf[ErrorResult].getErrorMessage)
    val e = errorResult.asInstanceOf[ErrorResult]

    if(ErrorType.PARSING.equals(e.getType)){
      exception = new ParsingException(e.getErrorMessage)
    }else if(ErrorType.VALIDATION.equals(e.getType)){
      exception = new ValidationException(e.getErrorMessage)
    }else if(ErrorType.EXECUTION.equals(e.getType)){
      exception = new ExecutionException(e.getErrorMessage);
    }else if(ErrorType.NOT_SUPPORTED.equals(e.getType)){
      exception = new UnsupportedException(e.getErrorMessage);
    }else{
      exception = new UnsupportedException(e.getErrorMessage);
    }

    errorFound = true;
    notify()
    //println("processError: notifyAll")
  }

  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  def waitForResult() : Result = synchronized {
    while(!errorFound && !allResults){
      //println("Waiting for results, errorFound: " + errorFound + " allResults: " + allResults)
      wait()
    }
    if(errorFound){
      throw exception
    }

    if(queryResult != null){
      //println("QueryResult ksChanged: " + queryResult.isKsChanged)
      return queryResult
    }

    return nonQueryResult
  }
}
