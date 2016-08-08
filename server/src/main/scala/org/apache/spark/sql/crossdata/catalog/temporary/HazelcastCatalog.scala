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
package org.apache.spark.sql.crossdata.catalog.temporary

import com.hazelcast.core.IMap
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.{StringNormalized, TableIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier, ViewIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.util.CreateRelationUtil

class HazelcastCatalog(
    private val tables: IMap[TableIdentifierNormalized, CrossdataTable],
    private val views: IMap[TableIdentifierNormalized, String]
)(implicit val catalystConf: CatalystConf)
    extends XDTemporaryCatalog
    with Serializable {

  override def relation(tableIdent: TableIdentifierNormalized)(
      implicit sqlContext: SQLContext): Option[LogicalPlan] = {
    Option(tables.get(tableIdent)) map (CreateRelationUtil.createLogicalRelation(sqlContext, _))
  } orElse {
    Option(views.get(tableIdent)) map (sqlContext.sql(_).logicalPlan)
  }

  override def allRelations(
      databaseName: Option[StringNormalized]): Seq[TableIdentifierNormalized] = {
    import scala.collection.JavaConversions._
    val tableIdentSeq = (tables ++ views).keys.toSeq
    databaseName.map { dbName =>
      tableIdentSeq.filter {
        case TableIdentifierNormalized(_, Some(dIdent)) =>
          dIdent == dbName.normalizedString
        case other => false
      }
    }.getOrElse(tableIdentSeq)
  }

  override def saveTable(tableIdentifier: TableIdentifierNormalized,
                         plan: LogicalPlan,
                         crossdataTable: Option[CrossdataTable]): Unit = {
    require(crossdataTable.isDefined, requireSerializablePlanMessage("CrossdataTable"))

    // TODO add create/drop if not exists => fail if exists instead of override the table
    Option(views get tableIdentifier) foreach (_ => dropView(tableIdentifier))
    tables set (tableIdentifier, crossdataTable.get)
  }

  override def saveView(viewIdentifier: ViewIdentifierNormalized,
                        plan: LogicalPlan,
                        query: Option[String]): Unit = {
    require(query.isDefined, requireSerializablePlanMessage("query"))

    Option(tables get viewIdentifier) foreach (_ => dropTable(viewIdentifier))
    views set (viewIdentifier, query.get)
  }

  override def dropTable(tableIdentifier: TableIdentifierNormalized): Unit =
    tables remove tableIdentifier

  override def dropView(viewIdentifier: ViewIdentifierNormalized): Unit =
    views remove viewIdentifier

  override def dropAllViews(): Unit = views clear ()

  override def dropAllTables(): Unit = tables clear ()

  override def isAvailable: Boolean = true

  private def requireSerializablePlanMessage(parameter: String) =
    s"Parameter $parameter is required. A LogicalPlan cannot be stored in Hazelcast"

}
