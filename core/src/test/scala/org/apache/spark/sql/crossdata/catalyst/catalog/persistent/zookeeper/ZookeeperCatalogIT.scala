package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.catalog.CatalogDatabase
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO}
import org.apache.spark.sql.crossdata.test.{SharedXDSession, XDTestUtils}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.io

//TODO: Extract generic test cases as in Crossdata for spark 1.6.x 's `GenericCatalogTests`
//TODO: Improve and user SharedXDSession
@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends BaseXDTest /* extends SharedXDSession*/ {

  /*override lazy val providedCatalogConfig: Option[Config] = Some {
    ConfigFactory.load("zookeeper-catalog.conf")
  }*/

  val xdSession = XDSession.builder()
    .master("local[2]")
    .config(new File("src/test/resources/zookeeper-catalog.conf"))
    .config(
      "crossdata-core.catalog.class",
      "org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.ZookeeperCatalog"
    )
    .create("user01")

  import xdSession.catalogConfig

  object DAO {
    val dbs = new DatabaseDAO(catalogConfig)
    val tables = new TableDAO(catalogConfig)
  }

  import xdSession.sharedState.externalCatalog

  "A Zookeeper persistent catalog" should "be able to persist databases" in {

    val databaseName = "customDatabase"

    val db = CatalogDatabase(
      databaseName,
      "some description",
      "/",
      Map("a" -> "b")
    )

    externalCatalog.createDatabase(db, false)

    DAO.dbs.dao.get(databaseName).isSuccess shouldBe true

  }

}
