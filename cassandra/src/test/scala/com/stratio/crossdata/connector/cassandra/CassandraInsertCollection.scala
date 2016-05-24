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
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

import scala.collection.immutable.ListMap
import scala.collection.JavaConverters._

trait CassandraInsertCollection extends CassandraWithSharedContext {

  override val Table = "studentsinserttest"

  override val schema = ListMap("id" -> "int", "age" -> "int", "comment" -> "text",
                                "enrolled" -> "boolean", "name" -> "text", "array_test" ->"list<text>", "map_test" -> "map<text,text>")

  override val pk = "(id)" :: "age" ::  Nil

  override val testData = (for (a <- 1 to 10) yield {
    a :: (10 + a) :: s"'Comment $a'" :: (a % 2 == 0) :: s"'Name $a'" :: List(a, a+1, a+2) :: Map("x" -> (a + 1), "y" -> (a + 2)) :: Nil
  }).toList

  abstract override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"CREATE TEMPORARY TABLE $Table")

  override def defaultOptions = super.defaultOptions + ("table" -> Table)

}