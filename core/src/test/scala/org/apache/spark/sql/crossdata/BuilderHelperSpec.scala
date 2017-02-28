package org.apache.spark.sql.crossdata


import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, PrivateMethodTester}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BuilderHelperSpec extends BaseXDTest with BuilderHelper with BeforeAndAfter {

  val configFile: File = new File("src/test/resources/core-reference-spec.conf")
  println(configFile.getAbsolutePath)
  val configuration: Config = ConfigFactory.parseFile(configFile)

  "BuilderEnhancer" should "get catalog config from Config" in {
    val catalogSet = getCatalogConf(configuration)
    checkCatalogConf(catalogSet) shouldBe true
  }

  it should "get Spark config from Config" in {
    val sparkSet = getSparkConf(configuration)
    checkSparkConf(sparkSet) shouldBe true
  }

  // ---------------------------------------------------------------------------

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
