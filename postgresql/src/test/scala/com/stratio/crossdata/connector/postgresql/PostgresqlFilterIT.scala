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
package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.{ExecutionType, XDDataFrame}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlFilterIT extends PostgresqlWithSharedContext {

/*
  "The Postgresql connector" should s"support a (SELECT *) natively" in {
    assumeEnvironmentIsUpAndRunning
    val dataframe = sql(s"SELECT * FROM $Table ")
    val schema = dataframe.schema
    val result = dataframe.collect(ExecutionType.Native)

    schema.fieldNames should equal (Seq("id", "age", "comment", "enrolled", "name"))
    result should have length 10
    result(0) should have length 5
  }

  it should s"support a query with limit 0 natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table LIMIT 0").collect(ExecutionType.Native)
    result should have length 0
  }

  it should s"support a (SELECT column) natively" in {

    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT id FROM $Table ").collect(ExecutionType.Native)
    result should have length 10
    result(0) should have length 1
  }

  it should s"support a (SELECT * ... WHERE PK = _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id = 1").collect(ExecutionType.Native)
    result should have length 1
  }

  it should s"support a (SELECT * ... WHERE PK <> _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id <> 1").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ... WHERE PK > _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id > 1").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ... WHERE PK < _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id < 10").collect(ExecutionType.Native)
    result should have length 9
  }

  it should s"support a (SELECT * ... WHERE PK >= _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id >= 1").collect(ExecutionType.Native)
    result should have length 10
  }

  it should s"support a (SELECT * ... WHERE PK <= _ ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $Table WHERE id <= 10").collect(ExecutionType.Native)
    result should have length 10
  }
*/
// IS NULL and project disappear in optimizedPlan
  it should s"support a (SELECT * ... WHERE ... IS NULL ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = _xdContext.sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id IS NULL ").asInstanceOf[XDDataFrame]
    val result = df.collect(ExecutionType.Native)
    result should have length 0
  }
/*// IS NOT NULL disappear in optimizedPlan
  it should s"support a (SELECT * ... WHERE ... IS NOT NULL ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id IS NOT NULL ").collect(ExecutionType.Native)
    result should have length 10
  }*/
  /*

    it should s"support a (SELECT * ... WHERE COLUMN IN (...) ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id IN (1,5,9)").collect(ExecutionType.Native)
      result should have length 3
    }

    it should s"support a (SELECT * ... WHERE COLUMN NOT IN (...) ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id NOT IN (1,5,9)").collect(ExecutionType.Native)
      result should have length 7
    }

    it should s"support a (SELECT * ...  WHERE ... LIKE '_%' ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE comment LIKE 'C%' ").collect(ExecutionType.Native)
      result should have length 10
    }

    it should s"support a (SELECT * ...  WHERE ... LIKE '%_' ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE comment LIKE '%3' ").collect(ExecutionType.Native)
      result should have length 1
    }

    it should s"support a (SELECT * ...  WHERE ... LIKE '%_%') natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE comment LIKE '%mm%' ").collect(ExecutionType.Native)
      result should have length 10
    }

    it should s"support a (SELECT * ...  WHERE  ... BETWEEN ...) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE age BETWEEN 11 AND 15").collect(ExecutionType.Native)
      result should have length 5
    }

    it should s"support a (SELECT * ...  WHERE ... = AND = ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id = 3 AND age = 13 AND comment = 'Comment 3' ").collect(ExecutionType.Native)
      result should have length 1
    }

    it should s"support a (SELECT * ...  WHERE ... = OR = ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE id = 3 OR age = 14 OR comment = 'Comment 5' ").collect(ExecutionType.Native)
      result should have length 3
    }

    it should s"support a (SELECT * ...  WHERE ... NOT(conditions) ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE NOT(id = 3 AND age = 13 AND comment = 'Comment 3') ").collect(ExecutionType.Native)
      result should have length 9
    }

    it should s"support a (SELECT * ...  WHERE ... (_ AND _) OR _) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE (id = 3 AND age = 13) OR comment = 'Comment 5' ").collect(ExecutionType.Native)
      result should have length 2
    }

    it should s"support a (SELECT * ...  WHERE ... (_ OR _) AND _) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE (id = 3 OR age = 13) AND comment = 'Comment 5' ").collect(ExecutionType.Native)
      result should have length 0
    }

    it should s"support a (SELECT * ...  WHERE ... (_ OR _) AND (_ OR _) natively" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $Table WHERE (id = 3 OR age = 13) AND (comment = 'Comment 4' OR comment = 'Comment 5') OR comment = 'Comment 6' ").collect(ExecutionType.Native)
      result should have length 1
    }
  */

}
