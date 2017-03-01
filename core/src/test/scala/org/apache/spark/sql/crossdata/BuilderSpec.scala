package org.apache.spark.sql.crossdata

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.junit.runner.RunWith
import org.scalatest.Ignore
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
@Ignore
class BuilderSpec extends BaseXDTest {

  val builder = new XDSession.Builder()

  val configFile: File = new File("src/test/resources/core-reference-spec.conf")
  val configuration: Config = ConfigFactory.parseFile(configFile)

  "BuilderEnhancer" should "set catalog & spark configuration properly in options from Config" in {
    builder.config(configuration)
    ()
  }

  it should "set catalog & spark configuration properly in options from Config File" in {
    builder.config(configFile)
    ()
  }

  it should "not set options if file is not readable" in {
    configFile.setReadable(false)
    builder.config(configFile)
    ()
  }

  it should "not set options if file doesn't exist" in {
    builder.config(new File("/path/to/non/existing/file"))
    ()
  }
}
