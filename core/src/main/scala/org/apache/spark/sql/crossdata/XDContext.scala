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


package org.apache.spark.sql.crossdata

import java.lang.reflect.Constructor
import java.util.ServiceLoader
import java.util.concurrent.atomic.AtomicReference

import com.stratio.crossdata.connector.FunctionInventory
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.catalyst._
import org.apache.spark.sql.catalyst.analysis.Analyzer
import org.apache.spark.sql.crossdata.execution.datasources.{ImportTablesUsingWithOptions, ExtendedDataSourceStrategy, XDDdlParser}
import org.apache.spark.sql.crossdata.execution.{ExtractNativeUDFs, NativeUDF, XDStrategies}
import org.apache.spark.sql.execution.ExtractPythonUDFs
import org.apache.spark.sql.execution.datasources.{PreInsertCastAndRename, PreWriteCheck}
import org.apache.spark.sql.{DataFrame, SQLContext, Strategy}
import org.apache.spark.util.Utils
import org.apache.spark.{Logging, SparkContext}

/**
 * CrossdataContext leverages the features of [[SQLContext]]
 * and adds some features of the Crossdata system.
 * @param sc A [[SparkContext]].
 */
class XDContext(@transient val sc: SparkContext) extends SQLContext(sc) with Logging {
  self =>

  override protected[sql] lazy val catalog: XDCatalog = {
    import XDContext._
    val xdConfig: Config = ConfigFactory.load

    val catalogClass = if (xdConfig.hasPath(CatalogClass))
      xdConfig.getString(CatalogClass)
    else
      DerbyClass

    val xdCatalog = Class.forName(catalogClass)

    val caseSensitive: Boolean = xdConfig.getBoolean(CaseSensitive)

    val constr: Constructor[_] = xdCatalog.getConstructor(classOf[CatalystConf], classOf[XDContext])

    constr.newInstance(
      new SimpleCatalystConf(caseSensitive), self).asInstanceOf[XDCatalog]
  }

  @transient
  override protected[sql] lazy val analyzer: Analyzer =
    new Analyzer(catalog, functionRegistry, conf) {
      override val extendedResolutionRules =
        ExtractPythonUDFs ::
          ExtractNativeUDFs::
          PreInsertCastAndRename ::
          Nil

      override val extendedCheckRules = Seq(
        PreWriteCheck(catalog)
      )
    }

  @transient
  class XDPlanner extends SparkPlanner with XDStrategies {
    override def strategies: Seq[Strategy] = Seq(XDDDLStrategy, ExtendedDataSourceStrategy) ++ super.strategies
  }

  @transient
  override protected[sql] val planner: SparkPlanner = new XDPlanner

  @transient
  protected[sql] override val ddlParser = new XDDdlParser(sqlParser.parse(_))


  { //Register built-in UDFs for each provider available.
    import scala.collection.JavaConversions._
    val loader = Utils.getContextOrSparkClassLoader
    val serviceLoader = ServiceLoader.load(classOf[FunctionInventory], loader)
    for {
      srv <- serviceLoader.iterator()
      udf <- srv.nativeBuiltinFunctions
    } functionRegistry.registerFunction(udf.name, e => NativeUDF(udf.name, udf.returnType, e))
  }

  override def sql(sqlText: String): DataFrame = {
    XDDataFrame(this, parseSql(sqlText))
  }

  /**
   * Drops the table in the persistent catalog.
   * It applies only to metadata, so data do not be deleted.
   *
   * @param tableIdentifier the table to be dropped.
   *
   */
  def dropTable(tableIdentifier: TableIdentifier): Unit = {
    cacheManager.tryUncacheQuery(table(tableIdentifier.unquotedString))
    catalog.dropTable(tableIdentifier.toSeq)
  }

  /**
   * Drops all tables in the persistent catalog.
   * It applies only to metadata, so data do not be deleted.
   *
   */
  def dropAllTables(): Unit = {
    cacheManager.clearCache()
    catalog.dropAllTables()
  }

  XDContext.setLastInstantiatedContext(self)

  /**
   * Imports tables from a DataSource in the persistent catalog.
   *
   * @param datasource
   * @param opts
   */
  def importTables(datasource:String, opts: Map[String,String]): Unit ={
    new ImportTablesUsingWithOptions(datasource, opts).run(this)
  }
}

/**
 * This XDContext object contains utility functions to create a singleton XDContext instance,
 * or to get the last created XDContext instance.
 */
object XDContext {

  val CatalogClass = "crossdata.catalog.class"
  val CaseSensitive = "crossdata.catalog.caseSensitive"
  val DerbyClass = "org.apache.spark.sql.crossdata.catalog.DerbyCatalog"

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

