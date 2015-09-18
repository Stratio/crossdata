/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.crossdata.test


import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions.SpecificMutableRow
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.types.{StructField, _}
import org.apache.spark.sql.{ColumnName, DataFrameHolder, Row}

import scala.language.implicitConversions
import scala.reflect.runtime.universe.TypeTag

/**
 * A collection of implicit methods for converting common Scala objects into [[org.apache.spark.sql.crossdata.XDDataFrame]]s.
 */
private[sql] abstract class XDImplicits {
  // TODO update with Spark 1.5
  protected def _xdContext: XDContext

  /**
   * An implicit conversion that turns a Scala `Symbol` into a Column.
   * @since 1.3.0
   */
  implicit def symbolToColumn(s: Symbol): ColumnName = new ColumnName(s.name)

  /**
   * Creates a DataFrame from an RDD of Product (e.g. case classes, tuples).
   * @since 1.3.0
   */
  implicit def rddToDataFrameHolder[A <: Product : TypeTag](rdd: RDD[A]): DataFrameHolder = {
    DataFrameHolder(_xdContext.createDataFrame(rdd))
  }

  /**
   * Creates a DataFrame from a local Seq of Product.
   * @since 1.3.0
   */
  implicit def localSeqToDataFrameHolder[A <: Product : TypeTag](data: Seq[A]): DataFrameHolder = {
    DataFrameHolder(_xdContext.createDataFrame(data))
  }

  // Do NOT add more implicit conversions. They are likely to break source compatibility by
  // making existing implicit conversions ambiguous. In particular, RDD[Double] is dangerous
  // because of [[DoubleRDDFunctions]].

  /**
   * Creates a single column DataFrame from an RDD[Int].
   * @since 1.3.0
   */
  implicit def intRddToDataFrameHolder(data: RDD[Int]): DataFrameHolder = {
    val dataType = IntegerType
    val rows = data.mapPartitions { iter =>
      val row = new SpecificMutableRow(dataType :: Nil)
      iter.map { v =>
        row.setInt(0, v)
        row: Row
      }
    }
    DataFrameHolder(
      _xdContext.createDataFrame(rows, StructType(StructField("_1", dataType) :: Nil)))
  }

  /**
   * Creates a single column DataFrame from an RDD[Long].
   * @since 1.3.0
   */
  implicit def longRddToDataFrameHolder(data: RDD[Long]): DataFrameHolder = {
    val dataType = LongType
    val rows = data.mapPartitions { iter =>
      val row = new SpecificMutableRow(dataType :: Nil)
      iter.map { v =>
        row.setLong(0, v)
        row: Row
      }
    }
    DataFrameHolder(
      _xdContext.createDataFrame(rows, StructType(StructField("_1", dataType) :: Nil)))
  }

  /**
   * Creates a single column DataFrame from an RDD[String].
   * @since 1.3.0
   */
  implicit def stringRddToDataFrameHolder(data: RDD[String]): DataFrameHolder = {
    val dataType = StringType
    val rows = data.mapPartitions { iter =>
      val row = new SpecificMutableRow(dataType :: Nil)
      iter.map { v =>
        row.update(0, UTF8String(v))
        row: Row
      }
    }
    DataFrameHolder(
      _xdContext.createDataFrame(rows, StructType(StructField("_1", dataType) :: Nil)))
  }
}


