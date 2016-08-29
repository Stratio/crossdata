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

package org.apache.spark.sql.crossdata.catalog.persistent

import java.net.Socket

import com.typesafe.config.Config
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.{IndexIdentifierNormalized, StringNormalized, TableIdentifierNormalized, XDCatalog, persistent}
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.impl.{AppTypesafeDAO, IndexTypesafeDAO, TableTypesafeDAO, ViewTypesafeDAO}
import org.apache.spark.sql.crossdata.models.{AppModel, IndexModel, TableModel, ViewModel}

import scala.util.Try

/**
  * Default implementation of the [[persistent.PersistentCatalogWithCache]] with persistence using Zookeeper.
  * Using the common Stratio components for access and manage Zookeeper connections with Apache Curator.
  *
  * @param catalystConf An implementation of the [[CatalystConf]].
  */
class ZookeeperCatalog(override val catalystConf: CatalystConf)
  extends PersistentCatalogWithCache(catalystConf){

  import XDCatalog._

  protected[crossdata] lazy val config: Config = XDContext.catalogConfig
  @transient lazy val tableDAO = new TableTypesafeDAO(config)
  @transient lazy val viewDAO = new ViewTypesafeDAO(config)
  @transient lazy val appDAO = new AppTypesafeDAO(config)
  @transient lazy val indexDAO = new IndexTypesafeDAO(config)


  override def lookupTable(tableIdentifier: TableIdentifierNormalized): Option[CrossdataTable] = {
    if (tableDAO.dao.count > 0) {
      val findTable = tableDAO.dao.getAll()
        .find(tableModel =>
          tableModel.name == tableIdentifier.table && tableModel.database == tableIdentifier.database)

      findTable match {
        case Some(zkTable) =>
          Option(CrossdataTable(TableIdentifierNormalized(zkTable.name, zkTable.database),
            Option(deserializeUserSpecifiedSchema(zkTable.schema)),
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


  override def getApp(alias: String): Option[CrossdataApp] = {
    if (appDAO.dao.count > 0) {
      val findApp = appDAO.dao.getAll()
        .find(appModel =>
          appModel.appAlias == alias)

      findApp match {
        case Some(zkApp) =>
          Option(CrossdataApp(zkApp.jar,
            zkApp.appAlias,
            zkApp.appClass))
        case None =>
          appDAO.logger.warn("App doesn't exist")
          None
      }
    } else {
      appDAO.logger.warn("App path doesn't exist")
      None
    }
  }


  override def allRelations(databaseName: Option[StringNormalized]): Seq[TableIdentifierNormalized] = {
    if (tableDAO.dao.count > 0) {
      tableDAO.dao.getAll()
        .flatMap(tableModel => {
          databaseName.fold(Option(TableIdentifierNormalized(tableModel.name, tableModel.database))) { dbName =>
            tableModel.database.flatMap(dbNameModel => {
              if (dbName.normalizedString == dbNameModel) Option(TableIdentifierNormalized(tableModel.name, tableModel.database))
              else None
            })
          }
        })
    } else {
      tableDAO.logger.warn("Tables path doesn't exist")
      Seq.empty
    }
  }

  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit = {
    val tableId = createId

    tableDAO.dao.create(tableId,
      TableModel(tableId,
        crossdataTable.tableIdentifier.table,
        serializeSchema(crossdataTable.schema.getOrElse(schemaNotFound())),
        crossdataTable.datasource,
        crossdataTable.tableIdentifier.database,
        crossdataTable.partitionColumn,
        crossdataTable.opts))
  }


  override def saveAppMetadata(crossdataApp: CrossdataApp): Unit = {
    val appId = createId

    appDAO.dao.create(appId,
      AppModel(
        crossdataApp.jar,
        crossdataApp.appAlias,
        crossdataApp.appClass))
  }


  override def dropTableMetadata(tableIdentifier: ViewIdentifierNormalized): Unit =
    tableDAO.dao.getAll().filter {
      tableModel => tableIdentifier.table == tableModel.name && tableIdentifier.database == tableModel.database
    } foreach { tableModel =>
      tableDAO.dao.delete(tableModel.id)
    }


  override def dropAllTablesMetadata(): Unit = {
    tableDAO.dao.deleteAll
    viewDAO.dao.getAll.foreach(view => viewDAO.dao.delete(view.id))
  }

  override def lookupView(viewIdentifier: ViewIdentifierNormalized): Option[String] = {
    if (viewDAO.dao.count > 0) {
      val findView = viewDAO.dao.getAll()
        .find(viewModel => viewModel.name == viewIdentifier.table && viewModel.database == viewIdentifier.database)

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

  override def persistViewMetadata(tableIdentifier: TableIdentifierNormalized, sqlText: String): Unit = {
    val viewId = createId
    viewDAO.dao.create(viewId, ViewModel(viewId, tableIdentifier.table, tableIdentifier.database, sqlText))
  }


  override def dropViewMetadata(viewIdentifier: ViewIdentifierNormalized): Unit =
    viewDAO.dao.getAll().filter {
      view => view.name == viewIdentifier.table && view.database == viewIdentifier.database
    } foreach { selectedView =>
      viewDAO.dao.delete(selectedView.id)
    }


  override def dropAllViewsMetadata(): Unit = viewDAO.dao.deleteAll

  override def isAvailable: Boolean = {
    //TODO this method must be changed when Stratio Commons provide a status connection of Zookeeper
    val value = XDContext.catalogConfig.getString("zookeeper.connectionString")
    val address = value.split(":")
    Try(new Socket(address(0), address(1).toInt)).map { s =>
      s.close()
      true
    }.getOrElse(false)
  }

  //TODO
  override def persistIndexMetadata(crossdataIndex: CrossdataIndex): Unit = {
    val indexId = createId
    indexDAO.dao.create(indexId, IndexModel(indexId, crossdataIndex))
  }

  override def dropIndexMetadata(indexIdentifier: IndexIdentifierNormalized): Unit =
    indexDAO.dao.getAll().filter(
      index => index.crossdataIndex.indexIdentifier == indexIdentifier
    ) foreach (selectedIndex => indexDAO.dao.delete(selectedIndex.indexId))

  override def dropAllIndexesMetadata(): Unit =
    indexDAO.dao.deleteAll

  override def lookupIndex(indexIdentifier: IndexIdentifierNormalized): Option[CrossdataIndex] = {
    if (indexDAO.dao.count > 0) {
      val res = indexDAO.dao.getAll().find(
        _.crossdataIndex.indexIdentifier == indexIdentifier
      ) map (_.crossdataIndex)
      if (res.isEmpty) indexDAO.logger.warn("Index path doesn't exist")
      res

    } else {
      indexDAO.logger.warn("Index path doesn't exist")
      None
    }

  }

  override def dropIndexMetadata(tableIdentifier: TableIdentifierNormalized): Unit =
    indexDAO.dao.getAll().filter(
      index => index.crossdataIndex.tableIdentifier == tableIdentifier
    ) foreach (selectedIndex => indexDAO.dao.delete(selectedIndex.indexId))

  override def lookupIndexByTableIdentifier(tableIdentifier: TableIdentifierNormalized): Option[CrossdataIndex] = {
    if (indexDAO.dao.count > 0) {
      val res = indexDAO.dao.getAll().find(
        _.crossdataIndex.tableIdentifier == tableIdentifier
      ) map (_.crossdataIndex)
      if (res.isEmpty) indexDAO.logger.warn("Index path doesn't exist")
      res
    } else {
      indexDAO.logger.warn("Index path doesn't exist")
      None
    }
  }
}