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

package org.apache.spark.sql.crossdata

import java.util.concurrent.atomic.AtomicReference

import org.apache.spark.sql.sources.crossdata.XDDdlParser
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{Logging, SparkContext}

/**
 * CrossdataContext leverages the features of [[SQLContext]]
 * and adds some features of the Crossdata system.
 * @param sc A [[SparkContext]].
 */
class XDContext(@transient val sc: SparkContext) extends SQLContext(sc) with Logging {
  self =>

  /*
  val xdConfig: Config = ConfigFactory.load
  val catalogClass: String = xdConfig.getString("crossdata.catalog.class")
  val caseSensitive: Boolean = xdConfig.getBoolean("crossdata.catalog.caseSensitive")

  import scala.collection.JavaConversions._

  val catalogArgs: util.List[String] =
    xdConfig.getList("crossdata.catalog.args").map(e => e.toString)

  val xdCatalog = Class.forName(catalogClass)

  val constr: Constructor[_] = xdCatalog.getConstructor(
    classOf[CatalystConf],
    classOf[util.List[String]])

  override protected[sql] lazy val catalog: XDCatalog =
    constr.newInstance(
      new SimpleCatalystConf(caseSensitive),
      catalogArgs).asInstanceOf[XDCatalog]

  catalog.open()
 */

  protected[sql] override val ddlParser = new XDDdlParser(sqlParser.parse(_))

  override def sql(sqlText: String): DataFrame = {
    XDDataFrame(this, parseSql(sqlText))
  }

  XDContext.setLastInstantiatedContext(self)
}

/**
 * This XDContext object contains utility functions to create a singleton XDContext instance,
 * or to get the last created XDContext instance.
 */
object XDContext {

  private val INSTANTIATION_LOCK = new Object()

  /**
   * Reference to the last created SQLContext.
   */
  @transient private val lastInstantiatedContext = new AtomicReference[XDContext]()

  /**
   * Get the singleton SQLContext if it exists or create a new one using the given SparkContext.
   * This function can be used to create a singleton SQLContext object that can be shared across
   * the JVM.
   */
  def getOrCreate(sparkContext: SparkContext): XDContext = {
    INSTANTIATION_LOCK.synchronized {
      Some(lastInstantiatedContext.get()).getOrElse(new XDContext(sparkContext))
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

  private[sql] def setLastInstantiatedContext(xdContext: XDContext): Unit = {
    INSTANTIATION_LOCK.synchronized {
      lastInstantiatedContext.set(xdContext)
    }
  }
}

