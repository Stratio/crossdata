===========================
Crossdata MongoDB Connector
===========================

This connector allows to send SQL-Like queries to MongoDB and execute them natively on MongoDB.

Requirements
************

This version was developed using Stratio MongoDB datasource, which uses MongoDB Java Driver 2.13, lower versions are not tested and may cause failures.

To register a MongoDB Database/Collection as a Spark Table in the Crossdata Context execute this::

   xdContext.sql("CREATE TEMPORARY TABLE <TABLE-NAME> (<FIELDS>) USING com.stratio.crossdata.connector.mongodb
            OPTIONS (host '<HOST>:<PORT>', database '<DB-NAME>',
            collection '<COLLECTION>')")


Also, you can see the datasource configuration for specific options:
    - https://github.com/Stratio/spark-mongodb/tree/0.10.0

Querying
********

To execute a SQL query using Crossdata after registering the table, just use the xdContext to send a valid SQL query::

    val dataframe = xdContext.sql("SELECT * FROM <TABLE-NAME> ")


Example::

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE students
            |USING com.stratio.crossdata.connector.mongodb
            |OPTIONS (
            |host '127.0.0.1:27017',
            |database 'highschool',
            |collection 'students'
            |)
         """.stripMargin.replaceAll("\n", " "))

      val dataframe = xdContext.sql(s"SELECT * FROM students")
      val schema = dataframe.schema
      val result = dataframe.collect(Native)

Advanced queries
****************

We can perform some advanced queries that cannot be executed natively by the connector using Spark. Here there are some examples

ORDER BY::

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

GROUP BY::

    xdContext.sql("SELECT count(enrolled) FROM students GROUP BY enrolled")

    +---+
    |_c0|
    +---+
    |  5|
    |  5|
    +---+

BETWEEN::

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

NOT BETWEEN::

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

LIKE::

    xdContext.sql(s"SELECT * FROM students WHERE comment LIKE 'Comment 1%'").show

    +---+---+----------+--------+----+
    | id|age|   comment|enrolled|name|
    +---+---+----------+--------+----+
    | 10| 20|Comment 10|    true|null|
    |  1| 11| Comment 1|   false|null|
    +---+---+----------+--------+----+

NOT LIKE::

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

