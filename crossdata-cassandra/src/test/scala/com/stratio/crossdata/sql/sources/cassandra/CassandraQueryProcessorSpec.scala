/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.sql.sources.cassandra

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.expressions.{AttributeReference, Literal}
import org.apache.spark.sql.execution.crossdata.NativeUDF
import org.apache.spark.sql.sources
import org.apache.spark.sql.types.DataTypes
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraQueryProcessorSpec extends BaseXDTest {

  val Keyspace = "keyspace"
  val Table = "table"
  val TableQN = s"$Keyspace.$Table"
  val ColumnAge = "age"
  val ColumnId = "id"
  val Limit = 12000
  val ValueAge = 25
  val ValueAge2 = 30
  val ValueId = "00123"
  
  val Function01 = "F#01"
  //val Function02 = "F#02"
  
  val udfs = Map(
    Function01 ->
      NativeUDF(getFunctionName(Function01), DataTypes.StringType, AttributeReference("id", DataTypes.StringType)
      ()::Nil)
  )

  protected def getFunctionName(fid: String): String = fid.split("#").head.trim

  "A CassandraQueryProcessor" should "build a query requiring some columns" in {
    val query = CassandraQueryProcessor.buildNativeQuery(TableQN, Array(ColumnId, ColumnAge), Array(), Limit)

    query should be(s"SELECT $ColumnId, $ColumnAge FROM $TableQN  LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with two equal filters" in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.EqualTo(ColumnAge, ValueAge), sources.EqualTo(ColumnId, ValueId)), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge = $ValueAge AND $ColumnId = '$ValueId' LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with a IN clause" in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.In(ColumnAge, Array(ValueAge, ValueAge2))), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge IN ($ValueAge,$ValueAge2) LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with a IN clause and a single value" in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.In(ColumnAge, Array(ValueAge))), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge IN ($ValueAge) LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with a LT clause " in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.LessThan(ColumnAge, ValueAge)), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge < $ValueAge LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with a LTE clause " in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.LessThanOrEqual(ColumnAge, ValueAge)), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge <= $ValueAge LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with a GT clause " in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.GreaterThan(ColumnAge, ValueAge)), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge > $ValueAge LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with a GTE clause " in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.GreaterThanOrEqual(ColumnAge, ValueAge)), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge >= $ValueAge LIMIT $Limit ALLOW FILTERING")
  }

  it should "build a query with an AND clause " in {
    val query = CassandraQueryProcessor.buildNativeQuery(
      TableQN, Array(ColumnId), Array(sources.And(sources.GreaterThan(ColumnAge, ValueAge), sources.LessThan(ColumnAge, ValueAge2))), Limit)

    query should be(s"SELECT $ColumnId FROM $TableQN WHERE $ColumnAge > $ValueAge AND $ColumnAge < $ValueAge2 LIMIT $Limit ALLOW FILTERING")
  }

  it should "built a query with filters calling a pushed-down function" in {

    val predicate2expectationOp = Map(
      sources.EqualTo(Function01, ValueId) -> "=",
      sources.GreaterThan(Function01, ValueId) -> ">",
      sources.LessThan(Function01, ValueId) -> "<",
      sources.GreaterThanOrEqual(Function01, ValueId) -> ">=",
      sources.LessThanOrEqual(Function01, ValueId) -> "<="
    )

    for((predicate, operatorStr) <- predicate2expectationOp) {
      val query = CassandraQueryProcessor.buildNativeQuery(
        TableQN, Array(ColumnId), Array(predicate), Limit, udfs
      )
      query should be(
        s"SELECT $ColumnId FROM $TableQN WHERE ${getFunctionName(Function01)}($ColumnId) ${operatorStr} '$ValueId' LIMIT $Limit ALLOW FILTERING"
      )
    }
  }
  
  /*
     "A CassandraXDSourceRelation" should "support natively a table scan" in {

       val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
       val table = LogicalRelation(cassandraXDSourceRelation)

       cassandraXDSourceRelation.isSupported(table, table) should be (true)
     }


     "A CassandraXDSourceRelation" should "support natively any filter" in {

       val idAttributeReference = AttributeReference("id", IntegerType)(ExprId(0), Seq("students"))
       val condition = EqualTo(idAttributeReference, Literal(1))
       val table = LogicalRelation(cassandraXDSourceRelation)
       val filter = Filter(condition, table)
       val optimizedLogicalPlan = filter

       cassandraXDSourceRelation.isSupported(table, optimizedLogicalPlan) should be (true)
       cassandraXDSourceRelation.isSupported(filter, optimizedLogicalPlan) should be (true)
     }

     "A CassandraXDSourceRelation" should "not support natively an aggregation" in {

       val attributeReference = AttributeReference("comment", StringType)(ExprId(1), Seq("students"))
       val projectPlan = Project(Seq(attributeReference), LogicalRelation(cassandraXDSourceRelation))
       val count = Count(Literal(1))
       val optimizedLogicalPlan = Aggregate(Seq(attributeReference),
         Seq(Alias(count, "c0")(ExprId(2)), attributeReference, Alias(count, "c2")(ExprId(3))), projectPlan)

       cassandraXDSourceRelation.isSupported(projectPlan, optimizedLogicalPlan) should be (true)
       cassandraXDSourceRelation.isSupported(optimizedLogicalPlan, optimizedLogicalPlan) should be (false)
     }*/

}
