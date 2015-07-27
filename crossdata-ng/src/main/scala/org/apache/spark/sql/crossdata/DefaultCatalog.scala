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
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.execution.LogicalRDD
import org.mapdb.{DB, DBMaker}

import scala.reflect.io.{Directory, Path}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * [[http://www.mapdb.org/ MapDB web site]].
 * @param xdContext An optional [[XDContext]]].
 * @param conf An implementation of the [[CatalystConf]].
 * @param args Possible extra arguments.
 */
class DefaultCatalog(val xdContext: Option[XDContext] = None,
                     val conf: CatalystConf = new SimpleCatalystConf(true),
                     val args: java.util.List[String] = new util.ArrayList[String]())
  extends XDCatalog with Logging {

  private lazy val path: Option[String] = args match {
    case e if e.isEmpty => None
    case e => Some(e.get(0))
  }

  private lazy val homeDir: String = System.getProperty("user.home")

  private lazy val dir: Directory =
    Path(homeDir + "/.crossdata").createDirectory(failIfExists = false)

  private val dbLocation = path match {
    case Some(v) => (v)
    case None => (dir + "/catalog")
  }

  val dbFile: File = new File(dbLocation)
  dbFile.getParentFile.mkdirs

  private val db: DB = DBMaker.newFileDB(dbFile).closeOnJvmShutdown.make

  private val tables: java.util.Map[String, LogicalPlan] = db.getHashMap("catalog")

  private val logicalRDDs: java.util.Map[String, Tuple2[Seq[Attribute], RDD[Row]]] =
    db.getHashMap("logicalRDDs")

  /**
   * @inheritdoc
   */
  override def open(args: Any*): Unit = {
    logInfo("XDCatalog: open")
  }

  /**
   * @inheritdoc
   */
  override def tableExists(tableIdentifier: Seq[String]): Boolean = {
    logInfo("XDCatalog: tableExists")
    val tableName: String = tableIdentifier.mkString(".")
    (tables.containsKey(tableName) || logicalRDDs.containsKey(tableName))
  }

  /**
   * @inheritdoc
   */
  override def unregisterAllTables(): Unit = {
    logInfo("XDCatalog: unregisterAllTables")
    tables.clear
    logicalRDDs.clear
    db.commit
  }

  /**
   * @inheritdoc
   */
  override def unregisterTable(tableIdentifier: Seq[String]): Unit = {
    logInfo("XDCatalog: unregisterTable")
    val tableName: String = tableIdentifier.mkString(".")
    tables.remove(tableName)
    logicalRDDs.remove(tableName)
    db.commit
  }

  /**
   * @inheritdoc
   */
  override def lookupRelation(tableIdentifier: Seq[String], alias: Option[String]): LogicalPlan = {
    logInfo("XDCatalog: lookupRelation")
    val tableName: String = alias match {
      case Some(a) => a
      case None => tableIdentifier.mkString(".")
    }
    if(tables.containsKey(tableName)){
      tables.get(tableName)
    } else {
      new LogicalRDD(logicalRDDs.get(tableName)._1, logicalRDDs.get(tableName)._2)(xdContext.get)
    }
  }

  /**
   * @inheritdoc
   */
  override def registerTable(tableIdentifier: Seq[String], plan: LogicalPlan): Unit = {
    logInfo("XDCatalog: registerTable")
    if(plan.isInstanceOf[LogicalRDD]){
      logicalRDDs.put(tableIdentifier.mkString("."),
        new Tuple2(plan.asInstanceOf[LogicalRDD].output, plan.asInstanceOf[LogicalRDD].rdd))
    } else {
      tables.put(tableIdentifier.mkString("."), plan)
    }
    db.commit
  }

  /**
   * @inheritdoc
   */
  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    logInfo("XDCatalog: getTables")
    import collection.JavaConversions._
    val allTables: Seq[(String, Boolean)] = tables.map {
      case (name, _) => (name, false)
    }.toSeq
    allTables.addAll(0, logicalRDDs.map {
      case (name, _) => (name, false)
    }.toSeq)
    allTables
  }

  /**
   * @inheritdoc
   */
  override def refreshTable(databaseName: String, tableName: String): Unit = {
    logInfo("XDCatalog: refreshTable")
  }

  /**
   * @inheritdoc
   */
  override def close(args: Any*): Unit = {
    db.close
  }
}
