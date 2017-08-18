package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlConnectorIT extends PostgresqlWithSharedContext {

  Seq(Native, Spark) foreach { executionType =>

    "The Postgresql connector" should s"support a (SELECT *) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning
      val dataframe = sql(s"SELECT * FROM $postgresqlSchema.$Table ")
      val schema = dataframe.schema
      val result = dataframe.collect(executionType)

      schema.fieldNames should equal (Seq("id", "age", "comment", "enrolled", "name"))
      result should have length 10
      result(0) should have length 5
    }

    it should s"support a query with limit 0 for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table LIMIT 0").collect(executionType)
      result should have length 0
    }

    it should s"support a (SELECT column) for $executionType execution" in {

      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT id FROM $postgresqlSchema.$Table ").collect(executionType)
      result should have length 10
      result(0) should have length 1
    }

    it should s"support a (SELECT * ... WHERE PK = _ ) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 1").collect(executionType)
      result should have length 1
    }

    it should s"support a (SELECT * ... WHERE COLUMN IN (...) ) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id IN (1,5,9)").collect(executionType)
      result should have length 3
    }

    it should s"support a (SELECT * ...  WHERE PK = _ AND FIELD_2 = _ AND FIELD_3 = _) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 3 AND age = 13 AND comment = 'Comment 3' ").collect(executionType)
      result should have length 1
    }

  }

}