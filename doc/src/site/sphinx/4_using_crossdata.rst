===============
Using Crossdata
===============

If you want to test Crossdata you can get our Sandbox follow the instructions of this
`link <Sandbox.rst>`__ (Coming soon)

TODO:

- APIs for writing custom transformations, interceptors and SQL Syntax–where applicable




MongoDB Connector
-----------------

TODO

Elasticsearch Connector
-----------------------

This example register the existent type "students" of the Elasticsearch Index "highschool" that has a few rows inserted.
Once the table is registered then execute a query by the native way::

    >xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.connector.elasticsearch
            OPTIONS (es.resource 'highschool/students', es.cluster 'elasticsearch',
            es.node '127.0.0.1', es.port '9200', es.nativePort '9300')")
    >xdContext.sql("SELECT * FROM students").collect()

The Option required for basic functions are:
  - es.resource: Elasticsearch resource location, where data is read and written to. Requires the format <index>/<type>, Required.
  - es.cluster: indicates the name of the Elastic Search cluster, Required.
  - es.node: List of Elasticsearch nodes to connect to. (default localhost)
  - es.port: HTTP/REST port used for connecting to Elasticsearch (default 9200)
  - es.nativePort Native port used for connecting to Elasticsearch (default 9300)

The Elasticsearch-Hadoop configuration options are in: https://www.elastic.co/guide/en/elasticsearch/hadoop/current/configuration.html