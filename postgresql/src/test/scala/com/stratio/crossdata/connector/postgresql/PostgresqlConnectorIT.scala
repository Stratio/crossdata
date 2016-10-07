package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.ExecutionType._
import org.apache.spark.sql.crossdata.exceptions.CrossdataException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlConnectorIT extends PostgresqlWithSharedContext {

//  Seq(Native, Spark) foreach { executionType =>
  Seq(Spark) foreach { executionType =>
    "The Postgresql connector" should s"support a (SELECT *) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning
      val dataframe = sql(s"SELECT * FROM $Table ")
      val schema = dataframe.schema
      val result = dataframe.collect(executionType)
      schema.fieldNames should equal (Seq("id", "age", "comment", "enrolled", "name"))
      result should have length 10
      result(0) should have length 5
    }

    it should s"support a query with limit 0 for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table LIMIT 0").collect(executionType)
      result should have length 0
    }

    it should s"support a (SELECT column) for $executionType execution" in {

      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT id FROM $Table ").collect(executionType)
      result should have length 10
      result(0) should have length 1
    }

    it should s"support a (SELECT * ... WHERE PK = _ ) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id = 1").collect(executionType)
      result should have length 1
    }

    it should s"support a (SELECT * ... WHERE LAST_PK_COLUMN IN (...) ) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id IN (1,5,9)").collect(executionType)
      result should have length 3
    }

    it should s"support a (SELECT * ...  WHERE CK._1 = _ AND CK._2 = _) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE age = 13 AND comment = 'Comment 3' ").collect(executionType)
      result should have length 1
    }

    it should s"support a (SELECT * ...  WHERE PK = _ AND CK._1 = _ AND CK._2 = _) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id = 3 AND age = 13 AND comment = 'Comment 3' ").collect(executionType)
      result should have length 1
    }

  }

//  "Postgresql connector" should " execute natively a (SELECT * ...  WHERE DEFAULT_COLUM = _ )" in {
//    assumeEnvironmentIsUpAndRunning
//
//    the[CrossdataException] thrownBy {
//      sql(s"SELECT * FROM $Table WHERE enrolled = 'true'").collect(Spark)
//    } should have message "The operation have to be executed natively"
//  }
//
//  it should " execute natively a (SELECT * ...  WHERE PK > _ )" in {
//    assumeEnvironmentIsUpAndRunning
//
//    the[CrossdataException] thrownBy {
//      sql(s"SELECT * FROM $Table WHERE id > 3").collect(Spark)
//    } should have message "The operation have to be executed natively"
//  }
//
//  it should " execute natively a (SELECT * ...  ORDER BY _ )" in {
//    assumeEnvironmentIsUpAndRunning
//
//    the[CrossdataException] thrownBy {
//      sql(s"SELECT * FROM $Table ORDER BY age").collect(Native)
//    } should have message "The operation have to be executed natively"
//  }

}