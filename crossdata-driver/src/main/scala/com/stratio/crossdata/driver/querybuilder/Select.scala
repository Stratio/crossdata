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
 * Select query
 */
class Select extends Query {

  var selection : Option[Selection] = None

  var from : Option[String] = None

  var join : Option[Join] = None

  var where : Option[Where] = None

  var window : Option[String] = None

  var limit : Option[Long] = None

  def this(columnName : String){
    this()
    selection = Option(new Selection(List(columnName)))
  }

  def this(columnNames : Seq[String]){
    this()
    selection = Option(new Selection(columnNames.toList))
  }

  def this(selectedColumns : Selection){
    this()
    selection = Option(selectedColumns)
  }

  def from(tableName : String) : Select = {
    from = Option(tableName)
    this
  }

  def join(tableName : String) : Join = {
    innerJoin(tableName)
  }

  def innerJoin(tableName: String) : Join = {
    join = Option(new Join(tableName, "INNER", this))
    join.get
  }

  def withWindow(window: String) : Select = {
    this.window = Option(window)
    this
  }

  def where(clause : String) : Where = {
    where = Option(new Where(clause, this))
    where.get
  }

  def limit(queryLimit : Long) : Select = {
    limit = Option(queryLimit)
    this
  }

  override def toString() : String = {
    val sb : StringBuilder = new StringBuilder("SELECT ");
    if(selection.nonEmpty){
      sb.append(selection.get.asString)
    }

    if(from.nonEmpty){
      sb.append(" FROM ").append(from.get)
    }

    if(window.nonEmpty){
      sb.append(" WITH WINDOW ").append(window.get)
    }

    if(join.nonEmpty){
      sb.append(join.get.asString)
    }

    if(where.nonEmpty){
      sb.append(" WHERE ").append(where.get.asString)
    }

    if(limit.nonEmpty){
      sb.append(" LIMIT ").append(limit.get)
    }

    sb.toString()
  }
}
