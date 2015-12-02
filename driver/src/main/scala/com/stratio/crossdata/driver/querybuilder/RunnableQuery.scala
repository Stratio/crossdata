package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.SortCriteria


abstract class RunnableQuery protected (val projections: Seq[Expression],
                    val relation: Relation,
                    val filters: Option[Predicate] = None,
                    val groupingExpressions: Seq[Expression] = Seq.empty,
                    val havingExpressions: Seq[Expression] = Seq.empty,
                    val ordering: Option[SortCriteria] = None,
                    val limit: Option[Int] = None
                     ) extends Expression with Relation with CombinableQuery {

  def this(projections: Seq[Expression], relations: Seq[Relation],filters: Predicate) =
    this(projections, relations, Some(filters))



  // It has to be abstract (simple runnable query has transitions) and concrete
  // implementations (grouped, limited, sorted...) should return their own type
  def where(condition: Predicate): RunnableQuery

  override def toXDQL: String = ???

  /*private[this] def buildSelect(): String = {
    s"""${initialSelect}
        | ${projectedSelect}
        | ${relatedSelect}
        | ${filteredSelect.getOrElse("")}
        | ${groupedSelect.getOrElse("")}
        | ${havingSelect.getOrElse("")}
        | ${orderedSelect.getOrElse("")}
        | ${limitedSelect.getOrElse("")}"""
      .stripMargin
      .replace(System.lineSeparator(), " ")
      .replaceAll ("""\s\s+""", " ").trim
  }

  override def toString: String = {
    if(initialSelect.bracketed)
      s"(${buildSelect()})"
    else
      s"${buildSelect()}"
  }*/
}
