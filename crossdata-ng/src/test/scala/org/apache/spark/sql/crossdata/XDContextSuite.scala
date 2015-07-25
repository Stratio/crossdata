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

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.FunSuite

class XDContextSuite extends FunSuite {

  test("XDContext: Pluggable catalog is instantiated") {

    val sparkConf = new SparkConf().setAppName("Crossdata").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(sparkConf)
    val ctx: XDContext = new XDContext(sc)
    val xdCatalog: Catalog = ctx.catalog
    assert(xdCatalog.conf.caseSensitiveAnalysis === true)
    sc.stop
  }

  test("XDContext: Performs a basic operation") {
    val sparkConf = new SparkConf().setAppName("Crossdata").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(sparkConf)
    val xdc: XDContext = new XDContext(sc)

    import xdc.implicits._

    val df = sc.parallelize((1 to 5).map(i => new String(s"val_$i"))).toDF()
    // Any RDD containing case classes can be registered as a table.  The schema of the table is
    // automatically inferred using scala reflection.
    df.registerTempTable("records")

    // Once tables have been registered, you can run SQL queries over them.
    val result: Array[Row] = xdc.sql("SELECT * FROM records").collect
    assert(result.length === 5)

    sc.stop
  }

}
