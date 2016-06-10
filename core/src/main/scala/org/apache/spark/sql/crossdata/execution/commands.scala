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
package org.apache.spark.sql.crossdata.execution

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.EliminateSubQueries
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.util.CreateRelationUtil.createLogicalRelation
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.{HadoopFsRelation, InsertableRelation}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{AnalysisException, DataFrame, Row, SQLContext, SaveMode}

private[crossdata] trait DoCatalogDataSourceTable extends RunnableCommand {

  protected val tableIdent: TableIdentifier
  protected val userSpecifiedSchema: Option[StructType]
  protected val provider: String
  protected val options: Map[String, String]
  protected val allowExisting: Boolean

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val crossdataContext = sqlContext.asInstanceOf[XDContext]

    if (crossdataContext.catalog.tableExists(tableIdent) && !allowExisting)
      throw new AnalysisException(s"Table ${tableIdent.unquotedString} already exists")
    else
      catalogDataSourceTable(
        crossdataContext,
        CrossdataTable(tableIdent.table, tableIdent.database, userSpecifiedSchema, provider, Array.empty[String], options)
      )

  }

  protected def catalogDataSourceTable(crossdataContext: XDContext, crossdataTable: CrossdataTable): Seq[Row]

}

private[crossdata] case class PersistDataSourceTable(
                                                      protected val tableIdent: TableIdentifier,
                                                      protected val userSpecifiedSchema: Option[StructType],
                                                      protected val provider: String,
                                                      protected val options: Map[String, String],
                                                      protected val allowExisting: Boolean
                                                    ) extends DoCatalogDataSourceTable {

  override protected def catalogDataSourceTable(
                                                 crossdataContext: XDContext,
                                                 crossdataTable: CrossdataTable): Seq[Row] = {
    crossdataContext.catalog.persistTable(crossdataTable, createLogicalRelation(crossdataContext, crossdataTable))
    Seq.empty[Row]
  }

}

private[crossdata] case class RegisterDataSourceTable(
                                                       protected val tableIdent: TableIdentifier,
                                                       protected val userSpecifiedSchema: Option[StructType],
                                                       protected val provider: String,
                                                       protected val options: Map[String, String],
                                                       protected val allowExisting: Boolean
                                                     ) extends DoCatalogDataSourceTable {

  override protected def catalogDataSourceTable(
                                                 crossdataContext: XDContext,
                                                 crossdataTable: CrossdataTable): Seq[Row] = {
    crossdataContext.catalog.registerTable(
      tableIdent,
      createLogicalRelation(crossdataContext, crossdataTable), Some(crossdataTable)
    )
    Seq.empty[Row]
  }

}

private[crossdata]
case class PersistSelectAsTable(
                                 tableIdent: TableIdentifier,
                                 provider: String,
                                 partitionColumns: Array[String],
                                 mode: SaveMode,
                                 options: Map[String, String],
                                 query: LogicalPlan) extends RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val crossdataContext = sqlContext.asInstanceOf[XDContext]

    // TODO REFACTOR HIVE CODE ***************
    var createMetastoreTable = false
    var existingSchema = None: Option[StructType]
    if (crossdataContext.catalog.tableExists(tableIdent)) {
      // Check if we need to throw an exception or just return.
      mode match {
        case SaveMode.ErrorIfExists =>
          throw new AnalysisException(s"Table ${tableIdent.unquotedString} already exists. " +
            s"If you are using saveAsTable, you can set SaveMode to SaveMode.Append to " +
            s"insert data into the table or set SaveMode to SaveMode.Overwrite to overwrite" +
            s"the existing data. " +
            s"Or, if you are using SQL CREATE TABLE, you need to drop ${tableIdent.unquotedString} first.")
        case SaveMode.Ignore =>
          // Since the table already exists and the save mode is Ignore, we will just return.
          Seq.empty[Row]
        case SaveMode.Append =>
          // Check if the specified data source match the data source of the existing table.
          val resolved = ResolvedDataSource(
            sqlContext, Some(query.schema.asNullable), partitionColumns, provider, options)
          val createdRelation = LogicalRelation(resolved.relation)
          EliminateSubQueries(sqlContext.catalog.lookupRelation(tableIdent)) match {
            case l@LogicalRelation(_: InsertableRelation | _: HadoopFsRelation, _) =>
              if (l.relation != createdRelation.relation) {
                val errorDescription =
                  s"Cannot append to table ${tableIdent.unquotedString} because the resolved relation does not " +
                    s"match the existing relation of ${tableIdent.unquotedString}. " +
                    s"You can use insertInto(${tableIdent.unquotedString}, false) to append this DataFrame to the " +
                    s"table ${tableIdent.unquotedString} and using its data source and options."
                val errorMessage =
                  s"""|$errorDescription
                      |== Relations ==
                      |${
                    sideBySide(
                      s"== Expected Relation ==" :: l.toString :: Nil,
                      s"== Actual Relation ==" :: createdRelation.toString :: Nil
                    ).mkString("\n")
                  }
                  """.stripMargin
                throw new AnalysisException(errorMessage)
              }
              existingSchema = Some(l.schema)
            case o =>
              throw new AnalysisException(s"Saving data in ${o.toString} is not supported.")
          }
        case SaveMode.Overwrite =>
          crossdataContext.catalog.dropTable(tableIdent)
          createMetastoreTable = true
      }
    } else {
      // The table does not exist. We need to create it in metastore.
      createMetastoreTable = true
    }

    val data = DataFrame(crossdataContext, query)
    val df = existingSchema match {
      // If we are inserting into an existing table, just use the existing schema.
      case Some(schema) => sqlContext.internalCreateDataFrame(data.queryExecution.toRdd, schema)
      case None => data
    }

    // **************** TODO end refactor

    if (createMetastoreTable) {
      val resolved = ResolvedDataSource(sqlContext, provider, partitionColumns, mode, options, df)
      val crossdataTable = CrossdataTable(tableIdent.table, tableIdent.database, Some(resolved.relation.schema), provider, Array.empty[String], options)
      crossdataContext.catalog.persistTable(crossdataTable, LogicalRelation(resolved.relation))
    }


    Seq.empty[Row]
  }

  private def sideBySide(left: Seq[String], right: Seq[String]): Seq[String] = {
    val maxLeftSize = left.map(_.size).max
    val leftPadded = left ++ Seq.fill(math.max(right.size - left.size, 0))("")
    val rightPadded = right ++ Seq.fill(math.max(left.size - right.size, 0))("")

    leftPadded.zip(rightPadded).map {
      case (l, r) => (if (l == r) " " else "!") + l + (" " * ((maxLeftSize - l.size) + 3)) + r
    }
  }
}


