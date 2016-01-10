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

package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{TableIdentifier, SimpleCatalystConf, CatalystConf}
import org.apache.spark.sql.crossdata.XDCatalog._
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.apache.spark.sql.crossdata.{XDCatalog, XDContext}
import org.apache.spark.sql.types.StructType

object ZookeeperCatalog {

  val ZookeeperConfigKey = "zookeeper"
  val BaseZKPath = "/stratio/crossdata"
  val TablesPath = s"$BaseZKPath/tables"
  val EphemeralTablesPath = s"$BaseZKPath/ephemeraltables"
  val StreamingQueriesPath = s"$BaseZKPath/streamingqueries"
}


/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using Zookeeper.
 * Using the common Stratio components for access and manage Zookeeper connections with Apache Curator.
 * @param conf An implementation of the [[CatalystConf]].
 */
class ZookeeperCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xdContext: XDContext)
  extends XDCatalog(conf, xdContext) with Logging with CrossdataSerializer
 //with ZookeeperRepositoryComponent with SparkLoggerComponent with TypesafeConfigComponent
{

  import ZookeeperCatalog._

  import org.apache.spark.sql.crossdata._

  private val catalogConfig = xdContext.catalogConfig

  //Crear la config correcta para zookeeper

  //puedo usar logger, repository y config!!

  override def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable] = {

    Some(
      CrossdataTable(table, Some(database), getUserSpecifiedSchema(schemaJSON), datasource, getPartitionColumn(partitionColumn), getOptions(optsJSON), version)
    )
  }

  override def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
  }

  override def persistTableMetadata(crossdataTable: CrossdataTable, logicalRelation: Option[LogicalPlan]): Unit = {

    val tableSchema = serializeSchema(crossdataTable.userSpecifiedSchema.getOrElse(new StructType()))
    val tableOptions = serializeOptions(crossdataTable.opts)
    val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)



    val tableIdentifier = TableIdentifier(crossdataTable.tableName,crossdataTable.dbName).toSeq
    //Try to register the table.
    registerTable(tableIdentifier, logicalRelation.getOrElse(lookupRelation(tableIdentifier))
  }

  override def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit = {

  }

  override def dropAllPersistedTables(): Unit = {

  }

}