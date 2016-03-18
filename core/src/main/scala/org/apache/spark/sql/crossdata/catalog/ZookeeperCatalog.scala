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
import org.apache.spark.sql.crossdata.daos.impl.TableTypesafeDAO
import org.apache.spark.sql.crossdata.models.TableModel
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.impl.{TableTypesafeDAO, ViewTypesafeDAO}
import org.apache.spark.sql.crossdata.models.{TableModel, ViewModel}
import org.apache.spark.sql.types.StructType

/**
  * Default implementation of the [[org.apache.spark.sql.crossdata.catalog.XDCatalog]] with persistence using Zookeeper.
  * Using the common Stratio components for access and manage Zookeeper connections with Apache Curator.
  * @param conf An implementation of the [[CatalystConf]].
  */
class ZookeeperCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xdContext: XDContext)
  extends XDCatalog(conf, xdContext) {

  val tableDAO = new TableTypesafeDAO(XDContext.catalogConfig)
  val viewDAO = new ViewTypesafeDAO(XDContext.catalogConfig)


  override def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable] = {
    if (tableDAO.dao.count > 0) {
      val findTable = tableDAO.dao.getAll()
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
          tableDAO.logger.warn("Table doesn't exist")
          None
      }
    } else {
      tableDAO.logger.warn("Tables path doesn't exist")
      None
    }
  }

  override def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    if (tableDAO.dao.count > 0) {
      tableDAO.dao.getAll()
        .flatMap(tableModel => {
          databaseName.fold(Option((tableModel.getExtendedName, false))) { dbName =>
            tableModel.database.flatMap(dbNameModel => {
              if (dbName == dbNameModel) Option((tableModel.getExtendedName, false))
              else None
            })
          }
        })
    } else {
      tableDAO.logger.warn("Tables path doesn't exist")
      Seq.empty[(String, Boolean)]
    }
  }

  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit = {
    val tableId = createId

    tableDAO.dao.create(tableId,
      TableModel(tableId,
        crossdataTable.tableName,
        serializeSchema(crossdataTable.schema.getOrElse(new StructType())),
        crossdataTable.datasource,
        crossdataTable.dbName,
        crossdataTable.partitionColumn,
        crossdataTable.opts))
  }

  override def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit =
    tableDAO.dao.getAll().filter(tableModel => tableName == tableModel.name && databaseName == tableModel.database)
      .foreach(tableModel => tableDAO.dao.delete(tableModel.id))

  override def dropAllPersistedTables(): Unit = {
    tableDAO.dao.deleteAll
    viewDAO.dao.getAll.foreach(view=>viewDAO.dao.delete(view.id))
  }

  override protected def lookupView(tableName: String, databaseName: Option[String]): Option[String] = {
    if (viewDAO.dao.count > 0) {
      val findView = viewDAO.dao.getAll()
        .find(viewModel => viewModel.name == tableName && viewModel.database == databaseName)

      findView match {
        case Some(zkView) =>
          Some(zkView.sqlViewField)
        case None =>
          viewDAO.logger.warn("View doesn't exist")
          None
      }
    } else {
      viewDAO.logger.warn("View path doesn't exist")
      None
    }
  }

  override protected[crossdata] def persistViewMetadata(tableIdentifier: TableIdentifier, sqlText: String): Unit = {
    val viewId = createId
    viewDAO.dao.create(viewId, ViewModel(viewId, tableIdentifier.table, tableIdentifier.database, sqlText))
  }

  override protected def dropPersistedView(viewName: String, databaseName: Option[String]): Unit = {
    viewDAO.dao.getAll().filter(view => view.name == viewName && view.database == databaseName).foreach(selectedView => viewDAO.dao.delete(selectedView.id))

  }

  override protected def dropAllPersistedViews(): Unit = {
    viewDAO.dao.deleteAll
  }
  override def checkConnectivity:Boolean = true

}