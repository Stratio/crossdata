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

/**
 * Join clause in a Select statement.
 */
class Join (parent : Select) extends SelectQuery(parent) {

  /**
   * String builder with the join on clauses.
   */
  val clauses : StringBuilder = new StringBuilder

  /**
   * Target table.
   */
  var tableName : String = ""

  /**
   * Class constructor.
   * @param table The target table.
   * @param joinType The type of join.
   * @param parent The parent select statement.
   */
  def this(table : String, joinType : String = "INNER",  parent : Select){
    this(parent)
    tableName = table
    clauses.append(" ").append(joinType).append(" JOIN ").append(tableName)
  }

  /**
   * Add an on clause to the join.
   * @param clause The clause as string.
   * @return The referenced Join element.
   */
  def on(clause : String) : Join = {
    if(clauses.contains("ON")){
      clauses.append(" AND ")
    }else{
      clauses.append(" ON ")
    }
    clauses.append(clause)
    this
  }

  /**
   * Get the string representation of the join clause.
   * @return A String.
   */
  def asString() : String = {
    clauses.toString
  }

}
