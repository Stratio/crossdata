package org.apache.spark.sql.crossdata.sources

import com.stratio.crossdata.sql.sources.MetadataOperations
import org.apache.spark.sql.catalyst.expressions.Row
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.sources.ResolvedDataSource
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{AnalysisException, SQLContext}


private[crossdata] case class ImportCatalogUsingWithOptions(provider: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand {
  override def run(sqlContext: SQLContext): Seq[Row] = ??? //TODO: Implement the command action
}

private[crossdata] case class CreateTable(tableName: String, userSpecifiedSchema: Option[StructType], provider: String,
                                          temporary: Boolean, opts: Map[String, String], allowExisting: Boolean,
                                          managedIfNoPath: Boolean, plan:LogicalPlan)
  extends LogicalPlan with RunnableCommand {
  override def run(sqlContext: SQLContext): Seq[Row] = {

    val xdContext = sqlContext.asInstanceOf[XDContext]

    if (xdContext.catalog.tableExists(tableName :: Nil)) {
      if (allowExisting) {
        return Seq.empty[Row]
      } else {
        throw new AnalysisException(s"Table $tableName already exists.")
      }
    }

    //TODO Create table in specified data source
    val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()
    resolved.asInstanceOf[MetadataOperations].createTable(xdContext,tableName,userSpecifiedSchema,opts)

    //Register plan
    val table=Seq[String]{tableName}
    xdContext.catalog.registerTable(table,plan)
    //Persist in XD Catalog
    xdContext.catalog.persistTableXD(tableName, userSpecifiedSchema, provider, temporary, opts, allowExisting, managedIfNoPath)

    Seq.empty[Row]
  }
}

