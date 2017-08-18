package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.crossdata.XDDataFrame
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ViewsIT extends SharedXDContextTest {

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
    a[RuntimeException] shouldBe thrownBy {
      df.registerTempTable("person")
      sql("CREATE VIEW persistedview AS SELECT * FROM person WHERE _1 < 3")
    }

  }

  "Create temp table" should "return a XDDataFrame when executing a SQL query" in {

    val sqlContext = _xdContext
    import sqlContext.implicits._

    val df  = sqlContext.sparkContext.parallelize(1 to 5).toDF
    df.registerTempTable("person")
    sql("CREATE TEMPORARY TABLE vn AS SELECT * FROM person WHERE _1 < 3")

    val dataframe = xdContext.sql("SELECT * FROM vn")

    dataframe shouldBe a[XDDataFrame]
    dataframe.collect() should have length 2
  }

  // TODO When we can add views to Zookeeper catalog, Views' test should be moved to GenericCatalogTests in order to test the specific implementations.
  "Create table as select" should "persist a view in the catalog only with persisted tables" in {
    val sqlContext = _xdContext
    import sqlContext.implicits._

    val df = sqlContext.sparkContext.parallelize(1 to 5).toDF
    a[RuntimeException] shouldBe thrownBy {
      df.registerTempTable("person")
      sql("CREATE TABLE persistedview AS SELECT * FROM person WHERE _1 < 3")
    }

  }



}
