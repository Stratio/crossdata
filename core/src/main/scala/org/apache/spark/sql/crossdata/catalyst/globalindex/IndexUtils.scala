/*
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
package org.apache.spark.sql.crossdata.catalyst.globalindex

import org.apache.spark.sql.catalyst.analysis.UnresolvedAttribute
import org.apache.spark.sql.catalyst.expressions.{And, AttributeReference, Contains, EqualTo, Expression, GreaterThan, GreaterThanOrEqual, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Or, Predicate, StartsWith}

import scala.annotation.tailrec

object IndexUtils {

  /**
    * Return if all  attribute in the exprs are indexed columns
    *
    * @param condition filter.condition
    * @param indexedCols
    * @return
    */
  def areAllAttributeIndexedInExpr(condition: Expression, indexedCols: Seq[String]): Boolean = {

    @tailrec
    def checkIfRemainExprAreSupported(remainExpr: Seq[Expression]): Boolean =
      remainExpr match {
        case seq if seq.isEmpty => true
        case nonEmptySeq =>
          nonEmptySeq.head match {
            case predicate: Predicate if !isSupportedPredicate(predicate) =>
              false
            case UnresolvedAttribute(name) if !indexedCols.contains(name.last) =>
              false // TODO TOFIX subdocuments can cause conflicts
            case AttributeReference(name, _, _, _) if !indexedCols.contains(name) =>
              false
            case head if head.children.nonEmpty =>
              checkIfRemainExprAreSupported(remainExpr.tail ++ remainExpr.head.children)
            case _ => checkIfRemainExprAreSupported(remainExpr.tail)
          }
      }

    checkIfRemainExprAreSupported(Seq(condition))
  }

  /**
    * Check if predicate is supported by ElasticSearch native queries to use the index
    *
    * @param predicate
    * @return
    */
  def isSupportedPredicate(predicate: Predicate): Boolean = predicate match {
    //TODO: Add more filters?? Reference: ElasticSearchQueryProcessor
    case _: And => true
    case _: Contains => true
    case _: EqualTo => true
    case _: GreaterThan => true
    case _: GreaterThanOrEqual => true
    case _: In => true
    case _: IsNull => true
    case _: IsNotNull => true
    case _: LessThan => true
    case _: LessThanOrEqual => true
    case _: Or => true
    case _: StartsWith => true

    case _ => false
  }

}
