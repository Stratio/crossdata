/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server.config

import org.scalatest.{Suite, BeforeAndAfterAll}
import com.stratio.meta.test.CCMHandler
import com.datastax.driver.core._
import org.testng.Assert._
import org.apache.log4j.Logger
import com.datastax.driver.core.exceptions.InvalidQueryException
import java.net.URL
import java.util
import java.io.IOException
import scala.collection.mutable.MutableList

trait BeforeAndAfterCassandra extends BeforeAndAfterAll {
  this:Suite =>

  /**
   * Default Cassandra HOST using 127.0.0.1.
   */
  private final val DEFAULT_HOST: String = "127.0.0.1"

  /**
   * Session to launch queries on C*.
   */
  protected var _session: Session = null


  /**
   * Class logger.
   */
  private final val logger: Logger = Logger.getLogger(classOf[BeforeAndAfterCassandra])

  /**
   * Establish the connection with Cassandra in order to be able to retrieve
   * metadata from the system columns.
   * @param host The target host.
   * @return Whether the connection has been established or not.
   */
  protected def connect(host: String): Boolean = {
    var result: Boolean = false
    val c: Cluster = Cluster.builder.addContactPoint(host).build
    _session = c.connect
    result = null == _session.getLoggedKeyspace
    return result
  }

  private def getHost: String = {
    return System.getProperty("cassandraTestHost", DEFAULT_HOST)
  }

  /**
   * Drop a keyspace if it exists in the database.
   * @param targetKeyspace The target keyspace.
   */
  def dropKeyspaceIfExists(targetKeyspace: String) {
    val query: String = "USE " + targetKeyspace
    var ksExists: Boolean = true
    try {
      val result: ResultSet = _session.execute(query)
    }
    catch {
      case iqe: InvalidQueryException => {
        ksExists = false
      }
    }
    if (ksExists) {
      val q: String = "DROP KEYSPACE " + targetKeyspace
      try {
        _session.execute(q)
      }
      catch {
        case e: Exception => {
          logger.error("Cannot drop keyspace: " + targetKeyspace, e)
        }
      }
    }
  }

  /**
   * Load a {@code keyspace} in Cassandra using the CQL sentences in the script
   * path. The script is executed if the keyspace does not exists in Cassandra.
   * @param keyspace The name of the keyspace.
   * @param path The path of the CQL script.
   */
  def loadTestData(keyspace: String, path: String) {
    val metadata: KeyspaceMetadata = _session.getCluster.getMetadata.getKeyspace(keyspace)
    if (metadata == null) {
      logger.info("Creating keyspace " + keyspace + " using " + path)
      val scriptLines: Iterator[String] = loadScript(path)

      var counter = 0
      while(scriptLines.hasNext){
        val cql = scriptLines.next()
        val result: ResultSet = _session.execute(cql)
        counter+=1
        if (logger.isDebugEnabled) {
          logger.debug("Executing: " + cql + " -> " + result.toString)
        }
      }
      logger.info("Executed " + scriptLines.size + " lines")
    }
    logger.info("Using existing keyspace " + keyspace)
  }

  /**
   * Load the lines of a CQL script containing one statement per line
   * into a list.
   * @param path The path of the CQL script.
   * @return The contents of the script.
   */
  def loadScript(path: String): Iterator[String] = {
    val url: URL = classOf[BeforeAndAfterCassandra].getResource(path)
    logger.info("Loading script from: " + url)
    val source = scala.io.Source.fromURL(url).getLines()
    val result = source.filter(line => (line.length > 0 && !line.startsWith("#")))
    return result
  }

  def checkColumnExists(keyspace: String, tablename: String, columnName: String): Boolean = {
    var exists: Boolean = false
    val metadata: KeyspaceMetadata = _session.getCluster.getMetadata.getKeyspace(keyspace)
    if(metadata != null){
      val tableMetadata: TableMetadata = metadata.getTable(tablename)
      if(tableMetadata != null){
        exists = tableMetadata.getColumn(columnName) != null
      }
    }
    return exists
  }

    def beforeCassandraStart(): Unit = {
      assertTrue(connect(getHost), "Cannot connect to cassandra")
    }

    override def beforeAll(): Unit = {
      beforeCassandraStart()
      CCMHandler.StartCCM()
      afterCassandraStart()
    }

    def afterCassandraStart(): Unit = {

    }

    def beforeCassandraFinish(): Unit = {

    }
    override def afterAll(): Unit = {
      beforeCassandraFinish()
      CCMHandler.FinishCCM()
      afterCassandraFinish()
    }

    def afterCassandraFinish(): Unit = {
      _session.close
    }

}
