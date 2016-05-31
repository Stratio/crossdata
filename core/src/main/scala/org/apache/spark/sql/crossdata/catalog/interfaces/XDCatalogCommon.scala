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
package org.apache.spark.sql.crossdata.catalog.interfaces

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

trait XDCatalogCommon extends SparkLoggerComponent {

  this: Catalog =>

  def relation(tableIdent: TableIdentifier, alias: Option[String] = None): Option[LogicalPlan]

  def contains(tableIdentifier: TableIdentifier): Boolean = relation(tableIdentifier).isDefined

  def allRelations(databaseName: Option[String]): Seq[(TableIdentifier, Boolean)]

  def isView(id: TableIdentifier): Boolean

  protected def normalizeDBIdentifier(dbName: String): String =
    if (conf.caseSensitiveAnalysis) dbName else dbName.toLowerCase

  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] =
    allRelations(databaseName) map {
      case (TableIdentifier(t, db), v) => (db.toSeq ++ Seq(t)).mkString(".") -> v
    }

  protected def notFound(resource: String) = {
    val message = s"$resource not found"
    logWarning(message)
    throw new RuntimeException(message)
  }
}
