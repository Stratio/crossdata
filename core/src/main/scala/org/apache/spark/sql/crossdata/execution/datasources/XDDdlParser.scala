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


import java.util.UUID


import scala.language.implicitConversions
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.execution.datasources.DDLParser
import org.apache.spark.sql.types._


class XDDdlParser(parseQuery: String => LogicalPlan, xDContext: XDContext) extends DDLParser(parseQuery){

  protected val IMPORT = Keyword("IMPORT")
  protected val TABLES = Keyword("TABLES")
  protected val DROP = Keyword("DROP")
  protected val VIEW = Keyword("VIEW")
  protected val EXTERNAL = Keyword("EXTERNAL")
  //Streaming keywords
  protected val EPHEMERAL = Keyword("EPHEMERAL")
  protected val GET = Keyword("GET")
  protected val STATUS = Keyword("STATUS")
  protected val STATUSES = Keyword("STATUSES")
  protected val UPDATE = Keyword("UPDATE")
  protected val QUERY = Keyword("QUERY")
  protected val QUERIES = Keyword("QUERIES")
  protected val ADD = Keyword("ADD")
  protected val WITH = Keyword("WITH")
  protected val WINDOW = Keyword("WINDOW")
  protected val SECS = Keyword("SECS")

  override protected lazy val ddl: Parser[LogicalPlan] =
    createTable | describeTable | refreshTable | importStart | dropTable |
      createView | createExternalTable | streamingSentences

  // TODO move to StreamingDdlParser
  protected lazy val streamingSentences: Parser[LogicalPlan] = existsEphemeralTable |
    getEphemeralTable | getAllEphemeralTables | createEphemeralTable | updateEphemeralTable | dropEphemeralTable |
    getAllEphemeralQueries | addEphemeralQuery  | dropEphemeralQuery | dropAllEphemeralQueries




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

  protected lazy val createExternalTable: Parser[LogicalPlan] = {

    CREATE ~> EXTERNAL ~> TABLE ~> tableIdentifier ~ tableCols ~ (USING ~> className) ~ (OPTIONS ~> options).? ^^ {
      case tableName ~ columns ~ provider ~ opts =>
        val userSpecifiedSchema = StructType(columns)
        val options = opts.getOrElse(Map.empty[String, String])

        CreateExternalTable(tableName, userSpecifiedSchema, provider, options)
    }

  }

  /**
   * Streaming
   */
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
    (CREATE ~ EPHEMERAL ~ TABLE ~> tableIdentifier) ~ tableCols ~ (OPTIONS ~> options) ^^ {
      case tableIdent ~ columns ~ opts => {
        val schema = StructType(columns)
        CreateEphemeralTable(tableIdent, schema, opts)
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
    (GET ~ EPHEMERAL ~ STATUSES)  ^^ {
      case operation => GetAllEphemeralStatuses()
    }
  }


  /**
  * Ephemeral Queries Functions
  */

  protected lazy val existsEphemeralQuery: Parser[LogicalPlan] = {
    (EXISTS ~ EPHEMERAL ~ QUERY~> tableIdentifier) ^^ {
      case tableIdent => ExistsEphemeralQuery(tableIdent)
    }
  }
  protected lazy val getEphemeralQuery: Parser[LogicalPlan] = {
    (GET ~ EPHEMERAL ~ QUERY ~> tableIdentifier) ^^ {
      case tableIdent => GetEphemeralQuery(tableIdent)
    }
  }
  protected lazy val getAllEphemeralQueries: Parser[LogicalPlan] = {
    (GET ~ EPHEMERAL ~ QUERIES) ^^ {
      case operation => GetAllEphemeralQueries()
    }
  }
  protected lazy val addEphemeralQuery: Parser[LogicalPlan] = {

    ADD.? ~ streamingSql ~ (WITH ~ WINDOW ~> numericLit <~ SECS ) ~ (AS ~> ident.?) ^^ {
      case addDefined ~ streamQl ~ litN ~ topIdent =>

        val ephTables: Seq[String] = xDContext.sql(streamQl).queryExecution.analyzed.collect{case StreamingRelation(ephTableName) => ephTableName}
        ephTables.distinct match {
          case Seq(eTableName) =>
            AddEphemeralQuery(eTableName,streamQl, topIdent.getOrElse(UUID.randomUUID().toString),new Integer(litN))
          case tableNames =>
            sys.error(s"Expected an epehemeral table within the query, but found ${tableNames.mkString(",")}")
        }
    }

  }

  // Returns the select statement without the streaming info
  protected lazy val streamingSql: Parser[String] = new Parser[String] {
    def apply(in: Input): ParseResult[String] = {
      val indexOfWithWindow = in.source.toString.indexOf("WITH WINDOW")
      if (indexOfWithWindow <= 0){
        restInput(in)
      } else {
        val streamSql = in.source.subSequence(in.offset, indexOfWithWindow).toString.trim

        def streamingInfoInput(inpt: Input): Input = {
          val startsWithWindow = inpt.source.subSequence(inpt.offset, inpt.source.length()).toString.trim.startsWith("WITH WINDOW")
          if (startsWithWindow) inpt else streamingInfoInput(inpt.rest)
        }
        Success(streamSql,streamingInfoInput(in))
      }
    }
  }

  protected lazy val dropEphemeralQuery: Parser[LogicalPlan] = {
    (DROP ~ EPHEMERAL ~ QUERY ~> tableIdentifier) ^^ {
      case queryIdentifier => DropEphemeralQuery(queryIdentifier)
    }
  }
  protected lazy val dropAllEphemeralQueries: Parser[LogicalPlan] = {
    (DROP ~ EPHEMERAL ~ QUERIES) ^^ {
      case operation => DropAllEphemeralQueries()
    }
  }

}
