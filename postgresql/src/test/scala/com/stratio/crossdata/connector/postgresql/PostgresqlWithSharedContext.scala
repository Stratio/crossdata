/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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

    // Basic table
    statement.execute(
      s"""CREATE TABLE $postgresqlSchema.$Table (${stringifySchema(schema)},
          |PRIMARY KEY (${pk.mkString(", ")}))""".stripMargin.replaceAll("\n", " "))

    // Aggregation table
    statement.execute(
      s"""CREATE TABLE $postgresqlSchema.$aggregationTable (${stringifySchema(schemaAgg)},
          |PRIMARY KEY (${pkAgg.mkString(", ")}))""".stripMargin.replaceAll("\n", " "))

    if(indexedColumn.nonEmpty){
      statement.execute(s"CREATE INDEX student_index ON $postgresqlSchema.$Table (name)")
    }

    def insertRow(row: List[Any], tableName: String, tableSchema: ListMap[String, String]): Unit = {
      statement.execute(
        s"""INSERT INTO $tableName (${tableSchema.map(p => p._1).mkString(", ")})
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

    testData.foreach(data => insertRow(data, s"$postgresqlSchema.$Table", schema))
    testDataAggTable.foreach(data => insertRow(data, s"$postgresqlSchema.$aggregationTable", schemaAgg))

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
    str2sparkTableDesc(s"CREATE TEMPORARY TABLE $postgresqlSchema.$Table") :+
    SparkTable(s"CREATE TEMPORARY TABLE $postgresqlSchema.$aggregationTable", aggTableOptions)

  override val runningError: String = "Postgresql and Spark must be up and running"

}

sealed trait postgresqlDefaultTestConstants {

  private lazy val config = ConfigFactory.load()
  val postgresqlSchema = "highschool"
  val Table = "students"
  val aggregationTable = "studentsagg"
  val UnregisteredTable = "teachers"
  val driver: String = "org.postgresql.Driver"
  val postgresqlHost: String = Try(config.getStringList("postgresql.host")).map(_.get(0)).getOrElse("127.0.0.1")

  //CD postgresql docker has this database, user and password defined
  val database = "hakama"
  val user = "hakama"
  val password = "hakama"

  val url: String = s"jdbc:postgresql://$postgresqlHost:5432/$database?user=$user&password=$password"
  val schema = ListMap("id" -> "integer", "age" -> "integer", "comment" -> "text", "enrolled" -> "boolean", "name" -> "text")
  val schemaAgg = ListMap("id" -> "integer", "mark" -> "numeric(4,2)", "country" -> "text")

  val pk = "id" :: "name" :: Nil
  val pkAgg = "id" :: "mark" :: Nil
  val indexedColumn = "name"

  val aggTableOptions = Map("url" -> url, "dbtable" -> s"$postgresqlSchema.$aggregationTable")

  val testData = (for (a <- 1 to 10) yield {
    a :: (10 + a) :: s"Comment $a" :: (a % 2 == 0) :: s"Name $a" :: Nil
  }).toList

  val testDataAgg = (for (a <- 1 to 10) yield {
    a :: a.toDouble + 0.25d :: {if(a < 6) "ES" else "US"} :: Nil
  }).toList

  lazy val testDataAggTable =  testDataAgg ++ testData.map{ row =>
    List(row(0).asInstanceOf[Int] + 10,
      row(1).asInstanceOf[Int].toDouble +0.25,
      row(2))
  }

  val SourceProvider = "com.stratio.crossdata.connector.postgresql"
}