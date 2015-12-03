package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.{XDQLStatement, SortCriteria}

object RunnableQuery {

  implicit class RunnaBleQueryAsExpression(runnableQuery: RunnableQuery) extends Expression {
    override def toXDQL: String = s"(${runnableQuery.toXDQL})"
  }

  implicit class RunnaBleQueryAsRelation(runnableQuery: RunnableQuery) extends Relation {
    override def toXDQL: String = s"(${runnableQuery.toXDQL})"
  }

}

abstract class RunnableQuery protected (protected val projections: Seq[Expression],
                    protected val relation: Relation,
                    protected val filters: Option[Predicate] = None,
                    protected val groupingExpressions: Seq[Expression] = Seq.empty,
                    protected val havingExpressions: Seq[Expression] = Seq.empty,
                    protected val ordering: Option[SortCriteria] = None,
                    protected val limit: Option[Int] = None
                     ) extends CombinableQuery {

  def this(projections: Seq[Expression], relations: Relation,filters: Predicate) =
    this(projections, relations, Some(filters))


  def where(condition: String): this.type = where(XDQLStatement(condition))
  // It has to be abstract (simple runnable query has transitions) and concrete
  // implementations (grouped, limited, sorted...) should return their own type
  def where(condition: Predicate): this.type

  override def toXDQL: String = {
    def stringfy[T](head: String, elements: Seq[T], element2str: T => String): String =
      elements.headOption.fold("")(_ => s"$head ${elements.map(element2str) mkString ", "}")

    def stringfyXDQL(head: String, elements: Seq[CrossdataSQLStatement]) =
      stringfy[CrossdataSQLStatement](head, elements, _.toXDQL)

    s"""
       | SELECT ${projections map(_.toXDQL) mkString ", "}
       | FROM ${relation.toXDQL}
       | ${stringfyXDQL("WHERE ", filters.toSeq)}
       | ${stringfyXDQL("GROUP BY", groupingExpressions)}
       | ${stringfyXDQL("HAVING", havingExpressions)}
       | ${stringfyXDQL("ORDER BY", ordering.toSeq)}
       | ${stringfy[Int]("LIMIT", limit.toSeq, _.toString)}
    """.stripMargin
  }


 /*       | ${projectedSelect}
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
