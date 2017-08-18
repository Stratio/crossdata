/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.{ExecutionType, XDDataFrame}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlFilterIT extends PostgresqlWithSharedContext {

  "The Postgresql connector" should s"support a (SELECT *) natively" in {
    assumeEnvironmentIsUpAndRunning
    val dataframe = sql(s"SELECT * FROM $postgresqlSchema.$Table ")
    val schema = dataframe.schema
    val result = dataframe.collect(ExecutionType.Native)

    schema.fieldNames should equal (Seq("id", "age", "comment", "enrolled", "name"))
    result should have length 10
    result(0) should have length 5
  }

  it should s"support a query with limit 0 natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table LIMIT 0").collect(ExecutionType.Native)
    result should have length 0
  }

  it should s"support a (SELECT column) natively" in {

    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT id FROM $postgresqlSchema.$Table ").collect(ExecutionType.Native)
    result should have length 10
    result(0) should have length 1
  }

  it should s"support a (SELECT * ... WHERE PK = _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 1").collect(ExecutionType.Native)
    result should have length 1
  }

  it should s"support a (SELECT * ... WHERE PK <> _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id <> 1").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ... WHERE PK > _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id > 1").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ... WHERE PK < _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id < 10").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ... WHERE PK >= _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id >= 1").collect(ExecutionType.Native)
    result should have length 10
  }

  it should s"support a (SELECT * ... WHERE PK <= _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id <= 10").collect(ExecutionType.Native)
    result should have length 10
  }

  it should s"support a (SELECT * ... WHERE ... IS NULL ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE enrolled IS NULL ").collect(ExecutionType.Native)
    result should have length 0
  }

  it should s"support a (SELECT * ... WHERE COLUMN IN (...) ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id IN (1,5,9)").collect(ExecutionType.Native)
    result should have length 3
  }

  it should s"support a (SELECT * ... WHERE COLUMN NOT IN (...) ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id NOT IN (1,5,9)").collect(ExecutionType.Native)
    result should have length 7
  }

  it should s"support a (SELECT * ...  WHERE ... LIKE '_%' ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE comment LIKE 'C%' ").collect(ExecutionType.Native)
    result should have length 10
  }

  it should s"support a (SELECT * ...  WHERE ... LIKE '%_' ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE comment LIKE '%3' ").collect(ExecutionType.Native)
    result should have length 1
  }

  it should s"support a (SELECT * ...  WHERE ... LIKE '%_%') natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE comment LIKE '%mm%' ").collect(ExecutionType.Native)
    result should have length 10
  }

  it should s"support a (SELECT * ...  WHERE  ... BETWEEN ...) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE age BETWEEN 11 AND 15").collect(ExecutionType.Native)
    result should have length 5
  }

  it should s"support a (SELECT * ...  WHERE ... = AND = ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 3 AND age = 13 AND comment = 'Comment 3' ").collect(ExecutionType.Native)
    result should have length 1
  }

  it should s"support a (SELECT * ...  WHERE ... = OR = ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 3 OR age = 14 OR comment = 'Comment 5' ").collect(ExecutionType.Native)
    result should have length 3
  }

  it should s"support a (SELECT * ...  WHERE ... NOT(conditions) ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE NOT(id = 3 AND age = 13 AND comment = 'Comment 3') ").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ...  WHERE ... (_ AND _) OR _) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE (id = 3 AND age = 13) OR comment = 'Comment 5' ").collect(ExecutionType.Native)
    result should have length 2
  }

  it should s"support a (SELECT * ...  WHERE ... (_ OR _) AND _) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE (id = 3 OR age = 13) AND comment = 'Comment 5' ").collect(ExecutionType.Native)
    result should have length 0
  }

  it should s"support a (SELECT * ...  WHERE ... (_ OR _) AND (_ OR _) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE (id = 3 OR age = 13) AND (comment = 'Comment 4' OR comment = 'Comment 5') OR comment = 'Comment 6' ").collect(ExecutionType.Native)
    result should have length 1
  }

}
