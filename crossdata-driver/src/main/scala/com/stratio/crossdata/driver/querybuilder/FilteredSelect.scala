/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.sql.querybuilder

class FilteredSelect(private[querybuilder] val relatedSelect: RelatedSelect, expressions: String) {

  def GroupBy(expressions: List[String] = List()): GroupedSelect = new GroupedSelect(this, expressions)

  def Having(expression: String = ""): HavingSelect = {
    new HavingSelect(new GroupedSelect(this, List()), expression)
  }

  def OrderBy(ordering: String = ""): OrderedSelect = {
    new OrderedSelect(Having(), ordering)
  }

  def Limit(expression: String = ""): LimitedSelect = {
    new LimitedSelect(OrderBy(), expression)
  }

  def build(): CompletedSelect = {
    val projectedSelect = relatedSelect.projectedSelect
    val initialSelect = projectedSelect.initialSelect

    CompletedSelect(
      initialSelect,
      projectedSelect,
      relatedSelect,
      Some(this))
  }

  override def toString: String = {
    if(expressions.isEmpty)
      s""
    else
      s"WHERE ${expressions} "
  }
}
