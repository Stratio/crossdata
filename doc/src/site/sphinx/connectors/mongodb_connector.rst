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

Import existing collections as tables into Crossdata
**********************************************
To import existing collections into the Crossdata Catalog, execute this query::

         IMPORT TABLES
            USING com.stratio.crossdata.connector.mongodb
            OPTIONS (
            host 'HOST:PORT',
            database 'highschool',
            collection 'students',
            schema_samplingRatio '0.1'
            )


Where:
- host (Mandatory): Host and port to connect with the database.
- database (Optional): Database to import their collections.
- collection (Optional): Collection to import.
- schema_samplingRatio (Optional and recommended): Ratio of documents that will be scanned to infer the schema.

Tips:
- To import every collection stored in MongoDB, don't specify database neither collection.
- To import every collection for a specific database, don't specify the collection.
- To import only a collection, should specify database and collection.
- If you specify a collection without a database, all the existing collections with this name will be imported.