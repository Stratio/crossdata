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
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.types.StructType

object MySQLCatalog{
  val Driver = "com.mysql.jdbc.Driver"
  val StringSeparator: String = "."
  case class CrossdataTable(tableName: String, database: Option[String] = None,  userSpecifiedSchema: Option[StructType], provider: String, opts: Map[String, String] = Map.empty[String, String])
}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * MySQL.
 * @param conf An implementation of the [[CatalystConf]].
 */
class MySQLCatalog(conf: CatalystConf = new SimpleCatalystConf(true))
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


//
//  /**
//   * @inheritdoc
//   */
//  override def unregisterAllTables(): Unit = {
//    super.unregisterAllTables()
//    logInfo("XDCatalog: unregisterAllTables")
//    pTables.clear()
//    db.commit()
//  }
//
//  /**
//   * @inheritdoc
//   */
//  override def unregisterTable(tableIdentifier: Seq[String]): Unit = {
//    super.unregisterTable(tableIdentifier)
//    logInfo("XDCatalog: unregisterTable")
//    val tableName: String = tableIdentifier.mkString(StringSeparator)
//    pTables.remove(tableName)
//    db.commit()
//  }
//
//  /**
//   * @inheritdoc
//   */
//  override def registerTable(tableIdentifier: Seq[String], plan: LogicalPlan): Unit = {
//    super.registerTable(tableIdentifier, plan)
//    logInfo("XDCatalog: registerTable")
//    if(!plan.isInstanceOf[LogicalRDD]) {
//      pTables.put(tableIdentifier.mkString(StringSeparator), plan)
//      db.commit()
//    }
//  }
//
//  /**
//   * @inheritdoc
//   */
//  override def refreshTable(databaseName: String, tableName: String): Unit = {
//    super.refreshTable(databaseName, tableName)
//    logInfo("XDCatalog: refreshTable")
//  }
//




  override def tableExists(tableIdentifier: Seq[String]): Boolean = {
    val existsInCache = super.tableExists(tableIdentifier)
    if (existsInCache){
      true
    } else{
      val table = lookUpTable(tableIdentifier) match {
        case Some(crossdataTable) =>
          //TODO provider => new instance => createRelation( table, options....); registerTempTable (tableIdentifier, crossdataTable)
          true
        case None =>
          false
      }
    }
  }

  private def lookUpTable(tableIdentifier: Seq[String]): Option[CrossdataTable] = {
    ???
  }



}
