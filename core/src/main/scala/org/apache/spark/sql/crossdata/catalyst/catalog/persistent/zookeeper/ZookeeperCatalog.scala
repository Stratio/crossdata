package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper

import java.io.IOException

import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.stratio.common.utils.components.logger.impl.Slf4jLoggerComponent
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkException
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.{DatabaseAlreadyExistsException, NoSuchDatabaseException, NoSuchPartitionException, NoSuchPartitionsException, NoSuchTableException, PartitionAlreadyExistsException, PartitionsAlreadyExistException, TableAlreadyExistsException}
import org.apache.spark.sql.catalyst.catalog.CatalogTypes.TablePartitionSpec
import org.apache.spark.sql.catalyst.catalog.ExternalCatalogUtils.escapePathName
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.XDExternalCatalog
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.XDExternalCatalog.TypesafeConfigSettings
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.{CatalogEntityModel, DatabaseModel, PartitionModel, TableModel}
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.{DatabaseDAO, PartitionDAO, TableDAO}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.catalyst.util.StringUtils

import scala.util.{Failure, Try}


class ZookeeperCatalog(settings: TypesafeConfigSettings)
  extends XDExternalCatalog[TypesafeConfigSettings](settings) {

  import settings.config

  //TODO A hadoop config is needed to deal with partitions. Should we get this config here from settings?
  //  lazy val hadoopTypesafeConf: Config = settings.config.getConfig("hadoop")
  lazy val hadoopConf = new Configuration()


  trait DaoContainer[M <: CatalogEntityModel] {
    val daoComponent: GenericDAOComponent[M] with Slf4jLoggerComponent
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

  implicit lazy val partitionDAOContainer = new DaoContainer[PartitionModel] {
    val daoComponent = new PartitionDAO(config)
    val entityName: String = "Partition"
  }

  /**
    * Aimed to serve its monitor lock. It is safer to use a private instance monitor lock rathen
    * than this's. It prevents dead-locks derived from eternal invocations to `synchronize`
    */
  private object lockObject

  // Erroneous dbName when db is not set in CatalogTable.identifier
  private val unknown = "unknown"

  private def getCatalogEntity[M <: CatalogEntityModel : Manifest](id: String)(
    implicit daoContainer: DaoContainer[M]
  ): Option[M] = lockObject.synchronized {
    import daoContainer._
    val logFailure: PartialFunction[Throwable, Try[Option[M]]] = {
      case cause =>
        daoComponent.logger.warn(s"$entityName doesn't exists. Error:\n " + cause)
        Failure(cause)
    }
    daoComponent.dao.get(id).recoverWith(logFailure).toOption.flatten
  }

  private def listCatalogEntities[M <: CatalogEntityModel : Manifest](
                                                                       implicit daoContainer: DaoContainer[M]
                                                                     ): Seq[M] = lockObject.synchronized {
    daoContainer.daoComponent.dao.getAll().get
  }

  private def getDBName(databaseModel: DatabaseModel): String = databaseModel.db.name

  private def getTableName(tableModel: TableModel): String = tableModel.tableDefinition.identifier.table


  override def createDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean): Unit = lockObject.synchronized {
    import databaseDAOContainer.daoComponent.dao
    if (!ignoreIfExists && databaseExists(dbDefinition.name))
      throw new DatabaseAlreadyExistsException(dbDefinition.name)
    dao.create(dbDefinition.name, DatabaseModel(dbDefinition))
  }

  override def dropDatabase(db: String, ignoreIfNotExists: Boolean, cascade: Boolean): Unit = lockObject.synchronized {
    if (!ignoreIfNotExists && !databaseExists(db)) throw new NoSuchDatabaseException(db)
    if (cascade)
      listTables(db).foreach(table => dropTable(db, table, true, true))

    databaseDAOContainer.daoComponent.dao.delete(db)
  }

  override def alterDatabase(dbDefinition: CatalogDatabase): Unit = lockObject.synchronized {
    val dbName = dbDefinition.name
    requireDbExists(dbName)
    import databaseDAOContainer.daoComponent.dao
    dao.update(dbName, DatabaseModel(dbDefinition))
  }

  override def getDatabase(db: String): CatalogDatabase = lockObject.synchronized {
    requireDbExists(db)
    getCatalogEntity[DatabaseModel](db).get.db
  }

  override def databaseExists(db: String): Boolean = getCatalogEntity[DatabaseModel](db).isDefined

  override def listDatabases(): Seq[String] = listCatalogEntities[DatabaseModel].map(getDBName)

  override def listDatabases(pattern: String): Seq[String] =
    StringUtils.filterPattern(listDatabases(), pattern)

  override def setCurrentDatabase(db: String): Unit = {
    /* no-op */
  }

  override def createTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean): Unit = lockObject.synchronized {
    import tableAndViewDAOContainer.daoComponent.dao
    val tableName = tableDefinition.identifier.table
    val dbName = tableDefinition.identifier.database.getOrElse(throw new NoSuchTableException(unknown, tableName))
    if (!ignoreIfExists && tableExists(dbName, tableName))
      throw new TableAlreadyExistsException(dbName, tableName)
    dao.create(s"$dbName.$tableName", TableModel(tableDefinition))
  }

  override def dropTable(db: String, table: String, ignoreIfNotExists: Boolean, purge: Boolean): Unit =
    lockObject.synchronized {
      if (!ignoreIfNotExists && !tableExists(db, table)) throw new NoSuchTableException(db, table)
      tableAndViewDAOContainer.daoComponent.dao.delete(s"$db.$table")
    }

  override def renameTable(db: String, oldName: String, newName: String): Unit = lockObject.synchronized {
    requireTableExists(db, oldName)
    val oldTable = getTable(db, oldName)
    val newTable = oldTable.copy(identifier = TableIdentifier(newName, Some(db)))
    dropTable(db, oldName, false, false)
    createTable(newTable, false)
  }

  override def alterTable(tableDefinition: CatalogTable): Unit = lockObject.synchronized {
    val tableName = tableDefinition.identifier.table
    val dbName = tableDefinition.identifier.database.getOrElse(throw new NoSuchTableException(unknown, tableName))
    requireTableExists(dbName, tableName)
    import tableAndViewDAOContainer.daoComponent.dao
    dao.update(s"$dbName.$tableName", TableModel(tableDefinition))
  }

  override def getTable(db: String, table: String): CatalogTable = lockObject.synchronized {
    requireTableExists(db, table)
    getCatalogEntity[TableModel](s"$db.$table").get.tableDefinition
  }

  override def getTableOption(db: String, table: String): Option[CatalogTable] =
    getCatalogEntity[TableModel](s"$db.$table").map(t => t.tableDefinition)

  override def tableExists(db: String, table: String): Boolean =
    getCatalogEntity[TableModel](s"$db.$table").isDefined

  override def listTables(db: String): Seq[String] = lockObject.synchronized {
    listCatalogEntities[TableModel].filter { table =>
      val dbName = table.tableDefinition.identifier.database
      if (dbName.isDefined) dbName.get == db else false
    } map getTableName
  }

  override def listTables(db: String, pattern: String): Seq[String] =
    StringUtils.filterPattern(listTables(db), pattern)

  override def loadTable(db: String, table: String, loadPath: String, isOverwrite: Boolean, holdDDLTime: Boolean): Unit =
    throw new UnsupportedOperationException("loadTable is not implemented")


  override def loadPartition(db: String, table: String, loadPath: String, partition: TablePartitionSpec, isOverwrite: Boolean, holdDDLTime: Boolean, inheritTableSpecs: Boolean): Unit =
    throw new UnsupportedOperationException("loadPartition is not implemented")

  override def loadDynamicPartitions(db: String, table: String, loadPath: String, partition: TablePartitionSpec, replace: Boolean, numDP: Int, holdDDLTime: Boolean): Unit =
    throw new UnsupportedOperationException("loadDynamicPartitions is not implemented")

  private def partitionId(specs: TablePartitionSpec): String =
    specs.map{case (k, v) => k + ":" +  v}.mkString("*").replace("/", "|")
    
  override def createPartitions(db: String, table: String, parts: Seq[CatalogTablePartition], ignoreIfExists: Boolean): Unit =
    lockObject.synchronized {

      requireTableExists(db, table)
    import partitionDAOContainer.daoComponent.dao
    val partitionPathExists = dao.existsPath
    val existingParts = if(partitionPathExists.getOrElse(false)) listCatalogEntities[PartitionModel].map(_.catalogTablePartition)
    else Seq.empty[CatalogTablePartition]
      if (!ignoreIfExists) {
      val dupSpecs = for(param <- parts; existing <- existingParts; if param == existing) yield param
        if (dupSpecs.nonEmpty) {
        throw new PartitionsAlreadyExistException(db = db, table = table, specs = dupSpecs.map(_.spec))
        }
      }


      val tableMeta = getTable(db, table)
      val tablePath = new Path(tableMeta.location)
      // TODO: we should follow hive to roll back if one partition path failed to create.
      parts.foreach { p =>
        val partitionPath = p.storage.locationUri.map(new Path(_)).getOrElse {
        ExternalCatalogUtils.generatePartitionPath(p.spec, tableMeta.partitionColumnNames, tablePath)
        }

        try {
          val fs = tablePath.getFileSystem(hadoopConf)
          if (!fs.exists(partitionPath)) {
            fs.mkdirs(partitionPath)
          }
        } catch {
          case e: IOException =>
            throw new SparkException(s"Unable to create partition path $partitionPath", e)
        }

        val newPartition = p.copy(storage = p.storage.copy(locationUri = Some(partitionPath.toString)))
      dao.create(s"$db.$table.${partitionId(p.spec)}", PartitionModel(newPartition))
      }
    }

  override def dropPartitions(
                               db: String, table: String, parts: Seq[TablePartitionSpec],
                               ignoreIfNotExists: Boolean, purge: Boolean, retainData: Boolean): Unit =
    lockObject.synchronized {
      requireTableExists(db, table)

      val existingParts = listCatalogEntities[PartitionModel]
        .map(part => part.catalogTablePartition.spec -> part.catalogTablePartition)
        .toMap

      if (!ignoreIfNotExists) {
        val missingSpecs = parts.collect { case s if !existingParts.contains(s) => s }
        if (missingSpecs.nonEmpty) {
          throw new NoSuchPartitionsException(db = db, table = table, specs = missingSpecs)
        }
      }

      val shouldRemovePartitionLocation = !retainData || getTable(db, table).tableType == CatalogTableType.MANAGED


      // TODO: we should follow hive to roll back if one partition path failed to delete, and support
      // partial partition spec.
      parts.foreach { p =>
        if (existingParts.contains(p) && shouldRemovePartitionLocation) {
          val partitionPath = new Path(existingParts(p).location)
          try {
            val fs = partitionPath.getFileSystem(hadoopConf)
            fs.delete(partitionPath, true)
          } catch {
            case e: IOException =>
              throw new SparkException(s"Unable to delete partition path $partitionPath", e)
          }
        }
      partitionDAOContainer.daoComponent.dao.delete(s"$db.$table.${partitionId(p)}")

      }
    }

    private def partitionExists(db: String, table: String, spec: TablePartitionSpec): Boolean =
      lockObject.synchronized {
        requireTableExists(db, table)
    getCatalogEntity[PartitionModel](s"$db.$table.${partitionId(spec)}").isDefined
      }

    private def requirePartitionsExist(
                                        db: String,
                                        table: String,
                                        specs: Seq[TablePartitionSpec]): Unit =
      lockObject.synchronized {
        specs.foreach { s =>
          if (!partitionExists(db, table, s)) {
            throw new NoSuchPartitionException(db = db, table = table, spec = s)
          }
        }
      }

  private def requirePartitionsNotExist(
                                         db: String,
                                         table: String,
                                         specs: Seq[TablePartitionSpec]): Unit =
    lockObject.synchronized {
      specs.foreach { s =>
        if (partitionExists(db, table, s)) {
          throw new PartitionAlreadyExistsException(db = db, table = table, spec = s)
        }
      }
    }

  override def renamePartitions(
                                 db: String, table: String,
                                 specs: Seq[TablePartitionSpec],
                                 newSpecs: Seq[TablePartitionSpec]): Unit = lockObject.synchronized {

    require(specs.size == newSpecs.size, "number of old and new partition specs differ")
    requirePartitionsExist(db, table, specs)
    requirePartitionsNotExist(db, table, newSpecs)

    val tableMeta = getTable(db, table)
    val partitionColumnNames = tableMeta.partitionColumnNames
    val tablePath = new Path(tableMeta.location)
    val shouldUpdatePartitionLocation = getTable(db, table).tableType == CatalogTableType.MANAGED
    // TODO: we should follow hive to roll back if one partition path failed to rename.
    specs.zip(newSpecs).foreach { case (oldSpec, newSpec) =>
      val oldPartition = getPartition(db, table, oldSpec)
      val newPartition = if (shouldUpdatePartitionLocation) {
        val oldPartPath = new Path(oldPartition.location)
        val newPartPath = ExternalCatalogUtils.generatePartitionPath(
          newSpec, partitionColumnNames, tablePath)
        try {
          val fs = tablePath.getFileSystem(hadoopConf)
          fs.rename(oldPartPath, newPartPath)
        } catch {
          case e: IOException =>
            throw new SparkException(s"Unable to rename partition path $oldPartPath", e)
        }
        oldPartition.copy(
          spec = newSpec,
          storage = oldPartition.storage.copy(locationUri = Some(newPartPath.toString)))
      } else {
        oldPartition.copy(spec = newSpec)
      }

      import partitionDAOContainer.daoComponent.dao
      val oldPartitionIdentifier = s"$db.$table.${partitionId(oldSpec)}"
      val newPartitionIdentifier = s"$db.$table.${partitionId(newSpec)}"
      dao.delete(oldPartitionIdentifier)
      dao.create(newPartitionIdentifier, PartitionModel(newPartition))
    }
  }

  override def alterPartitions(db: String, table: String,
                               parts: Seq[CatalogTablePartition]): Unit = lockObject.synchronized {
    requirePartitionsExist(db, table, parts.map(p => p.spec))
    dropPartitions(db, table, parts.map(_.spec), false, false, false)
    createPartitions(db, table, parts, false)
  }

  override def getPartition(db: String, table: String, spec: TablePartitionSpec): CatalogTablePartition =
    lockObject.synchronized {
      requireTableExists(db, table)
    getCatalogEntity[PartitionModel](s"$db.$table.${partitionId(spec)}").map(_.catalogTablePartition)
        .getOrElse(throw new NoSuchPartitionException(db, table, spec))
    }

  override def getPartitionOption(db: String, table: String, spec: TablePartitionSpec): Option[CatalogTablePartition] =
    getCatalogEntity[PartitionModel](s"$db.$table.${partitionId(spec)}").map(_.catalogTablePartition)

  override def listPartitionNames(db: String, table: String, partialSpec: Option[TablePartitionSpec]): Seq[String] =
    lockObject.synchronized {
      val partitionColumnNames = getTable(db, table).partitionColumnNames
      listPartitions(db, table, partialSpec).map { partition =>
        partitionColumnNames.map { name =>
        escapePathName(name) + "=" + escapePathName(partition.spec(name)) //partitionColumnName always should be inside spec
        }.mkString("/")
      }.sorted
    }

  private def isPartialPartitionSpec(
                                      spec1: TablePartitionSpec,
                                      spec2: TablePartitionSpec): Boolean = spec1.forall {
    case (partitionColumn, value) =>
      spec2.get(partitionColumn).contains(value)
  }

  override def listPartitions(db: String, table: String,
                              partialSpec: Option[TablePartitionSpec]): Seq[CatalogTablePartition] =
    lockObject.synchronized {
      requireTableExists(db, table)

      partialSpec match {
        case None => listCatalogEntities[PartitionModel].map(_.catalogTablePartition)
        case Some(partial) =>
          listCatalogEntities[PartitionModel].map(_.catalogTablePartition).collect {
            case partition @ CatalogTablePartition(spec, _, _) if isPartialPartitionSpec(partial, spec) => partition
          }
      }
  }

  override def listPartitionsByFilter(db: String, table: String, predicates: Seq[Expression]): Seq[CatalogTablePartition] =
    throw new UnsupportedOperationException("listPartitionsByFilter is not implemented")

  override def createFunction(db: String, funcDefinition: CatalogFunction): Unit =
    throw new UnsupportedOperationException("createFunction is not implemented")

  override def dropFunction(db: String, funcName: String): Unit =
    throw new UnsupportedOperationException("dropFunction is not implemented")

  override def renameFunction(db: String, oldName: String, newName: String): Unit =
    throw new UnsupportedOperationException("renameFunction is not implemented")

  override def getFunction(db: String, funcName: String): CatalogFunction =
    throw new UnsupportedOperationException("getFunction is not implemented")

  override def functionExists(db: String, funcName: String): Boolean =
    throw new UnsupportedOperationException("functionExists is not implemented")

  override def listFunctions(db: String, pattern: String): Seq[String] =
    throw new UnsupportedOperationException("listFunctions is not implemented")
}
