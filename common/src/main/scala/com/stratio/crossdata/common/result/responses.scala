/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.common.result

import java.util.UUID

import com.stratio.crossdata.common.QueryCancelledReply

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, TimeoutException}
import scala.util.{Success, Try}

trait Response extends Serializable {
  def id: UUID
}

// TODO id (requestId) should be removed from the API (version 2.0)
case class SQLResponse(id: UUID, sqlResult: Future[SQLResult]) extends Response {

  def waitForResult(duration: Duration = Duration.Inf): SQLResult = {
    Try {
      Await.result(sqlResult, duration) : SQLResult
    } recoverWith {
      case _: TimeoutException => Success(ErrorSQLResult(s"Not found answer to request: $id. Timeout was exceed."))
      case other: Throwable => Success(ErrorSQLResult(other.getLocalizedMessage, Some(other)))
    } get
  }

  def cancelCommand(): Future[QueryCancelledReply] =
    throw new RuntimeException("The query cannot be cancelled. Use sql(query).cancelCommand")
}

object SQLResponse {
  implicit def sqlResponseToSQLResult(response: SQLResponse): SQLResult = response.waitForResult()
}
