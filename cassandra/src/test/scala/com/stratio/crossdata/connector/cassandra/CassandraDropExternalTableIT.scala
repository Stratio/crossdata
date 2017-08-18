package com.stratio.crossdata.connector.cassandra

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.Seq

@RunWith(classOf[JUnitRunner])
class CassandraDropExternalTableIT extends CassandraWithSharedContext {

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    val createTableQueryString1 =
      s"""|CREATE EXTERNAL TABLE $Catalog.dropTable1 (
          |id Integer,
          |name String,
          |booleanFile boolean,
          |timeTime Timestamp,
          |binaryType Binary,
          |arrayType ARRAY<STRING>,
          |mapType MAP<INT, INT>,
          |decimalType DECIMAL
          |)
          |USING $SourceProvider
          |OPTIONS (
          |keyspace '$Catalog',
          |table 'dropTable1',
          |cluster '$ClusterName',
          |pushdown "true",
          |spark_cassandra_connection_host '$CassandraHost',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString1).collect()

    val createTableQueryString2 =
      s"""|CREATE EXTERNAL TABLE dropTable2 (
          |id Integer,
          |name String,
          |booleanFile boolean,
          |timeTime Timestamp,
          |binaryType Binary,
          |arrayType ARRAY<STRING>,
          |mapType MAP<INT, INT>,
          |decimalType DECIMAL
          |)
          |USING $SourceProvider
          |OPTIONS (
          |keyspace '$Catalog',
          |table 'drop_table_example',
          |cluster '$ClusterName',
          |pushdown "true",
          |spark_cassandra_connection_host '$CassandraHost',
          |primary_key_string 'id'
          |)
      """.stripMargin.replaceAll("\n", " ")
    sql(createTableQueryString2).collect()

  }

  "The Cassandra connector" should "execute a DROP EXTERNAL TABLE" in {

    //Precondition
    xdContext.table(s"$Catalog.dropTable1") should not be null

    val cassandraTableName = "dropTable1"

    //DROP
    val dropExternalTableQuery = s"DROP EXTERNAL TABLE $Catalog.dropTable1"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table(s"$Catalog.dropTable1"))
    client.get._1.getMetadata.getKeyspace(Catalog).getTable(cassandraTableName) shouldBe null

  }

  "The Cassandra connector" should "execute a DROP EXTERNAL TABLE without specify database" in {

    //Precondition
    xdContext.table("dropTable2") should not be null

    val cassandraTableName = "drop_table_example"

    //DROP
    val dropExternalTableQuery = "DROP EXTERNAL TABLE dropTable2"
    sql(dropExternalTableQuery).collect() should be (Seq.empty)

    //Expectations
    an[Exception] shouldBe thrownBy(xdContext.table("dropTable2"))
    client.get._1.getMetadata.getKeyspace(Catalog).getTable(cassandraTableName) shouldBe null

  }
  
}
