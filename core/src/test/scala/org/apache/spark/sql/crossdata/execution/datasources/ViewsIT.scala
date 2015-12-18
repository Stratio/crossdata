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

import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.crossdata.XDDataFrame
import org.apache.spark.sql.crossdata.test.CoreWithSharedContext
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ViewsIT extends CoreWithSharedContext {

  "Create temp view" should "return a XDDataFrame when executing a SQL query" in {

    val sqlContext = _xdContext
    import sqlContext.implicits._

    val df  = xdContext.sparkContext.parallelize(1 to 5).toDF

    df.registerTempTable("person")

    sql("CREATE TEMPORARY VIEW vn AS SELECT * FROM person WHERE _1 < 3")

    val dataframe = sql("SELECT * FROM vn")
    dataframe shouldBe a[XDDataFrame]
    dataframe.collect() should have length 2
  }

  "Create view" should "return an analysis exception" in {

    val sqlContext = _xdContext
    import sqlContext.implicits._

    val df  = xdContext.sparkContext.parallelize(1 to 5).toDF

    df.registerTempTable("person")

    an [AnalysisException] should be thrownBy sql("CREATE VIEW vn AS SELECT * FROM person WHERE _1 < 3")


  }


}
