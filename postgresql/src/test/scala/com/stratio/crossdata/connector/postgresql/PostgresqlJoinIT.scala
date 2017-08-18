package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.{ExecutionType, XDDataFrame}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlJoinIT extends PostgresqlWithSharedContext {

  "The Postgresql connector" should s"support a simple JOIN natively" in {
    assumeEnvironmentIsUpAndRunning
    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table JOIN $postgresqlSchema.$aggregationTable ON $postgresqlSchema.$Table.id = $postgresqlSchema.$aggregationTable.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  // SQLBuilder does not support table alias, so in this case direct query is executed.as second attempt.

  it should s"support a JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a INNER  JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A INNER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a FULL OUTER JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A FULL OUTER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 20
  }

  it should s"support a LEFT OUTER JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A LEFT OUTER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a RIGHT OUTER JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A RIGHT OUTER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 20
  }

  it should s"support a UNION natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT id FROM $postgresqlSchema.$Table UNION ALL SELECT id FROM $postgresqlSchema.$aggregationTable")
    val result = df.collect(ExecutionType.Native)

    result should have length 30
  }

  it should s"support a INTERSECT natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT id FROM $postgresqlSchema.$Table INTERSECT SELECT id FROM $postgresqlSchema.$aggregationTable")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a EXCEPT natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT id FROM $postgresqlSchema.$Table EXCEPT SELECT id FROM $postgresqlSchema.$aggregationTable")
    val result = df.collect(ExecutionType.Native)

    result should have length 0
  }

}
