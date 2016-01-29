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
package com.stratio.crossdata.connector.cassandra.statements

import com.stratio.crossdata.connector.cassandra.DefaultSource.CassandraDataSourceKeyspaceNameProperty
import com.stratio.crossdata.connector.cassandra.DefaultSource.CassandraDataSourcePrimaryKeySrtingProperty
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

case class CreateTableStatement(tableName: String,
                                schema: StructType,
                                options: Map[String, String]
                               ) {

  override def toString(): String = {
    val cqlCommand = StringBuilder.newBuilder
    cqlCommand.append("CREATE TABLE ")

    if (ifNotExists) {
      cqlCommand.append("IF NOT EXISTS ")
    }

    cqlCommand.append(keyspace).append(".").append(tableName)

    cqlCommand.append(" (")
    schema.fields.foreach { field: StructField =>

      val cassandraDataType = CassandraUtils.fromSparkSqlType(field.dataType)
      cqlCommand.append(field.name).append(" ").append(cassandraDataType.cqlTypeName).append(", ")
    }
    cqlCommand.append("PRIMARY KEY (") .append(primaryKeyString).append("))")

    cqlCommand.toString()
  }


  lazy val ifNotExists: Boolean = {
    options.contains("ifNotExist")
  }

  lazy val keyspace: String = {
    options.get("keyspace").get
  }

  lazy val primaryKeyString:String = {
    require(options.contains(CassandraDataSourcePrimaryKeySrtingProperty),
      s"$CassandraDataSourcePrimaryKeySrtingProperty required when use CREATE EXTERNAL TABLE command")
    options.get(CassandraDataSourcePrimaryKeySrtingProperty).get
  }



}
