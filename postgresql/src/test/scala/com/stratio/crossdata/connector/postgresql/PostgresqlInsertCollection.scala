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
package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import scala.collection.immutable.ListMap

trait PostgresqlInsertCollection extends PostgresqlWithSharedContext {

  override val Table = "studentsinserttest"

  override val schema = ListMap("id" -> "int", "age" -> "int", "comment" -> "text",
    "enrolled" -> "boolean", "name" -> "text")

  override val testData = (for (a <- 1 to 10) yield {
    a ::
      (10 + a) ::
      s"Comment $a" ::
      (a % 2 == 0) ::
      s"Name $a" ::Nil
  }).toList

  abstract override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"CREATE TEMPORARY TABLE $postgresqlSchema.$Table")

  override def defaultOptions = super.defaultOptions + ("dbtable" -> s"$postgresqlSchema.$Table")

}