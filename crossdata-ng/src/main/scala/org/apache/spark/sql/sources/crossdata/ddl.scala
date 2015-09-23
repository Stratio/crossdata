package org.apache.spark.sql.sources.crossdata

import com.google.protobuf.WireFormat.FieldType
import com.stratio.crossdata.sql.sources.TableInventory
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.expressions.Row
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.sources.{RelationProvider, LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.types.{StructField, StructType}


private [crossdata] case class ImportCatalogUsingWithOptions(provider: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {

  //TODO: Implement tables description persistence
  protected def persistenceStep(tables: Seq[TableInventory.Table]): Unit = ()

  override def run(sqlContext: SQLContext): Seq[Row] = {
    //Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()
    /*ResolvedDataSource(sqlContext, Some(StructType(Array.empty[StructField])), Array.empty[String],
      provider,
      opts.updated("table","").updated("keyspace", ""))
    //opts.updated("table","").updated("keyspace", "")
    */
    val inventoryRelation = resolved.asInstanceOf[TableInventory] //As inventory provider
    //TODO: Check error management. It may happen that the provided datasource doesn't support inventory
    val providerRelation = resolved.asInstanceOf[RelationProvider] //As relation provider

    //Obtain the list of tables and persist it (if persistence implemented)
    //TODO: Check error management. It may happen that a cluster name has not been provided
    val tables = inventoryRelation.listTables(sqlContext, opts)
    persistenceStep(tables)

    //Register the source tables in the catalog
    for(
      t: TableInventory.Table <- tables;
      tableid = t.database::t.tableName::Nil;
      doExist = sqlContext.catalog.tableExists(tableid);
      if(inventoryRelation.exclusionFilter(t) && {
        if(doExist) log.info(s"IMPORT CATALOG omitted already registered table: ${tableid mkString "."}")
        !doExist
      })
    ) {
      sqlContext.
        catalog.registerTable(
          tableid,
          LogicalRelation(providerRelation.createRelation(
            sqlContext,
            inventoryRelation.generateConnectorOpts(t, opts))
          )
        )
    }
    Seq.empty
  }
}
