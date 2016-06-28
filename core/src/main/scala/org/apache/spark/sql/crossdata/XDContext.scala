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

import java.io.InputStream
import java.lang.reflect.{Constructor, Method}
import java.net.{URL, URLClassLoader}
import java.nio.file.StandardCopyOption
import java.util.ServiceLoader
import java.util.concurrent.atomic.AtomicReference

import com.stratio.crossdata.connector.FunctionInventory
import com.stratio.crossdata.utils.HdfsUtils
import com.typesafe.config.Config
import org.apache.log4j.Logger
import org.apache.spark.sql.catalyst.analysis.{Analyzer, CleanupAliases, ComputeCurrentTime, DistinctAggregationRewriter, FunctionRegistry, HiveTypeCoercion, NoSuchTableException, ResolveUpCast, UnresolvedAttribute, UnresolvedRelation}
import org.apache.spark.sql.catalyst.expressions.{And, Attribute, AttributeReference, Contains, EqualTo, Expression, GreaterThan, GreaterThanOrEqual, In, IsNotNull, IsNull, LessThan, LessThanOrEqual, Literal, Or, Predicate, StartsWith, UnsafeRow}
import org.apache.spark.sql.catalyst.optimizer.{DefaultOptimizer, Optimizer}
import org.apache.spark.sql.catalyst.plans.logical
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.catalyst.{CatalystConf, InternalRow, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDContext.{SecurityAuditConfigKey, SecurityClassConfigKey, SecurityPasswordConfigKey, SecuritySessionConfigKey, SecurityUserConfigKey, StreamingCatalogClassConfigKey}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataApp, CrossdataIndex, IndexIdentifier}
import org.apache.spark.sql.crossdata.catalog._
import org.apache.spark.sql.crossdata.catalog.inmemory.HashmapCatalog
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDPersistentCatalog, XDStreamingCatalog, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.catalyst.ExtendedUnresolvedRelation
import org.apache.spark.sql.crossdata.catalyst.analysis.{PrepareAggregateAlias, ResolveAggregateAlias}
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.execution.datasources.{DDLUtils, ExtendedDataSourceStrategy, ImportTablesUsingWithOptions, XDDdlParser}
import org.apache.spark.sql.crossdata.execution.{ExtractNativeUDFs, NativeUDF, XDStrategies}
import org.apache.spark.sql.crossdata.launcher.SparkJobLauncher
import org.apache.spark.sql.crossdata.security.{Credentials, SecurityManager}
import org.apache.spark.sql.crossdata.user.functions.GroupConcat
import org.apache.spark.sql.execution.{ExtractPythonUDFs, SparkPlan}
import org.apache.spark.sql.execution.datasources.{LogicalRelation, PreInsertCastAndRename, PreWriteCheck}
import org.apache.spark.sql.sources.BaseRelation
import org.apache.spark.sql.types.{DataType, IntegerType, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext, Strategy, execution => sparkexecution}
import org.apache.spark.util.Utils
import org.apache.spark.{Logging, SparkContext}

import scala.annotation.tailrec
import scala.util.{Failure, Success}

/**
  * CrossdataContext leverages the features of [[SQLContext]]
  * and adds some features of the Crossdata system.
  *
  * @param sc A [[SparkContext]].
  */
class XDContext protected (@transient val sc: SparkContext,
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

  private val catalystConf: CatalystConf = {
    import XDContext.CaseSensitive
    val caseSensitive: Boolean = catalogConfig.getBoolean(CaseSensitive)
    new SimpleCatalystConf(caseSensitive)
  }

  @transient
  override protected[sql] lazy val catalog: XDCatalog = {
    val catalogs: List[XDCatalogCommon] =  temporaryCatalog :: externalCatalog :: streamingCatalog.toList
    CatalogChain(catalogs:_*)(catalystConf)
  }

  @transient
  private lazy val temporaryCatalog: XDTemporaryCatalog = new HashmapCatalog(catalystConf)

  @transient
  private lazy val externalCatalog: XDPersistentCatalog = {

    import XDContext.DerbyClass
    val externalCatalogName = if (catalogConfig.hasPath(XDContext.ClassConfigKey))
      catalogConfig.getString(XDContext.ClassConfigKey)
    else DerbyClass

    val externalCatalogClass = Class.forName(externalCatalogName)
    val constr: Constructor[_] = externalCatalogClass.getConstructor(classOf[SQLContext], classOf[CatalystConf])

    constr.newInstance(self, catalystConf).asInstanceOf[XDPersistentCatalog]
  }


  @transient
  private lazy val streamingCatalog: Option[XDStreamingCatalog] = {
    if (xdConfig.hasPath(StreamingCatalogClassConfigKey)) {
      val streamingCatalogClass = xdConfig.getString(StreamingCatalogClassConfigKey)
      val xdStreamingCatalog = Class.forName(streamingCatalogClass)
      val constr: Constructor[_] = xdStreamingCatalog.getConstructor(classOf[CatalystConf])
      Option(constr.newInstance(catalystConf).asInstanceOf[XDStreamingCatalog])
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

  object IndexUtils {

    /**
      * Return if all  attribute in the exprs are indexed columns
      *
      * @param condition filter.condition
      * @param indexedCols
      * @return
      */
    def areAllAttributeIndexedInExpr(condition: Expression, indexedCols: Seq[String]): Boolean = { //TODO: ES capabilities

      @tailrec
      def helper(remainExpr: Seq[Expression], allValids: Boolean): Boolean = {
        if(remainExpr.isEmpty) allValids
        else {
          val (supportedOperation, foundAttr, isIndexed) = remainExpr.head match {
            case predicate: Predicate => (isSupportedPredicate(predicate), false, false)
            case UnresolvedAttribute(name)=> (true, true, indexedCols.contains(name.last))
            case AttributeReference(name,_,_,_) => (true, true, indexedCols.contains(name))
            case _ => (true, false, false)
          }
          if(supportedOperation) {
            if (foundAttr) {
              if (isIndexed) {
                if (remainExpr.head.children.length > 0) helper(remainExpr.tail ++ remainExpr.head.children, true)
                else helper(remainExpr.tail, true)
              } else {
                false
              }
            } else {
              if (remainExpr.head.children.length > 0) helper(remainExpr.tail ++ remainExpr.head.children, allValids)
              else helper(remainExpr.tail, allValids)
            }
          } else{
            false
          }
        }
      }

      helper(Seq(condition), false)
    }

    /**
      * Check if predicate is supported by ElasticSearch native queries to use the index
      *
      * @param predicate
      * @return
      */
    def isSupportedPredicate(predicate: Predicate): Boolean = predicate match {
      //TODO: Add more filters?? Reference: ElasticSearchQueryProcessor
      case _: And => true
      case _: Contains => true
      case _: EqualTo => true
      case _: GreaterThan => true
      case _: GreaterThanOrEqual => true
      case _: In => true
      case _: IsNull => true
      case _: IsNotNull => true
      case _: LessThan => true
      case _: LessThanOrEqual => true
      case _: Or => true
      case _: StartsWith => true

      case _ => false
    }

  }

  @transient
  override protected[sql] lazy val analyzer: Analyzer =
    new Analyzer(catalog, functionRegistry, catalystConf) {
      override val extendedResolutionRules =
        ResolveAggregateAlias ::
          ExtractPythonUDFs ::
          ExtractNativeUDFs ::
          PreInsertCastAndRename ::
          Nil

      override val extendedCheckRules = Seq(
        PreWriteCheck(catalog)
      )

      object XDResolveRelations extends Rule[LogicalPlan] {

        def planWithAvailableIndex(plan: LogicalPlan): Boolean = {

          //Get filters and escape projects to check if plan could be resolved using Indexes
          @tailrec
          def helper(filtersConditions: Seq[Expression], actual:LogicalPlan): Boolean = actual match {
            case logical.Filter(condition, child: LogicalPlan) =>
              helper(filtersConditions :+ condition, child)

            case p @ logical.Project(_, child: LogicalPlan) =>
              helper(filtersConditions, child)

            case u: UnresolvedRelation =>
              //Check if table has index and if there are some Filter that have all its attributes indexed
              val index = catalog.obtainTableIndex(u.tableIdentifier)
              if(index.isDefined){
                val canBeResolvedByFilter = filtersConditions filter { condition =>
                  IndexUtils.areAllAttributeIndexedInExpr(condition, index.get.indexedCols)
                }
                canBeResolvedByFilter.length > 0
              } else{
                false
              }

            case _ => false
          }

          helper(Seq(), plan)
        }

        override def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {

          case  pp if planWithAvailableIndex(pp) =>
            plan resolveOperators {
              case unresolvedRelation: UnresolvedRelation =>
                ExtendedUnresolvedRelation(unresolvedRelation.tableIdentifier, unresolvedRelation)
            }
        }
      }

      val preparationRules = Seq(PrepareAggregateAlias)

      override lazy val batches: Seq[Batch] = Seq(
        Batch("Substitution", fixedPoint,
          CTESubstitution,
          WindowsSubstitution),
        Batch("Preparation", fixedPoint, preparationRules : _*),
        Batch("Resolution", fixedPoint,
          XDResolveRelations ::
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
  override protected[sql] lazy val optimizer: Optimizer = XDOptimizer

  object XDOptimizer extends Optimizer {

    def convertStrategy(strategy: DefaultOptimizer.Strategy): Strategy = strategy.maxIterations match {
      case 1 => Once
      case n => FixedPoint(n)
    }

    def convertBatches(batch: DefaultOptimizer.Batch): XDOptimizer.Batch =
      Batch(batch.name, convertStrategy(batch.strategy), batch.rules: _*)

    override protected val batches: Seq[XDOptimizer.Batch] =
      (DefaultOptimizer.batches map (convertBatches(_))) ++ Seq(Batch("Global indexes phase", FixedPoint(10), CheckGlobalIndexInFilters))
  }

  object CheckGlobalIndexInFilters extends Rule[LogicalPlan] {

    private def analyze(plan: LogicalPlan): LogicalPlan = {
      val analyzed = self.analyzer.execute(plan)
      self.analyzer.checkAnalysis(analyzed)
      analyzed
    }

    private def analyzeAndOptimize(plan: LogicalPlan): LogicalPlan = {
      self.optimizer.execute(analyze(plan))
    }

    private def optimizeAndToSparkPlan(plan: LogicalPlan): SparkPlan = {
      self.planner.plan(analyzeAndOptimize(plan)).next()
    }

    private def schemaToAttribute(schema: StructType): Seq[UnresolvedAttribute] =
      schema.fields map {field => UnresolvedAttribute(field.name)}

    private def resultPksToLiterals(rows: Array[Row], dataType:DataType): Seq[Literal] = // TODOrow changed
      rows map { row =>
        val valTransformed = row.toSeq.head // TODO changed
        Literal.create(valTransformed, dataType)
      } //TODO compound PK

    private def buildIndexRequestLogicalPlan(condition: Expression, index: CrossdataIndex): LogicalPlan = {

      val (logical,base) = self.catalog.lookupRelation(index.indexIdentifier.asTableIdentifier) match {
        case Subquery(_, logicalRelation @ LogicalRelation(relation: BaseRelation, _)) => (logicalRelation,relation)
      }

      //We need to retrieve all the retrieve cols for use the filter
      val pkAndColsIndexed: Seq[UnresolvedAttribute] = schemaToAttribute(DDLUtils.extractSchema(Seq(index.pk)++index.indexedCols, base.schema))

      //Old attributes reference have to be updated
      val convertedCondition = condition transform {
        case UnresolvedAttribute(name) => (pkAndColsIndexed filter (_.name == name)).head
        case AttributeReference(name, _, _, _) => (pkAndColsIndexed filter (_.name == name)).head
      }

      Filter(convertedCondition, Project(pkAndColsIndexed, logical))
    }

    def combineFiltersAndRelation(filters: Seq[LogicalPlan], relation: LogicalPlan): LogicalPlan =
      filters.foldRight(relation){(filter, accum) => filter.withNewChildren(Seq(accum))}


    def apply(plan: LogicalPlan): LogicalPlan = plan transform {

      case FilterWithIndexLogicalPlan(filters, projects, ExtendedUnresolvedRelation(tableIdentifier, child)) =>
        val crossdataIndex = self.catalog.asInstanceOf[XDCatalog].obtainTableIndex(tableIdentifier) getOrElse
          (sys.error("Unexpected error. Can't find index for enhance query with indexes"))

        //Change the filters that has indexed rows, with a Filter IN with ES results or LocalRelation if we don't have results
        val newFilters: Seq[LogicalPlan] = filters map { filter =>
          if(IndexUtils.areAllAttributeIndexedInExpr(filter.condition, crossdataIndex.indexedCols)) {
            val indexLogicalPlan = buildIndexRequestLogicalPlan(filter.condition, crossdataIndex)
            val indexedRows = XDDataFrame(self, indexLogicalPlan).collect() //TODO: Warning memory issues
            if (indexedRows.length > 0) {

              //Convert to query with filter IN
              val lr = child.collectFirst{ case lr@ LogicalRelation(baseRelation, _) => lr }.get
              val pkSchema = DDLUtils.extractSchema(Seq(crossdataIndex.pk), lr.schema)
              val pkAttribute =  schemaToAttribute(pkSchema).head
              analyzeAndOptimize(logical.Filter(In(pkAttribute, resultPksToLiterals(indexedRows,pkSchema.fields.head.dataType)),child))

            } else {
              LocalRelation(filter.output)
            }
          } else {
            filter
          }
        }

        //If LocalRelation appear there are no results
        val noResults: Option[LocalRelation] = newFilters collectFirst {
          case localRelation : LocalRelation => localRelation
        }

        noResults getOrElse {
          //If projects exists, just remain the first in the tree + Filters + Relation
          val combined: LogicalPlan = combineFiltersAndRelation(newFilters, child)
          if(projects.length > 0){
            analyzeAndOptimize(projects.head.withNewChildren(Seq(combined)))
          } else {
            analyzeAndOptimize(combined)
          }
        }

    }


    object FilterWithIndexLogicalPlan {
      type ReturnType = (Seq[Filter], Seq[Project], ExtendedUnresolvedRelation)

      def unapply(plan: LogicalPlan): Option[ReturnType] = plan match {
        case f @ logical.Filter(condition, child: LogicalPlan) =>
          recoverFilterAndProjects(Seq(f), Seq(), child)

        case _ => None
      }

      @tailrec
      def recoverFilterAndProjects(filters: Seq[logical.Filter], projects: Seq[logical.Project], actual:LogicalPlan): Option[ReturnType] = actual match {
        case f @ logical.Filter(_, child: LogicalPlan) =>
          recoverFilterAndProjects(filters :+ f, projects, child)

        case p @ logical.Project(_, child: LogicalPlan) =>
          recoverFilterAndProjects(filters, projects :+ p, child)

        case u: ExtendedUnresolvedRelation =>
          Some(filters, projects, u)

        case _ => None
      }
    }
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
  def addJar(path: String, toClasspath:Option[Boolean]=None) = {
    super.addJar(path)
    if ((path.toLowerCase.startsWith("hdfs://")) && (toClasspath.getOrElse(true))){
      val hdfsIS: InputStream = HdfsUtils(xdConfig.getConfig(CoreConfig.HdfsKey)).getFile(path)
      val file: java.io.File = createFile(hdfsIS, s"${xdConfig.getConfig(CoreConfig.JarsRepo).getString("externalJars")}/${path.split("/").last}")
      addToClasspath(file)
    }else if (scala.reflect.io.File(path).exists){
      val file=new java.io.File(path)
      addToClasspath(file)
    }else{
      sys.error("File doesn't exist or is not a hdfs file")
    }

  }

  private def addToClasspath(file:java.io.File): Unit = {
    if (file.exists) {
      val method: Method = classOf[URLClassLoader].getDeclaredMethod("addURL", classOf[URL])
      method.setAccessible(true)
      method.invoke(ClassLoader.getSystemClassLoader, file.toURI.toURL)
      method.setAccessible(false)
    } else {
      sys.error(s"The file ${file.getName} not exists.")
    }
  }

  private def createFile(hdfsIS: InputStream, path: String): java.io.File = {
    val targetFile = new java.io.File(path)
    java.nio.file.Files.copy(hdfsIS, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
    targetFile
  }

  def addApp(path: String, clss: String, alias: String): Option[CrossdataApp] = {
    val crossdataApp = CrossdataApp(path, alias, clss)
    catalog.persistAppMetadata(crossdataApp)
    catalog.lookupApp(alias)
  }

  def executeApp(appName: String, arguments: Seq[String], submitOptions: Option[Map[String, String]] = None): Seq[Row] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val crossdataApp = catalog.lookupApp(appName).getOrElse(sys.error(s"There is not any app called $appName"))
    val launcherConfig = xdConfig.getConfig(CoreConfig.LauncherKey)
    SparkJobLauncher.getSparkJob(launcherConfig, this.sparkContext.master, crossdataApp.appClass, arguments, crossdataApp.jar, crossdataApp.appAlias, submitOptions) match {
      case Failure(exception) =>
        logError(exception.getMessage, exception)
        sys.error("Validation error: " + exception.getMessage)
        Seq(Row(exception.getMessage))

      case Success(job) =>
        logInfo("SparkJob created. Ready to submit.")
        job.submit()
        Seq(Row("Spark app launched"))
    }
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
  def checkCatalogConnection: Boolean =
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

  var xdConfig: Config = _
  //This is definitely NOT right and will only work as long a single instance of XDContext exits
  var catalogConfig: Config = _ //This is definitely NOT right and will only work as long a single instance of XDContext exits

  val CaseSensitive = "caseSensitive"
  val DerbyClass = "org.apache.spark.sql.crossdata.catalog.persistent.DerbyCatalog"
  val DefaultSecurityManager = "org.apache.spark.sql.crossdata.security.DefaultSecurityManager"
  val ZookeeperClass = "org.apache.spark.sql.crossdata.catalog.persistent.ZookeeperCatalog"
  val ZookeeperStreamingClass = "org.apache.spark.sql.crossdata.catalog.streaming.ZookeeperStreamingCatalog"
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
      Option(lastInstantiatedContext.get()).filter(
        _.getClass == classOf[XDContext]
      ).getOrElse(new XDContext(sparkContext, userConfig))
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

