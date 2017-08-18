/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
    def checkIfRemainExprAreSupported(remainExpr: Seq[Expression]): Boolean = remainExpr match {
      case seq if seq.isEmpty => true
      case nonEmptySeq => nonEmptySeq.head match {
        case predicate: Predicate if !isSupportedPredicate(predicate) => false
        case UnresolvedAttribute(name) if !indexedCols.contains(name.last) => false // TODO TOFIX subdocuments can cause conflicts
        case AttributeReference(name, _, _, _) if !indexedCols.contains(name) => false
        case head if head.children.nonEmpty => checkIfRemainExprAreSupported(remainExpr.tail ++ remainExpr.head.children)
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