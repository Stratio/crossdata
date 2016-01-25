package com.stratio.crossdata.connector.cassandra.statements

import org.apache.spark.sql.types.StructType

case class CreateTableStatement(tableName:String,
                                catalog:String,
                                schema: StructType,
                                options: Map[String, String]
                               ) {


  val ifNotExists:Boolean = {
    options.contains("ifNotExist")
  }

  override def toString():String = {
    val cqlCommand = StringBuilder.newBuilder
    cqlCommand.append("CREATE TABLE ")

    if (ifNotExists) {
      cqlCommand.append("IF NOT EXISTS ")
    }

    cqlCommand.append(tableName).append( " (")

    schema.foreach{ column =>
      cqlCommand.append(column.name).append(" ")
      cqlCommand.append(column.dataType.typeName)
    }

    cqlCommand.toString()
  }

}
