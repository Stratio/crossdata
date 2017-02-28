package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{NoSuchTableException, TableAlreadyExistsException}
import org.apache.spark.sql.catalyst.catalog.{CatalogStorageFormat, CatalogTable, CatalogTableType}
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
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

  val databaseName = "customDatabase"

  val db = CatalogDatabase(
    databaseName,
    "some description",
    "/",
    Map("a" -> "b")
  )

  val version = "1.11.0"
  val tableName ="table"
  val tableNameNotExists = "tableNotExists"
  val newTableName = "newTableName"
  val tableIdentifier: TableIdentifier= new TableIdentifier(tableName,Some(db.name))
  val tableIdentifierNotExists: TableIdentifier= new TableIdentifier(tableNameNotExists)
  val newTableIdentifier: TableIdentifier= new TableIdentifier(newTableName)
  val tableType: CatalogTableType=CatalogTableType.EXTERNAL
  val storage: CatalogStorageFormat=new CatalogStorageFormat(None,None,None,None,false, Map.empty)
  val Field1Name = "column1"
  val Field2Name = "column2"
  val FieldWitStrangeChars = "1+x"
  val SubField1Name = "subcolumn1"
  val SubField2Name = "subcolumn2"
  val Field1 = StructField(Field1Name, StringType, nullable = true)
  val Field2 = StructField(Field2Name, StringType, nullable = true)
  val SubField = StructField(SubField1Name, StringType, nullable = true)
  val SubField2 = StructField(SubField2Name, StringType, nullable = true)
  val arrayField = StructField(SubField2Name, ArrayType(StringType), nullable = true)
  val Fields = Seq[StructField](Field1, Field2)
  val SubFields = Seq(SubField, SubField2)
  val schema: StructType = StructType(Fields)
  //val schema:StructType=null
  val schemaAlter: StructType = new StructType()
  val catalogTable:CatalogTable=new CatalogTable(tableIdentifier, tableType, storage, schema)
  val catalogTableAlter:CatalogTable = new CatalogTable(tableIdentifier, tableType, storage, schemaAlter)
  val catalogTableAlterNotExists:CatalogTable = new CatalogTable(tableIdentifierNotExists, tableType, storage, schemaAlter)



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

  "A Zookeeper persistent catalog" should  " rename table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    externalCatalog.renameTable(db.name, tableName, newTableName)
    externalCatalog.tableExists(db.name,newTableName) shouldBe true

  }

  "A Zookeeper persistent catalog" should  " not rename table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.renameTable(db.name, tableNameNotExists, newTableName)
    }
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

  "A Zookeeper persistent catalog" should  " get a table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    val table=externalCatalog.getTable(tableName, db.name)
    table.identifier.database shouldBe db.name
    table.identifier.table shouldBe tableName
    table.schema.toList should contain theSameElementsAs schema.toList
  }

  "A Zookeeper persistent catalog" should  " get table option" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db.name,tableName) shouldBe true
    val table=externalCatalog.getTableOption(tableName,db.name).get
    table should not be None
    table.identifier.database shouldBe db.name
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

  "A Zookeeper persistent catalog" should  " loadTables" in {
    externalCatalog.loadTable(db.name,tableName,"",false,false)
  }



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
