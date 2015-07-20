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

package com.stratio.crossdata.driver.result

import com.stratio.crossdata.common.data.{ResultSet, Row}
import com.stratio.crossdata.common.metadata.ColumnMetadata
import com.stratio.crossdata.common.result.{ QueryResult, Result}
import org.apache.log4j.Logger

/**
 * Synchronous result handler compatible with paginated queries.
 */
class PaginationSyncDriverResultHandler extends SyncDriverResultHandler {

  override lazy val logger = Logger.getLogger(classOf[PaginationSyncDriverResultHandler])

  val partialResults  = scala.collection.mutable.Map.empty[Int, java.util.List[Row]]

  override def processResult(result: Result): Unit = synchronized {
    result match {
      case lResult: QueryResult => {
        partialResults.put(lResult.getResultPage, lResult.getResultSet.getRows)
        if (lResult.isLastResultSet) {
          queryResult = QueryResult.createQueryResult(lResult.getQueryId, resultSet(lResult.getResultPage, lResult.getResultSet.getColumnMetadata), 0, true)
          allResults = true
        }
      }
      case _ => {
        nonQueryResult = result
        allResults = true
      }
    }

    if(allResults) notify()
  }

  private[this] def resultSet(lastPage: Int, colMetadata: java.util.List[ColumnMetadata]) : ResultSet = {
    val rSet = new ResultSet()
    for ( i <- 0 to lastPage){
      rSet.getRows.addAll( partialResults(i))
    }
    rSet.setColumnMetadata(colMetadata)
    rSet
  }


/*
 //TODO more efficient paginationSyncDriver assuming ordered results
 override def processResult(result: Result): Unit = synchronized {
    result match {
      case r: QueryResult =>
        if (queryResult == null) {
          queryResult = r
        } else {
          queryResult.getResultSet.getRows.addAll(
            r.getResultSet.getRows)
          if(r.isLastResultSet) {
            queryResult.setLastResultSet(true)
            allResults = true
          }
        }
      case _ =>
        nonQueryResult = result
        allResults = true
    }

    if(allResults) notify()
  }*/

}
