package org.apache.spark.sql.sources.crossdata

import com.stratio.crossdata.sql.sources.TableInventory
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.expressions.Row
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.sources.{RelationProvider, LogicalRelation, ResolvedDataSource}


private [crossdata] case class ImportCatalogUsingWithOptions(provider: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    //Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()

    val inventoryRelation = resolved.asInstanceOf[TableInventory] //As inventory provider
    val providerRelation = resolved.asInstanceOf[RelationProvider] //As relation provider

    //Obtains the list of tables and persist it (if persistence implemented)
    val tables = inventoryRelation.listTables(sqlContext, opts)

    //Register the source tables in the catalog
    for(
      t: TableInventory.Table <- tables;
      tableid = t.database::t.tableName::Nil;
      doExist = sqlContext.catalog.tableExists(tableid);
      if(inventoryRelation.exclusionFilter(t) && {
        if(doExist) log.info(s"IMPORT CATALOG omitted already registered table: ${tableid mkString "."}")
        !doExist
      })
    ) sqlContext.
        catalog.registerTable(
          tableid,
          LogicalRelation(providerRelation.createRelation(
            sqlContext,
            inventoryRelation.generateConnectorOpts(t, opts))
          )
        )
    Seq.empty
  }
}
