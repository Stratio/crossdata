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

class OrderedSelect(private[querybuilder] val havingSelect: HavingSelect, ordering: String) {

  def Limit(expression: String = ""): LimitedSelect = new LimitedSelect(this, expression)

  def build(): CompletedSelect = {
    val groupedSelect = havingSelect.groupedSelect
    val filteredSelect = groupedSelect.filteredSelect
    val relatedSelect = filteredSelect.relatedSelect
    val projectedSelect = relatedSelect.projectedSelect
    val initialSelect = projectedSelect.initialSelect

    CompletedSelect(
      initialSelect,
      projectedSelect,
      relatedSelect,
      Some(filteredSelect),
      Some(groupedSelect),
      Some(havingSelect),
      Some(this))
  }

  override def toString: String = {
    if(ordering.isEmpty)
      s""
    else
      s"ORDER BY ${ordering} "
  }
}
