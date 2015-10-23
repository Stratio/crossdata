/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.execution

import org.apache.spark.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.execution.datasources.{CreateTableUsingAsSelect, CreateTableUsing}
import org.apache.spark.sql.execution.{ExecutedCommand, SparkPlan, SparkStrategies}

import org.apache.spark.sql.Strategy

case class NativeUDFSparkPlan(udfName: String, output: Seq[Attribute], child: SparkPlan) extends SparkPlan {
  override def children: Seq[SparkPlan] = child :: Nil

  override protected def doExecute(): RDD[InternalRow] = {
    ???
  }

}

trait XDStrategies extends SparkStrategies {
  self: XDContext#XDPlanner =>

  object XDDDLStrategy extends Strategy {
    def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case CreateTableUsing(tableIdent, userSpecifiedSchema, provider, false, opts, allowExisting, _) =>
        val cmd = PersistDataSourceTable(tableIdent, userSpecifiedSchema, provider, opts, allowExisting)
        ExecutedCommand(cmd) :: Nil

      case CreateTableUsingAsSelect(tableIdent, provider, false, partitionCols, mode, opts, query) =>
        val cmd = PersistSelectAsTable(tableIdent, provider, partitionCols, mode, opts, query)
        ExecutedCommand(cmd) :: Nil

      case _ => Nil
    }
  }

  object NativeUDFStrategy extends Strategy with Logging {
    override def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case e @ EvaluateNativeUDF(udf, child, at) =>
        NativeUDFSparkPlan(udf.name, e.output, planLater(child))::Nil
      case _ => Nil
    }
  }

}
