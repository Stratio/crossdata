/**
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

package com.stratio.crossdata.common

import java.util.UUID

import com.stratio.crossdata.common.security.Session
import org.apache.spark.sql.Row

import scala.concurrent.duration.FiniteDuration

trait Command

case class SQLCommand(
                       query: String,
                       queryId: UUID = UUID.randomUUID(),
                       retrieveColumnNames: Boolean = false,
                       timeout: Option[FiniteDuration] = None
                     ) extends Command {

  def this(
            query: String,
            queryId: UUID,
            retrieveColumnNames: Boolean,
            timeout: FiniteDuration
          ) = this(query, queryId, retrieveColumnNames, Option(timeout))

  def this(
            query: String,
            queryId: UUID,
            retrieveColumnNames: Boolean
          ) = this(query, queryId, retrieveColumnNames, None)


}

trait ControlCommand extends Command {
  val queryId: UUID
}
case class CancelQueryExecution(queryId: UUID) extends ControlCommand
case class GetJobStatus(queryId: UUID) extends ControlCommand

case class SecureCommand(cmd: Command, session: Session)

trait Result extends Serializable {
  val queryId: UUID
  def hasError: Boolean
}

trait SQLResult extends Result {
  def resultSet: Array[Row]
}

