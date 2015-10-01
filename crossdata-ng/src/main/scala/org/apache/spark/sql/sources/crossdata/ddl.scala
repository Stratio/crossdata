package org.apache.spark.sql.sources.crossdata

import com.stratio.crossdata.sql.sources.TableInventory
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.RelationProvider
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.RunnableCommand


private [crossdata] case class ImportTablesUsingWithOptions(provider: String, opts: Map[String, String])
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
      //TODO: WARNING: The change described in the comment below should be done ASAP!
      //TODO: Recover this `t.database.toList :+ t.tableName` when XDCatalog is ready;
      tableid = s"`${(t.database.toList :+ t.tableName) mkString "."}`"::Nil;
      if(inventoryRelation.exclusionFilter(t) && {
        val doExist = sqlContext.catalog.tableExists(tableid);
        if(doExist) log.info(s"IMPORT TABLE omitted already registered table: ${tableid mkString "."}")
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
