package com.stratio.crossdata.connector.cassandra.statements

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CreateTableStatementSpec extends  BaseXDTest {

  val Keyspace = "testKeyspace"
  val Table = "testTable"
  val idField = StructField("id", IntegerType, false)
  val nameField = StructField("name", StringType, false)

  "A CreateTableStatementSpec" should "Build a simple CreateExternalTableStatement " in {


    val schema: StructType = StructType(Seq(idField, nameField))
    val options: Map[String, String] = Map("keyspace" -> Keyspace, "primary_key_string" ->"id")
    val stm = new CreateTableStatement(Table, schema, options)

    //Experimentation
    val query = stm.toString()

    //Expectations
    print(query)
    query should be(s"CREATE TABLE $Keyspace.$Table (id int, name varchar, PRIMARY KEY (id))")
  }

  it should "Build a CreateExternalTableStatement with a Composed PrimKey" in {


    val schema: StructType = StructType(Seq(idField, nameField))
    val options: Map[String, String] = Map("keyspace" -> Keyspace, "primary_key_string" ->"id, name")
    val stm = new CreateTableStatement(Table, schema, options)

    //Experimentation
    val query = stm.toString()

    //Expectations
    print(query)
    query should be(s"CREATE TABLE $Keyspace.$Table (id int, name varchar, PRIMARY KEY (id, name))")
  }
}
