=================================
Crossdata Elasticsearch Connector
=================================


Table of Contents
*****************

-  `1) Requirements <#requirements>`__

-  `2) First steps <#first-steps>`__

   -  `2.1) Import tables <#import-tables>`__

-  `3) Supported operators <#supported-operators>`__

-  `4) Advanced queries <#advanced-queries>`__

   -  `4.1) Order by <#order-by>`__
   -  `4.2) Group by <#order-by>`__
   -  `4.3) Between <#between>`__
   -  `4.4) Not between <#not-between>`__
   -  `4.5) Not Like <#not-like>`__

This connector allows to send SQL-Like queries to Elasticsearch and execute them natively on Elasticsearch.



1. Requirements
----------------

This version was developed using Elasticsearch 1.7, lower versions are not tested and may cause failures.

To register an Elasticsearch Index/Type as a Spark Table in the Crossdata Context execute this::

   xdContext.sql("CREATE TEMPORARY TABLE <TABLE-NAME> (<FIELDS>)USING com.stratio.crossdata.connector.elasticsearch
            OPTIONS (es.resource '<INDEX-NAME>/<TYPE-NAME>', es.cluster '<CLUSTER-NAME>',
            es.nodes '<HOSTS>', es.port '<REST-PORT>', es.nativePort '<NATIVE-PORT>')")

The Option required for basic functions are:
  - es.resource: Elasticsearch resource location, where data is read and written to. Requires the format <index>/<type>, Required.
  - es.cluster: indicates the name of the Elastic Search cluster, Required.
  - es.node: List of Elasticsearch nodes to connect to. (default localhost)
  - es.port: HTTP/REST port used for connecting to Elasticsearch (default 9200)
  - es.nativePort Native port used for connecting to Elasticsearch (default 9300)

Also, you can use the elasticsearch-hadoop configuration to be used if the query can not be executed natively and needs to run in Spark, more info in:
    - https://www.elastic.co/guide/en/elasticsearch/hadoop/master/configuration.html
    - https://www.elastic.co/guide/en/elasticsearch/hadoop/master/spark.html

2. First Steps
---------------

To execute a SQL query using Crossdata after registering the table, just use the xdContext to send a valid SQL query::

    val dataframe = xdContext.sql("SELECT * FROM <TABLE-NAME> ")


Example::

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE students
            |(id INT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN, birthday DATE)
            |USING com.stratio.crossdata.connector.elasticsearch
            |OPTIONS (
            |resource 'highschool/students',
            |es.nodes 'localhost',
            |es.port '9200',
            |es.nativePort '9300',
            |es.cluster 'elasticCluster'
            |)
         """.stripMargin.replaceAll("\n", " "))

      val dataframe = xdContext.sql(s"SELECT * FROM students ")
      val schema = dataframe.schema
      val result = dataframe.collect(Native)

2.1 Import tables
-----------------

Coming soon...

3. Supported Operators
----------------------

The following operators are executed natively in the WHERE clause:

+-------------+---------------------------------------------------------------------------------+
|Operator     |Description                                                                      |
+=============+=================================================================================+
|     =       | Equal                                                                           |
+-------------+---------------------------------------------------------------------------------+
|     >       | Greater than                                                                    |
+-------------+---------------------------------------------------------------------------------+
|     <       | Less than                                                                       |
+-------------+---------------------------------------------------------------------------------+
|     >=      | Greater than or equal                                                           |
+-------------+---------------------------------------------------------------------------------+
|     <=      | Less than or equal                                                              |
+-------------+---------------------------------------------------------------------------------+
|     IN      | To specify multiple possible values for a column                                |
+-------------+---------------------------------------------------------------------------------+
|    LIKE     | Search for a pattern, support: %word% and word%                                 |
+-------------+---------------------------------------------------------------------------------+
|   Is Null   | To specify Null value for a column                                              |
+-------------+---------------------------------------------------------------------------------+
| Is Not Null | To specify Not Null value for a column                                          |
+-------------+---------------------------------------------------------------------------------+

4. ADVANCED QUERIES
--------------------

We can perform some advanced queries that cannot be executed natively by the connector using Spark. Here there are some examples.

4.1 ORDER BY
-------------

   ::

     xdContext.sql(s"SELECT * FROM students ORDER BY age DESC")

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


4.2 GROUP BY
-------------


  ::

    xdContext.sql(("SELECT count(enrolled) FROM students GROUP BY enrolled")

    +---+
    |_c0|
    +---+
    |  5|
    |  5|
    +---+



4.3 BETWEEN
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


4.4 NOT BETWEEN
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



4.5 NOT LIKE
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

Notes
*****
Given the Elasticsearch nature, you can't use *Equals* operator in String column if the field is analyzed,
If you need use *Equals* operator in a String column, see this Elasticsearch documentation:
https://www.elastic.co/guide/en/elasticsearch/guide/current/_finding_exact_values.html#_term_filter_with_text

