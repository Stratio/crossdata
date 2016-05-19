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
package com.stratio.crossdata.connector.cassandra.statements

import com.stratio.crossdata.connector.cassandra.DefaultSource.CassandraDataSourcePrimaryKeyStringProperty
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

case class CreateTableStatement(tableName: String,
                                schema: StructType,
                                options: Map[String, String]
                               ) {

  override def toString(): String = {

    s"CREATE TABLE ${if(ifNotExists) "IF NOT EXISTS " else ""}$keyspace.$tableName (" + schema.fields.foldLeft("") {
      case (prev: String, next: StructField) =>
        val cassandraDataType = CassandraUtils.fromSparkSqlType(next.dataType)
        prev + s"${next.name} ${cassandraDataType.cqlTypeName}, "
    } + s"PRIMARY KEY ($primaryKeyString))"

  }


  lazy val ifNotExists: Boolean = {
    options.contains("ifNotExist")
  }

  lazy val keyspace: String = {
    options.get("keyspace").get
  }

  lazy val primaryKeyString:String = {
    require(options.contains(CassandraDataSourcePrimaryKeyStringProperty),
      s"$CassandraDataSourcePrimaryKeyStringProperty required when use CREATE EXTERNAL TABLE command")
    options.get(CassandraDataSourcePrimaryKeyStringProperty).get
  }



}
