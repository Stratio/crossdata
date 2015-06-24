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
 * Select query builder.
 */
class Select extends Query {

  /**
   * The selected columns.
   */
  var selection : Option[Selection] = None

  /**
   * The target table in the from clause.
   */
  var from : Option[String] = None

  /**
   * The join clause.
   */
  var join : Option[Join] = None

  /**
   * The where clause.
   */
  var where : Option[Where] = None

  /**
   * The window clause.
   */
  var window : Option[String] = None

  /**
   * The group by clause.
   */
  var groupBy : Option[String] = None

  /**
   * The order by clause.
   */
  var orderBy : Option[String] = None

  /**
   * The limit clause.
   */
  var limit : Option[Long] = None

  /**
   * Class constructor.
   * @param columnName The name of the column to be selected.
   */
  def this(columnName : String){
    this()
    selection = Option(new Selection(List(columnName)))
  }

  /**
   * Class constructor.
   * @param columnNames A sequence of string column names.
   */
  def this(columnNames : Seq[String]){
    this()
    selection = Option(new Selection(columnNames.toList))
  }

  /**
   * Class constructor.
   * @param selectedColumns The Selection with the required columns.
   */
  def this(selectedColumns : Selection){
    this()
    selection = Option(selectedColumns)
  }

  /**
   * Add a from clause to the current query.
   * @param tableName The target table name.
   * @return The current Select object.
   */
  def from(tableName : String) : Select = {
    from = Option(tableName)
    this
  }

  /**
   * Add a join clause to the current query.
   * @param tableName The target table to perform the join.
   * @return A default Join.
   */
  def join(tableName : String) : Join = {
    innerJoin(tableName)
  }

  /**
   * Add an inner join clause to the current query.
   * @param tableName The target table to perform the join.
   * @return An inner Join
   */
  def innerJoin(tableName: String) : Join = {
    join = Option(new Join(tableName, "INNER", this))
    join.get
  }

  /**
   * Add a window clause to the current query.
   * @param window The window.
   * @return The current Select object.
   */
  def withWindow(window: String) : Select = {
    this.window = Option(window)
    this
  }

  /**
   * Add a where clause to the current query.
   * @param clause The where clause.
   * @return A Where object.
   */
  def where(clause : String) : Where = {
    where = Option(new Where(clause, this))
    where.get
  }

  /**
   * Add a group by clause to the current query.
   * @param clause The group by clause.
   * @return The current Select object.
   */
  def groupBy(clause : String) : Select = {
    groupBy = Option(clause)
    this
  }

  /**
   * Add an order by clause to the current query.
   * @param clause The order by clause.
   * @return The current Select object.
   */
  def orderBy(clause : String) : Select = {
    orderBy = Option(clause)
    this
  }

  /**
   * Add a limit clause to the current query.
   * @param queryLimit The limit.
   * @return The current Select object.
   */
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

    if(orderBy.nonEmpty){
      sb.append(" ORDER BY ").append(orderBy.get)
    }

    if(groupBy.nonEmpty){
      sb.append(" GROUP BY ").append(groupBy.get)
    }

    if(limit.nonEmpty){
      sb.append(" LIMIT ").append(limit.get)
    }

    sb append ";"

    sb mkString
  }
}
