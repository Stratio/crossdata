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

package org.apache.spark.sql.crossdata.hive

import java.util.concurrent.atomic.AtomicReference

import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.crossdata.XDDataframe
import org.apache.spark.sql.hive.HiveContext

/**
 * CrossdataHiveContext leverages the features of [[org.apache.spark.sql.hive.HiveContext]]
 * and adds the features of the [[org.apache.spark.sql.crossdata.XDContext]].
 *
 * @param sc A [[org.apache.spark.SparkContext]].
 */
class XDHiveContext(sc: SparkContext) extends HiveContext(sc) {

  override def sql(sqlText: String): DataFrame = {
    XDDataframe(this, parseSql(sqlText))
  }
}

/**
 * This XDHiveContext object contains utility functions to create a singleton XDContext instance,
 * or to get the last created XDHiveContext instance.
 */
object XDHiveContext {

  private val INSTANTIATION_LOCK = new Object()

  /**
   * Reference to the last created SQLContext.
   */
  @transient private val lastInstantiatedContext = new AtomicReference[XDHiveContext]()

  /**
   * Get the singleton SQLContext if it exists or create a new one using the given SparkContext.
   * This function can be used to create a singleton SQLContext object that can be shared across
   * the JVM.
   */
  def getOrCreate(sparkContext: SparkContext): XDHiveContext = {
    INSTANTIATION_LOCK.synchronized {
      Some(lastInstantiatedContext.get()).getOrElse(new XDHiveContext(sparkContext))
    }
    lastInstantiatedContext.get()
  }

  private[sql] def clearLastInstantiatedContext(): Unit = {
    INSTANTIATION_LOCK.synchronized {
      // scalastyle:off
      lastInstantiatedContext.set(null)
      // scalastyle:on
    }
  }

  private[sql] def setLastInstantiatedContext(xdHiveContext: XDHiveContext): Unit = {
    INSTANTIATION_LOCK.synchronized {
      lastInstantiatedContext.set(xdHiveContext)
    }
  }
}


