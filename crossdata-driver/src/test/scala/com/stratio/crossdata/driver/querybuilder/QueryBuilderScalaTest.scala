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

package com.stratio.crossdata.driver.querybuilder

import org.scalatest.FunSuite
import org.testng.Assert._

/**
 * Created by dhiguero on 12/12/14.
 */
class QueryBuilderScalaTest extends FunSuite{


  test("selectFrom") {
    val expected: String = "SELECT * FROM table;"
    val s = QueryBuilder.selectAll.from("table")
    assertEquals(s.toString, expected, "Query does not match")
  }

  test("selectFrom2Columns") {
    val expected: String = "SELECT col1, col2 FROM table;"
    val s = QueryBuilder.select("col1", "col2").from("table")
    assertEquals(s.toString, expected, "Query does not match")
  }

  test("selectFromWhere") {
    val expected: String = "SELECT * FROM table WHERE id = 42;"
    val s = QueryBuilder.selectAll.from("table").where("id = 42")
    assertEquals(s.toString, expected, "Query does not match")
  }

}
