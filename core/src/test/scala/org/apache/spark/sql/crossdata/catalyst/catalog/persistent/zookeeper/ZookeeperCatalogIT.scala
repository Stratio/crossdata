package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.{CatalogStorageFormat, CatalogTable, CatalogTableType}
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.TableModel
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO}
import org.apache.spark.sql.crossdata.test.{SharedXDSession, XDTestUtils}
import org.apache.spark.sql.types.StructType
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

  "A Zookeeper persistent catalog" should "persist entries" in {
    ()
  }

  "A Zookeeper persistent catalog" should "persist tables" in {

    val version = "1.11.0"
    val identifier: TableIdentifier= new TableIdentifier("table")
    val tableType: CatalogTableType=CatalogTableType.EXTERNAL
    val storage: CatalogStorageFormat=new CatalogStorageFormat(None,None,None,None,false, Map.empty)
    val schema: StructType = new StructType()

    val catalog:CatalogTable=new CatalogTable(identifier, tableType, storage, schema)
    val table =  new TableModel(catalog, version)
  }

}
