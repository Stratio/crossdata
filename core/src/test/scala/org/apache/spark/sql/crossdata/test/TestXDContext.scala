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

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}


/**
 * A special [[SQLContext]] prepared for testing.
 */
private[sql] class TestXDContext(sc: SparkContext, catalogConfig: Config)
  extends XDContext(sc, catalogConfig) {

  def this() {
    this(new SparkContext(
      "local[2]",
      "test-xd-context",
      new SparkConf().set("spark.cores.max", "2").set("spark.sql.testkey", "true").set("spark.sql.shuffle.partitions", "3")
    ), ConfigFactory.empty())
  }

  def this(catalogConfig: Config) {
    this(new SparkContext(
      "local[2]",
      "test-xd-context",
      new SparkConf().set("spark.cores.max", "2").set("spark.sql.testkey", "true").set("spark.sql.shuffle.partitions", "3")
    ), catalogConfig)
  }

}
