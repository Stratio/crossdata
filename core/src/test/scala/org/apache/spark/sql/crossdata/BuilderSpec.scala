package org.apache.spark.sql.crossdata


import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class BuilderSpec extends BaseXDTest with BuilderEnhancer with BeforeAndAfter{

  private var options: mutable.Set[(String, String)] = mutable.Set.empty[(String, String)]

  override type BuilderType = BuilderEnhancer

  val configFile: File = new File("src/test/resources/core-reference-spec.conf")
  val configuration: Config = ConfigFactory.parseFile(configFile)

  override def config(key: String, value: String): BuilderType = {
    options += key -> value
    this
  }

  "BuilderEnhancer" should "get catalog config from Config" in {
    val catalogSet = getCatalogConf(configuration)
    checkCatalogConf(catalogSet) shouldBe true
  }

  it should "get Spark config from Config" in {
    val sparkSet = getSparkConf(configuration)
    checkSparkConf(sparkSet) shouldBe true
  }

  it should "set catalog & spark configuration properly in options from Config" in {
    config(configuration)
    checkCatalogConf(options.toSet) shouldBe true
    checkSparkConf(options.toSet) shouldBe true
  }

  it should "set catalog & spark configuration properly in options from Config File" in {
    config(configFile)
    checkCatalogConf(options.toSet) shouldBe true
    checkSparkConf(options.toSet) shouldBe true
  }

  it should "not set options if file is not readable" in {
    configFile.setReadable(false)
    config(configFile)
    options.isEmpty shouldBe true
  }

  it should "not set options if file doesn't exist" in {
    config(new File("/path/to/non/existing/file"))
    options.isEmpty shouldBe true
  }

  // ---------------------------------------------------------------------------

  before {
    configFile.setReadable(true)
  }

  after {
    cleanOptions()
  }

  private def cleanOptions(): Unit = {
    options = mutable.Set.empty[(String, String)]
  }
  
  private def checkSparkConf(confSet: Set[(String, String)]): Boolean = {
    confSet.nonEmpty &&
      confSet.contains(("spark.app.name", "CrossdataServer")) &&
      confSet.contains(("spark.master", "local[*]")) &&
      confSet.contains(("spark.akka.heartbeat.interval", "5000")) &&
      confSet.contains(("spark.driver.maxResultSize", "1G")) &&
      confSet.contains(("spark.scheduler.mode", "FAIR"))
  }

  private def checkCatalogConf(confSet: Set[(String, String)]): Boolean = {
    confSet.nonEmpty &&
      confSet.contains(("crossdata-core.catalog.zookeeper.connectionString", "localhost:2181")) &&
      confSet.contains(("crossdata-core.catalog.zookeeper.connectionTimeout", "15000")) &&
      confSet.contains(("crossdata-core.catalog.zookeeper.sessionTimeout", "60000")) &&
      confSet.contains(("crossdata-core.catalog.zookeeper.retryAttempts", "5")) &&
      confSet.contains(("crossdata-core.catalog.zookeeper.retryInterval", "10000")) &&
      confSet.contains(("crossdata-core.catalog.prefix", "crossdataCluster"))
  }
}
