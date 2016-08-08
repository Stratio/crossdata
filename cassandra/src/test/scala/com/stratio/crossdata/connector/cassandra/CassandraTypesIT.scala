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
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest
import org.apache.spark.sql.crossdata.test.SharedXDContextTypesTest.SparkSQLColDef
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class CassandraTypesIT extends CassandraWithSharedContext with SharedXDContextTypesTest {

  //Prepare test

  override val emptyTypesSetError: String = "Type test entries should have been already inserted"

  override def saveTypesData: Int = {
    val session = client.get._2

    val tableDDL: Seq[String] =
      s"CREATE TYPE $Catalog.STRUCT (field1 INT, field2 INT)" ::
        s"CREATE TYPE $Catalog.STRUCT1 (structField1 VARCHAR, structField2 INT)" ::
          s"CREATE TYPE $Catalog.STRUCT_DATE (field1 TIMESTAMP, field2 INT)" ::
            s"CREATE TYPE $Catalog.STRUCT_STRUCT (field1 TIMESTAMP, field2 INT, struct1 frozen<STRUCT1>)" ::
              s"CREATE TYPE $Catalog.STRUCT_DATE1 (structField1 TIMESTAMP, structField2 INT)" ::
                s"CREATE TYPE $Catalog.STRUCT_ARRAY_STRUCT (stringfield VARCHAR, arrayfield LIST<frozen<STRUCT>>)" ::
                  s"""
           |CREATE TABLE $Catalog.$TypesTable
           |(
           |  id INT,
           |  int INT PRIMARY KEY,
           |  bigint BIGINT,
           |  long BIGINT,
           |  string VARCHAR,
           |  boolean BOOLEAN,
           |  double DOUBLE,
           |  float FLOAT,
           |  decimalint DECIMAL,
           |  decimallong DECIMAL,
           |  decimaldouble DECIMAL,
           |  decimalfloat DECIMAL,
           |  date TIMESTAMP,
           |  timestamp TIMESTAMP,
           |  tinyint INT,
           |  smallint INT,
           |  binary BLOB,
           |  arrayint LIST<INT>,
           |  arraystring LIST<VARCHAR>,
           |  mapintint MAP<INT, INT>,
           |  mapstringint MAP<VARCHAR, INT>,
           |  mapstringstring MAP<VARCHAR, VARCHAR>,
           |  struct frozen<STRUCT>,
           |  arraystruct LIST<frozen<STRUCT>>,
           |  arraystructwithdate LIST<frozen<STRUCT_DATE>>,
           |  structofstruct frozen<STRUCT_STRUCT>,
           |  mapstruct MAP<VARCHAR, frozen<STRUCT_DATE1>>,
           |  arraystructarraystruct LIST<frozen<STRUCT_ARRAY_STRUCT>>
           |)
      """.stripMargin :: Nil

    tableDDL.foreach(session.execute)

    val dataQuery = s"""|
          |INSERT INTO $Catalog.$TypesTable (
          |  id, int, bigint, long, string, boolean, double, float, decimalInt, decimalLong,
          |  decimalDouble, decimalFloat, date, timestamp, tinyint, smallint, binary,
          |  arrayint, arraystring, mapintint, mapstringint, mapstringstring, struct, arraystruct,
          |  arraystructwithdate, structofstruct, mapstruct, arraystructarraystruct
          |)
          |VALUES (
          |	1,  2147483647, 9223372036854775807, 9223372036854775807, 'string', true,
          |	3.3, 3.3, 42, 42, 42.0, 42.0, '2015-11-30 10:00', '2015-11-30 10:00', 1, 1, textAsBlob('binary data'),
          |	[4, 42], ['hello', 'world'], { 0 : 1 }, {'b' : 2 }, {'a':'A', 'b':'B'}, {field1: 1, field2: 2}, [{field1: 1, field2: 2}],
          |	[{field1: 9223372036854775807, field2: 2}], {field1: 9223372036854775807, field2: 2, struct1: {structField1: 'string', structField2: 42}},
          |	{'structid' : {structField1: 9223372036854775807, structField2: 42}},
          | [{stringfield: 'aa', arrayfield: [{field1: 1, field2: 2}, {field1: -1, field2: -2}]},
          | {stringfield: 'bb', arrayfield: [{field1: 11, field2: 22}]}]
          |);
      """.stripMargin

    Try(session.execute(dataQuery)).map(_ => 1).getOrElse(0)

  }

  override protected def typesSet: Seq[SparkSQLColDef] =
    super.typesSet flatMap {
      case SparkSQLColDef(_, "TINYINT", _) | SparkSQLColDef(_, "SMALLINT", _) =>
        Nil
      case SparkSQLColDef(name, "DATE", typeChecker) =>
        SparkSQLColDef(name, "TIMESTAMP", _.isInstanceOf[java.sql.Timestamp]) :: Nil
      case SparkSQLColDef(name, sqlClause, typeChecker) if name contains "struct" =>
        SparkSQLColDef(name, sqlClause.replace("DATE", "TIMESTAMP"), typeChecker) :: Nil
      case other =>
        other :: Nil
    }

  override def sparkAdditionalKeyColumns: Seq[SparkSQLColDef] =
    Seq(SparkSQLColDef("id", "INT"))
  override def dataTypesSparkOptions: Map[String, String] = Map(
      "table" -> TypesTable,
      "keyspace" -> Catalog,
      "cluster" -> ClusterName,
      "pushdown" -> "true",
      "spark_cassandra_connection_host" -> CassandraHost
  )

  //Perform test
  doTypesTest("The Cassandra connector")

}
