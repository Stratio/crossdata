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

import com.stratio.crossdata.driver.querybuilder.dslentities.{And, CombinationInfo, SortCriteria, XDQLStatement}

object RunnableQuery {

  implicit class RunnableQueryAsExpression(runnableQuery: RunnableQuery) extends Expression {
    override private[querybuilder] def toXDQL: String = s"( ${runnableQuery.toXDQL})"
  }

  implicit class RunnableQueryAsRelation(runnableQuery: RunnableQuery) extends Relation {
    override private[querybuilder] def toXDQL: String = s"( ${runnableQuery.toXDQL})"
  }

}

abstract class RunnableQuery protected(protected val projections: Seq[Expression],
                                       protected val relation: Relation,
                                       protected val filters: Option[Predicate] = None,
                                       protected val groupingExpressions: Seq[Expression] = Seq.empty,
                                       protected val havingExpressions: Option[Predicate] = None,
                                       protected val ordering: Option[SortCriteria] = None,
                                       protected val limit: Option[Int] = None,
                                       protected val composition: Option[CombinationInfo] = None
                                        ) extends Combinable {

  def where(condition: String): this.type = where(XDQLStatement(condition))

  // It has to be abstract (simple runnable query has transitions) and concrete
  // implementations (grouped, limited, sorted...) should return their own type
  def where(condition: Predicate): this.type

  protected def combinePredicates(newCondition: Predicate): Predicate =
    filters.map(And(_, newCondition)).getOrElse(newCondition)

  override private[querybuilder] def toXDQL: String = {
    def stringfy[T](head: String, elements: Seq[T], element2str: T => String): String =
      elements.headOption.fold("")(_ => s"$head ${elements.map(element2str) mkString ", "}")

    def stringfyXDQL(head: String, elements: Seq[CrossdataSQLStatement]) =
      stringfy[CrossdataSQLStatement](head, elements, _.toXDQL)

    //Intentionally this way spaced
    s"""
        |SELECT ${projections map (_.toXDQL) mkString ", "}
        | FROM ${relation.toXDQL}
        | ${stringfyXDQL(" WHERE", filters.toSeq)}
        |${stringfyXDQL(" GROUP BY", groupingExpressions)}
        |${stringfyXDQL(" HAVING", havingExpressions.toSeq)}
        |${stringfyXDQL("", ordering.toSeq)}
        |${stringfy[Int](" LIMIT", limit.toSeq, _.toString)}
        |${composition.fold("")(_.toXDQL)}
     """.stripMargin.replace(System.lineSeparator(), "")
    //.replaceAll ("""\s\s+""", " ").trim
  }

  def build: String = toXDQL

}
