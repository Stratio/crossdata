==============================
Crossdata Cassandra Connector
==============================

Table of Contents
*****************

-  `1) Requirements <#requirements>`__

-  `2) First steps <#first-steps>`__

   -  `2.1) Import tables <#import-tables>`__

-  `3) Advanced queries <#advanced-queries>`__

   -  `3.1) Order by <#order-by>`__
   -  `3.2) Group by <#order-by>`__
   -  `3.3) Between <#between>`__
   -  `3.4) Not between <#not-between>`__
   -  `3.5) Like <#like>`__
   -  `3.6) Not Like <#not-like>`__

-  `4) Built-in functions <#built_in-functions>`__

This connector allows to send SQL-Like queries to Cassandra and execute them natively on Cassandra.

1. Requirements
-----------------

This version was developed using Datastax Cassandra connector, which uses Cassandra 2.1, lower versions are not tested and may cause failures.

To register a Cassandra Keyspace/Table as a Spark Table in the Crossdata Context execute this::

   xdContext.sql("CREATE TEMPORARY TABLE <TABLE-NAME> (<FIELDS>) USING com.stratio.crossdata.connector.cassandra
            OPTIONS (spark_cassandra_connection_host '<HOST>', keyspace '<KEYSPACE>',
            table '<TABLE>', cluster '<CLUSTER-NAME>' )")


Also, you can see the datasource configuration for specific options:
    - https://github.com/datastax/spark-cassandra-connector/tree/v1.5.0-M2

2. First steps
--------------

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

2.1 Import tables
-----------------

It is possible to register every table from a cluster. This is an example::

    xdContext.sql(
      s"""
          |IMPORT TABLES
          |USING com.stratio.crossdata.connector.cassandra
          |OPTIONS (
          | cluster "Test Cluster",
          | spark_cassandra_connection_host '127.0.0.1'
          |)
      """.stripMargin


And the dataframe returned must contain the following::

    +----------------------+
    |       tableIdentifier|
    +----------------------+
    |[highschool, teachers]|
    |[highschool, students]|
    +----------------------+

3. ADVANCED QUERIES
--------------------

We can perform some advanced queries that cannot be executed natively by the connector using Spark. Here there are some examples.

3.1 ORDER BY
-------------

   ::

     xdContext.sql(s"SELECT * FROM highschool ORDER BY age DESC")

    +---+---+----------+--------+----+
    | id|age|   comment|enrolled|name|
    +---+---+----------+--------+----+
    | 10| 20|Comment 10|    true|null|
    |  9| 19| Comment 9|   false|null|
    |  8| 18| Comment 8|    true|null|
    |  7| 17| Comment 7|   false|null|
    |  6| 16| Comment 6|    true|null|
    |  5| 15| Comment 5|   false|null|
    |  4| 14| Comment 4|    true|null|
    |  3| 13| Comment 3|   false|null|
    |  2| 12| Comment 2|    true|null|
    |  1| 11| Comment 1|   false|null|
    +---+---+----------+--------+----+


3.2 GROUP BY
-------------


  ::

    xdContext.sql(("SELECT count(enrolled) FROM students GROUP BY enrolled")

    +---+
    |_c0|
    +---+
    |  5|
    |  5|
    +---+



3.3 BETWEEN
------------


   ::


    xdContext.sql(s"SELECT * FROM students WHERE age NOT BETWEEN 10 AND 15")

    +---+---+---------+--------+----+
    | id|age|  comment|enrolled|name|
    +---+---+---------+--------+----+
    |  5| 15|Comment 5|   false|null|
    |  1| 11|Comment 1|   false|null|
    |  2| 12|Comment 2|    true|null|
    |  4| 14|Comment 4|    true|null|
    |  3| 13|Comment 3|   false|null|
    +---+---+---------+--------+----+


3.4 NOT BETWEEN
----------------

   ::



    xdContext.sql(s"SELECT * FROM students WHERE age NOT BETWEEN 10 AND 15").show

    +---+---+----------+--------+----+
    | id|age|   comment|enrolled|name|
    +---+---+----------+--------+----+
    | 10| 20|Comment 10|    true|null|
    |  8| 18| Comment 8|    true|null|
    |  7| 17| Comment 7|   false|null|
    |  6| 16| Comment 6|    true|null|
    |  9| 19| Comment 9|   false|null|
    +---+---+----------+--------+----+


3.5 LIKE
----------

   ::


    xdContext.sql(s"SELECT * FROM students WHERE comment LIKE 'Comment 1%'").show

    +---+---+----------+--------+----+
    | id|age|   comment|enrolled|name|
    +---+---+----------+--------+----+
    | 10| 20|Comment 10|    true|null|
    |  1| 11| Comment 1|   false|null|
    +---+---+----------+--------+----+



3.6 NOT LIKE
-------------

   ::

    xdContext.sql(s"SELECT * FROM students WHERE comment NOT LIKE 'Comment 1%'").show

    +---+---+---------+--------+----+
    | id|age|  comment|enrolled|name|
    +---+---+---------+--------+----+
    |  5| 15|Comment 5|   false|null|
    |  8| 18|Comment 8|    true|null|
    |  2| 12|Comment 2|    true|null|
    |  4| 14|Comment 4|    true|null|
    |  7| 17|Comment 7|   false|null|
    |  6| 16|Comment 6|    true|null|
    |  9| 19|Comment 9|   false|null|
    |  3| 13|Comment 3|   false|null|
    +---+---+---------+--------+----+

4.0 Built-in functions
-----------------------

The following cassandra native functions are supported:

- now()
- dateOf(<date>)
- toDate(<date>)