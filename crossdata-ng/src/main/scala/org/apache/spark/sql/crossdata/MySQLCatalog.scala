/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.spark.sql.crossdata

import java.sql.{Connection, DriverManager}

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.types.StructType

object MySQLCatalog{
  val Driver = "com.mysql.jdbc.Driver"
  val StringSeparator: String = "."
  val CrossdataVersion = "1.0.0-SNAPSHOT"
  case class CrossdataTable(tableName: String, database: Option[String] = None,  userSpecifiedSchema: Option[StructType], provider: String, crossdataVersion: String, opts: Map[String, String] = Map.empty[String, String])
}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * MySQL.
 * @param conf An implementation of the [[CatalystConf]].
 */
class MySQLCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true))
  extends XDCatalog(conf) with Logging  {

  import MySQLCatalog._

  // TODO Refactor close method, singleton??
  // TODO  in config
  val URL = "jdbc:mysql://localhost:3306/crossdata"
  val User = "root"
  val Password = "stratio"


  lazy val connection: Connection = {
      Class.forName(Driver)
      DriverManager.getConnection(URL, User, Password)
  }


  /**
   * Persist in XD Catalog
   */
  override def persistTable(tableIdentifier: Seq[String], crossdataTable: CrossdataTable):
  Unit = {
    //super.registerTable(tableName, plan)
    logInfo("XDCatalog: Persist Table")

//    val tableSchema = write(userSpecifiedSchema)
//    val tableOptions = write(opts)
//    val statement = connection.createStatement
//    statement.executeQuery(
//      s"INSERT INTO crossdataTables (tableName, schema, options) VALUES ($tableName,$tableSchema,$tableOptions)")

  }


  /**
   * Drop all tables of catalog
   */
  override def dropAllTables(): Unit = {
    super.unregisterAllTables()
    logInfo("XDCatalog: unregisterAllTables")
    val statement = connection.createStatement
    statement.executeQuery(s"DROP TABLE crossdataTables")
  }

  /**
   * Drop table from XD catalog
   */
  override def dropTable(tableIdentifier: Seq[String]): Unit = {
    super.unregisterTable(tableIdentifier)
    logInfo("XDCatalog: unregisterTable")
    val tableName: String = tableIdentifier.mkString(StringSeparator)
    val statement = connection.createStatement
    statement.executeQuery(s"DELETE FROM crossdataTable WHERE tableName='$tableName'")
  }




  override def tableExists(tableIdentifier: Seq[String]): Boolean = {
    val existsInCache = super.tableExists(tableIdentifier)
    if (existsInCache){
      true
    } else{
      lookUpTable(tableIdentifier) match {
        case Some(crossdataTable) =>
          //TODO provider => new instance => createRelation( table, options....); registerTempTable (tableIdentifier, crossdataTable)
          true
        case None =>
          false
      }
    }
  }

  override def lookupRelation(tableIdentifier: Seq[String], alias: Option[String]): LogicalPlan = {

    lookupRelationCache(tableIdentifier, alias).getOrElse{
      lookUpTable(tableIdentifier) match {
        case Some(crossdataTable) =>
          //TODO provider => new instance => createRelation( table, options....); registerTempTable (tableIdentifier, crossdataTable)
          val table = ???
          val logicalPlan: LogicalPlan = ???
          val tableIdent: Seq[String] = ???
          val tableFullName = ???
          val tableWithQualifiers = Subquery(tableIdent.last, table)
          // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
          // properly qualified with this alias.
          alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers)

        case None =>
          val tableFullName = ???
          sys.error(s"Table Not Found: $tableFullName")
      }

    }
  }


  private def lookUpTable(tableIdentifier: Seq[String]): Option[CrossdataTable] = {
    ???
  }

  // ** TODO ()
  // Logical plan
  //val resolved = ResolvedDataSource.lookupDataSource(provider).newInstance()
  // if schema is provided and resolved implemnts SchemaRelationProvider=> val providerRelation = resolved.asInstanceOf[SchemaRelationProvider] //As relation provider
  // if there is no schema => val providerRelation = resolved.asInstanceOf[RelationProvider] //As relation provider
  //LogicalRelation(providerRelation.createRelation(sqlContext,inventoryRelation.generateConnectorOpts(t, opts)))


}
