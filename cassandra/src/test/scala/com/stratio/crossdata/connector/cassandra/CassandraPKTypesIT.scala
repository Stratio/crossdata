/**
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
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.{ExecutionType, XDDataFrame}
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest.SparkSQLColdDef
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
abstract class CassandraPKTypesIT extends CassandraWithSharedContext with SharedXDContextTypesTest {

  val uuid = java.util.UUID.randomUUID.toString
  override val Catalog = s"ks-${uuid}"
  override val Table = s"t-${uuid}"

  override val defaultOptions = Map(
    "table"    -> Table,
    "keyspace" -> Catalog,
    "cluster"  -> ClusterName,
    "pushdown" -> "true",
    "spark_cassandra_connection_host" -> CassandraHost
  )

  override val emptyTypesSetError: String = "Filter with a PK of Timestamp type must be execute natively"

  val session = client.get._2

  val queries: Seq[String] =
    s"""CREATE KEYSPACE $Catalog WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}
        |  AND durable_writes = true;""".stripMargin::
      s"CREATE TABLE $Catalog.$Table (date timestamp PRIMARY KEY)"::
      s"INSERT INTO $Catalog.$Table(date) VALUES ('2015-06-23 10:30+0100')"::Nil

  queries.foreach(session.execute)

  override protected def typesSet: Seq[SparkSQLColdDef] = super.typesSet flatMap {
    case SparkSQLColdDef(_, "TINYINT", _) | SparkSQLColdDef(_, "SMALLINT", _) => Nil
    case SparkSQLColdDef(name, "DATE", typeChecker) =>
      SparkSQLColdDef(name, "TIMESTAMP", _.isInstanceOf[java.sql.Timestamp])::Nil
    case SparkSQLColdDef(name, sqlClause, typeChecker) if name contains "struct" =>
      SparkSQLColdDef(name, sqlClause.replace("DATE", "TIMESTAMP"), typeChecker)::Nil
    case other =>
      other::Nil
  }

  override def sparkAdditionalKeyColumns: Seq[SparkSQLColdDef] = Seq(SparkSQLColdDef("date", "TIMESTAMP"))
  override def dataTypesSparkOptions: Map[String, String] = Map(
    "table"    -> TypesTable,
    "keyspace" -> Catalog,
    "cluster"  -> ClusterName,
    "pushdown" -> "true",
    "spark_cassandra_connection_host" -> CassandraHost
  )

  //Perform test
  doTypesTest("The Cassandra connector")

  override def doTypesTest(datasourceName: String): Unit = {
    datasourceName should s"be able to execute a filter on a PK of Timestamp type" in {
      val dframe = sql("SELECT " + typesSet.map(_.colname).mkString(", ") + s" FROM $Catalog.$Table")
      val result = dframe.asInstanceOf[XDDataFrame].collect(ExecutionType.Native)
      assert(result.apply(0).mkString.equalsIgnoreCase("2015-06-23 10:30+0100"), s"Wrong result")
    }
  }

}
