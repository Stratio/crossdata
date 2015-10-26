/*
 * Copyright 2014-2015, DataStax, Inc.
 * Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
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

package org.apache.spark.sql.cassandra

import java.io.IOException

import com.datastax.driver.core.Metadata
import com.datastax.spark.connector.cql.{CassandraConnector, CassandraConnectorConf, Schema}
import com.datastax.spark.connector.rdd.{CassandraRDD, ReadConf}
import com.datastax.spark.connector.util.NameTools
import com.datastax.spark.connector.util.Quote._
import com.datastax.spark.connector.writer.{SqlRowWriter, WriteConf}
import com.datastax.spark.connector._
import com.stratio.crossdata.connector.{NativeFunctionExecutor, NativeScan}
import com.stratio.crossdata.connector.cassandra.CassandraQueryProcessor
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.cassandra.DataTypeConverter._
import org.apache.spark.sql.catalyst.expressions.{Literal, AttributeReference, Attribute}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.crossdata.execution.{NativeUDFAttribute, NativeUDF, EvaluateNativeUDF}
import org.apache.spark.sql.sources.Filter
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.{DataTypes, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext, sources}
import org.apache.spark.{Logging, SparkConf}


/**
 * Implements [[org.apache.spark.sql.sources.BaseRelation]]]], [[org.apache.spark.sql.sources.InsertableRelation]]]]
 * and [[org.apache.spark.sql.sources.PrunedFilteredScan]]]]
 * It inserts data to and scans Cassandra table. If filterPushdown is true, it pushs down
 * some filters to CQL
 */
class CassandraXDSourceRelation(
                                 tableRef: TableRef,
                                 userSpecifiedSchema: Option[StructType],
                                 filterPushdown: Boolean,
                                 tableSizeInBytes: Option[Long],
                                 val connector: CassandraConnector,
                                 readConf: ReadConf,
                                 writeConf: WriteConf,
                                 @transient override val sqlContext: SQLContext)
  extends BaseRelation
  with InsertableRelation
  with PrunedFilteredScan
  with NativeFunctionExecutor
  with NativeScan with Logging {

  // NativeScan implementation ~~
  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = {
    logDebug(s"Processing ${optimizedLogicalPlan.toString()}")
    val queryExecutor = CassandraQueryProcessor(this, optimizedLogicalPlan)
    queryExecutor.execute()

  }

  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = logicalStep match {
    case ln: LeafNode => true // TODO leafNode == LogicalRelation(xdSourceRelation)
    case un: UnaryNode => un match {
      case Limit(_, _) | Project(_, _) | Filter(_, _) | EvaluateNativeUDF(_, _, _) => true
      case _ => false

    }
    case unsupportedLogicalPlan => log.debug(s"LogicalPlan $unsupportedLogicalPlan cannot be executed natively"); false
  }

  // ~~ NativeScan implementation 

  lazy val tableDef = {
    val tableName = tableRef.table
    val keyspaceName = tableRef.keyspace
    Schema.fromCassandra(connector, Some(keyspaceName), Some(tableName)).tables.headOption match {
      case Some(t) => t
      case None =>
        val metadata: Metadata = connector.withClusterDo(_.getMetadata)
        val suggestions = NameTools.getSuggestions(metadata, keyspaceName, tableName)
        val errorMessage = NameTools.getErrorString(keyspaceName, tableName, suggestions)
        throw new IOException(errorMessage)
    }
  }

  override def schema: StructType = {
    userSpecifiedSchema.getOrElse(StructType(tableDef.columns.map(toStructField)))
  }

  override def insert(data: DataFrame, overwrite: Boolean): Unit = {
    if (overwrite) {
      connector.withSessionDo {
        val keyspace = quote(tableRef.keyspace)
        val table = quote(tableRef.table)
        session => session.execute(s"TRUNCATE $keyspace.$table")
      }
    }

    implicit val rwf = SqlRowWriter.Factory
    val columns = SomeColumns(data.columns.map(x => x: ColumnRef): _*)
    data.rdd.saveToCassandra(tableRef.keyspace, tableRef.table, columns, writeConf)
  }

  override def sizeInBytes: Long = {
    //TODO  Retrieve table size from C* system table from Cassandra 2.1.4
    // If it's not found, use SQLConf default setting
    tableSizeInBytes.getOrElse(sqlContext.conf.defaultSizeInBytes)
  }

  implicit val cassandraConnector = connector
  implicit val readconf = readConf
  private[this] lazy val baseRdd =
    sqlContext.sparkContext.cassandraTable[CassandraSQLRow](tableRef.keyspace, tableRef.table)

  def buildScan(): RDD[Row] = baseRdd.asInstanceOf[RDD[Row]]

  override def buildScan(requiredColumns: Array[String], filters: Array[Filter]): RDD[Row] = {
    buildScan(requiredColumns, filters, Map.empty)
  }

  private def resolveUDFsReferences(strId: String, udfs: Map[String, NativeUDF]): Option[FunctionCallRef] =
    udfs get(strId) map { udf =>
      val actualParams = udf.children.collect {
        case at: NativeUDFAttribute => Left(resolveUDFsReferences(at.toString, udfs).get)
        case at: AttributeReference => Left(ColumnName(at.name))
        case lit@Literal(_, DataTypes.StringType) => Right(s"$lit")
        case lit: Literal => Right(lit.toString)
      }
      FunctionCallRef(udf.name, actualParams)
    }


  override def buildScan(requiredColumns: Array[String],
                         filters: Array[Filter],
                         udfs: Map[String, NativeUDF]): RDD[Row] = {


    val prunedRdd = maybeSelect(baseRdd, requiredColumns, udfs)
    logInfo(s"filters: ${filters.mkString(", ")}")
    val prunedFilteredRdd = {
      if (filterPushdown) {
        val filterPushdown = new PredicatePushDown(filters.toSet, tableDef)
        val pushdownFilters = filterPushdown.predicatesToPushDown.toSeq
        logInfo(s"pushdown filters: ${pushdownFilters.toString()}")
        val filteredRdd = maybePushdownFilters(prunedRdd, pushdownFilters)
        filteredRdd.asInstanceOf[RDD[Row]]
      } else {
        prunedRdd
      }
    }
    prunedFilteredRdd.asInstanceOf[RDD[Row]]
  }

  /** Define a type for CassandraRDD[CassandraSQLRow]. It's used by following methods */
  private type RDDType = CassandraRDD[CassandraSQLRow]

  /** Transfer selection to limit to columns specified */
  private def maybeSelect(
                           rdd: RDDType,
                           requiredColumns: Array[String],
                           udfs: Map[String, NativeUDF] = Map.empty): RDDType = {
    if (requiredColumns.nonEmpty) {
      val cols = requiredColumns.map(column => resolveUDFsReferences(column, udfs).getOrElse(column: ColumnRef))
      rdd.select(cols: _*)
    } else {
      rdd
    }
  }

  /** Push down filters to CQL query */
  private def maybePushdownFilters(rdd: RDDType, filters: Seq[Filter]): RDDType = {
    whereClause(filters) match {
      case (cql, values) if values.nonEmpty => rdd.where(cql, values: _*)
      case _ => rdd
    }
  }

  /** Construct Cql clause and retrieve the values from filter */
  private def filterToCqlAndValue(filter: Any): (String, Seq[Any]) = {
    filter match {
      case sources.EqualTo(attribute, value) => (s"${quote(attribute)} = ?", Seq(value))
      case sources.In(attribute, values) =>
        (quote(attribute) + " IN " + values.map(_ => "?").mkString("(", ", ", ")"), values.toSeq)
      case sources.LessThan(attribute, value) => (s"${quote(attribute)} < ?", Seq(value))
      case sources.LessThanOrEqual(attribute, value) => (s"${quote(attribute)} <= ?", Seq(value))
      case sources.GreaterThan(attribute, value) => (s"${quote(attribute)} > ?", Seq(value))
      case sources.GreaterThanOrEqual(attribute, value) => (s"${quote(attribute)} >= ?", Seq(value))

      case _ =>
        throw new UnsupportedOperationException(
          s"It's not a valid filter $filter to be pushed down, only >, <, >=, <= and In are allowed.")
    }
  }

  /** Construct where clause from pushdown filters */
  private def whereClause(pushdownFilters: Seq[Any]): (String, Seq[Any]) = {
    val cqlValue = pushdownFilters.map(filterToCqlAndValue)
    val cql = cqlValue.map(_._1).mkString(" AND ")
    val args = cqlValue.flatMap(_._2)
    (cql, args)
  }

}

//TODO buildScan => CassandraTableScanRDD[CassandraSQLRow] => fetchTokenRange


object CassandraXDSourceRelation {

  val tableSizeInBytesProperty = "spark.cassandra.table.size.in.bytes"

  val Properties = Seq(
    tableSizeInBytesProperty
  )

  val defaultClusterName = "default"

  def apply(
             tableRef: TableRef,
             sqlContext: SQLContext,
             options: CassandraSourceOptions = CassandraSourceOptions(),
             schema: Option[StructType] = None): CassandraXDSourceRelation = {

    val sparkConf = sqlContext.sparkContext.getConf
    val sqlConf = sqlContext.getAllConfs
    val conf =
      consolidateConfs(sparkConf, sqlConf, tableRef, options.cassandraConfs)
    val tableSizeInBytesString = conf.getOption(tableSizeInBytesProperty)
    val tableSizeInBytes = {
      if (tableSizeInBytesString.nonEmpty) {
        Option(tableSizeInBytesString.get.toLong)
      } else {
        None
      }
    }
    val cassandraConnector =
      new CassandraConnector(CassandraConnectorConf(conf))
    val readConf = ReadConf.fromSparkConf(conf)
    val writeConf = WriteConf.fromSparkConf(conf)

    new CassandraXDSourceRelation(
      tableRef = tableRef,
      userSpecifiedSchema = schema,
      filterPushdown = options.pushdown,
      tableSizeInBytes = tableSizeInBytes,
      connector = cassandraConnector,
      readConf = readConf,
      writeConf = writeConf,
      sqlContext = sqlContext)
  }

  /**
   * Consolidate Cassandra conf settings in the order of
   * table level -> keyspace level -> cluster level ->
   * default. Use the first available setting. Default
   * settings are stored in SparkConf.
   */
  def consolidateConfs(
                        sparkConf: SparkConf,
                        sqlConf: Map[String, String],
                        tableRef: TableRef,
                        tableConf: Map[String, String]): SparkConf = {
    // Default settings
    val conf = sparkConf.clone()
    // Keyspace/Cluster level settings
    for (prop <- DefaultSource.confProperties) {
      val cluster = tableRef.cluster.getOrElse(defaultClusterName)
      val clusterLevelValue = sqlConf.get(s"$cluster/$prop")
      if (clusterLevelValue.nonEmpty)
        conf.set(prop, clusterLevelValue.get)
      val keyspaceLevelValue =
        sqlConf.get(s"$cluster:${tableRef.keyspace}/$prop")
      if (keyspaceLevelValue.nonEmpty)
        conf.set(prop, keyspaceLevelValue.get)
      val tableLevelValue = tableConf.get(prop)
      if (tableLevelValue.nonEmpty)
        conf.set(prop, tableLevelValue.get)
    }
    conf
  }
}