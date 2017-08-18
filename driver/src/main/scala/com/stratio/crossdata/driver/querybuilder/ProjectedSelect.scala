package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.XDQLStatement

class ProjectedSelect(selection: Expression*)(implicit context: String => String = x => x) {

  def from(relation: Relation): SimpleRunnableQuery = new SimpleRunnableQuery(selection, relation, context)

  def from(relations: Relation*): SimpleRunnableQuery = {
    val rel = relations.reduce((a: Relation, b: Relation) => a.join(b))
    new SimpleRunnableQuery(selection, rel, context)
  }

  def from(strRelation: String): SimpleRunnableQuery =
    new SimpleRunnableQuery(selection, XDQLStatement(strRelation), context)
}
