package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.Config
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.analysis.DatabaseAlreadyExistsException
import org.apache.spark.sql.catalyst.catalog.CatalogTypes.TablePartitionSpec
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.{CatalogEntityModel, DatabaseModel, TableModel}
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, TableDAO}

import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe.TypeTag

class ZookeeperCatalog(conf: SparkConf, hadoopConf: Configuration) extends ExternalCatalog {


  // TODO we need a Catalog Config
  protected[crossdata] lazy val config: Config = ??? //XDSharedState.catalogConfig

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

  private def listCatalogEntities[M <: CatalogEntityModel]: Seq[String] = {
    //TODO Add genetic key listing method and use it
    ???
  }

  override def createDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = {
    val dao = databaseDAOContainer.daoComponent.dao
    if(!ignoreIfExists && databaseExists(dbDefinition.name))
      throw new DatabaseAlreadyExistsException(dbDefinition.name)
    dao.create(dbDefinition.name, DatabaseModel(dbDefinition))
  }

  override def dropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = {
    //TODO ignoreifNotExists and cascade(delete all tables of this db if cascade == true)
    tableAndViewDAOContainer.daoComponent.dao.delete(db)
  }

  override def alterDatabase(dbDefinition: CatalogDatabase): Unit = ???

  override def getDatabase(db: String): CatalogDatabase = {
    requireDbExists(db)
    getCatalogEntity[DatabaseModel](db).get.db
  }

  override def databaseExists(db: String): Boolean =
    getCatalogEntity[DatabaseModel](db).isDefined

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
