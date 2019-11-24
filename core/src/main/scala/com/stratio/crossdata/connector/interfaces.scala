/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalyst.NativeUDF
import org.apache.spark.sql.sources.{DataSourceRegister, Filter}
import org.apache.spark.sql.types.{DataType, StructType}

import scala.util.Try


/**
 * A BaseRelation that can execute the whole logical plan without running the query
 * on the Spark cluster. If a specific logical plan cannot be resolved by the datasource
 * a None should be returned and the process will be executed on Spark.
 */
@DeveloperApi
trait NativeScan extends PushDownable {
  def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]]
  def buildScan(optimizedLogicalPlan: LogicalPlan, sqlText: String): Option[Array[Row]] =
      buildScan(optimizedLogicalPlan)
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


sealed trait GenerateConnectorOptions {

  import TableInventory.Table

  /**
   *
   * @param item Table description case class instance
   * @param userOpts Options provided by the parsed sentence
   * @return A concrete (for a given connector) translation of the high level table description
   *         to a low-level option map.
   */
  def generateConnectorOpts(item: Table, userOpts: Map[String, String] = Map.empty): Map[String, String]
}
/**
 * Interface including data source operations for listing and describing tables
 * at a data source.
 *
 */
@DeveloperApi
trait TableInventory extends GenerateConnectorOptions{

  import TableInventory.Table

  /**
   * Overriding this function allows tables import filtering. e.g: Avoiding system tables.
   *
   * @param table Table description case class instance
   * @return `true` if the table shall be imported, `false` otherwise
   */
  def exclusionFilter(table: TableInventory.Table): Boolean = true

  /**
   *
   * @param context SQLContext at which the command will be executed.
   * @param options SQL Sentence user options
   * @return A list of tables descriptions extracted from the datasource using a connector.0
   */
  def listTables(context: SQLContext, options: Map[String, String]): Seq[Table]

}

object TableInventory {
  //Table description
  case class Table(tableName: String, database: Option[String] = None, schema: Option[StructType] = None)
}

/* Interface for providing lists and UDF discovery services */
trait FunctionInventory extends DataSourceRegister{
  import FunctionInventory.UDF

  //Get builtin functions manifest
  def nativeBuiltinFunctions: Seq[UDF]
}

object FunctionInventory {
  //Native function (either built-in or user defined) description.
  case class UDF(name: String, database: Option[String] = None, formalParameters: StructType, returnType: DataType)

  def qualifyUDF(datasourceName: String, udfName: String) = s"${datasourceName}_$udfName"
}

/**
  Interface for data sources which are able to execute functions (native or user defined) natively
 */
trait NativeFunctionExecutor {
  def buildScan(requiredColumns: Array[String], filters: Array[Filter], udfs: Map[String, NativeUDF]): RDD[Row]
}

/**
  * Interface including data source operations for Table manipulation like
  * CREATE/DROP EXTERNAL TABLE
  *
  */
trait TableManipulation extends GenerateConnectorOptions{


  def createExternalTable(context: SQLContext,
                          tableName: String,
                          databaseName: Option[String],
                          schema: StructType,
                          options: Map[String, String]): Option[TableInventory.Table]

  def dropExternalTable(context: SQLContext,
                        options: Map[String, String]): Try[Unit]
}