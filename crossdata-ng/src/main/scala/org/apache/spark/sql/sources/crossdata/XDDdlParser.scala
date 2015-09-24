package org.apache.spark.sql.sources.crossdata

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.datasources.DDLParser

class XDDdlParser(parseQuery: String => LogicalPlan) extends DDLParser(parseQuery) {

  protected val IMPORT = Keyword("IMPORT")
  protected val CATALOG = Keyword("CATALOG")

  override protected lazy val ddl: Parser[LogicalPlan] =
    createTable | describeTable | refreshTable | importStart

  protected lazy val importStart: Parser[LogicalPlan] =
    (IMPORT ~> (CATALOG | (TABLE ~> tableIdentifier))) ~ (USING ~> className) ~  (OPTIONS ~> options).?  ^^ {
      case "catalog" ~ provider ~ ops =>
        ImportCatalogUsingWithOptions(provider.asInstanceOf[String], ops.getOrElse(Map.empty))
      case other => ???
    }

}
