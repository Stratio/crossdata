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

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

/**
 * Selection builder
 */
class Selection(){

  /**
   * String builder with the selected columns.
   */
  val selected : StringBuilder = new StringBuilder

  /**
   * Class constructor.
   * @param columnName The first column to be selected.
   */
  def this(columnName : String){
    this()
    selected.append(columnName)
  }

  /**
   * Class constructor.
   * @param selectionList The list of columns to be selected.
   */
  def this(selectionList : List[String]){
    this()
    selected.append(selectionList.mkString(", "))
  }

  /**
   * Add a new column.
   * @param columnName The column name.
   * @return The resulting Selection.
   */
  def and(columnName : String) : Selection = {
    addSeparator()
    selected.append(columnName)
    this
  }

  /**
   * Add a new column with an alias.
   * @param columnName The column name.
   * @param alias The alias.
   * @return The resulting Selection.
   */
  def and(columnName : String, alias : String) : Selection = {
    addSeparator()
    selected.append(columnName + " AS " + alias)
    this
  }

  /**
   * Get the string representation of the selected columns.
   * @return A String.
   */
  def asString() : String = {
    selected.toString
  }

  private def addSeparator() : Unit = {
    if(selected.size > 0){
      selected.append(", ");
    }
  }

}