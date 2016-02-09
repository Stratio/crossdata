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

import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.daos.impl._
import org.apache.spark.sql.crossdata.models._

class ZookeeperStreamingCatalog(xdContext: XDContext) extends XDStreamingCatalog(xdContext) {

  // TODO add several requires
  require(xdContext.xdConfig.hasPath(XDContext.StreamingConfigKey))
  private val streamingConfig = xdContext.xdConfig.getConfig(XDContext.StreamingConfigKey)

  val ephemeralTableDAO = new EphemeralTableTypesafeDAO(streamingConfig.getConfig(XDContext.CatalogConfigKey))
  val ephemeralQueriesDAO = new EphemeralQueriesTypesafeDAO(streamingConfig.getConfig(XDContext.CatalogConfigKey))
  val ephemeralTableStatusDAO = new EphemeralTableStatusTypesafeDAO(streamingConfig.getConfig(XDContext.CatalogConfigKey))

  /**
   * Ephemeral Table Functions
   */
  override def existsEphemeralTable(tableIdentifier: String): Boolean =
    ephemeralTableDAO.dao.exists(tableIdentifier)

  override def getEphemeralTable(tableIdentifier: String): Option[EphemeralTableModel] =
    ephemeralTableDAO.dao.get(tableIdentifier)

  override def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel] =
    if(!existsEphemeralTable(ephemeralTable.name)){
      createEphemeralStatus(ephemeralTable.name, EphemeralStatusModel(ephemeralTable.name, EphemeralExecutionStatus.NotStarted))
      Right(ephemeralTableDAO.dao.upsert(ephemeralTable.name, ephemeralTable))
      }
    else Left("Ephemeral table exists")

  override def updateEphemeralTable(ephemeralTable: EphemeralTableModel): Unit =
    ephemeralTableDAO.dao.update(ephemeralTable.name, ephemeralTable)

  override def dropEphemeralTable(tableIdentifier: String): Unit = {
    ephemeralTableDAO.dao.delete(tableIdentifier)
    ephemeralTableStatusDAO.dao.delete(tableIdentifier)
  }

  override def dropAllEphemeralTables(): Unit = {
    ephemeralTableDAO.dao.deleteAll
    ephemeralTableStatusDAO.dao.deleteAll
  }

  override def getAllEphemeralTables: Seq[EphemeralTableModel] =
    ephemeralTableDAO.dao.getAll()


  /**
   * Ephemeral Queries Functions
   */
  override def existsEphemeralQuery(queryAlias: String): Boolean =
    ephemeralQueriesDAO.dao.exists(queryAlias)

  override def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel] =
    if(!existsEphemeralQuery(ephemeralQuery.alias))
      Right(ephemeralQueriesDAO.dao.upsert(ephemeralQuery.alias, ephemeralQuery))
    else Left("Ephemeral query exists")

  override def getEphemeralQuery(queryAlias: String): Option[EphemeralQueryModel] =
    ephemeralQueriesDAO.dao.get(queryAlias)

  override def getAllEphemeralQueries: Seq[EphemeralQueryModel] =
    ephemeralQueriesDAO.dao.getAll()

  override def updateEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Unit =
    ephemeralQueriesDAO.dao.update(ephemeralQuery.alias, ephemeralQuery)

  override def dropEphemeralQuery(queryAlias: String): Unit =
    ephemeralQueriesDAO.dao.delete(queryAlias)

  override def dropAllEphemeralQueries(): Unit = ephemeralQueriesDAO.dao.deleteAll

  /**
   * Ephemeral Status Functions
   */
  override def createEphemeralStatus(tableIdentifier: String,
                                     ephemeralStatusModel: EphemeralStatusModel): EphemeralStatusModel =
    ephemeralTableStatusDAO.dao.upsert(tableIdentifier, ephemeralStatusModel)

  override def getEphemeralStatus(tableIdentifier: String): Option[EphemeralStatusModel] =
    ephemeralTableStatusDAO.dao.get(tableIdentifier)

  override def getAllEphemeralStatuses: Seq[EphemeralStatusModel] =
    ephemeralTableStatusDAO.dao.getAll()

  override def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel): Unit =
    ephemeralTableStatusDAO.dao.update(tableIdentifier, status)

  override protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit =
    ephemeralTableStatusDAO.dao.delete(tableIdentifier)

  override protected[crossdata] def dropAllEphemeralStatus(): Unit =
    ephemeralTableStatusDAO.dao.deleteAll

}
