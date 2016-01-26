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
import org.apache.spark.sql.crossdata.models.EphemeralStatusModel
import org.apache.spark.sql.execution.datasources.DDLParser
import org.apache.spark.sql.types.StructType

class XDDdlParser(parseQuery: String => LogicalPlan) extends DDLParser(parseQuery) {

  protected val IMPORT = Keyword("IMPORT")
  protected val TABLES = Keyword("TABLES")
  protected val DROP = Keyword("DROP")
  protected val VIEW = Keyword("VIEW")
  protected val EPHEMERAL = Keyword("EPHEMERAL")
  protected val GET = Keyword("GET")
  protected val STATUS = Keyword("STATUS")
  protected val UPDATE = Keyword("UPDATE")


  override protected lazy val ddl: Parser[LogicalPlan] =
    createTable | describeTable | refreshTable | importStart | dropTable | createView | existsEphemeralTable |
      getEphemeralTable | getAllEphemeralTables | createEphemeralTable | updateEphemeralTable | dropEphemeralTable |
      getEphemeralStatus | getAllEphemeralStatuses | updateEphemeralStatus

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

    (CREATE ~> TEMPORARY.? <~ VIEW) ~ tableIdentifier ~ (AS ~> restInput) ^^ {
      case temp ~ viewIdentifier ~ query =>
        if (temp.isDefined)
          CreateTempView(viewIdentifier, parseQuery(query))
        else
          CreateView(viewIdentifier, parseQuery(query), query)
    }
  }

  /**
  * Ephemeral Table Functions
  */
  protected lazy val existsEphemeralTable: Parser[LogicalPlan] = {
    (EXISTS ~ EPHEMERAL ~ TABLE ~> tableIdentifier) ^^ {
      case tableIdent => ExistsEphemeralTable(tableIdent)
    }
  }

  protected lazy val getEphemeralTable: Parser[LogicalPlan] = {
    (GET ~ EPHEMERAL ~ TABLE ~> tableIdentifier) ^^ {
      case tableIdent => GetEphemeralTable(tableIdent)
    }
  }

  protected lazy val getAllEphemeralTables: Parser[LogicalPlan] = {
    (GET ~ EPHEMERAL ~ TABLES) ^^ {
      case operation => GetAllEphemeralTables()
    }
  }

  protected lazy val createEphemeralTable: Parser[LogicalPlan] = {
    (CREATE ~ EPHEMERAL ~ TABLE ~> tableIdentifier) ~ (OPTIONS ~> options) ^^ {
      case tableIdent ~ opts => {
        CreateEphemeralTable(tableIdent, opts)
      }
    }
  }

  protected lazy val updateEphemeralTable: Parser[LogicalPlan] = {
    (UPDATE ~ EPHEMERAL ~ TABLE ~> tableIdentifier) ~ (OPTIONS ~> options) ^^ {
      case tableIdent ~ opts => UpdateEphemeralTable(tableIdent, opts)
    }
  }

  protected lazy val dropEphemeralTable: Parser[LogicalPlan] = {
    (DROP ~ EPHEMERAL ~ TABLE ~> tableIdentifier)  ^^ {
      case tableIdent => DropEphemeralTable(tableIdent)
    }
  }

  protected lazy val dropAllEphemeralTables: Parser[LogicalPlan] = {
    (DROP ~ EPHEMERAL ~ TABLES)  ^^ {
      case operation => DropAllEphemeralTables()
    }
  }

  /**
  * Ephemeral Status Functions
  */

  protected lazy val getEphemeralStatus: Parser[LogicalPlan] = {
    (GET ~ EPHEMERAL ~ STATUS ~> tableIdentifier)  ^^ {
      case tableIdent => GetEphemeralStatus(tableIdent)
    }
  }

  protected lazy val getAllEphemeralStatuses: Parser[LogicalPlan] = {
    (GET ~ EPHEMERAL ~ TABLES)  ^^ {
      case operation => GetAllEphemeralStatuses()
    }
  }

  protected lazy val updateEphemeralStatus: Parser[LogicalPlan] = {
    (UPDATE ~ EPHEMERAL ~ TABLE ~> tableIdentifier) ~ (OPTIONS ~> options) ^^ {
      case tableIdent ~ opts => UpdateEphemeralStatus(tableIdent, opts)
    }
  }


  /**
  * Ephemeral Queries Functions
  */

}
