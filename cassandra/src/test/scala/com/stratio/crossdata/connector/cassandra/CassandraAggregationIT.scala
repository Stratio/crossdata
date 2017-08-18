/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.ExecutionType._
import org.apache.spark.sql.crossdata.exceptions.CrossdataException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraAggregationIT extends CassandraWithSharedContext {

  val nativeErrorMessage =  "The operation cannot be executed without Spark"
  // PRIMARY KEY id
  // CLUSTERING KEY age, comment
  // DEFAULT enrolled
  // SECONDARY_INDEX name

  "The Cassandra connector" should "not execute natively a (SELECT count(*) FROM _)" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT count(*) FROM $Table").collect(Native)
    } should have message nativeErrorMessage

  }

  it should "not execute natively a (SELECT count(*) AS alias FROM _)" in {
    assumeEnvironmentIsUpAndRunning
    the[CrossdataException] thrownBy {
      sql(s"SELECT count(*) as agg FROM $Table").collect(Native)
    } should have message nativeErrorMessage
  }


  it should "not execute natively a (SELECT count(*) FROM _ WHERE _)" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT count(*) FROM $Table WHERE id = 5").collect(Native)
    } should have message nativeErrorMessage

  }

  // NOT SUPPORTED NATIVELY
  it should "not execute natively a (SELECT count(*) FROM _ GROUP BY _)" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT count(*) FROM $Table GROUP BY id").collect(Native)
    } should have message nativeErrorMessage

  }

  // TODO review it in future C* versions (built-in functions will be added)
  it should "not execute natively a (SELECT max(id) FROM _ )" in {
    assumeEnvironmentIsUpAndRunning

    the[CrossdataException] thrownBy {
      sql(s"SELECT max(id) FROM $Table").collect(Native)
    } should have message nativeErrorMessage
  }

}
