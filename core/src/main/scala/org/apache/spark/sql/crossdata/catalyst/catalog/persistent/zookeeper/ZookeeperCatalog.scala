package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.util.Properties

import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{DatabaseAlreadyExistsException, NoSuchDatabaseException, NoSuchTableException, TableAlreadyExistsException}
import org.apache.spark.sql.catalyst.catalog.CatalogTypes.TablePartitionSpec
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.{CatalogEntityModel, DatabaseModel, TableModel}
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO}

import scala.util.{Failure, Try}


class ZookeeperCatalog(conf: SparkConf, hadoopConf: Configuration) extends ExternalCatalog {


  // TODO we need a Catalog Config
//  protected[crossdata] lazy val config: Config = ??? //XDSharedState.catalogConfig

  val defaultProperties = new Properties()
  defaultProperties.setProperty("zookeeper.connectionString" , "127.0.0.1:2181")
  protected[crossdata] lazy val config: Config = ConfigFactory.parseProperties(defaultProperties)

  trait DaoContainer[M <: CatalogEntityModel] {
    val daoComponent: GenericDAOComponent[M] with SparkLoggerComponent
    val entityName: String
  }

  implicit lazy val databaseDAOContainer = new DaoContainer[DatabaseModel] {
    val daoComponent = new DatabaseDAO(config)
    val entityName: String = "Database"
  }

  implicit lazy val tableAndViewDAOContainer = new DaoContainer[TableModel] {
    val daoComponent = new TableDAO(config)
    val entityName: String = "Table or view"
  }

  // Erroneous dbName when db is not set in CatalogTable.identifier
  private val unknown = "unknown"

  private def getCatalogEntity[M <: CatalogEntityModel : Manifest](id: String)(
    implicit daoContainer: DaoContainer[M]
  ): Option[M] = {
    import daoContainer._
    val logFailure: PartialFunction[Throwable, Try[Option[M]]] = { case cause =>
      daoComponent.logger.warn(s"$entityName doesn't exists. Error:\n " + cause)
      Failure(cause)
    }
    daoComponent.dao.get(id).recoverWith(logFailure).toOption.flatten
  }

  private def listCatalogEntities[M <: CatalogEntityModel: Manifest](
    implicit daoContainer: DaoContainer[M]
  ): Seq[M] = daoContainer.daoComponent.dao.getAll().get

  private def getDBName(databaseModel: DatabaseModel): String = databaseModel.db.name

  private def getDBNameWithPattern(databaseModel: DatabaseModel, pattern: String): Option[String] = {
    val dbName = getDBName(databaseModel)
    if(dbName.contains(pattern)) Some(dbName) else None
  }

  private def getTableName(tableModel: TableModel): String = tableModel.tableDefinition.identifier.table

  private def getTableNameWithPattern(tableModel: TableModel, pattern: String): Option[String] = {
    val tableName = getTableName(tableModel)
    if(tableName.contains(pattern)) Some(tableName) else None
  }

  override def createDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = {
    val dao = databaseDAOContainer.daoComponent.dao
    if(!ignoreIfExists && databaseExists(dbDefinition.name))
      throw new DatabaseAlreadyExistsException(dbDefinition.name)
    dao.create(dbDefinition.name, DatabaseModel(dbDefinition))
  }

  override def dropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
    if(ignoreIfNotExists && !databaseExists(db))
      throw new NoSuchDatabaseException(db)
    if(cascade)
      listTables(db).foreach(table => dropTable(db, table, true, true))

    databaseDAOContainer.daoComponent.dao.delete(db)
  }

  override def alterDatabase(dbDefinition: CatalogDatabase): Unit = {
    val dbName = dbDefinition.name
    requireDbExists(dbName)
    val dao = databaseDAOContainer.daoComponent.dao
    dao.update(dbName, DatabaseModel(dbDefinition))
  }

  override def getDatabase(db: String): CatalogDatabase = {
    requireDbExists(db)
    getCatalogEntity[DatabaseModel](db).get.db
  }

  override def databaseExists(db: String): Boolean =
    getCatalogEntity[DatabaseModel](db).isDefined

  override def listDatabases(): Seq[String] = listCatalogEntities[DatabaseModel].map(getDBName)

  override def listDatabases(pattern: String): Seq[String] =
    listCatalogEntities[DatabaseModel].flatMap(db => getDBNameWithPattern(db, pattern))

  override def setCurrentDatabase(db: String): Unit = { /* no-op */ }

  // TODO TableIdentifier.database doesn't make sense as option
  // TODO should DAO check db and table in its operations? tableEntity = db.table? force to specify db?

  override def createTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean): Unit = {
    val dao = tableAndViewDAOContainer.daoComponent.dao
    val tableName = tableDefinition.identifier.table
    val dbName = tableDefinition.identifier.database.getOrElse(throw new NoSuchTableException(unknown, tableName)) //TODO review exception
    if(!ignoreIfExists && tableExists(dbName, tableName))
      throw new TableAlreadyExistsException(dbName, tableName)
    dao.create(s"$dbName.$tableName", TableModel(tableDefinition)) //TODO id in create method should be database.table
  }

  override def dropTable(db: String, table: String, ignoreIfNotExists: Boolean, purge: Boolean): Unit = {
    if(ignoreIfNotExists && !tableExists(db, table))
      throw new NoSuchTableException(db, table)

    tableAndViewDAOContainer.daoComponent.dao.delete(s"$db.$table") // TODO and db info not used? purge??
  }

  override def renameTable(db: String, oldName: String, newName: String): Unit = {
    requireTableExists(db, oldName)
    val dao = tableAndViewDAOContainer.daoComponent.dao
    val oldTable = getTable(db, oldName)
    val newTable = oldTable.copy(identifier = TableIdentifier(newName, Some(db)))
    dao.update(s"$db.$oldName", TableModel(newTable))
  }

  override def alterTable(tableDefinition: CatalogTable): Unit = {
    val tableName = tableDefinition.identifier.table
    val dbName = tableDefinition.identifier.database.getOrElse(throw new NoSuchTableException(unknown, tableName))
    requireTableExists(dbName, tableName)
    val dao = tableAndViewDAOContainer.daoComponent.dao
    dao.update(s"$dbName.$tableName", TableModel(tableDefinition))
  }

  override def getTable(db: String, table: String): CatalogTable = {
    requireTableExists(db, table)
    getCatalogEntity[TableModel](s"$db.$table").get.tableDefinition
  }

  override def getTableOption(db: String, table: String): Option[CatalogTable] =
    getCatalogEntity[TableModel](s"$db.$table").map(t => t.tableDefinition)

  override def tableExists(db: String, table: String): Boolean = getCatalogEntity[TableModel](s"$db.$table").isDefined

  override def listTables(db: String): Seq[String] =
    listCatalogEntities[TableModel].filter { table =>
      val dbName = table.tableDefinition.identifier.database
      if(dbName.isDefined) dbName.get == db else false
    }.map(getTableName)

  override def listTables(db: String, pattern: String): Seq[String] =
    listCatalogEntities[TableModel].filter { table =>
      val dbName = table.tableDefinition.identifier.database
      if(dbName.isDefined) dbName.get == db else false
    }.flatMap(table => getTableNameWithPattern(table, pattern))

  override def loadTable(db: String, table: String, loadPath: String, isOverwrite: Boolean, holdDDLTime: Boolean): Unit =
    throw new UnsupportedOperationException("loadTable is not implemented")


  override def loadPartition(db: String, table: String, loadPath: String, partition: TablePartitionSpec, isOverwrite: Boolean, holdDDLTime: Boolean, inheritTableSpecs: Boolean): Unit = ???

  override def loadDynamicPartitions(db: String, table: String, loadPath: String, partition: TablePartitionSpec, replace: Boolean, numDP: Int, holdDDLTime: Boolean): Unit = ???

  override def createPartitions(db: String, table: String, parts: Seq[CatalogTablePartition], ignoreIfExists: Boolean): Unit = ???

  override def dropPartitions(db: String, table: String, parts: Seq[TablePartitionSpec], ignoreIfNotExists: Boolean, purge: Boolean, retainData: Boolean): Unit = ???

  override def renamePartitions(db: String, table: String, specs: Seq[TablePartitionSpec], newSpecs: Seq[TablePartitionSpec]): Unit = ???

  override def alterPartitions(db: String, table: String, parts: Seq[CatalogTablePartition]): Unit = ???

  override def getPartition(db: String, table: String, spec: TablePartitionSpec): CatalogTablePartition = ???

  override def getPartitionOption(db: String, table: String, spec: TablePartitionSpec): Option[CatalogTablePartition] = ???

  override def listPartitionNames(db: String, table: String, partialSpec: Option[TablePartitionSpec]): Seq[String] = ???

  override def listPartitions(db: String, table: String, partialSpec: Option[TablePartitionSpec]): Seq[CatalogTablePartition] = ???

  override def listPartitionsByFilter(db: String, table: String, predicates: Seq[Expression]): Seq[CatalogTablePartition] = ???

  override def createFunction(db: String, funcDefinition: CatalogFunction): Unit = ???

  override def dropFunction(db: String, funcName: String): Unit = ???

  override def renameFunction(db: String, oldName: String, newName: String): Unit = ???

  override def getFunction(db: String, funcName: String): CatalogFunction = ???

  override def functionExists(db: String, funcName: String): Boolean = ???

  override def listFunctions(db: String, pattern: String): Seq[String] = ???
}
