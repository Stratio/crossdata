package org.apache.spark.sql.crossdata.execution

import org.apache.spark.rdd.{EmptyRDD, RDD}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{Attribute, In, Literal}
import org.apache.spark.sql.catalyst.plans.logical
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.{BinaryNode, Filter, SparkPlan}

/**
  * Created by usarasola on 16/06/16.
  */
case class XDIndexJoin(left: SparkPlan, @transient right: LogicalPlan) extends XDBinaryNodeWithoutResolveRight {


  override protected def doExecute(): RDD[InternalRow] = {
    //Execute first left plan and modify the filter in right plan with the results from index
    val indexResultRows = left.executeCollect()

    if(indexResultRows.length > 0){
      val sparkPlanForIn: Iterator[SparkPlan] = sqlContext.planner.plan(
        right match {
          case logical.Filter(In(attribute, exprs), child) =>
            val newExprs = indexResultRows map {row => Literal.create(row, left.schema.fields(0).dataType)}
            logical.Filter(In(attribute, newExprs), child)
        }
      )

      sparkPlanForIn.next().execute()
    } else {
      sparkContext.emptyRDD
    }


  }

  override def output: Seq[Attribute] = right.output

}

private[sql] trait XDBinaryNodeWithoutResolveRight extends SparkPlan {
  def left: SparkPlan
  def right: LogicalPlan

  override def children: Seq[SparkPlan] = Seq(left)
}

