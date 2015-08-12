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

package org.apache.spark.sql.crossdata.hive

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.XDDataframe
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDHiveContextIT extends FlatSpec with BeforeAndAfterAll {

  private lazy val xctx = org.apache.spark.sql.crossdata.hive.test.TestXDHiveContext

  "A XDContext" should "perform a collect with a collection" in {
    import xctx.implicits._

    val df = xctx.sparkContext.parallelize((1 to 5).map(i => new String(s"val_$i"))).toDF()
    // Any RDD containing case classes can be registered as a table.  The schema of the table is
    // automatically inferred using scala reflection.
    df.registerTempTable("records")

    // Once tables have been registered, you can run SQL queries over them.
    val result: Array[Row] = xctx.sql("SELECT * FROM records").collect()
    assert(result.length === 5)
  }


  it must "return a XDDataFrame when executing a SQL query" in {
    import xctx.implicits._

    val df = xctx.sparkContext.parallelize((1 to 5).map(i => new String(s"val_$i"))).toDF()
    df.registerTempTable("records")

    val dataframe = xctx.sql("SELECT * FROM records")
    assert(dataframe.isInstanceOf[XDDataframe])
  }


}

