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
package org.apache.spark.sql.crossdata

import com.stratio.crossdata.connector.NativeScan
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.sql.{SQLContext, DataFrame, Row}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, LeafNode, Project, Limit, Aggregate}
import org.apache.spark.sql.crossdata.ExecutionType.{ExecutionType, Default, Native, Spark}
import org.apache.spark.sql.crossdata.exceptions.NativeExecutionException
import org.apache.spark.sql.execution.datasources.LogicalRelation
import XDDataFrame.findNativeQueryExecutor
import org.apache.spark.sql.types.{ArrayType, StructField, StructType}

private[sql] object XDDataFrame {

  def apply(sqlContext: SQLContext, logicalPlan: LogicalPlan): DataFrame = {
    new XDDataFrame(sqlContext, logicalPlan)
  }

  /**
   * Finds a [[org.apache.spark.sql.sources.BaseRelation]] mixing-in [[NativeScan]] supporting native execution.
   *
   * The logical plan must involve only base relation from the same datasource implementation. For example,
   * if there is a join with a [[org.apache.spark.rdd.RDD]] the logical plan cannot be executed natively.
   *
   * @param optimizedLogicalPlan the logical plan once it has been processed by the parser, analyzer and optimizer.
   * @return
   */
  def findNativeQueryExecutor(optimizedLogicalPlan: LogicalPlan): Option[NativeScan] = {

    def allLeafsAreNative(leafs: Seq[LeafNode]): Boolean = {
      leafs.forall {
        case LogicalRelation(ns: NativeScan, _) => true
        case _ => false
      }
    }

    val leafs = optimizedLogicalPlan.collect { case leafNode: LeafNode => leafNode }

    if (!allLeafsAreNative(leafs)) {
      None
    } else {
      val nativeExecutors: Seq[NativeScan] = leafs.map { case LogicalRelation(ns: NativeScan, _) => ns }

      nativeExecutors match {
        case Seq(head) =>
          Some(head)

        case _ =>
          if (nativeExecutors.sliding(2).forall { tuple =>
            tuple.head.getClass == tuple.head.getClass
          }) {
            nativeExecutors.headOption
          } else {
            None
          }
      }
    }
  }

}

/**
 * Extends a [[DataFrame]] to provide native access to datasources when performing Spark actions.
 */
class XDDataFrame private[sql](@transient override val sqlContext: SQLContext,
                               @transient override val queryExecution: SQLContext#QueryExecution)
  extends DataFrame(sqlContext, queryExecution) {

  def this(sqlContext: SQLContext, logicalPlan: LogicalPlan) = {
    this(sqlContext, {
      val qe = sqlContext.executePlan(logicalPlan)
      if (sqlContext.conf.dataFrameEagerAnalysis) {
        qe.assertAnalyzed() // This should force analysis and throw errors if there are any
      }
      qe
    }
    )
  }


  /**
   * @inheritdoc
   */
  override def collect(): Array[Row] = {
    // If cache doesn't go through native
    if (sqlContext.cacheManager.lookupCachedData(this).nonEmpty) {
      super.collect()
    } else {
      val nativeQueryExecutor: Option[NativeScan] = findNativeQueryExecutor(queryExecution.optimizedPlan)
      nativeQueryExecutor.flatMap(executeNativeQuery).getOrElse(super.collect())
    }
  }

  def flattenedCollect(): Array[Row] = {

    def flattenProjectedColumns(exp: Expression, prev: List[String] = Nil): (List[String], Boolean) = exp match {
      case GetStructField(child, field, _) =>
        flattenProjectedColumns(child, field.name :: prev)
      case AttributeReference(name, _, _, _) =>
        (name :: prev, false)
      case Alias(child @ GetStructField(_, StructField(fname, _, _, _), _), name) if(fname == name) =>
        flattenProjectedColumns(child)
      case Alias(child, name) =>
        List(name) -> true
      case _ => prev -> false
    }

    def flatRows(
                  rows: Seq[Row],
                  firstLevelNames: Seq[(Seq[String], Boolean)] = Seq.empty
                ): Seq[Row] = {

      def baseName(parentName: String): String = parentName.headOption.map(_ => s"$parentName.").getOrElse("")

      def flatRow(
                   row: GenericRowWithSchema,
                   parentsNamesAndAlias: Seq[(String, Boolean)] = Seq.empty): Array[(StructField, Any)] = {
        (row.schema.fields zip row.values zipAll(parentsNamesAndAlias, null, "" -> false)) flatMap {
          case (null, _) => Seq.empty
          case ((StructField(_, t, nable, mdata), vobject), (name, true)) =>
            Seq((StructField(name, t, nable, mdata),vobject))
          case ((StructField(name, StructType(_), _, _), col : GenericRowWithSchema), (parentName, false)) =>
            flatRow(col, Seq.fill(col.schema.size)(s"${baseName(parentName)}$name" -> false))
          case ((StructField(name, dtype, nullable, meta), vobject), (parentName, false)) =>
            Seq((StructField(s"${baseName(parentName)}$name", dtype, nullable, meta), vobject))
        }
      }

      require(firstLevelNames.isEmpty || firstLevelNames.size == rows.headOption.map(_.length).getOrElse(0))
      val thisLevelNames = firstLevelNames.map {
        case (nameseq, true) => (nameseq.headOption.getOrElse(""), true)
        case (nameseq, false) => (nameseq.init mkString ".", false)
      }

      rows map {
        case row: GenericRowWithSchema =>
          val newFieldsArray = flatRow(row, thisLevelNames)
          val horizontallyFlattened: Row = new GenericRowWithSchema(
            newFieldsArray.map(_._2), StructType(newFieldsArray.map(_._1)))
          horizontallyFlattened
        case row: Row =>
          row
      }
    }

    def verticallyFlatRowArrays(row: GenericRowWithSchema): Seq[GenericRowWithSchema] = {

      def cartesian[T](ls: Seq[Seq[T]]): Seq[Seq[T]] = (ls :\ Seq(Seq.empty[T])) {
        case (cur: Seq[T], prev) => for(x <- prev; y <- cur) yield y +: x
      } filterNot(_.isEmpty)

      val newSchema = StructType(
        row.schema map {
          case StructField(name, ArrayType(etype, _), nullable, meta) =>
            StructField(name, etype, true)
          case other => other
        }
      )

      val elementsWithIndex = row.values zipWithIndex

      val arrayColumnValues: Seq[Seq[(Int, _)]] = elementsWithIndex collect {
        case (res: Seq[_], idx) => res map(idx -> _)
      }

      cartesian(arrayColumnValues) map { case replacements: Seq[(Int, _)] =>
        val idx2newVal: Map[Int, Any] = replacements.toMap
        val values = elementsWithIndex map { case (prevVal, idx: Int) =>
          idx2newVal.getOrElse(idx, prevVal)
        }
        new GenericRowWithSchema(values, newSchema)
      }
    }

    def iterativeFlatten(
                          rows: Seq[Row],
                          firstLevelNames: Seq[(Seq[String], Boolean)] = Seq.empty
                        ): Seq[Row] =
      flatRows(rows, firstLevelNames) flatMap {
        case row: GenericRowWithSchema =>
          row.schema collectFirst {
            case StructField(_, _: ArrayType, _, _) =>
              iterativeFlatten(verticallyFlatRowArrays(row))
          } getOrElse Seq(row)
        case row => Seq(row)
      }

    def processProjection(plist: Seq[NamedExpression], child: LogicalPlan): Array[Row] = {
      val fullyAnnotatedRequestedColumns = plist map (flattenProjectedColumns(_))
      iterativeFlatten(collect(), fullyAnnotatedRequestedColumns) toArray
    }

    queryExecution.optimizedPlan match {
      case Limit(_, Project(plist, child)) => processProjection(plist, child)
      case Project(plist, child) => processProjection(plist, child)
      case _ => iterativeFlatten(collect()) toArray
    }

  }

  /**
   * Collect using an specific [[ExecutionType]]. Only for testing purpose so far.
   *
   * @param executionType one of the [[ExecutionType]]
   * @return the query result
   */
  @DeveloperApi
  def collect(executionType: ExecutionType): Array[Row] = executionType match {
    case Default => collect()
    case Spark => super.collect()
    case Native =>
      val result = findNativeQueryExecutor(queryExecution.optimizedPlan).flatMap(executeNativeQuery)
      if (result.isEmpty) throw new NativeExecutionException
      result.get
  }


  /**
   * @inheritdoc
   */
  override def collectAsList(): java.util.List[Row] = java.util.Arrays.asList(collect(): _*)

  /**
   * @inheritdoc
   */
  override def limit(n: Int): DataFrame = XDDataFrame(sqlContext, Limit(Literal(n), logicalPlan))

  /**
   * @inheritdoc
   */
  override def count(): Long = {
    val aggregateExpr = Seq(Alias(Count(Literal(1)), "count")())
    XDDataFrame(sqlContext, Aggregate(Seq(), aggregateExpr, logicalPlan)).collect().head.getLong(0)
  }


  /**
   * Executes the logical plan.
   *
   * @param provider [[org.apache.spark.sql.sources.BaseRelation]] mixing-in [[NativeScan]]
   * @return an array that contains all of [[Row]]s in this [[XDDataFrame]]
   *         or None if the provider cannot resolve the entire [[XDDataFrame]] natively.
   */
  private[this] def executeNativeQuery(provider: NativeScan): Option[Array[Row]] = {

    val containsSubfields = notSupportedProject(queryExecution.optimizedPlan)
    val planSupported = !containsSubfields && queryExecution.optimizedPlan.map(lp => lp).forall(provider.isSupported(_, queryExecution.optimizedPlan))
    if(planSupported) provider.buildScan(queryExecution.optimizedPlan) else None

  }

  private[this] def notSupportedProject(optimizedLogicalPlan: LogicalPlan): Boolean = {

    optimizedLogicalPlan collectFirst {
      case a@Project(seq, _) if seq.collectFirst { case b: GetMapValue => b }.isDefined => a
      case a@Project(seq, _) if seq.collectFirst { case b: GetStructField => b }.isDefined => a
      case a@Project(seq, _) if seq.collectFirst { case Alias(b: GetMapValue, _) => a }.isDefined => a
      case a@Project(seq, _) if seq.collectFirst { case Alias(b: GetStructField, _) => a }.isDefined => a
    } isDefined
  }
}