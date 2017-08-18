package org.apache.spark.sql.crossdata.catalyst.streaming

import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LeafNode

case class StreamingRelation(ephemeralTableName: String) extends LeafNode {
  override def output: Seq[Attribute] = Seq.empty

  override def productElement(n: Int): Any = throw new IndexOutOfBoundsException

  override def productArity: Int = 0
}
