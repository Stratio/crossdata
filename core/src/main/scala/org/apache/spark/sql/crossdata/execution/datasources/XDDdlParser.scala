/**
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
package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.datasources.DDLParser

class XDDdlParser(parseQuery: String => LogicalPlan) extends DDLParser(parseQuery) {

  protected val IMPORT = Keyword("IMPORT")
  protected val TABLES = Keyword("TABLES")
  protected val DROP = Keyword("DROP")
  protected val VIEW = Keyword("VIEW")

  override protected lazy val ddl: Parser[LogicalPlan] =
    createTable | describeTable | refreshTable | importStart | dropTable | createView

  protected lazy val importStart: Parser[LogicalPlan] =
    IMPORT ~> TABLES ~> (USING ~> className) ~ (OPTIONS ~> options).? ^^ {
      case provider ~ ops =>
        ImportTablesUsingWithOptions(provider.asInstanceOf[String], ops.getOrElse(Map.empty))
    }

  protected lazy val dropTable: Parser[LogicalPlan] =
    DROP ~> TABLE ~> tableIdentifier ^^ {
      case tableId =>
        DropTable(tableId)
    }


  protected lazy val createView: Parser[LogicalPlan] = {
    // TODO: Support database.table.
    (CREATE ~> TEMPORARY.? <~ VIEW) ~ tableIdentifier ~ (AS ~> restInput) ^^ {
      case temp  ~ viewIdentifier ~ query =>
        if (temp.isDefined) {
          CreateTempView(viewIdentifier, parseQuery(query))
        } else {
          CreateView(viewIdentifier, query)
        }

    }
  }

}
