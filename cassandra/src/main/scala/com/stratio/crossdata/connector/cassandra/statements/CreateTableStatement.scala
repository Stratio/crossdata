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
