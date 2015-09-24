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

import java.io._
import java.util

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.{TableIdentifier, SimpleCatalystConf, CatalystConf}
import org.apache.spark.sql.catalyst.analysis.{OverrideCatalog, SimpleCatalog}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.LogicalRDD
import org.mapdb.{DB, DBMaker}

import scala.reflect.io.{Directory, Path}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * [[http://www.mapdb.org/ MapDB web site]].
 * @param conf An implementation of the [[CatalystConf]].
 * @param args Possible extra arguments.
 */
class DefaultCatalog(conf: CatalystConf = new SimpleCatalystConf(true),
                     val args: java.util.List[String] = new util.ArrayList[String]())
  extends SimpleCatalog(conf) with OverrideCatalog with XDCatalog with Logging {

  private val StringSeparator: String = "."

  private lazy val path: Option[String] = args match {
    case e if e.isEmpty => None
    case e => Some(e.get(0))
  }

  private lazy val homeDir: String = System.getProperty("user.home")

  private lazy val dir: Directory =
    Path(homeDir + "/.crossdata").createDirectory(failIfExists = false)

  private val dbLocation = path.getOrElse(dir + "/catalog")

  // File where to persist the data with MapDB.
  val dbFile: File = new File(dbLocation)
  dbFile.getParentFile.mkdirs

  private val db: DB = DBMaker.newFileDB(dbFile).closeOnJvmShutdown.make

  private val pTables: java.util.Map[String, LogicalPlan] = db.getHashMap("catalog")

  /**
   * @inheritdoc
   */
  override def open(args: Any*): Unit = {
    logInfo("XDCatalog: open")
    import collection.JavaConversions._
    pTables.map(e => (e._1.split("\\.").toSeq, e._2)).foreach(e => super.registerTable(e._1, e._2))
  }

  /**
   * @inheritdoc
   */
  override def unregisterAllTables(): Unit = {
    super.unregisterAllTables()
    logInfo("XDCatalog: unregisterAllTables")
    pTables.clear()
    db.commit()
  }

  /**
   * @inheritdoc
   */
  override def unregisterTable(tableIdentifier: Seq[String]): Unit = {
    super.unregisterTable(tableIdentifier)
    logInfo("XDCatalog: unregisterTable")
    val tableName: String = tableIdentifier.mkString(StringSeparator)
    pTables.remove(tableName)
    db.commit()
  }

  /**
   * @inheritdoc
   */
  override def registerTable(tableIdentifier: Seq[String], plan: LogicalPlan): Unit = {
    super.registerTable(tableIdentifier, plan)
    logInfo("XDCatalog: registerTable")
    if(!plan.isInstanceOf[LogicalRDD]) {
      pTables.put(tableIdentifier.mkString(StringSeparator), plan)
      db.commit()
    }
  }

  /**
   * @inheritdoc
   */
  override def refreshTable(tableIdent: TableIdentifier): Unit = {
    super.refreshTable(tableIdent)
    logInfo("XDCatalog: refreshTable")
  }

  /**
   * @inheritdoc
   */
  override def close(): Unit = {
    logInfo("XDCatalog: close")
    db.close()
  }
}
