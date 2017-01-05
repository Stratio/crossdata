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
package org.apache.spark.sql.crossdata.session

import com.typesafe.config.ConfigException
import org.apache.spark.sql.{SparkSession, Strategy, UDFRegistration, execution}
import org.apache.spark.sql.catalyst.analysis.{Analyzer, CleanupAliases, EliminateUnions, FunctionRegistry, ResolveInlineTables, ResolveTableValuedFunctions, TimeWindowing, TypeCoercion}
import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.catalyst.optimizer.Optimizer
import org.apache.spark.sql.catalyst.parser.ParserInterface
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.ExtractNativeUDFs
import org.apache.spark.sql.crossdata.execution.XDQueryExecution
import org.apache.spark.sql.execution.datasources._
import org.apache.spark.sql.execution._
import org.apache.spark.sql.internal.{SQLConf, SessionState}

import scala.util.Try

class XDSessionState(
                      val sparkSession: SparkSession//,
                      //val sqlConf: XDSQLConf,
                      //val temporaryCatalogs: Seq[XDTemporaryCatalog]
                    ) extends SessionState(sparkSession){


  // TODO override val conf: SQLConf = _
  // TODO override val functionRegistry: FunctionRegistry = _
  // TODO override val catalog: SessionCatalog = _
  // TODO override val udf: UDFRegistration = _
  // TODO override val analyzer: Analyzer = _
  // TODO override val optimizer: Optimizer = _
  // TODO override val sqlParser: ParserInterface = _

  // TODO override def planner: SparkPlanner = super.planner
  // TODO is needed?? def executeSql(sql: String): QueryExecution = executePlan(sqlParser.parsePlan(sql))

  override def executePlan(plan: LogicalPlan): QueryExecution = {
    /*val catalogIdentifier: String = Try(xdConfig.getString(CatalogPrefixConfigKey)).recover {
      case _: ConfigException =>
        logger.warn("Catalog identifier not found. Using the default identifier may cause some problems")
        CoreConfig.DefaultCatalogIdentifier
    }.get*/
    val catalogIdentifier = "str"
    new XDQueryExecution(sparkSession, plan, catalogIdentifier)
  }

  // TODO override def refreshTable(tableName: String): Unit = super.refreshTable(tableName)
  // TODO override def analyze(tableName: String): Unit = super.analyze(tableName)
   /**
    * Logical query plan analyzer for resolving unresolved attributes and relations.
    */
  override lazy val analyzer: Analyzer = {
    new Analyzer(catalog, conf) {

      override lazy val batches: Seq[Batch] = Seq(
        Batch("Substitution", fixedPoint,
          CTESubstitution,
          WindowsSubstitution,
          EliminateUnions),
        Batch("Resolution", fixedPoint,
          ResolveTableValuedFunctions ::
            ResolveRelations ::
            ResolveReferences ::
            ResolveDeserializer ::
            ResolveNewInstance ::
            ResolveUpCast ::
            ResolveGroupingAnalytics ::
            ResolvePivot ::
            ResolveOrdinalInOrderByAndGroupBy ::
            ResolveMissingReferences ::
            ExtractGenerator ::
            ResolveGenerate ::
            ResolveFunctions ::
            ResolveAliases ::
            ResolveSubquery ::
            ResolveWindowOrder ::
            ResolveWindowFrame ::
            ResolveNaturalAndUsingJoin ::
            ExtractWindowExpressions ::
            GlobalAggregates ::
            ResolveAggregateFunctions ::
            TimeWindowing ::
            ResolveInlineTables ::
            TypeCoercion.typeCoercionRules ++
              extendedResolutionRules : _*),
        Batch("Nondeterministic", Once,
          PullOutNondeterministic),
        Batch("UDF", Once,
          HandleNullInputsForUDF),
        Batch("FixNullability", Once,
          FixNullability),
        Batch("Cleanup", fixedPoint,
          CleanupAliases))

      /* TODO old rules => override lazy val batches: Seq[Batch] = Seq(
        Batch("Substitution", fixedPoint,
          CTESubstitution,
          WindowsSubstitution),
        Batch("Preparation", fixedPoint, PrepareAggregateAlias :: Nil),
        Batch("Resolution", fixedPoint,
          WrapRelationWithGlobalIndex(catalog) ::
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
              extendedResolutionRules: _*),
        Batch("Nondeterministic", Once,
          PullOutNondeterministic,
          ComputeCurrentTime),
        Batch("UDF", Once,
          HandleNullInputsForUDF),
        Batch("Cleanup", fixedPoint,
          CleanupAliases)
      )*/

      override val extendedResolutionRules =
        AnalyzeCreateTableAsSelect(sparkSession) ::
          PreprocessTableInsertion(conf) ::
          new FindDataSourceTable(sparkSession) ::
          DataSourceAnalysis(conf) ::
          (if (conf.runSQLonFile) new ResolveDataSource(sparkSession) :: Nil else Nil)

      /* TODO old rules => override val extendedResolutionRules =
        ResolveAggregateAlias ::
          ResolveReferencesXD(conf) ::
          ExtractPythonUDFs ::
          ExtractNativeUDFs ::
          PreInsertCastAndRename ::
          Nil
        */

      override val extendedCheckRules = Seq(datasources.PreWriteCheck(conf, catalog))
    }

  }


  // TODO globalIndexOptimizer => override val optimizer: Optimizer = _

  // TODO ddlParser with parserCombinator => override val sqlParser: ParserInterface = _

  // TODO add DDL and ExtendedDatasourceStrategy => override def planner: SparkPlanner = super.planner

  /*
  @transient
  class XDPlanner extends sparkexecution.SparkPlanner(this) with XDStrategies {
    override def strategies: Seq[Strategy] = Seq(XDDDLStrategy, ExtendedDataSourceStrategy) ++ super.strategies
  }

  @transient
  override protected[sql] val planner: sparkexecution.SparkPlanner = new XDPlanner
  */

}
