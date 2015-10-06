/*
 * Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
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

import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}
import org.apache.spark.sql.{ColumnName, DataFrame}

import scala.language.implicitConversions

/**
 * Helper trait for SQL test suites where all tests share a single [[TestXDContext]].
 */
trait SharedXDContextTest extends XDTestUtils {

  /**
   * The [[TestXDContext]] to use for all tests in this suite.
   *
   * By default, the underlying [[org.apache.spark.SparkContext]] will be run in local
   * mode with the default test configurations.
   */
  private var _ctx: org.apache.spark.sql.crossdata.test.TestXDContext = null

  /**
   * The [[TestXDContext]] to use for all tests in this suite.
   */
  protected def ctx: TestXDContext = _ctx

  protected def xdContext: TestXDContext = _ctx

  protected override def _xdContext: XDContext = _ctx

  /**
   * Initialize the [[TestXDContext]].
   */
  protected override def beforeAll(): Unit = {
    if (_ctx == null) {
      _ctx = new TestXDContext
    }
    // Ensure we have initialized the context before calling parent code
    super.beforeAll()
  }

  /**
   * Stop the underlying [[org.apache.spark.SparkContext]], if any.
   */
  protected override def afterAll(): Unit = {
    try {
      if (_ctx != null) {
        _ctx.sparkContext.stop()
        _ctx = null
      }
    } finally {
      super.afterAll()
    }
  }

  /**
   * Converts $"col name" into an Column.
   * @since 1.3.0
   */
  // This must be duplicated here to preserve binary compatibility with Spark < 1.5.
  implicit class StringToColumn(val sc: StringContext) {
    def $(args: Any*): ColumnName = {
      new ColumnName(sc.s(args: _*))
    }
  }

  implicit def dataFrameToXDFrame(dataFrame: DataFrame): XDDataFrame = new XDDataFrame(dataFrame.sqlContext, dataFrame.queryExecution.logical)
}
