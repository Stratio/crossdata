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

package org.apache.spark.sql.crossdata

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.sources.CreateTableUsing
import org.apache.spark.sql.types._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class MySQLCatalogSpec extends BaseXDTest {

  "the table" should "be stored" in {
    val field1 = StructField("column1", StringType, true)
    val field2 = StructField("column2", StringType, true)
    val fields = Seq[StructField](field1, field2)
    val columns = StructType(fields)
    val createTable = CreateTableUsing("example", Option(columns), "Cassandra", false, Map[String, String](), false, false)
    val table = Seq("example")

  }
}