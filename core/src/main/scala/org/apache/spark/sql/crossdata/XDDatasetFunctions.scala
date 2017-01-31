/*
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
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, Expression, GenericRowWithSchema, GetArrayStructFields, GetStructField, NamedExpression}
import org.apache.spark.sql.catalyst.plans.logical.{Limit, LogicalPlan, Project}
import org.apache.spark.sql.crossdata.ExecutionType.{Default, ExecutionType, Native, Spark}
import org.apache.spark.sql.crossdata.execution.XDPlan
import org.apache.spark.sql.types.{ArrayType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row}

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.BufferLike
import scala.collection.{GenTraversableOnce, immutable, mutable}
import scala.reflect.ClassTag


trait XDDatasetFunctions {

  implicit def dataset2xddataset[T : ClassTag](ds: Dataset[T]): XDDataset[T] = new XDDataset[T](ds)

  implicit def dataset2xddatase(ds: Dataset[Row]): XDDataFrame = new XDDataFrame(ds)


  class XDDataset[T : ClassTag](dataset: Dataset[T]) {

    private def withCallback[U](name: String, df: DataFrame)(action: DataFrame => U) = {
      try {
        df.queryExecution.executedPlan.foreach { plan =>
          plan.resetMetrics()
        }
        val start = System.nanoTime()
        val result = action(df)
        val end = System.nanoTime()
        dataset.sparkSession.listenerManager.onSuccess(name, df.queryExecution, end - start)
        result
      } catch {
        case e: Exception =>
          dataset.sparkSession.listenerManager.onFailure(name, df.queryExecution, e)
          throw e
      }
    }

    private val boundEnc = dataset.exprEnc.resolveAndBind(dataset.logicalPlan.output, dataset.sparkSession.sessionState.analyzer)

    /**
      * Collect using an specific [[ExecutionType]]. Only for testing purpose so far.
      * When using the Security Manager, this method has to be invoked with the parameter [[ExecutionType.Default]]
      * in order to ensure that the workflow of the execution reaches the point where the authorization is called.
      *
      * @param executionType one of the [[ExecutionType]]
      * @return the query result
      */
    @DeveloperApi
    def collect(executionType: ExecutionType): Array[T] = executionType match {

      case Default =>
        dataset.collect()

      case Native =>
        def execute(): Array[T] = dataset.withNewExecutionId {
          val optimizedPlan = dataset.queryExecution.optimizedPlan
          val nativeQueryExecutor: Option[NativeScan] = XDPlan.findNativeQueryExecutor(optimizedPlan)
          val internalRows = nativeQueryExecutor.flatMap(XDPlan.executeNativeQuery(_, optimizedPlan)).getOrElse(
            throw new RuntimeException("The operation cannot be executed without Spark")
          )
          internalRows.map(boundEnc.fromRow)
        }

        withCallback("collect", dataset.toDF())(_ => execute())
      // TODO test Dataset[Person] natively

      case Spark =>
        def execute(): Array[T] = dataset.withNewExecutionId {

          val plan = dataset.queryExecution.sparkPlan
          val sparkPlan = plan match {
            case xdPlan: XDPlan => xdPlan.sparkPlan
            case _ => plan
          }
          sparkPlan.executeCollect().map(boundEnc.fromRow)
        }

        withCallback("collect", dataset.toDF())(_ => execute())
    }
  }

  class XDDataFrame(dataset: Dataset[Row]) {

    def flattenedCollect(): Array[Row] = {

      val a = dataset

      def flattenProjectedColumns(exp: Expression, prev: List[String] = Nil): (List[String], Boolean) = exp match {
        case GetStructField(child, _, Some(fieldName))  =>
          flattenProjectedColumns(child, fieldName :: prev)
        case GetArrayStructFields(child, field,_,_,_)=>
          flattenProjectedColumns(child, field.name :: prev)
        case AttributeReference(name, _, _, _) =>
          (name :: prev, false)
        case Alias(child @ GetStructField(_, _, Some(fname)), name) if fname == name =>
          flattenProjectedColumns(child)
        case Alias(child @ GetArrayStructFields(childArray, field,_,_,_), name) =>
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
              Seq((StructField(name, t, nable, mdata), vobject))
            case ((StructField(name, StructType(_), _, _), col: GenericRowWithSchema), (parentName, false)) =>
              flatRow(col, Seq.fill(col.schema.size)(s"${baseName(parentName)}$name" -> false))
            case ((StructField(name, dtype, nullable, meta), vobject), (parentName, false)) =>
              Seq((StructField(s"${baseName(parentName)}$name", dtype, nullable, meta), vobject))
          }
        }

        require(firstLevelNames.isEmpty ||rows.isEmpty || firstLevelNames.size == rows.headOption.map(_.length).getOrElse(0))
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

      def verticallyFlatRowArrays(row: GenericRowWithSchema)(limit: Int): Seq[GenericRowWithSchema] = {

        def cartesian[T](ls: Seq[Seq[T]]): Seq[Seq[T]] = (ls :\ Seq(Seq.empty[T])) {
          case (cur: Seq[T], prev) => for(x <- prev; y <- cur) yield y +: x
        }

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

        cartesian(arrayColumnValues).take(limit) map { case replacements: Seq[(Int, _) @unchecked] =>
          val idx2newVal: Map[Int, Any] = replacements.toMap
          val values = elementsWithIndex map { case (prevVal, idx: Int) =>
            idx2newVal.getOrElse(idx, prevVal)
          }
          new GenericRowWithSchema(values, newSchema)
        }
      }


      import WithTrackerFlatMapSeq._

      def iterativeFlatten(
                            rows: Seq[Row],
                            firstLevelNames: Seq[(Seq[String], Boolean)] = Seq.empty
                          )(limit: Int = Int.MaxValue): Seq[Row] =
        flatRows(rows, firstLevelNames) withTrackerFlatMap {
          case (_, Some(currentSize)) if(currentSize >= limit) => Seq()
          case (row: GenericRowWithSchema, currentSize) =>
            row.schema collectFirst {
              case StructField(_, _: ArrayType, _, _) =>
                val newLimit = limit-currentSize.getOrElse(0)
                iterativeFlatten(verticallyFlatRowArrays(row)(newLimit))(newLimit)
            } getOrElse Seq(row)
          case (row: Row, _) => Seq(row)
        }

      def processProjection(plist: Seq[NamedExpression], child: LogicalPlan, limit: Int = Int.MaxValue): Array[Row] = {
        val fullyAnnotatedRequestedColumns = plist map (flattenProjectedColumns(_))
        iterativeFlatten(dataset.collect(), fullyAnnotatedRequestedColumns)(limit) toArray
      }

      dataset.queryExecution.optimizedPlan match {
        case Limit(lexp, Project(plist, child)) => processProjection(plist, child, lexp.toString().toInt)
        case Project(plist, child) => processProjection(plist, child)
        case Limit(lexp, _) => iterativeFlatten(dataset.collect())(lexp.toString().toInt) toArray
        case _ => iterativeFlatten(dataset.collect())() toArray
      }

    }

    //TODO: Move to a common library
    private[crossdata] object WithTrackerFlatMapSeq {
      implicit def seq2superflatmapseq[T](s: Seq[T]): WithTrackerFlatMapSeq[T] = new WithTrackerFlatMapSeq(s)
    }

    //TODO: Move to a common library
    private[crossdata] class WithTrackerFlatMapSeq[T] private(val s: Seq[T])
      extends scala.collection.immutable.Seq[T] {
      override def length: Int = s.length
      override def apply(idx: Int): T = s(idx)
      override def iterator: Iterator[T] = s.iterator


      def withTrackerFlatMap[B, That](
                                       f: (T, Option[Int]) => GenTraversableOnce[B]
                                     )(implicit bf: CanBuildFrom[immutable.Seq[T], B, That]): That = {
        def builder : mutable.Builder[B, That] = bf(repr)
        val b = builder
        val builderAsBufferLike = b match {
          case bufferl: BufferLike[_, _] => Some(bufferl)
          case _ => None
        }
        for (x <- this) b ++= f(x, builderAsBufferLike.map(_.length)).seq
        b.result
      }

    }

  }

}
