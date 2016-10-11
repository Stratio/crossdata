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
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.StructType
import org.apache.spark.Partition

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

  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = ???

    /**
    * Checks the ability to execute a [[LogicalPlan]].
    *
    * @param logicalStep      isolated plan
    * @param wholeLogicalPlan the whole DataFrame tree
    * @return whether the logical step within the entire logical plan is supported
    */
  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = ???
}
