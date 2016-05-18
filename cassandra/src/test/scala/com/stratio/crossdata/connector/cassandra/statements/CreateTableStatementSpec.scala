/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
