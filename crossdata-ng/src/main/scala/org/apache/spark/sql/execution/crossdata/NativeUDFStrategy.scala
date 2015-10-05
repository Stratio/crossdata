package org.apache.spark.sql.execution.crossdata

import java.io.Serializable

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.{Row, SQLContext, Strategy}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.{SparkPlan, RunnableCommand, ExecutedCommand}

case class DummyRun(udfName: String, attr: Attribute) extends RunnableCommand {
  override def run(sqlContext: SQLContext): Seq[Row] = {
    Seq(Row("dummyRes"))
  }

  override def output: Seq[Attribute] = attr::Nil
}

object NativeUDFStrategy extends Strategy with Logging {
  override def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
    case EvaluateNativeUDF(udf, _, at) =>
      ExecutedCommand(DummyRun(udf.name, at))::Nil
    case _ => Nil
  }

}
