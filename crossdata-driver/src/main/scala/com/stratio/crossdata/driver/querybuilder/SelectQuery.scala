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
 * Select query class that enables specific clauses to access
 * parent methods on the Select statement.
 */
class SelectQuery (parent : Select) extends Select{

  override def limit(queryLimit : Long) : Select = {
    parent.limit(queryLimit)
  }

  override def where(clause : String) : Where = {
    parent.where(clause)
  }

  override def groupBy(clause : String) : Select = {
    parent.groupBy(clause)
  }

  override def orderBy(clause : String) : Select = {
    parent.orderBy(clause)
  }

  override def toString() : String = {
    parent.toString
  }
}
