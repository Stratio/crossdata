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
import com.typesafe.config.Config
import org.apache.log4j.Logger
import org.apache.spark.sql.catalyst.analysis.{Analyzer, CleanupAliases, FunctionRegistry, HiveTypeCoercion}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDContext.{SecurityAuditConfigKey, SecurityClassConfigKey, SecurityPasswordConfigKey, SecuritySessionConfigKey, SecurityUserConfigKey, StreamingCatalogClassConfigKey}
import org.apache.spark.sql.catalyst.analysis.{Analyzer, CleanupAliases, ComputeCurrentTime, DistinctAggregationRewriter, FunctionRegistry, HiveTypeCoercion, ResolveUpCast}
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataApp
import org.apache.spark.sql.crossdata.catalog.{XDCatalog, XDStreamingCatalog}
import org.apache.spark.sql.crossdata.catalyst.analysis.{PrepareAggregateAlias, ResolveAggregateAlias}
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.execution.datasources.{ExtendedDataSourceStrategy, ImportTablesUsingWithOptions, XDDdlParser}
import org.apache.spark.sql.crossdata.execution.{ExtractNativeUDFs, NativeUDF, XDStrategies}
import org.apache.spark.sql.crossdata.security.{Credentials, SecurityManager}
import org.apache.spark.sql.crossdata.user.functions.GroupConcat
import org.apache.spark.sql.{DataFrame, Row, SQLContext, Strategy, execution => sparkexecution}
import org.apache.spark.sql.execution.ExtractPythonUDFs
import org.apache.spark.sql.execution.datasources.{PreInsertCastAndRename, PreWriteCheck}
import org.apache.spark.sql.types.StructType
import org.apache.spark.util.Utils
import org.apache.spark.{Logging, SparkContext}

/**
 * CrossdataContext leverages the features of [[SQLContext]]
 * and adds some features of the Crossdata system.
  *
  * @param sc A [[SparkContext]].
 */
class XDContext private (@transient val sc: SparkContext,
                userConfig: Option[Config] = None,
                credentials: Credentials = Credentials()) extends SQLContext(sc) with Logging  {
  self =>

  def this(sc: SparkContext) =
    this(sc, None)

  def this(sc: SparkContext, config: Config) =
    this(sc, Some(config))


  /* TODO: Remove the config attributes from the companion object!!!
     This only works because you can only have a SQLContext per running instance
     but its a dirty trick to avoid typesafe's Config serialization when sending the tasks
     to each worker.

     Config should be changed by a map and implicitly converted into `Config` whenever one of its
     methods is called.
     */
  import XDContext.{catalogConfig, config, xdConfig}

  xdConfig = userConfig.fold(config) { userConf =>
    userConf.withFallback(config)
  }

  catalogConfig = xdConfig.getConfig(CoreConfig.CatalogConfigKey)

  @transient
  override protected[sql] lazy val catalog: XDCatalog = {

    import XDContext.{CaseSensitive, DerbyClass}

    val catalogClass = if (catalogConfig.hasPath(XDContext.ClassConfigKey))
      catalogConfig.getString(XDContext.ClassConfigKey)
    else DerbyClass

    val xdCatalog = Class.forName(catalogClass)

    val caseSensitive: Boolean = catalogConfig.getBoolean(CaseSensitive)

    val constr: Constructor[_] = xdCatalog.getConstructor(classOf[CatalystConf], classOf[XDContext])

    constr.newInstance(
      new SimpleCatalystConf(caseSensitive), self).asInstanceOf[XDCatalog]
  }

  @transient
  protected[crossdata] lazy val streamingCatalog: Option[XDStreamingCatalog] = {
    if (xdConfig.hasPath(StreamingCatalogClassConfigKey)) {
      val streamingCatalogClass = xdConfig.getString(StreamingCatalogClassConfigKey)
      val xdStreamingCatalog = Class.forName(streamingCatalogClass)
      val constr: Constructor[_] = xdStreamingCatalog.getConstructor(classOf[XDContext])

      Option(constr.newInstance(self).asInstanceOf[XDStreamingCatalog])
    } else {
      logError("Empty streaming catalog")
      None
    }
  }

  @transient
  protected[crossdata] lazy val securityManager = {

    import XDContext.DefaultSecurityManager

    val securityClass = if (xdConfig.hasPath(SecurityClassConfigKey))
      xdConfig.getString(SecurityClassConfigKey)
    else DefaultSecurityManager

    val audit: java.lang.Boolean = if (xdConfig.hasPath(SecurityAuditConfigKey))
      xdConfig.getBoolean(SecurityAuditConfigKey)
    else false

    val userConfig = if (xdConfig.hasPath(SecurityUserConfigKey))
      Some(xdConfig.getString(SecurityUserConfigKey))
    else None

    val passwordConfig = if (xdConfig.hasPath(SecurityPasswordConfigKey))
      Some(xdConfig.getString(SecurityPasswordConfigKey))
    else None

    val sessionIdConfig = if (xdConfig.hasPath(SecuritySessionConfigKey))
      Some(xdConfig.getString(SecuritySessionConfigKey))
    else None

    val securityManagerClass = Class.forName(securityClass)

    val constr: Constructor[_] = securityManagerClass.getConstructor(classOf[Credentials], classOf[Boolean])

    val fallbackCredentials = credentials.copy(
      user = credentials.user match {
        case Some(u) => Some(u)
        case _ => userConfig
      },
      password = credentials.password match {
        case Some(p) => Some(p)
        case _ => passwordConfig
      },
      sessionId = credentials.sessionId match {
        case Some(s) => Some(s)
        case _ => sessionIdConfig
      })

    constr.newInstance(fallbackCredentials, audit).asInstanceOf[SecurityManager]
  }

  @transient
  override protected[sql] lazy val analyzer: Analyzer =
    new Analyzer(catalog, functionRegistry, conf) {
      override val extendedResolutionRules =
        ResolveAggregateAlias ::
          ExtractPythonUDFs ::
          ExtractNativeUDFs ::
          PreInsertCastAndRename ::
          Nil

      override val extendedCheckRules = Seq(
        PreWriteCheck(catalog)
      )

      val preparationRules = Seq(PrepareAggregateAlias)

      override lazy val batches: Seq[Batch] = Seq(
        Batch("Substitution", fixedPoint,
          CTESubstitution,
          WindowsSubstitution),
        Batch("Preparation", fixedPoint, preparationRules : _*),
        Batch("Resolution", fixedPoint,
          ResolveRelations ::
            ResolveReferences ::
            ResolveGroupingAnalytics ::
            ResolvePivot ::
            ResolveUpCast ::
            ResolveSortReferences ::
            ResolveGenerate ::
            ResolveFunctions ::
            ResolveAliases ::
            ExtractWindowExpressions ::
            GlobalAggregates ::
            ResolveAggregateFunctions ::
            DistinctAggregationRewriter(conf) ::
            HiveTypeCoercion.typeCoercionRules ++
              extendedResolutionRules : _*),
        Batch("Nondeterministic", Once,
          PullOutNondeterministic,
          ComputeCurrentTime),
        Batch("UDF", Once,
          HandleNullInputsForUDF),
        Batch("Cleanup", fixedPoint,
          CleanupAliases)
      )
    }

  @transient
  class XDPlanner extends sparkexecution.SparkPlanner(this) with XDStrategies {
    override def strategies: Seq[Strategy] = Seq(XDDDLStrategy, ExtendedDataSourceStrategy) ++ super.strategies
  }

  @transient
  override protected[sql] val planner: sparkexecution.SparkPlanner = new XDPlanner

  @transient
  protected[sql] override val ddlParser = new XDDdlParser(sqlParser.parse(_), this)

  @transient
  override protected[sql] lazy val functionRegistry: FunctionRegistry =
    new XDFunctionRegistry(FunctionRegistry.builtin.copy(), functionInventoryServices)

  private def functionInventoryLoader = {
    val loader = Utils.getContextOrSparkClassLoader
    ServiceLoader.load(classOf[FunctionInventory], loader)
  }

  @transient
  private lazy val functionInventoryServices: Seq[FunctionInventory] = {
    import scala.collection.JavaConversions._
    functionInventoryLoader.iterator().toSeq
  }

  {
    //Register built-in UDFs for each provider available.
    import FunctionInventory.qualifyUDF
    for {srv <- functionInventoryServices
         datasourceName = srv.shortName()
         udf <- srv.nativeBuiltinFunctions
    } functionRegistry
      .registerFunction(qualifyUDF(datasourceName, udf.name), e => NativeUDF(udf.name, udf.returnType, e))

    val gc = new GroupConcat(", ")
    udf.register("group_concat", gc)
  }

  override def sql(sqlText: String): DataFrame = {
    XDDataFrame(this, parseSql(sqlText))
  }

  /**
    * Add JAR file from XD Driver to the context
    *
    * @param path The local path or hdfs path where SparkContext will take the JAR
    */
  override def addJar(path: String) = {
       super.addJar(path)
  }

  def addApp(path: String, clss: String, alias: String): Unit ={
    catalog.persistAppMetadata(CrossdataApp(path,alias,clss))
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
    catalog.dropTable(tableIdentifier)
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

  /**
   * Imports tables from a DataSource in the persistent catalog.
   *
   * @param datasource
   * @param opts
   */
  def importTables(datasource: String, opts: Map[String, String]): Unit =
    ImportTablesUsingWithOptions(datasource, opts).run(this)


  /**
    * Check if there is Connection with the catalog
    *
    * @return if connection is possible
    */
  def checkCatalogConnection : Boolean=
    catalog.checkConnectivity



  def createDataFrame(rows: Seq[Row], schema: StructType): DataFrame =
    DataFrame(self, LocalRelation.fromExternalRows(schema.toAttributes, rows))


  XDContext.setLastInstantiatedContext(self)
}

/**
 * This XDContext object contains utility functions to create a singleton XDContext instance,
 * or to get the last created XDContext instance.
 */
object XDContext extends CoreConfig {

  /* TODO: Remove the config attributes from the companion object!!!
      AS WELL AS change the companion object so it doesn't extends `CoreConfig`!!!!
   */

  //This is definitely NOT right and will only work as long a single instance of XDContext exits
  override lazy val logger = Logger.getLogger(classOf[XDContext])

  var xdConfig: Config = _ //This is definitely NOT right and will only work as long a single instance of XDContext exits
  var catalogConfig: Config = _ //This is definitely NOT right and will only work as long a single instance of XDContext exits

  val CaseSensitive = "caseSensitive"
  val DerbyClass = "org.apache.spark.sql.crossdata.catalog.DerbyCatalog"
  val DefaultSecurityManager = "org.apache.spark.sql.crossdata.security.DefaultSecurityManager"
  val JDBCClass = "org.apache.spark.sql.crossdata.catalog.JDBCCatalog"
  val ZookeeperClass = "org.apache.spark.sql.crossdata.catalog.ZookeeperCatalog"
  val CatalogConfigKey = "catalog"
  val StreamingConfigKey = "streaming"
  val SecurityConfigKey = "security"
  val SecurityManagerConfigKey = "manager"
  val ClassConfigKey = "class"
  val AuditConfigKey = "audit"
  val UserConfigKey = "user"
  val PasswordConfigKey = "password"
  val SessionConfigKey = "session"
  val CatalogClassConfigKey = s"$CatalogConfigKey.$ClassConfigKey"
  val StreamingCatalogClassConfigKey = s"$StreamingConfigKey.$CatalogConfigKey.$ClassConfigKey"
  val SecurityClassConfigKey = s"$SecurityConfigKey.$SecurityManagerConfigKey.$ClassConfigKey"
  val SecurityAuditConfigKey = s"$SecurityConfigKey.$SecurityManagerConfigKey.$AuditConfigKey"
  val SecurityUserConfigKey = s"$SecurityConfigKey.$SecurityManagerConfigKey.$UserConfigKey"
  val SecurityPasswordConfigKey = s"$SecurityConfigKey.$SecurityManagerConfigKey.$PasswordConfigKey"
  val SecuritySessionConfigKey = s"$SecurityConfigKey.$SecurityManagerConfigKey.$SessionConfigKey"


  @transient private val INSTANTIATION_LOCK = new Object()

  /**
   * Reference to the last created SQLContext.
   */
  @transient private val lastInstantiatedContext = new AtomicReference[XDContext]()

  /**
   * Get the singleton SQLContext if it exists or create a new one using the given SparkContext.
   * This function can be used to create a singleton SQLContext object that can be shared across
   * the JVM.
   */
  def getOrCreate(sparkContext: SparkContext, userConfig: Option[Config] = None): XDContext = {
    INSTANTIATION_LOCK.synchronized {
      Option(lastInstantiatedContext.get()).getOrElse(new XDContext(sparkContext, userConfig))
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

