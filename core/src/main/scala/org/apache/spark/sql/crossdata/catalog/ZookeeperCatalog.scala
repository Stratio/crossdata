/**
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

package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog._
import org.apache.spark.sql.crossdata.daos.TableDAOComponent
import org.apache.spark.sql.crossdata.models.TableModel
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.crossdata.daos.DAOConstants._

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.catalog.XDCatalog]] with persistence using Zookeeper.
 * Using the common Stratio components for access and manage Zookeeper connections with Apache Curator.
 * @param conf An implementation of the [[CatalystConf]].
 */
class ZookeeperCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xdContext: XDContext)
  extends XDCatalog(conf, xdContext) with TableDAOComponent {

  override val config = new TypesafeConfig(Option(xdContext.catalogConfig))

  override def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable] = {
    if (dao.count > 0) {
      val findTable = dao.getAll()
        .find(tableModel =>
          tableModel.name == tableName && tableModel.database == databaseName)

      findTable match {
        case Some(zkTable) =>
          Option(CrossdataTable(zkTable.name,
            zkTable.database,
            getUserSpecifiedSchema(zkTable.schema),
            zkTable.dataSource,
            zkTable.partitionColumns.toArray,
            zkTable.options,
            zkTable.version))
        case None =>
          logger.warn("Table not exists")
          None
      }
    } else {
      logger.warn("Tables path not exists")
      None
    }
  }

  override def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    if (dao.count > 0) {
      dao.getAll()
        .flatMap(tableModel => {
          databaseName.fold(Option((tableModel.getExtendedName, false))) { dbName =>
            tableModel.database.flatMap(dbNameModel => {
              if (dbName == dbNameModel) Option((tableModel.getExtendedName, false))
              else None
            })
          }
        })
    } else {
      logger.warn("Tables path not exists")
      Seq.empty[(String, Boolean)]
    }
  }

  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit = {
    val tableId = createId
    val tableIdentifier = TableIdentifier(crossdataTable.tableName, crossdataTable.dbName).toSeq

    dao.create(tableId,
      TableModel(tableId,
        crossdataTable.tableName,
        serializeSchema(crossdataTable.userSpecifiedSchema.getOrElse(new StructType())),
        crossdataTable.datasource,
        crossdataTable.dbName,
        crossdataTable.partitionColumn,
        crossdataTable.opts))
  }

  override def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit =
    dao.getAll().filter(tableModel => tableName == tableModel.name && databaseName == tableModel.database)
      .foreach(tableModel => dao.delete(tableModel.id))

  override def dropAllPersistedTables(): Unit = dao.getAll().foreach(tableModel => dao.delete(tableModel.id))

}