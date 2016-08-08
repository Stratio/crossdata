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
package org.apache.spark.sql.crossdata.util

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import XDCatalog.CrossdataTable
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.{HadoopFsRelationProvider, RelationProvider, SchemaRelationProvider}

object CreateRelationUtil extends SparkLoggerComponent {

  protected[crossdata] def createLogicalRelation(
      sqlContext: SQLContext,
      crossdataTable: CrossdataTable): LogicalRelation = {

    /** Although table schema is inferred and persisted in XDCatalog, the schema can't be specified in some cases because
      *the source does not implement SchemaRelationProvider (e.g. JDBC) */
    val tableSchema =
      ResolvedDataSource.lookupDataSource(crossdataTable.datasource).newInstance() match {
        case _: SchemaRelationProvider | _: HadoopFsRelationProvider =>
          crossdataTable.schema
        case _: RelationProvider =>
          None
        case other =>
          val msg = s"Unexpected datasource: $other"
          logError(msg)
          throw new RuntimeException(msg)
      }

    val resolved = ResolvedDataSource(sqlContext,
                                      tableSchema,
                                      crossdataTable.partitionColumn,
                                      crossdataTable.datasource,
                                      crossdataTable.opts)
    LogicalRelation(resolved.relation)
  }

}
