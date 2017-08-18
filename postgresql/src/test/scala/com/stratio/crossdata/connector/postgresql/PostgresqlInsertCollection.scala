package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import scala.collection.immutable.ListMap

trait PostgresqlInsertCollection extends PostgresqlWithSharedContext {

  override val Table = "studentsinserttest"

  override val schema = ListMap("id" -> "int", "age" -> "int", "comment" -> "text",
    "enrolled" -> "boolean", "name" -> "text")

  override val testData = (for (a <- 1 to 10) yield {
    a ::
      (10 + a) ::
      s"Comment $a" ::
      (a % 2 == 0) ::
      s"Name $a" ::Nil
  }).toList

  abstract override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"CREATE TEMPORARY TABLE $postgresqlSchema.$Table")

  override def defaultOptions = super.defaultOptions + ("dbtable" -> s"$postgresqlSchema.$Table")

}