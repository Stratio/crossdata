/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.catalyst.execution

import java.sql.{Date, Timestamp}

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.{TableInventory, TableManipulation}
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Project, Subquery}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog._
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._
import org.apache.spark.sql.execution.RunnableCommand
import org.apache.spark.sql.execution.datasources._
import org.apache.spark.sql.sources.{BaseRelation, HadoopFsRelation, InsertableRelation}
import org.apache.spark.sql.types.{StructType, _}

import scala.language.implicitConversions
import scala.reflect.io.File
import scala.util.{Failure, Success, Try}

object DDLUtils {

  type RowValues = Seq[Any]

  implicit def tableIdentifierToSeq(tableIdentifier: TableIdentifier): Seq[String] =
    tableIdentifier.database.toSeq :+ tableIdentifier.table

  /**
    * Convert from String, Map or Seq to Scala Types
    * @param value the String, Map or Seq
    * @param sparkDataType the original SparkDatatype
    * @return The value converted from SparkDatatype to Scala
    */
  def convertSparkDatatypeToScala(value: Any, sparkDataType: DataType): Try[Any] = {

    (value, sparkDataType) match {
      case (value: String, _: ByteType) => Try(value.toByte)
      case (value: String, _: ShortType) => Try(value.toShort)
      case (value: String, _: IntegerType) => Try(value.toInt)
      case (value: String, _: LongType) => Try(value.toLong)
      case (value: String, _: FloatType) => Try(value.toFloat)
      case (value: String, _: DoubleType) => Try(value.toDouble)
      case (value: String, _: DecimalType) => Try(BigDecimal(value))
      case (value: String, _: StringType) => Try(value)
      case (value: String, _: BooleanType) => Try(value.toBoolean)
      case (value: String, _: DateType) => Try(Date.valueOf(value))
      case (value: String, _: TimestampType) => Try(Timestamp.valueOf(value))

      case (seq: Seq[_], ArrayType(elementType, withNulls)) =>
        seqOfTryToTryOfSeq(seq map { seqValue => convertSparkDatatypeToScala(seqValue, elementType) })

      case (invalidSeq, ArrayType(elementType, withNulls)) =>
        Failure(new RuntimeException("Invalid array passed as argument:" + invalidSeq.toString))

      case (mapParsed: Map[_, _], MapType(keyType, valueType, withNulls)) =>
        Try(
          mapParsed map {
            case (key, value) => (convertSparkDatatypeToScala(key, keyType).get, convertSparkDatatypeToScala(value, valueType).get)
          }
        )

      case (invalidMap, MapType(keyType, valueType, withNulls)) =>
        Failure(new RuntimeException("Invalid map passed as argument:" + invalidMap.toString))

      case unparsed => Failure(new RuntimeException("Impossible to parse value as Spark DataType provided:" + unparsed.toString))
    }
  }

  def extractSchema(schemaFromUser: Seq[String], tableSchema: StructType): StructType = {
    val fields = schemaFromUser map { column =>
      tableSchema(tableSchema.fieldIndex(column))
    }
    StructType(fields)
  }

  private def seqOfTryToTryOfSeq[T](tries: Seq[Try[T]]): Try[Seq[T]] = {
    Try(
      tries map (_.get)
    )
  }

}

private[crossdata] case class ImportTablesUsingWithOptions(datasource: String, opts: Map[String, String])
  extends LogicalPlan with RunnableCommand with SparkLoggerComponent {

  // The result of IMPORT TABLE has only tableIdentifier so far.
  override val output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("tableIdentifier", ArrayType(StringType), false), StructField("ignored", BooleanType, false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    def tableExists(tableId: TableIdentifier): Boolean = {
      val doExist = sqlContext.catalog.tableExists(tableId)
      if (doExist) log.warn(s"IMPORT TABLE omitted already registered table: ${tableId.unquotedString}")
      doExist
    }

    // Get a reference to the inventory relation.
    val resolved = ResolvedDataSource.lookupDataSource(datasource).newInstance()
    val inventoryRelation = resolved.asInstanceOf[TableInventory]

    // Obtains the list of tables and persist it (if persistence implemented)
    val tables = inventoryRelation.listTables(sqlContext, opts)

    for {
      table: TableInventory.Table <- tables
      tableId = TableIdentifier(table.tableName, table.database)
      if inventoryRelation.exclusionFilter(table)
    } yield {
      val ignoreTable = tableExists(tableId)
      if (!ignoreTable) {
        logInfo(s"Importing table ${tableId.unquotedString}")
        val optionsWithTable = inventoryRelation.generateConnectorOpts(table, opts)
        val identifier = TableIdentifier(table.tableName, table.database).normalize(sqlContext.conf)
        val crossdataTable = CrossdataTable(identifier, table.schema, datasource, Array.empty, optionsWithTable)
        import org.apache.spark.sql.crossdata.util.CreateRelationUtil._
        sqlContext.catalog.persistTable(crossdataTable, createLogicalRelation(sqlContext, crossdataTable))
      }
      val tableSeq = DDLUtils.tableIdentifierToSeq(tableId)
      Row(tableSeq, ignoreTable)
    }

  }
}

private[crossdata] case class DropTable(tableIdentifier: TableIdentifier) extends RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.catalog.dropRelation(tableIdentifier)

    Seq.empty
  }

}

private[crossdata] case class DropExternalTable(tableIdentifier: TableIdentifier) extends RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val crossadataTable = sqlContext.catalog.tableMetadata(tableIdentifier) getOrElse (sys.error("Error dropping external table. Table doesn't exist in the catalog"))

    val provider = crossadataTable.datasource
    val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()

    resolved match {

      case _ if !sqlContext.catalog.tableExists(tableIdentifier) =>
        throw new AnalysisException(s"Table ${tableIdentifier.unquotedString} does not exist")

      case tableManipulation: TableManipulation =>

        tableManipulation.dropExternalTable(sqlContext, crossadataTable.opts) map { result =>
          sqlContext.catalog.dropTable(tableIdentifier)
          Seq.empty
        } getOrElse (sys.error("Impossible to drop external table"))

      case _ =>
        sys.error("The Datasource does not support DROP EXTERNAL TABLE command")
    }

    Seq.empty
  }

}

private[crossdata] case object DropAllTables extends RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.dropAllTables()
    Seq.empty
  }

}

private[crossdata] case class InsertIntoTable(tableIdentifier: TableIdentifier, parsedRows: Seq[DDLUtils.RowValues], schemaFromUser: Option[Seq[String]] = None)
  extends RunnableCommand {

  override def output: Seq[Attribute] = {
    val schema = StructType(
      Seq(StructField("Number of insertions", IntegerType, nullable = false))
    )
    schema.toAttributes
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    sqlContext.catalog.lookupRelation(tableIdentifier) match {

      case Subquery(_, LogicalRelation(relation: BaseRelation, _)) =>

        val schema = schemaFromUser map (DDLUtils.extractSchema(_, relation.schema)) getOrElse relation.schema

        relation match {
          case insertableRelation: InsertableRelation =>
            val dataframe = convertRows(sqlContext, parsedRows, schema)

            sqlContext.catalog.indexMetadataByTableIdentifier(tableIdentifier).foreach{ idxIdentifier =>
              indexData(sqlContext, idxIdentifier, schema)
            }

            insertableRelation.insert(dataframe, overwrite = false)

          case hadoopFsRelation: HadoopFsRelation =>
            sys.error("Operation not supported")
          //TODO: Available from Spark 2.0
          /*val dataframe = convertRows(sqlContext, parsedRows, schema)
            sqlContext.executePlan(
            InsertIntoHadoopFsRelation(
              hadoopFsRelation,
              dataframe.logicalPlan,
              mode = SaveMode.Append)).toRdd*/

          case _ =>
            sys.error("The Datasource does not support INSERT INTO table VALUES command")
        }


      case _ =>
        sys.error("Table not found. Are you trying to insert values into a view/temporary table?")
    }

    Row(parsedRows.length) :: Nil
  }

  /**
    * Index data into related globalIndex
    *
    * @param sqlContext
    */
  private def indexData(sqlContext: SQLContext, crossdataIndex: CrossdataIndex, tableSchema: StructType): Unit = {

    val columnsToIndex: Seq[String] = crossdataIndex.pk +: crossdataIndex.indexedCols.filter(tableSchema.getFieldIndex(_).isDefined)

    val filteredParsedRows = parsedRows.map { row =>
      columnsToIndex map { idxCol =>
        row(tableSchema.getFieldIndex(idxCol).get)
      }
    }

    InsertIntoTable(crossdataIndex.indexIdentifier.asTableIdentifierNormalized.toTableIdentifier, filteredParsedRows, Some(columnsToIndex)).run(sqlContext)
  }

  private def convertRows(sqlContext: SQLContext, rows: Seq[DDLUtils.RowValues], tableSchema: StructType): DataFrame = {

    val parsedRowsConverted: Seq[Row] = parsedRows map { values =>

      if (tableSchema.fields.length != values.length) sys.error("Invalid length of parameters")

      val valuesConverted = tableSchema.fields zip values map {
        case (schemaCol, value) =>
          DDLUtils.convertSparkDatatypeToScala(value, schemaCol.dataType) match {
            case Success(converted) => converted
            case Failure(exception) => throw exception
          }
      }
      Row.fromSeq(valuesConverted)
    }

    val dataframe = sqlContext.asInstanceOf[XDContext].createDataFrame(parsedRowsConverted, tableSchema)
    dataframe
  }
}

private[crossdata] object InsertIntoTable {
  def apply(tableIdentifier: TableIdentifier, parsedRows: Seq[DDLUtils.RowValues]) = new InsertIntoTable(tableIdentifier, parsedRows)
}

object CreateTempView {
  def apply(
             viewIdentifier: ViewIdentifier,
             queryPlan: LogicalPlan,
             sql: String
           ): CreateTempView = new CreateTempView(viewIdentifier, queryPlan, Some(sql))
}

private[crossdata] case class CreateTempView(
                                              viewIdentifier: ViewIdentifier,
                                              queryPlan: LogicalPlan,
                                              sql: Option[String]
                                            )
  extends RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.registerView(viewIdentifier, queryPlan, sql)
    Seq.empty
  }

}

private[crossdata] case class CreateView(viewIdentifier: ViewIdentifier, queryPlan: LogicalPlan, sql: String)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.persistView(viewIdentifier, queryPlan, sql)
    Seq.empty
  }

}


private[crossdata] case class DropView(viewIdentifier: ViewIdentifier)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    sqlContext.catalog.dropView(viewIdentifier)
    Seq.empty
  }
}

private[crossdata] case class AddJar(jarPath: String)
  extends LogicalPlan with RunnableCommand {

  override def run(sqlContext: SQLContext): Seq[Row] = {
    if (jarPath.toLowerCase.startsWith("hdfs://") || File(jarPath).exists) {
      sqlContext.addJar(jarPath)
      Seq.empty
    } else {
      sys.error("File doesn't exist or is not a hdfs file")
    }
  }
}


object CreateGlobalIndex {
  val DefaultDatabaseName = "gidx"
}


private[crossdata] case class CreateGlobalIndex(
                                                 index: TableIdentifier,
                                                 tableIdent: TableIdentifier,
                                                 cols: Seq[String],
                                                 pk: String,
                                                 provider: Option[String],
                                                 options: Map[String, String]
                                               ) extends LogicalPlan with RunnableCommand {

  import CreateGlobalIndex._

  private def createElasticIndex(sqlContext: SQLContext): Try[CrossdataIndex] =
    Try {
      val indexProvider = provider getOrElse "com.stratio.crossdata.connector.elasticsearch"

      val finalIndex = IndexIdentifier(index.table, index.database getOrElse DefaultDatabaseName).normalize(sqlContext.conf)

      val colsWithoutSchema = Seq(pk) ++ cols

      val elasticSchema = sqlContext.catalog.lookupRelation(tableIdent) match {

        case Subquery(_, LogicalRelation(relation: BaseRelation, _)) =>
          DDLUtils.extractSchema(colsWithoutSchema, relation.schema)

        case _ =>
          sys.error("Not found the table you want to index")
      }

      //TODO: Change index name, for allowing multiple index ???
      CreateExternalTable(TableIdentifier(finalIndex.indexType, Option(finalIndex.indexName)), elasticSchema, indexProvider, options).run(sqlContext)

      CrossdataIndex(tableIdent.normalize(sqlContext.conf), finalIndex, cols, pk, indexProvider, options)

    }


  private def saveIndexMetadata(sqlContext: SQLContext, crossdataIndex: CrossdataIndex) = {

    sqlContext.catalog.persistIndex(crossdataIndex)
  }

  override def run(sqlContext: SQLContext): Seq[Row] = {

    val crossdataIndex = createElasticIndex(sqlContext).get
    saveIndexMetadata(sqlContext, crossdataIndex)
    Seq.empty
    //TODO: Recover if something bad happens
  }
}

  private[crossdata] case class AddApp(jarPath: String, className: String, aliasName: Option[String] = None)
    extends LogicalPlan with RunnableCommand {

    override def run(sqlContext: SQLContext): Seq[Row] = {
      if (File(jarPath).exists) {
        sqlContext.addJar(jarPath)
      } else {
        sys.error("File doesn't exist")
      }
      sqlContext.asInstanceOf[XDContext].addApp(path = jarPath, clss = className, alias = aliasName.getOrElse(jarPath.split("/").last.split('.').head))
      Seq.empty
    }
  }

  private[crossdata] case class ExecuteApp(appName: String, arguments: Seq[String], options: Option[Map[String, String]])
    extends LogicalPlan with RunnableCommand {

    override val output: Seq[Attribute] = {
      val schema = StructType(Seq(
        StructField("infoMessage", StringType, nullable = true)
      ))
      schema.toAttributes
    }

    override def run(sqlContext: SQLContext): Seq[Row] = {
      sqlContext.asInstanceOf[XDContext].executeApp(appName, arguments, options)
    }

  }

  case class CreateExternalTable(
                                  tableIdent: TableIdentifier,
                                  userSpecifiedSchema: StructType,
                                  provider: String,
                                  options: Map[String, String],
                                  allowExisting: Boolean = false) extends LogicalPlan with RunnableCommand {


    override def run(sqlContext: SQLContext): Seq[Row] = {

      val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()

      resolved match {

        case _ if sqlContext.catalog.tableExists(tableIdent) =>
          throw new AnalysisException(s"Table ${tableIdent.unquotedString} already exists")

        case tableManipulation: TableManipulation =>

          val tableInventory = tableManipulation.createExternalTable(sqlContext, tableIdent.table, tableIdent.database, userSpecifiedSchema, options)
          tableInventory.map { tableInventory =>
            val optionsWithTable = tableManipulation.generateConnectorOpts(tableInventory, options)
            val identifier = TableIdentifier(tableIdent.table, tableIdent.database).normalize(sqlContext.conf)
            val crossdataTable = CrossdataTable(identifier, Option(userSpecifiedSchema), provider, Array.empty, optionsWithTable)
            import org.apache.spark.sql.crossdata.util.CreateRelationUtil._
            sqlContext.catalog.persistTable(crossdataTable, createLogicalRelation(sqlContext, crossdataTable))
          } getOrElse (throw new RuntimeException(s"External table can't be created"))

        case _ =>
          sys.error("The Datasource does not support CREATE EXTERNAL TABLE command")
      }

      Seq.empty

    }

  }



