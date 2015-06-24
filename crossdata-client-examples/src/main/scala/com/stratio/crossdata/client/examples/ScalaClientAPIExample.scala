/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.client.examples


import org.apache.log4j.Logger
import org.jfairy.Fairy
import org.jfairy.producer.BaseProducer
import org.jfairy.producer.person.Person
import com.stratio.crossdata.common.data.ResultSet
import com.stratio.crossdata.common.exceptions.ExecutionException
import com.stratio.crossdata.common.result.QueryResult
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.driver.BasicDriver
import com.stratio.crossdata.driver.querybuilder.QueryBuilder

import scala.collection.mutable


object ScalaClientAPIExample extends App {

  val LOG: Logger = Logger.getLogger(classOf[ClientAPIExample])

  val INMEMORY_CLUSTER: String = "InMemoryCluster"
  val NUMBER_OF_ROWS: Int = 100
  val USER_NAME: String = "stratio"
  val PASSWORD: String = "stratio"
  val CATALOG_NAME: String = "customers"
  val TABLE_NAME: String = "products"
  val PARTITION_KEY: List[String] = List("id")
  val COLUMN_TYPE: Map[String, String] = Map(
    "id" -> "INT",
    "serial" -> "INT",
    "name" -> "TEXT",
    "rating" -> "DOUBLE",
    "email" -> "TEXT"
  )

  val basicDriver: BasicDriver = new BasicDriver

  try {

    val xdConnection = basicDriver.connect(USER_NAME, PASSWORD)
    LOG.info("Connected to Crossdata Server")

    // RESET SERVER DATA
    xdConnection.resetServerdata

    // ATTACH CLUSTER
    xdConnection.attachCluster(INMEMORY_CLUSTER, "InMemoryDatastore", options = Map("TableRowLimit" -> 100))

    // ATTACH STRATIO INMEMORY CONNECTOR
    xdConnection.attachConnector("InMemoryConnector", INMEMORY_CLUSTER)

    // CREATE CATALOG
    xdConnection.createCatalog(CATALOG_NAME)

    // CREATE TABLE
    xdConnection.createTable(CATALOG_NAME, TABLE_NAME, INMEMORY_CLUSTER, COLUMN_TYPE, PARTITION_KEY)

    // INSERT RANDOM DATA
    val fairy: Fairy = Fairy.create
    val baseProducer: BaseProducer = fairy.baseProducer
    val columnValue = mutable.Map.empty[String, Any]
    for (_ <- 1 to NUMBER_OF_ROWS ) {
      val person: Person = fairy.person()
      columnValue += "id" -> generateSerial(baseProducer)
      columnValue += "serial" -> generateSerial(baseProducer)
      columnValue += "name" -> generateName(person)
      columnValue += "rating" -> generateRating(baseProducer)
      columnValue += "email" -> generateEmail(person)

      xdConnection.insert(CATALOG_NAME, TABLE_NAME, columnValue.toMap)
      LOG.info("Row for first table inserted.")
    }

    //SELECT
    val qResult = xdConnection.executeQuery(QueryBuilder.selectAll from s"$CATALOG_NAME.$TABLE_NAME" orderBy "rating" limit 20)
    val resultSet: ResultSet = getResultSet(qResult)
    LOG.info("ResultSet size: " + resultSet.size)

    //DROP TABLE
    xdConnection.dropTable(CATALOG_NAME, TABLE_NAME)

    //DROP CATALOG
    xdConnection.dropCatalog(CATALOG_NAME)

    // CLOSE CONNECTION
    basicDriver.disconnect

    LOG.info("Connection closed")
  } catch {
    case ex: Exception => LOG.error(ex.getMessage)
    case other => LOG.error(other)
  }

  basicDriver.close


  private def generateCompany(person: Person): String = {
    return person.getCompany.name
  }

  private def generateAge(person: Person): Int = {
    return person.age
  }

  private def generateLastName(person: Person): String = {
    return person.lastName
  }

  private def generateInt(baseProducer: BaseProducer, nRows: Int): Int = {
    return baseProducer.randomBetween(1, nRows * 8)
  }

  private def generateSerial(baseProducer: BaseProducer): Int = {
    return baseProducer.randomBetween(1, Integer.MAX_VALUE - 1)
  }

  private def generateName(person: Person): String = {
    return person.username
  }

  private def generateRating(baseProducer: BaseProducer): Double = {
    val rating: Double = baseProducer.randomBetween(0.0, 10.0)
    return Math.round(rating * 100.0) / 100.0
  }

  private def generateEmail(person: Person): String = {
    return person.email
  }

  private def getResultSet(queryResult: Result): ResultSet = {
    if (queryResult.hasError) {
      throw new ExecutionException("Error executing the query")
    }
    else {
      return (queryResult.asInstanceOf[QueryResult]).getResultSet
    }
  }


}
