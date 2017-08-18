/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalyst.planning

import org.apache.spark.sql.Strategy
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.TableIdentifierNormalized
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalyst.execution.{PersistDataSourceTable, PersistSelectAsTable, RegisterDataSourceTable}
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, CreateTableUsingAsSelect}
import org.apache.spark.sql.execution.{ExecutedCommand, SparkPlan, SparkStrategies}

trait XDStrategies extends SparkStrategies {
  self: XDContext#XDPlanner =>

  object XDDDLStrategy extends Strategy {
    def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case CreateTableUsing(tableIdent, userSpecifiedSchema, provider, temporary, opts, allowExisting, _) =>

        val crossdataTable = CrossdataTable(
          TableIdentifierNormalized(tableIdent.table, tableIdent.database),
          userSpecifiedSchema,
          provider,
          Array.empty[String],
          opts
        )

        val cmd = if(temporary)
          RegisterDataSourceTable(crossdataTable, allowExisting)
        else
          PersistDataSourceTable(crossdataTable, allowExisting)

        ExecutedCommand(cmd) :: Nil

      case CreateTableUsingAsSelect(tableIdent, provider, false, partitionCols, mode, opts, query) =>
        val cmd = PersistSelectAsTable(tableIdent, provider, partitionCols, mode, opts, query)
        ExecutedCommand(cmd) :: Nil

      case _ => Nil
    }
  }

}
