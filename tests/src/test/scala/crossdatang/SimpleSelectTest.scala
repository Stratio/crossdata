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
