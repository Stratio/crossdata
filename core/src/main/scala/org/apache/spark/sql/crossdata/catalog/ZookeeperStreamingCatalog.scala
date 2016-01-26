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
import org.apache.spark.sql.crossdata.daos.impl.{EphemeralTableStatusTypesafeDAO, EphemeralQueriesTypesafeDAO, EphemeralTableTypesafeDAO}
import org.apache.spark.sql.crossdata.models.{EphemeralQueryModel, EphemeralStatusModel, EphemeralTableModel}

class ZookeeperStreamingCatalog(xdContext: XDContext) extends XDStreamingCatalog(xdContext) {

  val ephemeralTableDAO = new EphemeralTableTypesafeDAO(xdContext.catalogConfig)
  val ephemeralQueriesDAO = new EphemeralQueriesTypesafeDAO(xdContext.catalogConfig)
  val ephemeralTableStatusDAO = new EphemeralTableStatusTypesafeDAO(xdContext.catalogConfig)
  /**
   * Ephemeral Table Functions
   */
  override def existsEphemeralTable(tableIdentifier: String): Boolean =
    ephemeralTableDAO.dao.exists(tableIdentifier)

  override def getEphemeralTable(tableIdentifier: String): Option[EphemeralTableModel] =
    ephemeralTableDAO.dao.get(tableIdentifier)

  override def createEphemeralTable(ephemeralTable: EphemeralTableModel): EphemeralTableModel =
    ephemeralTableDAO.dao.upsert(ephemeralTable.name, ephemeralTable)

  override def updateEphemeralTable(ephemeralTable: EphemeralTableModel): Unit =
    ephemeralTableDAO.dao.update(ephemeralTable.name, ephemeralTable)

  override def dropEphemeralTable(tableIdentifier: String): Unit =
    ephemeralTableDAO.dao.delete(tableIdentifier)

  override def dropAllEphemeralTables(): Unit =
    ephemeralTableDAO.dao.deleteAll

  override def getAllEphemeralTables(): Seq[EphemeralTableModel] =
    ephemeralTableDAO.dao.getAll()


  /**
   * Ephemeral Queries Functions
   */
  override def existsEphemeralQuery(queryAlias: String): Boolean =
    ephemeralQueriesDAO.dao.exists(queryAlias)

  override def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): EphemeralQueryModel =
    ephemeralQueriesDAO.dao.upsert(ephemeralQuery.alias, ephemeralQuery)

  override def getEphemeralQuery(queryAlias: String): Option[EphemeralQueryModel] =
    ephemeralQueriesDAO.dao.get(queryAlias)

  override def getAllEphemeralQueries(): Seq[EphemeralQueryModel] =
    ephemeralQueriesDAO.dao.getAll()

  override def updateEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Unit =
    ephemeralQueriesDAO.dao.update(ephemeralQuery.alias, ephemeralQuery)

  override def dropEphemeralQuery(queryAlias: String): Unit =
    ephemeralQueriesDAO.dao.delete(queryAlias)

  override def dropAllEphemeralQueries(): Unit =
    ephemeralQueriesDAO.dao.deleteAll


  /**
   * Ephemeral Status Functions
   */
  override def getEphemeralStatus(tableIdentifier: String): Option[EphemeralStatusModel] =
    ephemeralTableStatusDAO.dao.get(tableIdentifier)

  override def getAllEphemeralStatuses(): Seq[EphemeralStatusModel] =
    ephemeralTableStatusDAO.dao.getAll()

  override def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel): Unit =
    ephemeralTableStatusDAO.dao.update(tableIdentifier, status)

  override protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit =
    ephemeralTableStatusDAO.dao.delete(tableIdentifier)

  override protected[crossdata] def dropAllEphemeralStatus(): Unit =
    ephemeralTableStatusDAO.dao.deleteAll
  
}