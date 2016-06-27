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
package org.apache.spark.sql.crossdata.catalog.streaming

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.interfaces.XDStreamingCatalog
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.daos.impl._
import org.apache.spark.sql.crossdata.execution.datasources.StreamingRelation
import org.apache.spark.sql.crossdata.models._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

class ZookeeperStreamingCatalog(val catalystConf: CatalystConf) extends XDStreamingCatalog {

  private[spark] val streamingConfig = XDContext.xdConfig.getConfig(CoreConfig.StreamingConfigKey)
  private[spark] val ephemeralTableDAO =
    new EphemeralTableTypesafeDAO(streamingConfig.getConfig(CoreConfig.CatalogConfigKey))
  private[spark] val ephemeralQueriesDAO =
    new EphemeralQueriesTypesafeDAO(streamingConfig.getConfig(CoreConfig.CatalogConfigKey))
  private[spark] val ephemeralTableStatusDAO =
    new EphemeralTableStatusTypesafeDAO(streamingConfig.getConfig(CoreConfig.CatalogConfigKey))


  override def relation(tableIdent: TableIdentifier)(implicit sqlContext: SQLContext): Option[LogicalPlan] = {
    val tableIdentifier: String = normalizeTableName(tableIdent)
    if (futurize(existsEphemeralTable(tableIdentifier)))
      Some(StreamingRelation(tableIdentifier))
    else
      None
  }

  // TODO
  override def isAvailable: Boolean = true

  // TODO It must not return the relations until the catalog can distinguish between real/ephemeral tables
  override def allRelations(databaseName: Option[String]): Seq[TableIdentifier] = Seq.empty

  private def futurize[P](operation: => P): P =
    Await.result(Future(operation), 5 seconds)

  /**
   * Ephemeral Table Functions
   */
  override def existsEphemeralTable(tableIdentifier: String): Boolean =
    futurize(ephemeralTableDAO.dao.exists(tableIdentifier))

  override def getEphemeralTable(tableIdentifier: String): Option[EphemeralTableModel] =
    futurize(ephemeralTableDAO.dao.get(tableIdentifier))

  override def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel] =
    if (!existsEphemeralTable(ephemeralTable.name)) {
      createEphemeralStatus(ephemeralTable.name, EphemeralStatusModel(ephemeralTable.name, EphemeralExecutionStatus.NotStarted))
      Right(ephemeralTableDAO.dao.upsert(ephemeralTable.name, ephemeralTable))
    }
    else Left("Ephemeral table exists")


  override def dropEphemeralTable(tableIdentifier: String): Unit = {
    val isRunning = ephemeralTableStatusDAO.dao.get(tableIdentifier).map { tableStatus =>
      tableStatus.status == EphemeralExecutionStatus.Started || tableStatus.status == EphemeralExecutionStatus.Starting
    } getOrElse notFound(tableIdentifier)

    if (isRunning) throw new RuntimeException("The ephemeral is running. The process should be stopped first using 'Stop <tableIdentifier>'")

    ephemeralTableDAO.dao.delete(tableIdentifier)
    ephemeralTableStatusDAO.dao.delete(tableIdentifier)

    ephemeralQueriesDAO.dao.getAll().filter(_.ephemeralTableName == tableIdentifier) foreach { query =>
      ephemeralQueriesDAO.dao.delete(query.alias)
    }
  }

  override def dropAllEphemeralTables(): Unit = {
    // TODO it should be improved after changing ephemeralTableDAO.dao.deleteAll
    Try {
      ephemeralTableDAO.dao.deleteAll
      ephemeralTableStatusDAO.dao.deleteAll
      ephemeralQueriesDAO.dao.deleteAll
    }
  }

  override def getAllEphemeralTables: Seq[EphemeralTableModel] =
    ephemeralTableDAO.dao.getAll()


  /**
   * Ephemeral Queries Functions
   */
  override def existsEphemeralQuery(queryAlias: String): Boolean =
    ephemeralQueriesDAO.dao.exists(queryAlias)

  override def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel] =
    if (!existsEphemeralQuery(ephemeralQuery.alias))
      Right(ephemeralQueriesDAO.dao.upsert(ephemeralQuery.alias, ephemeralQuery))
    else Left("Ephemeral query exists")

  override def getEphemeralQuery(queryAlias: String): Option[EphemeralQueryModel] =
    ephemeralQueriesDAO.dao.get(queryAlias)

  override def getAllEphemeralQueries: Seq[EphemeralQueryModel] =
    ephemeralQueriesDAO.dao.getAll()

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
