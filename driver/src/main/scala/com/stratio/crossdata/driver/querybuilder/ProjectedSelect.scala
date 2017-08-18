/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
