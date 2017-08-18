package org.apache.spark.sql.crossdata.catalog.temporary

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.{StringNormalized, TableIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier, ViewIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDTemporaryCatalog}

import scala.collection.mutable

abstract class MapCatalog(catalystConf: CatalystConf) extends XDTemporaryCatalog {

  // TODO map with catalog/tableIdentifier

  protected def newMap: mutable.Map[String, LogicalPlan]

  private val tables: mutable.Map[String, LogicalPlan] = newMap
  private val views: mutable.Map[String, LogicalPlan] = newMap

  implicit def tableIdent2string(tident: TableIdentifierNormalized): String = XDCatalogCommon.stringifyTableIdentifierNormalized(tident)

  override def relation(tableIdent: TableIdentifierNormalized)(implicit sqlContext: SQLContext): Option[LogicalPlan] =
    (tables get tableIdent) orElse (views get tableIdent)

  override def allRelations(databaseName: Option[StringNormalized]): Seq[TableIdentifierNormalized] = {
    (tables ++ views).toSeq collect {
      case (k, _) if databaseName.map(_.normalizedString == k.split("\\.")(0)).getOrElse(true) =>
        k.split("\\.") match {
          case Array(db, tb) => TableIdentifierNormalized(tb, Option(db))
          case Array(tb) => TableIdentifierNormalized(tb)
        }
    }
  }

  override def saveTable(
                          tableIdentifier: TableIdentifierNormalized,
                          plan: LogicalPlan,
                          crossdataTable: Option[CrossdataTable] = None): Unit = {

    views get tableIdentifier foreach (_ => dropView(tableIdentifier))
    tables put(tableIdentifier, plan)
  }

  override def saveView(
                         viewIdentifier: ViewIdentifierNormalized,
                         plan: LogicalPlan,
                         query: Option[String] = None): Unit = {
    tables get viewIdentifier foreach (_ => dropTable(viewIdentifier))
    views put(viewIdentifier, plan)
  }

  override def dropView(viewIdentifier: ViewIdentifierNormalized): Unit =
    views remove viewIdentifier

  override def dropAllViews(): Unit = views clear

  override def dropAllTables(): Unit = tables clear

  override def dropTable(tableIdentifier: TableIdentifierNormalized): Unit =
    tables remove tableIdentifier

}
