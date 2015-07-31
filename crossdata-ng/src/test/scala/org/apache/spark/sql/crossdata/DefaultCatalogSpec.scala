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
import org.apache.spark.sql.catalyst.plans.logical.{Subquery, LogicalPlan, Project}
import org.apache.spark.sql.execution.ShowTablesCommand
import org.apache.spark.sql.sources.LogicalRelation
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DefaultCatalogSpec extends FlatSpec {

   "A Defaultcatalog" should "be able to register a table" in {

     val tmpTable: Seq[String] = Seq(UUID.randomUUID.toString)

     val sc = new SparkContext(
       (new SparkConf()).setAppName("Crossdata").setMaster("local[2]"))

     val xdc: XDContext = new XDContext(sc)

     import xdc.implicits._

     val df: DataFrame = sc.parallelize((1 to 5).map(i => new String(s"val_$i"))).toDF()

     xdc.registerDataFrameAsTable(df, tmpTable.mkString("."))

     assert(xdc.catalog.tableExists(tmpTable))
     xdc.catalog.unregisterTable(tmpTable)
     xdc.catalog.close

     xdc.sparkContext.stop
   }

  "A Default catalog" should "be able to specificy a file for the persistence" in  {
    val dc: DefaultCatalog = new DefaultCatalog(
      args = util.Arrays.asList("/tmp/crossdata/catalog"))
    dc.open()
    val tmpTable: Seq[String] = Seq(UUID.randomUUID.toString)
    dc.registerTable(tmpTable, new ShowTablesCommand(None))
    assert(dc.tableExists(tmpTable))
    dc.unregisterTable(tmpTable)
    dc.close
  }

  "A Default Catalog" should "persist tables" in {
    val dc1: DefaultCatalog = new DefaultCatalog
    dc1.open()
    val tmpTable: Seq[String] = Seq("TestOfDefaultCatalogPersistence")
    val sq: Subquery = new Subquery("aliasIsTheTableName", LogicalRelation(new MockBaseRelation))
    val lp: LogicalPlan = new Project(Seq(), sq)
    dc1.registerTable(tmpTable, lp)
    assert(dc1.tableExists(tmpTable))
    dc1.close

    val dc2: DefaultCatalog = new DefaultCatalog
    dc2.open()
    assert(dc2.tableExists(tmpTable))
    assert(dc2.lookupRelation(tmpTable).isInstanceOf[LogicalPlan])
    dc2.unregisterTable(tmpTable)
    dc2.close
  }

}

