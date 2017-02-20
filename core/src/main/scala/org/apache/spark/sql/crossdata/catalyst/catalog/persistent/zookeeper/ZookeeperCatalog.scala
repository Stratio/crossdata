package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import com.typesafe.config.Config
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.catalog.CatalogTypes.TablePartitionSpec
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.{DatabaseModel, TableModel}
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO, ViewDAO}

import scala.util.{Failure, Success}

class ZookeeperCatalog(conf: SparkConf, hadoopConf: Configuration) extends ExternalCatalog {


  // TODO we need a Catalog Config
  protected[crossdata] lazy val config: Config = ??? //XDSharedState.catalogConfig

  //TODO we need to define diferents DAOs to keep catalog information
  @transient lazy val DatabaseDAO = new DatabaseDAO(config) // TODO Should be a MapDAO??
  @transient lazy val tableDAO = new TableDAO(config)
  @transient lazy val viewDAO = new ViewDAO(config)


  //TODO is possible one method to get each model?? use getorelse instead of match
  // With this methods We avoid get all elements for the catalog
  private def getDB(dbName: String): Option[DatabaseModel] = {
    DatabaseDAO.dao.get(dbName) match {
      case Success(model) => model
      case Failure(e) => DatabaseDAO.logger.warn("Database doesn't exists. Error:\n " + e); None
    }
  }

  private def getTable(tableName: String): Option[TableModel] = {
    tableDAO.dao.get(tableName) match {
      case Success(model) => model
      case Failure(e) => tableDAO.logger.warn("Table doesn't exists. Error:\n " + e); None
    }
  }

  private def getView(viewName: String): Option[TableModel] = {
    viewDAO.dao.get(viewName) match {
      case Success(model) => model
      case Failure(e) => viewDAO.logger.warn("View doesn't exists. Error:\n " + e); None
    }
  }


  override def createDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = {
    //TODO ignoreIfExists
    DatabaseDAO.dao.create(DatabaseDAO.dao.entity, DatabaseModel(dbDefinition))
  }

  override def dropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
    //TODO ignoreifNotExists and cascade(delete all tables of this db if cascade == true)
    DatabaseDAO.dao.delete(db)

  }

  override def alterDatabase(dbDefinition: CatalogDatabase): Unit = ???

  override def getDatabase(db: String): CatalogDatabase = ???

  override def databaseExists(db: String): Boolean = ???

  override def listDatabases(): Seq[String] = ???

  override def listDatabases(pattern: String): Seq[String] = ???

  override def setCurrentDatabase(db: String): Unit = ???

  override def createTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean): Unit = ???

  override def dropTable(db: String, table: String, ignoreIfNotExists: Boolean, purge: Boolean): Unit = ???

  override def renameTable(db: String, oldName: String, newName: String): Unit = ???

  override def alterTable(tableDefinition: CatalogTable): Unit = ???

  override def getTable(db: String, table: String): CatalogTable = ???

  override def getTableOption(db: String, table: String): Option[CatalogTable] = ???

  override def tableExists(db: String, table: String): Boolean = ???

  override def listTables(db: String): Seq[String] = ???

  override def listTables(db: String, pattern: String): Seq[String] = ???

  override def loadTable(db: String, table: String, loadPath: String, isOverwrite: Boolean, holdDDLTime: Boolean): Unit = ???

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
