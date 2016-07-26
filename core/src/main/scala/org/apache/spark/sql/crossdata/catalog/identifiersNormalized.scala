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
package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.catalog.XDCatalog.IndexIdentifier

case class TableIdentifierNormalized(table: String, database: Option[String]) {
  def this(table: String) = this(table, None)

  override def toString: String = quotedString

  def quotedString: String =
    database.map(db => s"`$db`.`$table`").getOrElse(s"`$table`")

  def unquotedString: String =
    database.map(db => s"$db.$table").getOrElse(table)

  def toTableIdentifier: TableIdentifier = TableIdentifier(table, database)
}

private[sql] object TableIdentifierNormalized {
  def apply(tableName: String): TableIdentifierNormalized =
    new TableIdentifierNormalized(tableName)
}

case class IndexIdentifierNormalized(indexType: String, indexName: String) {
  def quotedString: String = s"`$indexName`.`$indexType`"
  def unquotedString: String = s"$indexName.$indexType"
  override def toString: String = quotedString
  def toIndexIdentifier: IndexIdentifier =
    IndexIdentifier(indexType, indexName)
  def asTableIdentifierNormalized: TableIdentifierNormalized =
    TableIdentifierNormalized(indexType, Option(indexName))
}

case class StringNormalized(normalizedString: String) {
  override def toString = normalizedString
}
