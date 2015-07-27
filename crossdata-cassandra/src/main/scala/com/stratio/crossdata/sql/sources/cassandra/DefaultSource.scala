/*
 *
 *  * Copyright (C) 2015 Stratio (http://stratio.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package com.stratio.crossdata.sql.sources.cassandra

import com.datastax.spark.connector.cql.CassandraConnectorConf
import com.datastax.spark.connector.rdd.ReadConf
import com.datastax.spark.connector.writer.WriteConf
import com.stratio.crossdata.sql.sources.cassandra.DefaultSource._
import org.apache.spark.sql.SaveMode._
import org.apache.spark.sql.cassandra.{CassandraSourceOptions, CassandraSourceRelation, CassandraXDSourceRelation, TableRef, DefaultSource => CassandraConnectorDS}
import org.apache.spark.sql.sources.BaseRelation
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

import scala.collection.mutable

class DefaultSource extends CassandraConnectorDS{

  override def createRelation(
                               sqlContext: SQLContext,
                               parameters: Map[String, String]): BaseRelation = {

    val (tableRef, options) = TableRefAndOptions(parameters)
    CassandraXDSourceRelation(tableRef, sqlContext, options)
  }

  override def createRelation(
                               sqlContext: SQLContext,
                               parameters: Map[String, String],
                               schema: StructType): BaseRelation = {

    val (tableRef, options) = TableRefAndOptions(parameters)
    CassandraXDSourceRelation(tableRef, sqlContext, options, Option(schema))
  }

  override def createRelation(
                               sqlContext: SQLContext,
                               mode: SaveMode,
                               parameters: Map[String, String],
                               data: DataFrame): BaseRelation = {

    val (tableRef, options) = TableRefAndOptions(parameters)
    val table = CassandraXDSourceRelation(tableRef, sqlContext, options)

    mode match {
      case Append => table.insert(data, overwrite = false)
      case Overwrite => table.insert(data, overwrite = true)
      case ErrorIfExists =>
        if (table.buildScan().isEmpty()) {
          table.insert(data, overwrite = false)
        } else {
          throw new UnsupportedOperationException("'Writing to a non-empty Cassandra Table is not allowed.'")
        }
      case Ignore =>
        if (table.buildScan().isEmpty()) {
          table.insert(data, overwrite = false)
        }
    }

    CassandraXDSourceRelation(tableRef, sqlContext, options)
  }
}

object DefaultSource {
  val CassandraDataSourceTableNameProperty = "table"
  val CassandraDataSourceKeyspaceNameProperty = "keyspace"
  val CassandraDataSourceClusterNameProperty = "cluster"
  val CassandraDataSourceUserDefinedSchemaNameProperty = "schema"
  val CassandraDataSourcePushdownEnableProperty = "pushdown"
  val CassandraDataSourceProviderPackageName = DefaultSource.getClass.getPackage.getName
  val CassandraDataSourceProviderClassName = CassandraDataSourceProviderPackageName + ".DefaultSource"


  /** Parse parameters into CassandraDataSourceOptions and TableRef object */
  def TableRefAndOptions(parameters: Map[String, String]) : (TableRef, CassandraSourceOptions) = {
    val tableName = parameters(CassandraDataSourceTableNameProperty)
    val keyspaceName = parameters(CassandraDataSourceKeyspaceNameProperty)
    val clusterName = parameters.get(CassandraDataSourceClusterNameProperty)
    val pushdown : Boolean = parameters.getOrElse(CassandraDataSourcePushdownEnableProperty, "true").toBoolean
    val cassandraConfs = buildConfMap(parameters)

    (TableRef(tableName, keyspaceName, clusterName), CassandraSourceOptions(pushdown, cassandraConfs))
  }

  val confProperties = ReadConf.Properties ++
    WriteConf.Properties ++
    CassandraConnectorConf.Properties ++
    CassandraSourceRelation.Properties

  // Dot is not allowed in Options key for Spark SQL parsers, so convert . to _
  // Map converted property to origin property name
  // TODO check SPARK 1.4 it may be fixed
  private val propertiesMap : Map[String, String] = {
    confProperties.map(prop => (prop.replace(".", "_"), prop)).toMap
  }

  /** Construct a map stores Cassandra Conf settings from options */
  def buildConfMap(parameters: Map[String, String]) : Map[String, String] = {
    val confMap = mutable.Map.empty[String, String]
    for (convertedProp <- propertiesMap.keySet) {
      val setting = parameters.get(convertedProp)
      if (setting.nonEmpty) {
        confMap += propertiesMap(convertedProp) -> setting.get
      }
    }
    confMap.toMap
  }

  /** Check whether the provider is Cassandra datasource or not */
  def cassandraSource(provider: String) : Boolean = {
    provider == CassandraDataSourceProviderPackageName || provider == CassandraDataSourceProviderClassName
  }
}

