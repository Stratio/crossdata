/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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