/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.sql.sources

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType

/**
 * A BaseRelation that can execute the whole logical plan without running the query
 * on the Spark cluster. If a specific logical plan cannot be resolved by the datasource
 * a None should be returned and the process will be executed on Spark.
 */
@DeveloperApi
trait NativeScan extends PushDownable{
  def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]]
}

/**
 * Interface for asking whether the datasource is able to push down an isolated logical plan.
 */
@DeveloperApi
sealed trait PushDownable {
  /**
   * Checks the ability to execute a [[LogicalPlan]].
   *
   * @param logicalStep isolated plan
   * @param wholeLogicalPlan the whole DataFrame tree
   * @return whether the logical step within the entire logical plan is supported
   */
  def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean
}

/**
 * Interface including data source operations for listing and describing tables
 * at a data source.
 *
 */
@DeveloperApi
trait TableInventory {

  import TableInventory._

  /**
   *
   * @param item Table description case class instance
   * @param userOpts Options provided by the parsed sentence
   * @return A concrete (for a given connector) translation of the high level table description
   *         to a low-level option map.
   */
  def generateConnectorOpts(item: Table, userOpts: Map[String, String] = Map.empty): Map[String, String]

  /**
   * Overriding this function allows tables import filtering. e.g: Avoiding system tables.
   *
   * @param table Table description case class instance
   * @return `true` if the table shall be imported, `false` otherwise
   */
  def exclusionFilter(table: TableInventory.Table) = true

  /**
   *
   * @param context SQLContext at which the command will be executed.
   * @param options SQL Sentence user options
   * @return A list of tables descriptions extracted from the datasource using a connector.0
   */
  def listTables(context: SQLContext, options: Map[String, String]): Seq[Table]


  //TODO: Add operation for describing a concrete table.
  //def fetchTableDescription(name: String, options: Map[String, String]): Table
}

object TableInventory {
  //Table description
  case class Table(database: String, tableName: String, clusterName: String, schema: StructType)
}