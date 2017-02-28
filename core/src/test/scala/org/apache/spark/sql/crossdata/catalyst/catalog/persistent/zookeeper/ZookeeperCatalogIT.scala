package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{NoSuchPartitionException, PartitionAlreadyExistsException}
import org.apache.spark.sql.catalyst.catalog.CatalogTypes.TablePartitionSpec
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.XDExternalCatalog.TypesafeConfigSettings
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.PartitionModel
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, PartitionDAO, TableDAO}
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

import scala.util.Try

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


  val db = "testdb"
  val table = "testtable"

  val catalogDB = CatalogDatabase(
    db,
    "description",
    "locationURI",
    Map[String, String]("property1" -> "value1"))

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
    TableIdentifier(table, Some(db)),
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
      CatalogStorageFormat.empty,
      Map("param1" -> "value1")
    )

  import xdSession.sharedState.externalCatalog
  val partitionDAO =  new PartitionDAO(xdSession.catalogConfig)

  val seqCatalogPartitions = Seq(basicCatalogTablePartition, basicCatalogTablePartitionWithParameters)

  override protected def beforeAll(): Unit = {
    externalCatalog.createDatabase(catalogDB, true)
    externalCatalog.createTable(catalogTable, true)
  }
  override def afterAll(): Unit = {
    externalCatalog.dropDatabase(db, true, false)
    externalCatalog.dropTable(db, table, true, false)
  }

  "A Zookeeper persistent catalog" should "create a partition" in {
    val seqWithOnePartitionSpec = Seq(basicCatalogTablePartition)
    externalCatalog.createPartitions(db, table, seqWithOnePartitionSpec, true)
//    externalCatalog.listPartitions(db, table, Option(spec1)) should be (seqWithOnePartitionSpec)
//    externalCatalog.dropPartitions(db, table, Seq(spec1), true, false, false)

    val partitionString = spec1.map{case (k, v) => k + ":" +  v}.mkString("*").replace("/", "|")
    val partitionIdentifier = s"$db.$table.$partitionString"
    partitionDAO.dao.get(partitionIdentifier).get.get should be (basicPartitionModel)

  }

  it should "thrown an exception creating a partition that already exists" in {
    val seqWithOnePartitionSpec = Seq(basicCatalogTablePartition)
    externalCatalog.createPartitions(db, table, seqWithOnePartitionSpec, false)
    an [PartitionAlreadyExistsException] should be thrownBy externalCatalog.createPartitions(db, table, seqWithOnePartitionSpec, false)
    externalCatalog.dropPartitions(db, table, Seq(spec1), true, false, false)
  }

  it should "create two partitions" in {
    externalCatalog.createPartitions(db, table, seqCatalogPartitions, true)
    val specs = spec1 ++ spec2
    val resultPartitions = externalCatalog.listPartitions(db, table, Option(specs))
    resultPartitions should be (seqCatalogPartitions)
    externalCatalog.dropPartitions(db, table, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "drop a partition" in {
    externalCatalog.createPartitions(db, table, Seq(basicCatalogTablePartition), true)
    externalCatalog.dropPartitions(db, table, seqCatalogPartitions.map(_.spec), true, false, false)
    an [NoSuchPartitionException] should be thrownBy externalCatalog.getPartition(db, table, basicCatalogTablePartition.spec)
  }

  it should "rename a partition" in {
    externalCatalog.createPartitions(db, table, Seq(basicCatalogTablePartition), true)
    val newSpecs: Seq[CatalogTypes.TablePartitionSpec] = Seq(spec1.map{case (k,v) => k -> (v + "1")})
    externalCatalog.renamePartitions(db, table, Seq(spec1), newSpecs)
    val renamedPartition = externalCatalog.getPartition(db, table, newSpecs.head)
    renamedPartition should be (basicCatalogTablePartition.copy(spec = newSpecs.head))
    externalCatalog.dropPartitions(db, table, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "get a partition" in {
    externalCatalog.createPartitions(db, table, Seq(basicCatalogTablePartition), true)
    externalCatalog.getPartition(db, table, basicCatalogTablePartition.spec) should be (basicCatalogTablePartition)
    externalCatalog.dropPartitions(db, table, seqCatalogPartitions.map(_.spec), true, false, false)
  }

}
