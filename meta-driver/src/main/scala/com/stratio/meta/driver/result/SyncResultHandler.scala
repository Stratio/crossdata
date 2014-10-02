/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.driver.result

import com.stratio.meta.common.data.CassandraResultSet
import com.stratio.meta.common.exceptions.{ExecutionException, ParsingException, UnsupportedException, ValidationException}
import com.stratio.meta.common.result._

/**
 * Synchronous result handler.
 */
class SyncResultHandler extends IResultHandler {

  var errorFound: Boolean = false

  var exception: Exception = null

  var allResults: Boolean = false

  var lastStatus: QueryStatus = QueryStatus.NONE

  var queryResult: QueryResult = null

  var nonQueryResult: Result = null

  override def processAck(queryId: String, status: QueryStatus): Unit = {
    lastStatus = status
    //println("ACK: " + lastStatus)
  }

  override def processResult(result: Result): Unit = synchronized {
    //println("Results received: " + result.getClass.toString)
    result match {
      case r: QueryResult =>
        if (queryResult == null) {
          queryResult = r
        } else {
          queryResult.getResultSet.asInstanceOf[CassandraResultSet].getRows.addAll(
            r.getResultSet.asInstanceOf[CassandraResultSet].getRows)
        }
      case _ =>
        nonQueryResult = result
        allResults = true;
    }
    allResults = true;
    notify()
  }

  override def processError(errorResult: Result): Unit = synchronized {
    val e = errorResult.asInstanceOf[ErrorResult]
    println("processError: " + e)
    if (ErrorType.PARSING.equals(e.getType)) {
      exception = new ParsingException(e.getErrorMessage)
    } else if (ErrorType.VALIDATION.equals(e.getType)) {
      exception = new ValidationException(e.getErrorMessage)
    } else if (ErrorType.EXECUTION.equals(e.getType)) {
      exception = new ExecutionException(e.getErrorMessage)
    } else if (ErrorType.NOT_SUPPORTED.equals(e.getType)) {
      exception = new UnsupportedException(e.getErrorMessage)
    } else {
      exception = new UnsupportedException(e.getErrorMessage)
    }

    errorFound = true;
    notify()
  }

  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  def waitForResult(): Result = synchronized {
    while (!errorFound && !allResults) {
      //println("Waiting for results, errorFound: " + errorFound + " allResults: " + allResults)
      wait()
    }
    if (errorFound) {
      throw exception
    }

    if (queryResult != null) {
      //println("QueryResult ksChanged: " + queryResult.isKsChanged)
      return queryResult
    }

    return nonQueryResult
  }
}
