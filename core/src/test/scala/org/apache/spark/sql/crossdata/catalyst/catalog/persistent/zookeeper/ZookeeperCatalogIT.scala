package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{NoSuchPartitionException, NoSuchPartitionsException, PartitionsAlreadyExistException}
import org.apache.spark.sql.catalyst.catalog.ExternalCatalogUtils.escapePathName
import org.apache.spark.sql.catalyst.catalog.{CatalogDatabase, _}
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.PartitionModel
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, PartitionDAO, TableDAO}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

//TODO: Extract generic test cases as in Crossdata for spark 1.6.x 's `GenericCatalogTests`
//TODO: Improve and user SharedXDSession
@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends BaseXDTest with BeforeAndAfterAll/* extends SharedXDSession*/ {

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

  import xdSession.sharedState.externalCatalog

  object DAO {
    import xdSession.catalogConfig
    val dbs = new DatabaseDAO(catalogConfig)
    val tables = new TableDAO(catalogConfig)
    val partitions = new PartitionDAO(catalogConfig)
  }

  protected def cleanPersistence(): Unit = Seq(DAO.dbs.dao, DAO.tables.dao, DAO.partitions.dao) foreach (_.deleteAll)

  override protected def beforeAll(): Unit = {
    cleanPersistence()
    externalCatalog.createDatabase(db, true)
    externalCatalog.createTable(catalogTable, true)
  }

  override protected def afterAll(): Unit = cleanPersistence()

  val databaseName = "customDatabase"
  val tableName = "customTable"
  val db = CatalogDatabase(
    databaseName,
    "some description",
    "/",
    Map("a" -> "b")
  )

  val db2 = db.copy(s"${db.name}2")

  val fieldName1 = "field1"
  val fieldName2 = "field2"
  val locationUri = "/catalogpath"

  val storageFormat = CatalogStorageFormat(
    Some(locationUri),
    None,
    None,
    None,
    false,
    Map.empty[String, String])

  val catalogTable = CatalogTable(
    TableIdentifier(tableName, Some(databaseName)),
    CatalogTableType.EXTERNAL,
    storageFormat,
    new StructType(Array(StructField(fieldName1, StringType))),
    partitionColumnNames = Seq(fieldName1)
  )

  val spec1 = Map(fieldName1 -> s"/path-$fieldName1")
  val spec2 = Map(fieldName2 -> s"/path-$fieldName2")

  val basicCatalogTablePartition =
    CatalogTablePartition(
      spec1,
      storageFormat,
      Map[String, String]()
    )
  val basicPartitionModel = PartitionModel(basicCatalogTablePartition)

  val basicCatalogTablePartitionWithParameters =
    CatalogTablePartition(
      spec2,
      storageFormat,
      Map("param1" -> "value1")
    )

  import xdSession.sharedState.externalCatalog

  val seqCatalogPartitions = Seq(basicCatalogTablePartition, basicCatalogTablePartitionWithParameters)
//
//  "A Zookeeper persistent catalog" should "be able to persist databases" in {
//    externalCatalog.createDatabase(db, true)
//    externalCatalog.databaseExists(databaseName) shouldBe true
//  }
//
//  it should "be able to check whether a table has been persited or not" in {
//    externalCatalog.databaseExists("fakeDb") shouldBe false
//    externalCatalog.databaseExists(databaseName) shouldBe true
//  }
//
//  it should "be able to retrieve a previously persisted database" in {
//    val retrievedDb = externalCatalog.getDatabase(databaseName)
//    retrievedDb shouldBe db
//  }
//
//  it should "be able to alter a previously persisted database" in {
//    val changedDb = db.copy(description = "a different description")
//    externalCatalog.alterDatabase(changedDb)
//    externalCatalog.getDatabase(databaseName) should not equal(db)
//  }
//
//  it should "be able to list all databases" in {
//    externalCatalog.createDatabase(db2, false)
//    externalCatalog.listDatabases() should contain theSameElementsAs (Seq(db,db2).map(_.name))
//  }
//
//  //TODO: Not currently implemented
//  /*it should "be able to set a persisted database as current database and isolate tables" in {
//  }*/
//
//  it should "be able to drop a database" in {
//    externalCatalog.databaseExists(databaseName) shouldBe true
//    externalCatalog.dropDatabase(databaseName, false, false)
//    externalCatalog.databaseExists(databaseName) shouldBe false
//  }
//
//  /*** PARTITIONS ***/
//
//  it should "create a partition" in {
//    val seqWithOnePartitionSpec = Seq(basicCatalogTablePartition)
//    externalCatalog.createPartitions(databaseName, tableName, seqWithOnePartitionSpec, true)
//    externalCatalog.listPartitions(databaseName, tableName, Option(spec1)) should be (seqWithOnePartitionSpec)
//    externalCatalog.dropPartitions(databaseName, tableName, Seq(spec1), true, false, false)
//  }
//
//  it should "thrown an exception creating a partition that already exists" in {
//    val seqWithOnePartitionSpec = Seq(basicCatalogTablePartition)
//    externalCatalog.createPartitions(databaseName, tableName, seqWithOnePartitionSpec, false)
//    an [PartitionsAlreadyExistException] should be thrownBy externalCatalog.createPartitions(databaseName, tableName, seqWithOnePartitionSpec, false)
//    externalCatalog.dropPartitions(databaseName, tableName, Seq(spec1), true, false, false)
//  }
//
//  it should "create two partitions" in {
//    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
//    //TODO use listPartitions instead of getPartition
//    val result = externalCatalog.getPartition(databaseName, tableName, spec1) ::
//      externalCatalog.getPartition(databaseName, tableName, spec2) :: Nil
//    result should be (seqCatalogPartitions)
//    //    val specs = spec1 ++ spec2
////    val result = externalCatalog.listPartitions(databaseName, tableName, Option(specs))
////    result should be (seqCatalogPartitions)
//    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
//  }
//
//  it should "drop a partition" in {
//    externalCatalog.createPartitions(databaseName, tableName, Seq(basicCatalogTablePartition), true)
//    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
//    an [NoSuchPartitionException] should be thrownBy externalCatalog.getPartition(databaseName, tableName, basicCatalogTablePartition.spec)
//  }
//
//
//  it should "throw an Exception erasing a partition that not exists" in {
//    an [NoSuchPartitionsException] should be thrownBy externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), false, false, false)
//  }
//
//  it should "rename a partition" in {
//    externalCatalog.createPartitions(databaseName, tableName, Seq(basicCatalogTablePartition), true)
//    val newSpecs: Seq[CatalogTypes.TablePartitionSpec] = Seq(spec1.map{case (k,v) => k -> (v + "1")})
//    externalCatalog.renamePartitions(databaseName, tableName, Seq(spec1), newSpecs)
//    val renamedPartition = externalCatalog.getPartition(databaseName, tableName, newSpecs.head)
//    renamedPartition should be (basicCatalogTablePartition.copy(spec = newSpecs.head))
//    externalCatalog.dropPartitions(databaseName, tableName, newSpecs, true, false, false)
//  }
//
//  it should "get a partition" in {
//    externalCatalog.createPartitions(databaseName, tableName, Seq(basicCatalogTablePartition), true)
//    externalCatalog.getPartition(databaseName, tableName, basicCatalogTablePartition.spec) should be (basicCatalogTablePartition)
//    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
//  }
//
//  it should "get None as a result of getPartitionOption when partition doesn't exists" in {
//    externalCatalog.getPartitionOption(databaseName, tableName, basicCatalogTablePartition.spec) should be (None)
//  }


  it should "list two partitions without partialSpec" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val partitions = externalCatalog.listPartitions(databaseName, tableName, None)
    partitions should be (seqCatalogPartitions)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "list two partitions with partialSpec" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val partitions = externalCatalog.listPartitions(databaseName, tableName, Some(seqCatalogPartitions.map(_.spec).reduce(_++_)))
    partitions should be (seqCatalogPartitions)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "list two partition names" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val partitionNames = externalCatalog.listPartitionNames(databaseName, tableName, Some(seqCatalogPartitions.map(_.spec).reduce(_++_)))
    val expectedResult = (spec1 ++ spec2).map { case (name, path) =>
        escapePathName(name) + "=" + escapePathName(path)
      }.mkString("/")
    partitionNames should be (expectedResult)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  //TODO
  it should "alter a partition" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val newSpecs: Seq[CatalogTypes.TablePartitionSpec] = Seq(spec1.map{case (k,v) => k -> (v + "1")})
    externalCatalog.alterPartitions(databaseName, tableName, seqCatalogPartitions)
    val alteredPartition = externalCatalog.getPartition(databaseName, tableName, newSpecs.head)
    alteredPartition should be (basicCatalogTablePartition.copy(spec = newSpecs.head))
    externalCatalog.dropPartitions(databaseName, tableName, newSpecs ++ seqCatalogPartitions.map(_.spec), true, false, false)
  }

}
