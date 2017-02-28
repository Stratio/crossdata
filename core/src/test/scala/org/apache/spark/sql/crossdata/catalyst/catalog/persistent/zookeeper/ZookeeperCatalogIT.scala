package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.File

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{NoSuchTableException, TableAlreadyExistsException}
import org.apache.spark.sql.catalyst.catalog.{CatalogStorageFormat, CatalogTable, CatalogTableType}
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO}
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner



//TODO: Extract generic test cases as in Crossdata for spark 1.6.x 's `GenericCatalogTests`
//TODO: Improve and user SharedXDSession
@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends BaseXDTest{ //with BeforeAndAfterAll/* extends SharedXDSession*/ {


  val version = "1.11.0"
  val db =  "db"
  val tableName ="table"
  val tableNameNotExists = "tableNotExists"
  val newTableName = "newTableName"
  val tableIdentifier: TableIdentifier= new TableIdentifier(tableName,Some(db))
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
  val schemaAlter: StructType = new StructType()
  val catalogTable:CatalogTable=new CatalogTable(tableIdentifier, tableType, storage, schema)
  val catalogTableAlter:CatalogTable = new CatalogTable(tableIdentifier, tableType, storage, schemaAlter)
  val catalogTableAlterNotExists:CatalogTable = new CatalogTable(tableIdentifierNotExists, tableType, storage, schemaAlter)

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

  //protected def cleanPersistence(): Unit = Seq(DAO.dbs.dao, DAO.tables.dao) foreach (_.deleteAll)

  //override protected def beforeAll(): Unit = cleanPersistence()

  import xdSession.sharedState.externalCatalog


  "A Zookeeper persistent catalog" should "persist tables" in {
    //xdSession.sharedState.externalCatalog.tableExists(db,tableName) shouldBe false
    //xdSession.sharedState.externalCatalog.createTable(catalogTable,false)
    //xdSession.sharedState.externalCatalog.tableExists(db,tableName) shouldBe true
    externalCatalog.tableExists(db,tableName) shouldBe false
    externalCatalog.createTable(catalogTable,false)
    externalCatalog.tableExists(db,tableName) shouldBe true
  }


  "A Zookeeper persistent catalog" should  " not persist tables that exist" in {
    an[TableAlreadyExistsException] shouldBe thrownBy{
      externalCatalog.createTable(catalogTable,false)
    }
  }

  "A Zookeeper persistent catalog" should  " drop table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
    externalCatalog.dropTable(db, tableName ,false, false)
    externalCatalog.tableExists(db,tableName) shouldBe false
  }

  "A Zookeeper persistent catalog" should  " not drop table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.dropTable(db, tableNameNotExists, false, false)
    }
  }

  "A Zookeeper persistent catalog" should  " rename table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
    externalCatalog.renameTable(db, tableName, newTableName)
    externalCatalog.tableExists(db,newTableName) shouldBe true

  }

  "A Zookeeper persistent catalog" should  " not rename table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.renameTable(db, tableNameNotExists, newTableName)
    }
  }

  "A Zookeeper persistent catalog" should  " alter table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
    externalCatalog.alterTable(catalogTableAlter)
    val alterTable=externalCatalog.getTable(db,tableName)
    alterTable.schema.toList should contain theSameElementsAs catalogTableAlter.schema.toList
  }

  "A Zookeeper persistent catalog" should  " not alter table that not exists" in {
    an [NoSuchTableException] shouldBe thrownBy {
      externalCatalog.alterTable(catalogTableAlterNotExists)
    }
  }

  "A Zookeeper persistent catalog" should  " get a table" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
    val table=externalCatalog.getTable(tableName, db)
    table.identifier.database shouldBe db
    table.identifier.table shouldBe tableName
    table.schema.toList should contain theSameElementsAs schema.toList
  }

  "A Zookeeper persistent catalog" should  " get table option" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
    val table=externalCatalog.getTableOption(tableName,db).get
    table should not be None
    table.identifier.database shouldBe db
    table.identifier.table shouldBe tableName
    table.schema.toList should contain theSameElementsAs schema.toList
  }

  "A Zookeeper persistent catalog" should  " know if table exists" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
  }

  "A Zookeeper persistent catalog" should  " listTables" in {
    externalCatalog.createTable(catalogTable,true)
    externalCatalog.tableExists(db,tableName) shouldBe true
    val listTables=externalCatalog.listTables(db)
    listTables.size shouldBe 1
    listTables should contain (tableName)

  }

  "A Zookeeper persistent catalog" should  " loadTables" in {
    externalCatalog.loadTable(db,tableName,"",false,false)
  }



  //override protected def afterAll(): Unit = cleanPersistence()

}
