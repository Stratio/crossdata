package org.apache.spark.sql.sources.crossdata

import com.stratio.crossdata.sql.sources.TableInventory
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.expressions.Row
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.sources.{RelationProvider, LogicalRelation, ResolvedDataSource}


private [crossdata] case class ImportCatalogUsingWithOptions(provider: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {

  //TODO: Implement tables description persistence
  protected def persistenceStep(tables: Seq[TableInventory.Table]): Unit = ()

  override def run(sqlContext: SQLContext): Seq[Row] = {

    //Get a reference to the inventory relation.
    val resolved = ResolvedDataSource(sqlContext, None, Array.empty[String], provider, opts)
    val inventoryRelation = resolved.relation.asInstanceOf[TableInventory] //As inventory provider
    //TODO: Check error management. It may happen that the provided datasource doesn't support inventory
    val providerRelation = resolved.relation.asInstanceOf[RelationProvider] //As relation provider

    //Obtain the list of tables and persist it (if persistence implemented)
    //TODO: Check error management. It may happen that a cluster name has not been provided
    val tables = inventoryRelation.listTables(sqlContext, opts)
    persistenceStep(tables)

    //Register the source tables in the catalog
    tables foreach { t: TableInventory.Table =>
      sqlContext.
        catalog.registerTable(
          t.database::t.tableName::Nil,
          LogicalRelation(providerRelation.createRelation(
            sqlContext,
            inventoryRelation.inventoryItem2optionsMap(t))
          )
        )
    }
    Seq.empty
  }
}
