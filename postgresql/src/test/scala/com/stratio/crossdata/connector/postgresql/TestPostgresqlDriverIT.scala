package com.stratio.crossdata.connector.postgresql

import java.sql.DriverManager

import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestPostgresqlDriverIT extends BaseXDTest
with BeforeAndAfterAll {

  val Catalog = "highschool"
  val Table = "students"
  val user = "postgres"
  val password = "mysecretpassword"
  //val url: String = s"jdbc:postgresql://localhost:5432/test"
  val url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword"
  Class.forName("org.postgresql.Driver")
  val conn = DriverManager.getConnection(url, user, password)

//  conn.createStatement.execute("CREATE SCHEMA schema_test")
//  conn.createStatement.execute("CREATE TABLE schema_test.table1 (name text,age integer, dni integer)")

  val metadata = conn.getMetaData
  val tables = metadata.getTables(null, null, null, Array("TABLE"))

  while (tables.next()) {
    val table = tables.getString(3)
    val columns = metadata.getColumns(null, null, table.toLowerCase, null)
    while(columns.next()) {
      val columnName = columns.getString(4)
      val idColumnType = columns.getInt(5)
      val columnType= columns.getString(6)
      println("Table: " + table + " " + columnName + " " + idColumnType + " " + columnType)
    }
  }

  //conn.createStatement.execute("DROP TABLE students")
  //conn.createStatement.execute("DROP TABLE teachers")
  //conn.createStatement.execute("DROP SCHEMA highschool")

  //conn.createStatement.execute("CREATE DATABASE test")
  //conn.createStatement.execute("CREATE TABLE table1 (name text,age integer, dni integer)")

  /** CREATE CONNECTIONS
    * A database is needed to create a connection, by default the same as the username (usually postgres)
    *
    * Postgresql has a schema inside database. If schema is not defined, the default schema 'public' is used.
    * It's possible create as much schemas as you want.
    *
    */

/*** This works:
  conn.createStatement.execute("CREATE SCHEMA schema_test")
  conn.createStatement.execute("CREATE TABLE schema_test.table1 (name text,age integer, dni integer)")
  conn.createStatement.execute("DROP SCHEMA schema_test CASCADE")
**/



//  val rs = conn.createStatement.executeQuery("SELECT * from schema_test.table1")
//  println(rs.getString("name"))

}
