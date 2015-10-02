package crossdatang

import cucumber.api.Scenario
import cucumber.api.scala.{EN, ScalaDsl}
import org.apache.spark.sql.Row
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.Matchers

class SimpleSelectTest extends ScalaDsl with EN with Matchers {

  val xDContext = org.apache.spark.sql.crossdata.test.acceptance.TestXDContext

  var DatasourceHost:String = null

  var result: Array[Row] = null

  Given("""^a DATASOURCE_HOST in the System Variable "([^"]*)"$"""){ (DATASOURCE_HOST:String) =>
    DatasourceHost = Option(System.getenv(DATASOURCE_HOST)).getOrElse("127.0.0.1")
  }

  Given("""^a table "([^"]*)" with the provider "([^"]*)" and options "([^"]*)"$"""){ (Table:String, SourceProvider:String, Options:String) =>
    val createTable =
      (s"CREATE TEMPORARY TABLE $Table USING $SourceProvider OPTIONS " +
            s"($Options)").replace("%DATASOURCE_HOST", DatasourceHost)

    xDContext.sql(createTable)
  }

  When( """^I query "([^"]*)"$""") { (selectQuery: String) =>
    result = xDContext.sql(selectQuery).collect()

  }
  Then( """^the xdContext return (\d+) rows;$""") { (expectedSize: Int) =>
    result.size should be (expectedSize)
  }
}
