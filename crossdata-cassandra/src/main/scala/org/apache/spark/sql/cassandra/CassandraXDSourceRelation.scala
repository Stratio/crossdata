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

package org.apache.spark.sql.cassandra

import com.datastax.spark.connector.cql.{CassandraConnectorConf, CassandraConnector}
import com.datastax.spark.connector.rdd.ReadConf
import com.datastax.spark.connector.writer.WriteConf
import com.stratio.crossdata.sql.sources.NativeScan
import org.apache.spark.{SparkConf, Logging}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType

private[cassandra] class CassandraXDSourceRelation(
                                                      tableRef: TableRef,
                                                      userSpecifiedSchema: Option[StructType],
                                                      filterPushdown: Boolean,
                                                      tableSizeInBytes: Option[Long],
                                                      connector: CassandraConnector,
                                                      readConf: ReadConf,
                                                      writeConf: WriteConf,
                                                      override val sqlContext: SQLContext)
  extends CassandraSourceRelation(
    tableRef,
    userSpecifiedSchema,
    filterPushdown,
    tableSizeInBytes,
    connector,
    readConf,
    writeConf,
    sqlContext)
  with NativeScan with Logging{

  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = {
    logInfo(s"We should process this plan ${optimizedLogicalPlan.toString}")
    None
  }
}


object CassandraXDSourceRelation{

  val tableSizeInBytesProperty = "spark.cassandra.table.size.in.bytes"

  val Properties = Seq(
    tableSizeInBytesProperty
  )

  val defaultClusterName = "default"

  def apply(
             tableRef: TableRef,
             sqlContext: SQLContext,
             options: CassandraSourceOptions = CassandraSourceOptions(),
             schema : Option[StructType] = None) : CassandraSourceRelation = {

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
                        tableConf: Map[String, String]) : SparkConf = {
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

