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
import org.apache.spark.sql.{SparkSession, UDFRegistration}
import org.apache.spark.sql.catalyst.analysis.{Analyzer, FunctionRegistry}
import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.catalyst.optimizer.Optimizer
import org.apache.spark.sql.catalyst.parser.ParserInterface
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.execution.XDQueryExecution
import org.apache.spark.sql.execution.{QueryExecution, SparkPlanner}
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
}
