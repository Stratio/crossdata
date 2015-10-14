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
package com.stratio.crossdata.sql.querybuilder

class RelatedSelect(private[querybuilder] val projectedSelect: ProjectedSelect, relations: List[String]) {

  def where(expression: String = ""): FilteredSelect = new FilteredSelect(this, expression)

  def groupBy(expressions: List[String] = List()): GroupedSelect = {
    new GroupedSelect(new FilteredSelect(this, ""), expressions)
  }

  def having(expressions: String = ""): HavingSelect = {
    new HavingSelect(groupBy(), expressions)
  }

  def orderBy(ordering: String = ""): OrderedSelect = {
    new OrderedSelect(having(), ordering)
  }

  def limit(expression: String = ""): LimitedSelect = {
    new LimitedSelect(orderBy(), expression)
  }

  def build(): CompletedSelect = {
    val initialSelect = projectedSelect.initialSelect

    CompletedSelect(
      initialSelect,
      projectedSelect,
      this)
  }

  override def toString: String = s"FROM ${relations.mkString(", ")} "
}
