package com.stratio.crossdata.common.result

import java.util.UUID

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

trait Response extends Serializable {
  def id: UUID
}

case class SQLResponse (id: UUID, sqlResult: Future[SQLResult]) extends Response  {
  def waitForResult(duration: Duration = Duration.Inf): SQLResult = {
    Try {
      Await.result(sqlResult, duration)
    } getOrElse ErrorSQLResult(s"Not found answer to query $id. Timeout was exceed.")
  }

  def cancelCommand(): Unit = throw new RuntimeException("The query cannot be cancelled. Use sql(query).cancelCommand")
}

case class QueryCancelledResponse(id: UUID) extends Response

object SQLResponse {
  implicit def sqlResponseToSQLResult(response: SQLResponse): SQLResult = response.waitForResult()
}
