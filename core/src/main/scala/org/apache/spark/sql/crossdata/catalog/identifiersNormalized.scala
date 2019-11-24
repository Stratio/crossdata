/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.catalog.XDCatalog.IndexIdentifier

case class TableIdentifierNormalized(table: String, database: Option[String]) {
  def this(table: String) = this(table, None)

  override def toString: String = quotedString

  def quotedString: String = database.map(db => s"`$db`.`$table`").getOrElse(s"`$table`")

  def unquotedString: String = database.map(db => s"$db.$table").getOrElse(table)

  def toTableIdentifier: TableIdentifier = TableIdentifier(table, database)
}

private[sql] object TableIdentifierNormalized {
  def apply(tableName: String): TableIdentifierNormalized = new TableIdentifierNormalized(tableName)
}

case class IndexIdentifierNormalized(indexType: String, indexName: String) {
  def quotedString: String = s"`$indexName`.`$indexType`"
  def unquotedString: String = s"$indexName.$indexType"
  override def toString: String = quotedString
  def toIndexIdentifier: IndexIdentifier = IndexIdentifier(indexType, indexName)
  def asTableIdentifierNormalized: TableIdentifierNormalized = TableIdentifierNormalized(indexType,Option(indexName))
}

case class StringNormalized(normalizedString: String){
  override def toString = normalizedString
}
