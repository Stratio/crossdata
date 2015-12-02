package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.Expression

abstract sealed class SortDirection
case object Ascending extends SortDirection
case object Descending extends SortDirection

case class SortOrder(expression: Expression, direction: SortDirection = Ascending) extends Expression

case class SortCriteria(global: Boolean, expressions: Seq[SortOrder])
