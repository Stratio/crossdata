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

import java.lang.{RuntimeException, Runtime}

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.crossdata.XDDataFrame

import org.apache.spark.sql.crossdata.test.CoreWithSharedContext

import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.test.SharedXDContextTest

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ViewsIT extends CoreWithSharedContext {

  "Create temp view" should "return a XDDataFrame when executing a SQL query" in {

    val sqlContext = _xdContext
    import sqlContext.implicits._


    val df  = sqlContext.sparkContext.parallelize(1 to 5).toDF

    df.registerTempTable("person")
    sql("CREATE TEMPORARY VIEW vn AS SELECT * FROM person WHERE _1 < 3")


    val dataframe = xdContext.sql("SELECT * FROM vn")

    dataframe shouldBe a[XDDataFrame]
    dataframe.collect() should have length 2
  }

  // TODO When we can add views to Zookeeper catalog, Views' test should be moved to GenericCatalogTests in order to test the specific implementations.
  "Create view" should "persist a view in the catalog only with persisted tables" in {
    val sqlContext = _xdContext
    import sqlContext.implicits._


    val df = sqlContext.sparkContext.parallelize(1 to 5).toDF
    a [RuntimeException] shouldBe thrownBy {
      df.registerTempTable("person")
      sql("CREATE VIEW persistedview AS SELECT * FROM person WHERE _1 < 3")
    }

  }



}
