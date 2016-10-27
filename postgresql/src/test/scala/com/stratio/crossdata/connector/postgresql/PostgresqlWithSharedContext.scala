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

package com.stratio.crossdata.connector.postgresql

import java.sql.{Connection, DriverManager, Statement}

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest
import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest.SparkTable
import org.scalatest.Suite

import scala.collection.immutable.ListMap
import scala.util.Try

trait PostgresqlWithSharedContext extends SharedXDContextWithDataTest
  with postgresqlDefaultTestConstants
  with SparkLoggerComponent {

  this: Suite =>

  override type ClientParams = (Connection, Statement)
  override val provider: String = SourceProvider
  override def defaultOptions = Map("url" -> url, "dbtable" -> s"$postgresqlSchema.$Table")

  abstract override def saveTestData: Unit = {
    val statement = client.get._2

    def stringifySchema(schema: Map[String, String]): String = schema.map(p => s"${p._1} ${p._2}").mkString(", ")


    statement.execute(s"CREATE SCHEMA $postgresqlSchema")

    statement.execute(
      s"""CREATE TABLE $postgresqlSchema.$Table (${stringifySchema(schema)},
          |PRIMARY KEY (${pk.mkString(", ")}))""".stripMargin.replaceAll("\n", " "))

    if(indexedColumn.nonEmpty){
      statement.execute(s"CREATE INDEX student_index ON $postgresqlSchema.$Table (name)")
    }

    def insertRow(row: List[Any]): Unit = {
      statement.execute(
        s"""INSERT INTO $postgresqlSchema.$Table(${schema.map(p => p._1).mkString(", ")})
            | VALUES (${parseRow(row)})""".stripMargin.replaceAll("\n", ""))
    }

    def parseRow(row: List[Any]): String = {
      row map {col => parseElement(col)} mkString ", "
    }

    def parseElement(element: Any): String = {
      element match {
        case map : Map[_,_] => map map { case (key,value) => s"${parseElement(key)} : ${parseElement(value)}" } mkString ("{", ", ", "}")
        case list : Seq[_] => list map {listElement => parseElement(listElement)} mkString ("[", ", ", "]")
        case string: String => s"'$string'"
        case other => other.toString
      }
    }

    testData.foreach(insertRow(_))

    //This creates a new table in the schema which will not be initially registered at the Spark
    if(UnregisteredTable.nonEmpty){
      statement.execute(
        s"""CREATE TABLE $postgresqlSchema.$UnregisteredTable (${stringifySchema(schema)},
            |PRIMARY KEY (${pk.mkString(", ")}))""".stripMargin.replaceAll("\n", " "))
    }

    super.saveTestData
  }

  override protected def terminateClient: Unit = {
    val connection = client.get._1
    val statement = client.get._2
    connection.close()
    statement.close()
  }

  override protected def cleanTestData: Unit = {
    client.get._2.execute(s"DROP SCHEMA $postgresqlSchema CASCADE")
  }

  override protected def prepareClient: Option[ClientParams] = Try {
    Class.forName(driver)
    val connection = DriverManager.getConnection(url)
    (connection, connection.createStatement())

  } toOption

  abstract override def sparkRegisterTableSQL: Seq[SparkTable] = super.sparkRegisterTableSQL :+
    str2sparkTableDesc(s"CREATE TEMPORARY TABLE $Table")

  override val runningError: String = "Postgresql and Spark must be up and running"

}

sealed trait postgresqlDefaultTestConstants {

  private lazy val config = ConfigFactory.load()
  val postgresqlSchema = "highschool"
  val Table = "students"
  val UnregisteredTable = "teachers"
  val driver: String = "org.postgresql.Driver"
  val postgresqlHost: String = Try(config.getStringList("postgresql.host")).map(_.get(0)).getOrElse("127.0.0.1")


  val url: String = s"jdbc:postgresql://$postgresqlHost:5432/postgres?user=postgres&password=mysecretpassword"
  val schema = ListMap("id" -> "integer", "age" -> "integer", "comment" -> "text", "enrolled" -> "boolean", "name" -> "text")
  val pk = "id" :: "age" :: "comment" :: Nil
  val indexedColumn = "name"

  val testData = (for (a <- 1 to 10) yield {
    a :: (10 + a) :: s"Comment $a" :: (a % 2 == 0) :: s"Name $a" :: Nil
  }).toList

  val SourceProvider = "com.stratio.crossdata.connector.postgresql"
}