package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{NoSuchPartitionException, NoSuchPartitionsException, NoSuchTableException, PartitionsAlreadyExistException, TableAlreadyExistsException}
import org.apache.spark.sql.catalyst.catalog.ExternalCatalogUtils.escapePathName
import org.apache.spark.sql.catalyst.catalog.{CatalogDatabase, _}
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.PartitionModel
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, PartitionDAO, TableDAO}
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner


//TODO: Extract generic test cases as in Crossdata for spark 1.6.x 's `GenericCatalogTests`
//TODO: Improve and user SharedXDSession
@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends BaseXDTest with BeforeAndAfterAll/* extends SharedXDSession*/ {

  lazy val version = "1.11.0"
  lazy val tableNameNotExists = "tableNotExists"
  lazy val databaseName = "customDatabase"
  lazy val tableName = "customTable"
  lazy val newTableName = "newTableName"
  lazy val tableIdentifier: TableIdentifier = new TableIdentifier(tableName,Some(db.name))
  lazy val tableIdentifierNotExists: TableIdentifier = new TableIdentifier(tableNameNotExists)
  lazy val newTableIdentifier: TableIdentifier = new TableIdentifier(newTableName)
  lazy val tableType: CatalogTableType = CatalogTableType.EXTERNAL
  lazy val storage = CatalogStorageFormat(
    Some(locationUri),
    None,
    None,
    None,
    false,
    Map.empty[String, String])
  
  lazy val Field1Name = "column1"
  lazy val Field2Name = "column2"
  lazy val FieldWitStrangeChars = "1+x"
  lazy val SubField1Name = "subcolumn1"
  lazy val SubField2Name = "subcolumn2"
  lazy val Field1 = StructField(Field1Name, StringType, nullable = true)
  lazy val Field2 = StructField(Field2Name, StringType, nullable = true)
  lazy val SubField = StructField(SubField1Name, StringType, nullable = true)
  lazy val SubField2 = StructField(SubField2Name, StringType, nullable = true)
  lazy val arrayField = StructField(SubField2Name, ArrayType(StringType), nullable = true)
  lazy val Fields = Seq[StructField](Field1, Field2)
  lazy val SubFields = Seq(SubField, SubField2)
  lazy val schema: StructType = StructType(Fields)
  lazy val schemaAlter: StructType = new StructType()
  lazy val catalogTable:CatalogTable = new CatalogTable(tableIdentifier, tableType, storage, schema, partitionColumnNames = Seq(fieldName1))
  lazy val catalogTableAlter:CatalogTable = new CatalogTable(tableIdentifier, tableType, storage, schemaAlter)
  lazy val catalogTableAlterNotExists:CatalogTable = new CatalogTable(tableIdentifierNotExists, tableType, storage, schemaAlter)



  lazy val db = CatalogDatabase(
    databaseName,
    "some description",
    "/",
    Map("a" -> "b")
  )

  lazy val db2 = db.copy(s"${db.name}2")
  lazy val fieldName1 = "field1"
  lazy val fieldName2 = "field2"
  lazy val locationUri = "/catalogpath"
  lazy val spec1 = Map(fieldName1 -> s"/path-$fieldName1")
  lazy val spec2 = Map(fieldName2 -> s"/path-$fieldName2")
  lazy val basicCatalogTablePartition =
    CatalogTablePartition(
      spec1,
      storage,
      Map[String, String]()
    )
  lazy val basicPartitionModel = PartitionModel(basicCatalogTablePartition)
  lazy val basicCatalogTablePartitionWithParameters =
    CatalogTablePartition(
      spec2,
      storage,
      Map("param1" -> "value1")
    )

  lazy val seqCatalogPartitions = Seq(basicCatalogTablePartition, basicCatalogTablePartitionWithParameters)

  lazy val xdSession = XDSession.builder()
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
  }

  override protected def afterAll(): Unit = cleanPersistence()

  import xdSession.sharedState.externalCatalog

  "A Zookeeper persistent catalog" should "persist tables" in {
    externalCatalog.tableExists(db.name,tableName) shouldBe false
    externalCatalog.createTable(catalogTable,false)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
  }

  "A Zookeeper persistent catalog" should  " not persist tables that exist" in {
    an[TableAlreadyExistsException] shouldBe thrownBy{
      externalCatalog.createTable(catalogTable,false)
    }
  }


  "A Zookeeper persistent catalog" should  " get a table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    val table = externalCatalog.getTable(db.name, tableName)
    table.identifier.database shouldBe Some(db.name)
    table.identifier.table shouldBe tableName
    table.schema.toList should contain theSameElementsAs schema.toList
  }

  "A Zookeeper persistent catalog" should  " get table option" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name, tableName) shouldBe true
    val table=externalCatalog.getTableOption(db.name, tableName).get
    table should not be None
    table.identifier.database shouldBe Some(db.name)
    table.identifier.table shouldBe tableName
    table.schema.toList should contain theSameElementsAs schema.toList
  }

  "A Zookeeper persistent catalog" should  " know if table exists" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
  }

  "A Zookeeper persistent catalog" should  " listTables" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    val listTables=externalCatalog.listTables(db.name)
    listTables.size shouldBe 1
    listTables should contain (tableName)

  }


//  TODO Uncomment when load table be available
//  "A Zookeeper persistent catalog" should  " loadTables" ignore {
//    val table=externalCatalog.loadTable(db.name,tableName,"/stratio/crossdata/crossdataCluster_tablesviews/customDatabase.table",false,false)
//  }


  "A Zookeeper persistent catalog" should "be able to persist databases" in {
    externalCatalog.createDatabase(db, true)
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
    externalCatalog.getDatabase(databaseName) should not equal(db)
  }

  it should "be able to list all databases" in {
    externalCatalog.createDatabase(db2, false)
    externalCatalog.listDatabases() should contain theSameElementsAs (Seq(db,db2).map(_.name))
  }

  //TODO: Not currently implemented
  /*it should "be able to set a persisted database as current database and isolate tables" in {
  }*/

  /*** PARTITIONS ***/

  it should "create a partition" in {
    val seqWithOnePartitionSpec = Seq(basicCatalogTablePartition)
    externalCatalog.createPartitions(databaseName, tableName, seqWithOnePartitionSpec, true)
    externalCatalog.listPartitions(databaseName, tableName, Option(spec1)) should be (seqWithOnePartitionSpec)
    externalCatalog.dropPartitions(databaseName, tableName, Seq(spec1), true, false, false)
  }

  it should "thrown an exception creating a partition that already exists" in {
    val seqWithOnePartitionSpec = Seq(basicCatalogTablePartition)
    externalCatalog.createPartitions(databaseName, tableName, seqWithOnePartitionSpec, false)
    an [PartitionsAlreadyExistException] should be thrownBy externalCatalog.createPartitions(databaseName, tableName, seqWithOnePartitionSpec, false)
    externalCatalog.dropPartitions(databaseName, tableName, Seq(spec1), true, false, false)
  }

  it should "create two partitions" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    externalCatalog.listPartitions(databaseName, tableName, None) should be (seqCatalogPartitions)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "drop a partition" in {
    externalCatalog.createPartitions(databaseName, tableName, Seq(basicCatalogTablePartition), true)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
    an [NoSuchPartitionException] should be thrownBy externalCatalog.getPartition(databaseName, tableName, basicCatalogTablePartition.spec)
  }


  it should "throw an Exception erasing a partition that not exists" in {
    an [NoSuchPartitionsException] should be thrownBy externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), false, false, false)
  }

  it should "rename a partition" in {
    externalCatalog.createPartitions(databaseName, tableName, Seq(basicCatalogTablePartition), true)
    val newSpecs: Seq[CatalogTypes.TablePartitionSpec] = Seq(spec1.map{case (k,v) => k -> (v + "1")})
    externalCatalog.renamePartitions(databaseName, tableName, Seq(spec1), newSpecs)
    val renamedPartition = externalCatalog.getPartition(databaseName, tableName, newSpecs.head)
    renamedPartition should be (basicCatalogTablePartition.copy(spec = newSpecs.head))
    externalCatalog.dropPartitions(databaseName, tableName, newSpecs, true, false, false)
  }

  it should "get a partition" in {
    externalCatalog.createPartitions(databaseName, tableName, Seq(basicCatalogTablePartition), true)
    externalCatalog.getPartition(databaseName, tableName, basicCatalogTablePartition.spec) should be (basicCatalogTablePartition)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "get None as a result of getPartitionOption when partition doesn't exists" in {
    externalCatalog.getPartitionOption(databaseName, tableName, basicCatalogTablePartition.spec) should be (None)
  }

  it should "list two partitions without partialSpec" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val partitions = externalCatalog.listPartitions(databaseName, tableName, None)
    partitions should be (seqCatalogPartitions)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "list two partitions with partialSpec" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val partitions = externalCatalog.listPartitions(databaseName, tableName, Some(spec1))
    partitions should be (Seq(basicCatalogTablePartition))
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "list two partition names" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val partitionNames = externalCatalog.listPartitionNames(databaseName, tableName, Some(spec1))
    val expectedResult = Seq(spec1.map { case (name, path) =>
      escapePathName(name) + "=" + escapePathName(path)
    }.mkString("/"))
    partitionNames should be (expectedResult)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  it should "alter a partition" in {
    externalCatalog.createPartitions(databaseName, tableName, seqCatalogPartitions, true)
    val resultPartitionAltered = basicCatalogTablePartition.copy(parameters = Map("paramAltered" -> "valueAltered"))
    val seqCatalogPartitionsAltered = Seq(resultPartitionAltered, basicCatalogTablePartitionWithParameters)
    externalCatalog.alterPartitions(databaseName, tableName, seqCatalogPartitionsAltered)
    val alteredPartition = externalCatalog.getPartition(databaseName, tableName, spec1)
    alteredPartition should be (resultPartitionAltered)
    externalCatalog.dropPartitions(databaseName, tableName, seqCatalogPartitions.map(_.spec), true, false, false)
  }

  "A Zookeeper persistent catalog" should  " alter table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    externalCatalog.alterTable(catalogTableAlter)
    val alterTable=externalCatalog.getTable(db.name,tableName)
    alterTable.schema.toList should contain theSameElementsAs catalogTableAlter.schema.toList
  }

  "A Zookeeper persistent catalog" should  " not alter table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.alterTable(catalogTableAlterNotExists)
    }
  }

  "A Zookeeper persistent catalog" should  " rename table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    externalCatalog.renameTable(db.name, tableName, newTableName)
    externalCatalog.tableExists(db.name, newTableName) shouldBe true

  }

  "A Zookeeper persistent catalog" should  " not rename table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.renameTable(db.name, tableNameNotExists, newTableName)
    }
  }

  "A Zookeeper persistent catalog" should  " drop table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    externalCatalog.dropTable(db.name, tableName ,false, false)
    externalCatalog.tableExists(db.name,tableName) shouldBe false
  }

  "A Zookeeper persistent catalog" should  " not drop table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.dropTable(db.name, tableNameNotExists, false, false)
    }
  }

  it should "be able to drop a database" in {
    externalCatalog.databaseExists(databaseName) shouldBe true
    externalCatalog.dropDatabase(databaseName, false, false)
    externalCatalog.databaseExists(databaseName) shouldBe false
  }
}
