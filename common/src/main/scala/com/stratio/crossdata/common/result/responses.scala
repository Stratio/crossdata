/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
      case _: TimeoutException => Success(ErrorSQLResult(s"Not found answer to request: $id. Timeout was exceed.")) //TODO: Cancel the operation in the server
      case other: Exception => Success(ErrorSQLResult(other.getLocalizedMessage, Some(other)))
    } get
  }

  def cancelCommand(): Future[QueryCancelledReply] =
    throw new RuntimeException("The query cannot be cancelled. Use sql(query).cancelCommand")
}

object SQLResponse {
  implicit def sqlResponseToSQLResult(response: SQLResponse): SQLResult = response.waitForResult()
}
