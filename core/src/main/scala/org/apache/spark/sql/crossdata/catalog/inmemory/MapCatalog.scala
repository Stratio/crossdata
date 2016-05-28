package org.apache.spark.sql.crossdata.catalog.inmemory

import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import org.apache.spark.sql.crossdata.catalog.XDCatalog.ViewIdentifier

import scala.collection.mutable.Map

trait MapCatalog extends XDCatalog {

  protected def newMap: Map[String, LogicalPlan]

  private val tables: Map[String, LogicalPlan] = newMap
  private val views: Map[String, LogicalPlan] = newMap

  implicit def tableIdent2string(tident: TableIdentifier): String = getTableName(tident)

  override def tableExists(tableIdent: TableIdentifier): Boolean =
    (tables contains tableIdent) || (views contains tableIdent)

  //Will throw [[NoSuchElementException]] if not present
  override def lookupRelation(tableIdent: TableIdentifier, alias: Option[String]): LogicalPlan =
    tables get(tableIdent) orElse(views get tableIdent) map (processAlias(tableIdent, _, alias)) get


  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    val dbName = databaseName.map(normalizeDBIdentifier)
    (tables ++ views).toSeq collect {
      case (k,_) if dbName.map(_ == k.split("\\.")(0)).getOrElse(true) =>
        k.split("\\.")(0) + "." + k.split("\\.")(1) -> true
    }
  }

  override def refreshTable(tableIdentifier: TableIdentifier): Unit = {
    throw new UnsupportedOperationException
  }

  override def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan): Unit = {
    views get tableIdent foreach (_ => unregisterView(tableIdent))
    tables put (getTableName(tableIdent), plan)
  }

  override def registerView(tableIdent: ViewIdentifier, plan: LogicalPlan) = {
    tables get tableIdent foreach (_ => unregisterTable(tableIdent))
    views put (getTableName(tableIdent), plan)
  }


  override def unregisterTable(tableIdent: TableIdentifier): Unit =
    tables remove getTableName(tableIdent)

  override def unregisterView(viewIdent: ViewIdentifier): Unit =
    views remove getTableName(viewIdent)

  override def unregisterAllTables(): Unit = {
    unregisterAllViews()
    tables clear
  }

  override def unregisterAllViews(): Unit = views clear

}
