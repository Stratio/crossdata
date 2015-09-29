package org.apache.spark.sql.sources.crossdata

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.sources.{DDLException, DDLParser}

class XDDdlParser(parseQuery: String => LogicalPlan) extends DDLParser(parseQuery) {

  protected val IMPORT = Keyword("IMPORT")
  protected val TABLES = Keyword("TABLES")

  override protected lazy val ddl: Parser[LogicalPlan] =
    createTable | describeTable | refreshTable | importStart

  protected lazy val importStart: Parser[LogicalPlan] =
    IMPORT ~> TABLES ~> (USING ~> className) ~ (OPTIONS ~> options).? ^^ {
      case provider ~ ops =>
        ImportTablesUsingWithOptions(provider.asInstanceOf[String], ops.getOrElse(Map.empty))
    }

  protected[sql] case class TableIdentifier(table: String, db: Option[String])

  //Based on Spark 1.5 DDLParser's "tableIdentifier parser"
  protected lazy val tableIdentifier: Parser[TableIdentifier] =
    (ident <~ ".").? ~ ident ^^ {
      case maybeDbName ~ tableName => TableIdentifier(tableName, maybeDbName)
    }

}
