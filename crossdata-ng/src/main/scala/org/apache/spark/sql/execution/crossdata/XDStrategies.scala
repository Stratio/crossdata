package org.apache.spark.sql.execution.crossdata

import org.apache.spark.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{GenericInternalRow, Attribute}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.execution.{SparkPlan, SparkStrategies}

import org.apache.spark.sql.Strategy
import org.apache.spark.unsafe.types.UTF8String

case class DummyRun(udfName: String, output: Seq[Attribute], child: SparkPlan) extends SparkPlan {
  override def children: Seq[SparkPlan] = child :: Nil

  //TODO: throw a more descriptive exception
  override protected def doExecute(): RDD[InternalRow] = ??? //Just native executions should work

}

trait XDStrategies extends SparkStrategies {
  self: XDContext#XDPlanner =>

  object NativeUDFStrategy extends Strategy with Logging {
    override def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case e @ EvaluateNativeUDF(udf, child, at) =>
        DummyRun(udf.name, e.output, planLater(child))::Nil
      case _ => Nil
    }
  }

}
