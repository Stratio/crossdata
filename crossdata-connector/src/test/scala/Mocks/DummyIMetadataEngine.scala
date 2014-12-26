/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package Mocks

import com.stratio.crossdata.common.connector.{IMetadataEngine}
import com.stratio.crossdata.common.data.{AlterOptions, CatalogName, ClusterName, TableName}
import com.stratio.crossdata.common.metadata.{CatalogMetadata, IndexMetadata, TableMetadata}
import com.stratio.crossdata.common.result.QueryResult
import org.apache.log4j.Logger
import com.stratio.crossdata.common.statements.structures.Selector

class DummyIMetadataEngine extends IMetadataEngine{

  lazy val logger = Logger.getLogger(classOf[DummyIMetadataEngine])
  override def createCatalog(targetCluster: ClusterName, catalogMetadata: CatalogMetadata): Unit = {}

  override def createTable(targetCluster: ClusterName, tableMetadata: TableMetadata): Unit = {
      logger.debug("very slow includes")
      for (i <- 1 to 5) {
        val a:Int=1000

        /**
         * time to wait the creation of table.
         */
        Thread.sleep(a)
        logger.debug(i + " seconds gone by ----")
      }
      logger.debug("very Slow process (end)")
      QueryResult.createSuccessQueryResult()
  }

  override def createIndex(targetCluster: ClusterName, indexMetadata: IndexMetadata): Unit = {}

  override def dropCatalog(targetCluster: ClusterName, name: CatalogName): Unit = {}

  override def dropTable(targetCluster: ClusterName, name: TableName): Unit = {}

  override def dropIndex(targetCluster: ClusterName, indexMetadata: IndexMetadata): Unit = {}

  override def alterCatalog(targetCluster: ClusterName, catalogName: CatalogName,
                            options: java.util.Map[Selector, Selector]): Unit = {}

  override def alterTable(targetCluster: ClusterName, name: TableName, alterOptions: AlterOptions): Unit = {}

  override def provideMetadata(clusterName: ClusterName): java.util.List[CatalogMetadata] = {
    return null
  }

  override def provideCatalogMetadata(clusterName: ClusterName, catalogName: CatalogName): CatalogMetadata = {
    return null
  }

  override def provideTableMetadata(clusterName: ClusterName, tableName: TableName): TableMetadata = {
    return null
  }

}
