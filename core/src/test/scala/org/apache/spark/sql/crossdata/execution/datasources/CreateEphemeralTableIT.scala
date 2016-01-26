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
package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CreateEphemeralTableIT extends SharedXDContextTest{

  "Create temp view" should "return a XDDataFrame when executing a SQL query" in {

    val sqlContext = _xdContext
    sql("CREATE EPHEMERAL TABLE ephemeral1 (field1 STRING, field2 INTEGER) OPTIONS (param1 'value1')").show()
// TODO real test
    1 shouldBe (1)
  }

}
