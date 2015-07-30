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

import java.util
import java.util.UUID

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.execution.ShowTablesCommand
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DefaultCatalogSpec extends FunSuite {

   test("Default catalog: Register Table") {

     val tmpTable: Seq[String] = Seq(UUID.randomUUID.toString)

     val sc = new SparkContext(
       (new SparkConf()).setAppName("Crossdata").setMaster("local[2]"))

     val xdc: XDContext = new XDContext(sc)

     import xdc.implicits._

     val df: DataFrame = sc.parallelize((1 to 5).map(i => new String(s"val_$i"))).toDF()

     xdc.registerDataFrameAsTable(df, tmpTable.mkString("."))

     assert(xdc.catalog.tableExists(tmpTable))
     xdc.catalog.unregisterTable(tmpTable)
     xdc.catalog.close()

     xdc.sparkContext.stop
   }

  test("Default catalog: Specific file") {
    val dc: DefaultCatalog = new DefaultCatalog(
      args = util.Arrays.asList("/tmp/crossdata/catalog"))
    val tmpTable: Seq[String] = Seq(UUID.randomUUID.toString)
    dc.registerTable(tmpTable, new ShowTablesCommand(None))
    assert(dc.tableExists(tmpTable))
    dc.unregisterTable(tmpTable)
    dc.close()
  }

}

