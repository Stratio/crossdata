package com.stratio.crossdata.connector.postgresql

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.Seq

@RunWith(classOf[JUnitRunner])
class PostgresqlDropExternalTableIT extends PostgresqlWithSharedContext {

  val dropSchema = "dropschema"

  def createExternalTable(dropTable: String) = {
    val create =
    s"""|CREATE EXTERNAL TABLE $dropSchema.$dropTable (
        |id Integer,
        |name String,
        |booleanFile Boolean,
        |timeTime Timestamp
        |)
        |USING $SourceProvider
        |OPTIONS (
        |url '$url',
        |dbtable '$dropSchema.$dropTable',
        |primary_key_string 'id'
        |)
    """.stripMargin.replaceAll("\n", " ")
    xdContext.sql(create).collect()
  }

  def dropTables = client.get._2.execute(s"DROP SCHEMA $dropSchema CASCADE")

  "The Postgresql connector" should "execute a DROP EXTERNAL TABLE" in {

    val dropTable = "drop1"
    createExternalTable(dropTable)

    //Precondition
    xdContext.table(s"$dropSchema.$dropTable") should not be null

    //DROP
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $dropSchema.$dropTable"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$dropSchema.$dropTable"))

    client.get._1.getMetaData.getTables(null, dropSchema, dropTable, null).next() shouldBe false
    dropTables
  }

  "The Postgresql connector" should "execute a DROP EXTERNAL TABLE without specify database" in {

    val dropTable = "drop2"
    createExternalTable(dropTable)

    //Postgresql datasource doesn't allow tables without specify schema
    an[Exception] should be thrownBy xdContext.table(dropTable)

    //DROP expectations: Postgresql table can't be dropped without specify schema. This table is not in the XDCatalog
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $dropTable"
    an[Exception] shouldBe thrownBy (sql(dropExternalTableQuery).collect())

    client.get._1.getMetaData.getTables(null, dropSchema, dropTable, null).next() shouldBe true
    dropTables
  }


}
