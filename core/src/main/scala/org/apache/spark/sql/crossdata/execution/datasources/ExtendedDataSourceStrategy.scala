package org.apache.spark.sql.crossdata.execution.datasources

import com.stratio.crossdata.connector.NativeFunctionExecutor
import org.apache.spark.sql.crossdata.catalyst.planning.ExtendedPhysicalOperation
import org.apache.spark.sql.crossdata.execution.NativeUDF
import org.apache.spark.sql.execution.datasources.{PartitionSpec, Partition, LogicalRelation}
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter.SimpleLogicalPlan
import org.apache.spark.{Logging, TaskContext}
import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.rdd.{MapPartitionsRDD, RDD, UnionRDD}
import org.apache.spark.sql.catalyst.CatalystTypeConverters.convertToScala
import org.apache.spark.sql.catalyst.{CatalystTypeConverters, InternalRow, expressions}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, LogicalPlan}
import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.sql.{Strategy, execution, sources, _}
import org.apache.spark.unsafe.types.UTF8String
import org.apache.spark.util.{SerializableConfiguration, Utils}

import org.apache.spark.sql.sources.Filter

private[sql] object ExtendedDataSourceStrategy extends Strategy with Logging {

  protected def pruneFilterProjectUdfs(plan: LogicalPlan,
                                       relation: LogicalRelation,
                                       projects: Seq[NamedExpression],
                                       filterPredicates: Seq[Expression],
                                       scanBuilder: (
                                         Seq[Attribute],
                                           Array[Filter],
                                           Map[String, NativeUDF]
                                         ) => RDD[InternalRow]
                                        ) = {
    import org.apache.spark.sql.sources.CatalystToCrossdataAdapter

    val (pro, fil, att2udf) =
      (CatalystToCrossdataAdapter.getConnectorLogicalPlan(plan, projects, filterPredicates): @unchecked) match {
      case (SimpleLogicalPlan(pro, fil, udfs), false) => (pro, fil, udfs)
    }

    val projectSet = AttributeSet(pro)
    val filterSet = AttributeSet(filterPredicates.flatMap(
      _.references flatMap {
        case nat: AttributeReference if (att2udf contains nat) =>
          CatalystToCrossdataAdapter.udfFlattenedActualParameters(nat, (x: Attribute) => x)(att2udf) :+ nat
        case x => Seq(relation.attributeMap(x))
      }
    ))

    val filterCondition = filterPredicates.reduceLeftOption(expressions.And)
    val requestedColumns = (projectSet ++ filterSet).toSeq

    val scan = execution.PhysicalRDD.createFromDataSource(
      requestedColumns,
      scanBuilder(requestedColumns, fil, att2udf map {case (k,v) => k.toString() -> v}),
      relation.relation)

    execution.Project(projects, filterCondition.map(execution.Filter(_, scan)).getOrElse(scan))
  }

  def apply(plan: LogicalPlan): Seq[execution.SparkPlan] = plan match {
    case ExtendedPhysicalOperation(projects, filters, l @ LogicalRelation(t: NativeFunctionExecutor))
      if plan.collectFirst { case _: Aggregate => false} getOrElse(true) =>
      pruneFilterProjectUdfs(
        plan,
        l,
        projects,
        filters,
        (requestedColumns, srcFilters, attr2udf) =>
          toCatalystRDD(l, requestedColumns, t.buildScan(
            requestedColumns.map {
              case nat: AttributeReference if(attr2udf contains nat.toString) => nat.toString
              case att => att.name
            }.toArray, srcFilters, attr2udf))
        ):: Nil
    case _ => Nil
  }

  private def buildPartitionedTableScan(
                                         logicalRelation: LogicalRelation,
                                         projections: Seq[NamedExpression],
                                         filters: Seq[Expression],
                                         partitionColumns: StructType,
                                         partitions: Array[Partition]): SparkPlan = {
    val relation = logicalRelation.relation.asInstanceOf[HadoopFsRelation]

    // Because we are creating one RDD per partition, we need to have a shared HadoopConf.
    // Otherwise, the cost of broadcasting HadoopConf in every RDD will be high.
    val sharedHadoopConf = SparkHadoopUtil.get.conf
    val confBroadcast =
      relation.sqlContext.sparkContext.broadcast(new SerializableConfiguration(sharedHadoopConf))

    // Now, we create a scan builder, which will be used by pruneFilterProject. This scan builder
    // will union all partitions and attach partition values if needed.
    val scanBuilder = {
      (columns: Seq[Attribute], filters: Array[Filter]) => {
        // Builds RDD[Row]s for each selected partition.
        val perPartitionRows = partitions.map { case Partition(partitionValues, dir) =>
          val partitionColNames = partitionColumns.fieldNames

          // Don't scan any partition columns to save I/O.  Here we are being optimistic and
          // assuming partition columns data stored in data files are always consistent with those
          // partition values encoded in partition directory paths.
          val needed = columns.filterNot(a => partitionColNames.contains(a.name))
          val dataRows =
            relation.buildScan(needed.map(_.name).toArray, filters, Array(dir), confBroadcast)

          // Merges data values with partition values.
          mergeWithPartitionValues(
            relation.schema,
            columns.map(_.name).toArray,
            partitionColNames,
            partitionValues,
            toCatalystRDD(logicalRelation, needed, dataRows))
        }

        val unionedRows =
          if (perPartitionRows.length == 0) {
            relation.sqlContext.emptyResult
          } else {
            new UnionRDD(relation.sqlContext.sparkContext, perPartitionRows)
          }

        unionedRows
      }
    }

    // Create the scan operator. If needed, add Filter and/or Project on top of the scan.
    // The added Filter/Project is on top of the unioned RDD. We do not want to create
    // one Filter/Project for every partition.
    val sparkPlan = pruneFilterProject(
      logicalRelation,
      projections,
      filters,
      scanBuilder)

    sparkPlan
  }

  // TODO: refactor this thing. It is very complicated because it does projection internally.
  // We should just put a project on top of this.
  private def mergeWithPartitionValues(
                                        schema: StructType,
                                        requiredColumns: Array[String],
                                        partitionColumns: Array[String],
                                        partitionValues: InternalRow,
                                        dataRows: RDD[InternalRow]): RDD[InternalRow] = {
    val nonPartitionColumns = requiredColumns.filterNot(partitionColumns.contains)

    // If output columns contain any partition column(s), we need to merge scanned data
    // columns and requested partition columns to form the final result.
    if (!requiredColumns.sameElements(nonPartitionColumns)) {
      val mergers = requiredColumns.zipWithIndex.map { case (name, index) =>
        // To see whether the `index`-th column is a partition column...
        val i = partitionColumns.indexOf(name)
        if (i != -1) {
          val dt = schema(partitionColumns(i)).dataType
          // If yes, gets column value from partition values.
          (mutableRow: MutableRow, dataRow: InternalRow, ordinal: Int) => {
            mutableRow(ordinal) = partitionValues.get(i, dt)
          }
        } else {
          // Otherwise, inherits the value from scanned data.
          val i = nonPartitionColumns.indexOf(name)
          val dt = schema(nonPartitionColumns(i)).dataType
          (mutableRow: MutableRow, dataRow: InternalRow, ordinal: Int) => {
            mutableRow(ordinal) = dataRow.get(i, dt)
          }
        }
      }

      // Since we know for sure that this closure is serializable, we can avoid the overhead
      // of cleaning a closure for each RDD by creating our own MapPartitionsRDD. Functionally
      // this is equivalent to calling `dataRows.mapPartitions(mapPartitionsFunc)` (SPARK-7718).
      val mapPartitionsFunc = (_: TaskContext, _: Int, iterator: Iterator[InternalRow]) => {
        val dataTypes = requiredColumns.map(schema(_).dataType)
        val mutableRow = new SpecificMutableRow(dataTypes)
        iterator.map { dataRow =>
          var i = 0
          while (i < mutableRow.numFields) {
            mergers(i)(mutableRow, dataRow, i)
            i += 1
          }
          mutableRow.asInstanceOf[InternalRow]
        }
      }

      // This is an internal RDD whose call site the user should not be concerned with
      // Since we create many of these (one per partition), the time spent on computing
      // the call site may add up.
      Utils.withDummyCallSite(dataRows.sparkContext) {
        new MapPartitionsRDD(dataRows, mapPartitionsFunc, preservesPartitioning = false)
      }

    } else {
      dataRows
    }
  }

  protected def prunePartitions(
                                 predicates: Seq[Expression],
                                 partitionSpec: PartitionSpec): Seq[Partition] = {
    val PartitionSpec(partitionColumns, partitions) = partitionSpec
    val partitionColumnNames = partitionColumns.map(_.name).toSet
    val partitionPruningPredicates = predicates.filter {
      _.references.map(_.name).toSet.subsetOf(partitionColumnNames)
    }

    if (partitionPruningPredicates.nonEmpty) {
      val predicate =
        partitionPruningPredicates
          .reduceOption(expressions.And)
          .getOrElse(Literal(true))

      val boundPredicate = InterpretedPredicate.create(predicate.transform {
        case a: AttributeReference =>
          val index = partitionColumns.indexWhere(a.name == _.name)
          BoundReference(index, partitionColumns(index).dataType, nullable = true)
      })

      partitions.filter { case Partition(values, _) => boundPredicate(values) }
    } else {
      partitions
    }
  }

  // Based on Public API.
  protected def pruneFilterProject(
                                    relation: LogicalRelation,
                                    projects: Seq[NamedExpression],
                                    filterPredicates: Seq[Expression],
                                    scanBuilder: (Seq[Attribute], Array[Filter]) => RDD[InternalRow]) = {
    pruneFilterProjectRaw(
      relation,
      projects,
      filterPredicates,
      (requestedColumns, pushedFilters) => {
        scanBuilder(requestedColumns, selectFilters(pushedFilters).toArray)
      })
  }

  // Based on Catalyst expressions.
  protected def pruneFilterProjectRaw(
                                       relation: LogicalRelation,
                                       projects: Seq[NamedExpression],
                                       filterPredicates: Seq[Expression],
                                       scanBuilder: (Seq[Attribute], Seq[Expression]) => RDD[InternalRow]) = {

    val projectSet = AttributeSet(projects.flatMap(_.references))
    val filterSet = AttributeSet(filterPredicates.flatMap(_.references))
    val filterCondition = filterPredicates.reduceLeftOption(expressions.And)

    val pushedFilters = filterPredicates.map { _ transform {
      case a: AttributeReference => relation.attributeMap(a) // Match original case of attributes.
    }}

    if (projects.map(_.toAttribute) == projects &&
      projectSet.size == projects.size &&
      filterSet.subsetOf(projectSet)) {
      // When it is possible to just use column pruning to get the right projection and
      // when the columns of this projection are enough to evaluate all filter conditions,
      // just do a scan followed by a filter, with no extra project.
      val requestedColumns =
        projects.asInstanceOf[Seq[Attribute]] // Safe due to if above.
          .map(relation.attributeMap)            // Match original case of attributes.

      val scan = execution.PhysicalRDD.createFromDataSource(
        projects.map(_.toAttribute),
        scanBuilder(requestedColumns, pushedFilters),
        relation.relation)
      filterCondition.map(execution.Filter(_, scan)).getOrElse(scan)
    } else {
      val requestedColumns = (projectSet ++ filterSet).map(relation.attributeMap).toSeq

      val scan = execution.PhysicalRDD.createFromDataSource(
        requestedColumns,
        scanBuilder(requestedColumns, pushedFilters),
        relation.relation)
      execution.Project(projects, filterCondition.map(execution.Filter(_, scan)).getOrElse(scan))
    }
  }

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

  /**
   * Convert RDD of Row into RDD of InternalRow with objects in catalyst types
   */
  private[this] def toCatalystRDD(relation: LogicalRelation, rdd: RDD[Row]): RDD[InternalRow] = {
    toCatalystRDD(relation, relation.output, rdd)
  }

  /**
   * Selects Catalyst predicate [[Expression]]s which are convertible into data source [[Filter]]s,
   * and convert them.
   */
  protected[sql] def selectFilters(filters: Seq[Expression]) = {
    def translate(predicate: Expression): Option[Filter] = predicate match {
      case expressions.EqualTo(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualTo(a.name, convertToScala(v, t)))
      case expressions.EqualTo(Literal(v, t), a: Attribute) =>
        Some(sources.EqualTo(a.name, convertToScala(v, t)))

      case expressions.EqualNullSafe(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualNullSafe(a.name, convertToScala(v, t)))
      case expressions.EqualNullSafe(Literal(v, t), a: Attribute) =>
        Some(sources.EqualNullSafe(a.name, convertToScala(v, t)))

      case expressions.GreaterThan(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThan(a.name, convertToScala(v, t)))
      case expressions.GreaterThan(Literal(v, t), a: Attribute) =>
        Some(sources.LessThan(a.name, convertToScala(v, t)))

      case expressions.LessThan(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThan(a.name, convertToScala(v, t)))
      case expressions.LessThan(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThan(a.name, convertToScala(v, t)))

      case expressions.GreaterThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.LessThanOrEqual(a.name, convertToScala(v, t)))

      case expressions.LessThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.LessThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThanOrEqual(a.name, convertToScala(v, t)))

      case expressions.InSet(a: Attribute, set) =>
        val toScala = CatalystTypeConverters.createToScalaConverter(a.dataType)
        Some(sources.In(a.name, set.toArray.map(toScala)))

      // Because we only convert In to InSet in Optimizer when there are more than certain
      // items. So it is possible we still get an In expression here that needs to be pushed
      // down.
      case expressions.In(a: Attribute, list) if !list.exists(!_.isInstanceOf[Literal]) =>
        val hSet = list.map(e => e.eval(EmptyRow))
        val toScala = CatalystTypeConverters.createToScalaConverter(a.dataType)
        Some(sources.In(a.name, hSet.toArray.map(toScala)))

      case expressions.IsNull(a: Attribute) =>
        Some(sources.IsNull(a.name))
      case expressions.IsNotNull(a: Attribute) =>
        Some(sources.IsNotNull(a.name))

      case expressions.And(left, right) =>
        (translate(left) ++ translate(right)).reduceOption(sources.And)

      case expressions.Or(left, right) =>
        for {
          leftFilter <- translate(left)
          rightFilter <- translate(right)
        } yield sources.Or(leftFilter, rightFilter)

      case expressions.Not(child) =>
        translate(child).map(sources.Not)

      case expressions.StartsWith(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringStartsWith(a.name, v.toString))

      case expressions.EndsWith(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringEndsWith(a.name, v.toString))

      case expressions.Contains(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringContains(a.name, v.toString))

      case _ => None
    }

    filters.flatMap(translate)
  }
}