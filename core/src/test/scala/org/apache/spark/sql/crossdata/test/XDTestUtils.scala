// scalastyle:off
/* Modification and adapations - Copyright (C) 2015 Stratio (http://stratio.com)
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
// scalastyle:on

package org.apache.spark.sql.crossdata.test

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.util.Utils
import org.scalatest.BeforeAndAfterAll

import scala.language.implicitConversions
import scala.util.Try

/**
 * Helper trait that should be extended by all SQL test suites.
 *
 * This allows subclasses to plugin a custom [[SQLContext]]. It comes with test data
 * prepared in advance as well as all implicit conversions used extensively by dataframes.
 * To use implicit methods, import `testImplicits._` instead of through the [[SQLContext]].
 *
 * Subclasses should *not* create [[SQLContext]]s in the test suite constructor, which is
 * prone to leaving multiple overlapping [[org.apache.spark.SparkContext]]s in the same JVM.
 */
trait XDTestUtils
  extends BaseXDTest
  with BeforeAndAfterAll {
  self =>

  protected def _xdContext: XDContext

  // Shorthand for running a query using our SQLContext
  protected lazy val sql = _xdContext.sql _


  /**
   * The Hadoop configuration used by the active [[SQLContext]].
   */
  protected def configuration: Configuration = {
    _xdContext.sparkContext.hadoopConfiguration
  }

  /**
   * A helper object for importing SQL implicits.
   *
   * Note that the alternative of importing `sqlContext.implicits._` is not possible here.
   * This is because we create the [[SQLContext]] immediately before the first test is run,
   * but the implicits import is needed in the constructor.
   */
  protected object testImplicits extends XDImplicits {
    protected override def _xdContext: XDContext = self._xdContext
  }


  /**
   * Sets all SQL configurations specified in `pairs`, calls `f`, and then restore all SQL
   * configurations.
   *
   * @todo Probably this method should be moved to a more general place
   */
  protected def withSQLConf(pairs: (String, String)*)(f: => Unit): Unit = {
    val (keys, values) = pairs.unzip
    val currentValues = keys.map(key => Try(_xdContext.conf.getConfString(key)).toOption)
    (keys, values).zipped.foreach(_xdContext.conf.setConfString)
    try f finally {
      keys.zip(currentValues).foreach {
        case (key, Some(value)) => _xdContext.conf.setConfString(key, value)
        case (key, None) => _xdContext.conf.unsetConf(key)
      }
    }
  }

  /**
   * Generates a temporary path without creating the actual file/directory, then pass it to `f`. If
   * a file/directory is created there by `f`, it will be delete after `f` returns.
   *
   * @todo Probably this method should be moved to a more general place
   */
  protected def withTempPath(f: File => Unit): Unit = {
    val path = Utils.createTempDir()
    path.delete()
    try f(path) finally Utils.deleteRecursively(path)
  }

  /**
   * Creates a temporary directory, which is then passed to `f` and will be deleted after `f`
   * returns.
   *
   * @todo Probably this method should be moved to a more general place
   */
  protected def withTempDir(f: File => Unit): Unit = {
    val dir = Utils.createTempDir().getCanonicalFile
    try f(dir) finally Utils.deleteRecursively(dir)
  }


  /**
   * Turn a logical plan into a [[DataFrame]]. This should be removed once we have an easier
   * way to construct [[XDDataFrame]] directly out of local data without relying on implicits.
   */
  protected implicit def logicalPlanToSparkQuery(plan: LogicalPlan): DataFrame = {
    XDDataFrame(_xdContext, plan)
  }
}