package org.apache.spark.sql.crossdata.execution.datasources

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class StreamingDdlParserIT extends SharedXDContextTest {

  override val catalogConfig : Option[Config] =
    Try(ConfigFactory.load("core-catalog.conf").getConfig(CoreConfig.ParentConfigName)).toOption


/*  "StreamingDDLParser" should "parse..." in {

    val sqlContext = _xdContext

    val temp = sqlContext.ddlParser.parse("ADD SELECT * FROM t")
    temp.toString()
  }
*/
  it should "parse... 2" in {

    val sqlContext = _xdContext
    //sqlContext.sql("CREATE EPHEMERAL TABLE t OPTIONS (ho 'hol')")
   // val temp = sqlContext.ddlParser.parse("ADD SELECT * FROM t WITH WINDOW 5 SECS AS topic")
    sqlContext.sql("ADD SELECT count(*) FROM t WITH WINDOW 5 SECS AS topic2")
    //temp.toString()
    println("dsf")
  }


}

