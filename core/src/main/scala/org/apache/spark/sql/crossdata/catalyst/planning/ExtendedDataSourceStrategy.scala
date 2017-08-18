package org.apache.spark.sql.crossdata.catalyst.planning

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.NativeFunctionExecutor
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, LogicalPlan}
import org.apache.spark.sql.catalyst.{InternalRow, expressions}
import org.apache.spark.sql.crossdata.catalyst.NativeUDF
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter.{CrossdataExecutionPlan, FilterReport, SimpleLogicalPlan}
import org.apache.spark.sql.sources.Filter
import org.apache.spark.sql.{Strategy, execution, _}

private[sql] object ExtendedDataSourceStrategy extends Strategy with SparkLoggerComponent {

  def apply(plan: LogicalPlan): Seq[execution.SparkPlan] = plan match {
    // TODO refactor => return None instead of check the aggregation
    case ExtendedPhysicalOperation(projects, filters, l @ LogicalRelation(t: NativeFunctionExecutor, _), crossdataPlan)
      if !crossdataPlan.containsIgnoredProjections && !crossdataPlan.containsAggregation  =>
      pruneFilterProjectUdfs(
        l,
        projects,
        filters,
        crossdataPlan,
        (requestedColumns, srcFilters, attr2udf) =>
          toCatalystRDD(l, requestedColumns, t.buildScan(
            requestedColumns.map {
              case nat: AttributeReference if attr2udf contains nat.toString => nat.toString
              case att => att.name
            }.toArray, srcFilters, attr2udf)
          )
      ):: Nil
    case _ => Nil
  }

  protected def pruneFilterProjectUdfs(relation: LogicalRelation,
                                       projects: Seq[NamedExpression],
                                       filterPredicates: Seq[Expression],
                                       crossdataExecutionPlan: CrossdataExecutionPlan,
                                       scanBuilder: (
                                         Seq[Attribute],
                                           Array[Filter],
                                           Map[String, NativeUDF]
                                         ) => RDD[InternalRow]
                                       ) = {
    import org.apache.spark.sql.sources.CatalystToCrossdataAdapter

    val (pro, fil, att2udf) = crossdataExecutionPlan match {
      case CrossdataExecutionPlan(_, _, FilterReport(_, udfsIgnored)) if udfsIgnored.nonEmpty =>
        cannotExecuteNativeUDF(udfsIgnored)
      case CrossdataExecutionPlan(SimpleLogicalPlan(pro, fil, udfs, _), _, _) =>
        (pro, fil, udfs)
    }

    val projectSet = AttributeSet(pro)
    val filterSet = AttributeSet(filterPredicates.flatMap(
      _.references flatMap {
        case nat: AttributeReference if att2udf contains nat =>
          CatalystToCrossdataAdapter.udfFlattenedActualParameters(nat, (x: Attribute) => x)(att2udf) :+ nat
        case x => Seq(relation.attributeMap(x))
      }
    ))

    val filterCondition = filterPredicates.reduceLeftOption(expressions.And)
    val requestedColumns = (projectSet ++ filterSet).toSeq

    val scan = execution.PhysicalRDD.createFromDataSource(
      requestedColumns,
      scanBuilder(requestedColumns, fil, att2udf map { case (k, v) => k.toString() -> v }),
      relation.relation)

    execution.Project(projects, filterCondition.map(execution.Filter(_, scan)).getOrElse(scan))
  }


  private def cannotExecuteNativeUDF(udfsIgnored: Seq[AttributeReference]) =
    throw new AnalysisException("Some filters containing native UDFS cannot be executed on the datasource." +
      " It may happen when a cast is automatically applied by Spark, so try using the same type")


  /**
   * Convert RDD of Row into RDD of InternalRow with objects in catalyst types
   */
  private[this] def toCatalystRDD(
                                   relation: LogicalRelation,
                                   output: Seq[Attribute],
                                   rdd: RDD[Row]): RDD[InternalRow] = {
    if (relation.relation.needConversion) {
      execution.RDDConversions.rowToRowRdd(rdd, output.map(_.dataType))
    } else {
      rdd.asInstanceOf[RDD[InternalRow]]
    }
  }

}