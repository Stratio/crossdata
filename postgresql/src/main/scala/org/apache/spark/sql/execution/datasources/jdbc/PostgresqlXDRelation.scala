/*
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

package org.apache.spark.sql.execution.datasources.jdbc

import java.util.Properties

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.NativeScan
import com.stratio.crossdata.connector.postgresql.PostgresqlQueryProcessor
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.StructType
import org.apache.spark.Partition
import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.BaseRelation

class PostgresqlXDRelation( url: String,
                            table: String,
                            parts: Array[Partition],
                            properties: Properties = new Properties(),
                            @transient override val sqlContext: SQLContext,
                            userSchema: Option[StructType] = None)
  extends JDBCRelation(url, table, parts)(sqlContext)
  with NativeScan
  with SparkLoggerComponent {

  override val schema: StructType = userSchema.getOrElse(JDBCRDD.resolveTable(url, table, properties))

  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = {
    logDebug(s"Processing ${optimizedLogicalPlan.toString()}")
    val queryExecutor = PostgresqlQueryProcessor(this, optimizedLogicalPlan, this.properties)

    val toCatalyst = CatalystTypeConverters.createToCatalystConverter(optimizedLogicalPlan.schema)
    val toScala = CatalystTypeConverters.createToScalaConverter(optimizedLogicalPlan.schema)

    queryExecutor.execute() map { rows =>
      rows map { row =>
        val iRow = toCatalyst(row)
        toScala(iRow).asInstanceOf[GenericRowWithSchema]
      }
    }

  }

    /**
    * Checks the ability to execute a [[LogicalPlan]].
    *
    * @param logicalStep      isolated plan
    * @param wholeLogicalPlan the whole DataFrame tree
    * @return whether the logical step within the entire logical plan is supported
    */
  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = logicalStep match {

    case ln: LeafNode => ln match {
      // It supports native query if all tables are in postgresql
      case LogicalRelation(br: BaseRelation, _) => br.isInstanceOf[PostgresqlXDRelation]
      case _ => true
    }

    case un: UnaryNode => un match {
      case Limit(_, _) | Project(_, _) | Filter(_, _) => true
      case _: Sort => true
      case _: Aggregate => true
      case _ => false
    }
    case bn: BinaryNode => bn match {
      case _: Join => true
      case _ => false
    }
    case unsupportedLogicalPlan =>logDebug(s"LogicalPlan $unsupportedLogicalPlan cannot be executed natively"); false
  }
}
