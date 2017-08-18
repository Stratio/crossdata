/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable

import scala.collection.immutable.ListMap
import scala.collection.JavaConverters._

trait CassandraInsertCollection extends CassandraWithSharedContext {

  override val Table = "studentsinserttest"

  override val schema = ListMap("id" -> "int", "age" -> "int", "comment" -> "text",
                                "enrolled" -> "boolean", "name" -> "text", "array_test" ->"list<text>", "map_test" -> "map<text,text>",
                                "array_map" -> "list<frozen<map<text,text>>>", "map_array" -> "map<text,frozen<list<text>>>")

  override val pk = "(id)" :: "age" ::  Nil

  override val testData = (for (a <- 1 to 10) yield {
    a ::
      (10 + a) ::
      s"Comment $a" ::
      (a % 2 == 0) ::
      s"Name $a" ::
      List(a.toString, (a+1).toString, (a+2).toString) ::
      Map("x" -> (a + 1).toString, "y" -> (a + 2).toString) ::
      List(Map("x" -> (a + 1).toString) ,Map("y" -> (a + 2).toString)) ::
      Map("x" -> List((a + 1).toString, (a + 2).toString), "y" -> List((a + 2).toString)) ::Nil
  }).toList

  abstract override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"CREATE TEMPORARY TABLE $Table")

  override def defaultOptions = super.defaultOptions + ("table" -> Table)

}