package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.catalog.CatalogDatabase
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

//TODO: Extract generic test cases as in Crossdata for spark 1.6.x 's `GenericCatalogTests`
//TODO: Improve and user SharedXDSession
@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends BaseXDTest with BeforeAndAfterAll /* extends SharedXDSession*/ {

  /*override lazy val providedCatalogConfig: Option[Config] = Some {
    ConfigFactory.load("zookeeper-catalog.conf")
  }*/

  val databaseName = "customDatabase"

  val db = CatalogDatabase(
    databaseName,
    "some description",
    "/",
    Map("a" -> "b")
  )

  val db2 = db.copy(s"${db.name}2")

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

  protected def cleanPersistence(): Unit = Seq(DAO.dbs.dao, DAO.tables.dao) foreach (_.deleteAll)

  override protected def beforeAll(): Unit = cleanPersistence()

  import xdSession.sharedState.externalCatalog

  "A Zookeeper persistent catalog" should "be able to persist databases" in {
    externalCatalog.databaseExists(databaseName) shouldBe false
    externalCatalog.createDatabase(db, false)
    externalCatalog.databaseExists(databaseName) shouldBe true
  }

  it should "be able to check whether a table has been persited or not" in {
    externalCatalog.databaseExists("fakeDb") shouldBe false
    externalCatalog.databaseExists(databaseName) shouldBe true
  }

  it should "be able to retrieve a previously persisted database" in {
    val retrievedDb = externalCatalog.getDatabase(databaseName)
    retrievedDb shouldBe db
  }

  it should "be able to alter a previously persisted database" in {
    val changedDb = db.copy(description = "a different description")
    externalCatalog.alterDatabase(changedDb)
    val changedRetrievedDb = externalCatalog.getDatabase(databaseName)
    externalCatalog.getDatabase(databaseName) should not equal(db)
  }

  it should "be able to list all databases" in {
    externalCatalog.createDatabase(db2, false)
    externalCatalog.listDatabases() should contain theSameElementsAs (Seq(db,db2).map(_.name))
  }

  //TODO: Not currently implemented
  /*it should "be able to set a persisted database as current database and isolate tables" in {
  }*/

  it should "be able to drop a database" in {
    externalCatalog.databaseExists(databaseName) shouldBe true
    externalCatalog.dropDatabase(databaseName, false, false)
    externalCatalog.databaseExists(databaseName) shouldBe false
  }

  override protected def afterAll(): Unit = cleanPersistence()

}
