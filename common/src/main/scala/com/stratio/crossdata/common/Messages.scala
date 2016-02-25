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

import org.apache.spark.sql.Row

import scala.concurrent.duration.FiniteDuration

trait Command {
  val commandId:UUID=UUID.randomUUID()
  def timeout:Option[FiniteDuration]

}

case class SQLCommand(
                       query: String,
                       retrieveColumnNames: Boolean = false,
                       timeout: Option[FiniteDuration] = None
                     ) extends Command {

  def this(
            query: String,
            retrieveColumnNames: Boolean,
            timeout: FiniteDuration
            ) = this(query, retrieveColumnNames, Option(timeout))

  def this(
            query: String,
            retrieveColumnNames: Boolean
          ) = this(query, retrieveColumnNames, None)


}

case class AddJARCommand(
                          path: String,
                          timeout: Option[FiniteDuration] = None
                        ) extends Command {
  def this(
            path: String,
            timeout: FiniteDuration
          ) = this(path,  Option(timeout))

  def this(
            path: String
          ) = this(path,   None)

}

trait Result extends Serializable {
  val queryId: UUID
  def hasError: Boolean
}

trait SQLResult extends Result {
  def resultSet: Array[Row]
}

