/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.catalyst.analysis.{UnresolvedAttribute, UnresolvedException}
import org.apache.spark.sql.catalyst.expressions.{Expression, Unevaluable}
import org.apache.spark.sql.types.DataType

case class PostponedAttribute(unresolvedAttribute: UnresolvedAttribute) extends Expression with Unevaluable {
  override def nullable: Boolean = throw new UnresolvedException(this, "nullable")

  override def dataType: DataType = throw new UnresolvedException(this, "dataType")

  override def children: Seq[Expression] = Seq()

  override lazy val resolved: Boolean = false
}

