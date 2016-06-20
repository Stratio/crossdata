package org.apache.spark.sql.crossdata.catalog.temporary

import com.hazelcast.core.IMap
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.util.CreateRelationUtil



class HazelcastCatalog(
                        private val tables: IMap[TableIdentifier, CrossdataTable], //TODO replace with map
                        private val views: IMap[TableIdentifier, String]
                      )(implicit val catalystConf: CatalystConf) extends XDTemporaryCatalog with Serializable{

  import XDCatalogCommon._


  override def relation(tableIdent: TableIdentifier, alias: Option[String])(implicit sqlContext: SQLContext): Option[LogicalPlan] = {
    val normalizedTableIdent = tableIdent.normalize(catalystConf);
    {
      Option(tables.get(normalizedTableIdent)) map (CreateRelationUtil.createLogicalRelation(sqlContext, _))
    } orElse {
      Option(views.get(normalizedTableIdent)) map (sqlContext.sql(_).logicalPlan)
    } map { logicalPlan =>
      processAlias(normalizedTableIdent, logicalPlan, alias)
    }
  }

  override def allRelations(databaseName: Option[String]): Seq[TableIdentifier] = {
    import scala.collection.JavaConversions._
    val normalizedDBName = databaseName.map(normalizeIdentifier)
    val tableIdentSeq = (tables ++ views).keys.toSeq
    normalizedDBName.map { dbName =>
      tableIdentSeq.filter {
        case TableIdentifier(_, Some(dIdent)) => dIdent == dbName
        case other => false
      }
    }.getOrElse(tableIdentSeq)
  }

  // TODO class NormalizedTableIdentifier => implicit conversion
  override def saveTable(tableIdentifier: TableIdentifier, plan: LogicalPlan, crossdataTable: Option[CrossdataTable]): Unit = {
    require(crossdataTable.isDefined, requireSerializablePlanMessage("CrossdataTable"))

    val normalizedTableIdentifier = tableIdentifier.normalize
    // TODO add create if not exists => fail if exists instead of override the table
    Option(views get normalizedTableIdentifier) foreach (_ => dropView(normalizedTableIdentifier))
    tables set(normalizedTableIdentifier, crossdataTable.get)
  }

  override def saveView(viewIdentifier: ViewIdentifier, plan: LogicalPlan, query: Option[String]): Unit = {
    require(query.isDefined, requireSerializablePlanMessage("query"))

    val normalizedViewIdentifier = viewIdentifier.normalize
    Option(tables get normalizedViewIdentifier) foreach (_ => dropTable(normalizedViewIdentifier))
    views set(normalizedViewIdentifier, query.get)
  }

  override def dropTable(tableIdentifier: TableIdentifier): Unit =
    tables remove tableIdentifier.normalize

  override def dropView(viewIdentifier: ViewIdentifier): Unit =
    views remove viewIdentifier.normalize

  override def dropAllViews(): Unit = views clear()

  override def dropAllTables(): Unit = tables clear()

  override def isAvailable: Boolean = true

  private def requireSerializablePlanMessage(parameter: String) =
    s"Parameter $parameter is required. A LogicalPlan cannot be stored in Hazelcast"

}