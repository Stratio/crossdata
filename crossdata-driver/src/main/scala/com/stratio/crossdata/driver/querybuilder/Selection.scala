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
//object Selection {
//
//  def select(columnName: String) : Selection = {
//    new Selection(List(columnName))
//  }
//
//  @annotation.varargs
//  def select(columnNames : String*) : Selection = {
//    new Selection(columnNames.toList)
//  }
//
//  def select(columnNames : java.util.List[String]) : Selection = {
//    new Selection(columnNames.asScala.toList)
//  }
//
//}

class Selection(selectionList : List[String]){

  def selectAs(columnName : String, alias : String) : Selection = {
    new Selection(selectionList ++ List(columnName + " AS " + alias))
  }

  def asString() : String = {
    selectionList.mkString(", ")
  }

}