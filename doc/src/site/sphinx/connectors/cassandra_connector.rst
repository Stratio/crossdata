===========================
Crossdata Cassandra Connector
===========================

This connector allows to send SQL-Like queries to Cassandra and execute them natively on Cassandra.

Requirements
************

This version was developed using Datastax Cassandra connector, which uses Cassandra 2.1, lower versions are not tested and may cause failures.

To register a Cassandra Keyspace/Table as a Spark Table in the Crossdata Context execute this::

   xdContext.sql("CREATE TEMPORARY TABLE <TABLE-NAME> (<FIELDS>) USING com.stratio.crossdata.connector.cassandra
            OPTIONS (spark_cassandra_connection_host '<HOST>', keyspace '<KEYSPACE>',
            table '<TABLE>', cluster '<CLUSTER-NAME>' )")


Also, you can see the datasource configuration for specific options:
    - https://github.com/datastax/spark-cassandra-connector/tree/v1.5.0-M2

Querying
********

To execute a SQL query using Crossdata after registering the table, just use the xdContext to send a valid SQL query::

    val dataframe = xdContext.sql("SELECT * FROM <TABLE-NAME> ")


Example::

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE students
            |USING com.stratio.crossdata.connector.cassandra
            |OPTIONS (
            |spark_cassandra_connection_host '127.0.0.1',
            |keyspace 'highschool',
            |table 'students',
            |cluster 'Test Cluster'
            |)
         """.stripMargin.replaceAll("\n", " "))

      val dataframe = xdContext.sql(s"SELECT * FROM students")
      val schema = dataframe.schema
      val result = dataframe.collect(Native)

Built-in functions
******************
Cassandra native functions

- now()
- dateOf(<date>)
- toDate(<date>)


Import existing tables into Crossdata
*************************************
To import existing tables into the Crossdata Catalog, execute this query::

         IMPORT TABLES
         USING com.stratio.crossdata.connector.cassandra
         OPTIONS (
          cluster "MuClusterName",
          keyspace "MyKeyspace",
          table "MyTable",
          spark_cassandra_connection_host "localhost"
          )


Where:
- keyspace (Optional): The Cassandra Keyspace name to import, if is omitted, Crossdata will import all keyspaces in the cluster.
- table (Optional): The Cassandra Table name to import, if is omitted, Crossdata will import All the tables in the Keyspace or Keyspaces.