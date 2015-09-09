package crossdatang

import cucumber.api.Scenario
import cucumber.api.scala.{EN, ScalaDsl}
import org.apache.spark.sql.Row
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.Matchers

class SimpleSelectTest extends ScalaDsl with EN with Matchers {

  val xDContext = org.apache.spark.sql.crossdata.test.acceptance.TestXDContext


  var result: Array[Row] = null

  Given( """^a XDContext instance with this createTable query "([^"]*)"$""") { (createTableSQL: String) =>
    xDContext.sql(createTableSQL)
  }

  When( """^I query "([^"]*)"$""") { (selectQuery: String) =>
    result = xDContext.sql(selectQuery).collect()

  }
  Then( """^the xdContext return (\d+) rows;$""") { (expectedSize: Int) =>
    result.size should be (expectedSize)
  }
}
