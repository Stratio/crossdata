=================================
Crossdata Elasticsearch Connector
=================================

This connector allows to send SQL-Like queries to Elasticsearch and execute they natively on Elasticsearch.

Requirements
************

This version was developed using Elasticsearch 1.7, lower versions are not tested and may cause failures.

To register a Elasticsearch Index/Type as a Spark Table in the Crossdata Context execute this::

   xdContext.sql("CREATE TEMPORARY TABLE <TABLE-NAME> (<FIELDS>)USING com.stratio.crossdata.connector.elasticsearch
            OPTIONS (es.resource '<INDEX-NAME>/<TYPE-NAME>', es.cluster '<CLUSTER-NAME>',
            es.node '<HOST>', es.port '<REST-PORT>', es.nativePort '<NATIVE-PORT>')")

The Option required for basic functions are:
  - es.resource: Elasticsearch resource location, where data is read and written to. Requires the format <index>/<type>, Required.
  - es.cluster: indicates the name of the Elastic Search cluster, Required.
  - es.node: List of Elasticsearch nodes to connect to. (default localhost)
  - es.port: HTTP/REST port used for connecting to Elasticsearch (default 9200)
  - es.nativePort Native port used for connecting to Elasticsearch (default 9300)

Also, you can use the elasticsearch-hadoop configuration to be used if the query can not be executed natively and needs to run in Spark, more info in:
    - https://www.elastic.co/guide/en/elasticsearch/hadoop/master/configuration.html
    - https://www.elastic.co/guide/en/elasticsearch/hadoop/master/spark.html

Querying
********

To execute a SQL query using Crossdata after register the table, just use the xdContext to send a valid SQL query::

    val dataframe = xdContext.sql("SELECT * FROM <TABLE-NAME> ")


Example::

      xdContext.sql(
        s"""|CREATE TEMPORARY TABLE students
            |(id INT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN, birthday DATE)
            |USING com.stratio.crossdata.connector.elasticsearch
            |OPTIONS (
            |resource 'highschool/students',
            |es.node 'localhost',
            |es.port '9200',
            |es.nativePort '9300',
            |es.cluster 'elasticCluster'
            |)
         """.stripMargin.replaceAll("\n", " "))

      val dataframe = xdContext.sql(s"SELECT * FROM students ")
      val schema = dataframe.schema
      val result = dataframe.collect(Native)


Supported Operators
*******************

The following operators can be used in the WHERE clause:

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

Notes
*****
Given the Elasticsearch nature, you can't use *Equals* operator in String column if the field is analyzed,
If you need use *Equals* operator in a String column, see this Elasticsearch documentation:
https://www.elastic.co/guide/en/elasticsearch/guide/current/_finding_exact_values.html#_term_filter_with_text

