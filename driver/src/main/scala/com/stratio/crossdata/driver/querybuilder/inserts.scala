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

package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.InitialSelectPhrases
import com.stratio.crossdata.driver.querybuilder.dslentities.{AsteriskExpression, XDQLStatement}

object InsertMode extends Enumeration {
  type InsertMode = Value
  val OVERWRITE = Value("OVERWRITE")
  val INTO = Value("INTO")
}

class Insert {
  def into(rel: Relation): ConfiguredInsert = new ConfiguredInsert(rel, InsertMode.INTO)
  def overwrite(rel: Relation): ConfiguredInsert = new ConfiguredInsert(rel, InsertMode.OVERWRITE)

  private[Insert] class ConfiguredInsert(val target: Relation, mode: InsertMode.InsertMode) extends InitialSelectPhrases
  {
    val context: String => String = qStr => s"INSERT $mode ${target.toXDQL} $qStr"

    override def select(projections: Expression*): ProjectedSelect = select(projections, context)
    override def select(projections: String): ProjectedSelect = select(XDQLStatement(projections)::Nil, context)
    override def selectAll: ProjectedSelect = select(AsteriskExpression()::Nil, context)
  }

  private[Insert] class RunnableInsert

}