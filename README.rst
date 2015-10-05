About
*****

|ImageLink|_

.. |ImageLink| image:: https://api.travis-ci.org/Stratio/Crossdata.svg?branch=master
.. _ImageLink: https://travis-ci.org/Stratio/Crossdata?branch=master

Crossdata is a fast and general-purpose computing system powered by Apache Spark. It adds some libraries to provide
native access to datastores when they are able to resolve the query avoiding the use of the Spark cluster.
We include some Spark connectors optimized to access to each datasource, but Crossdata is fully compatible with any connector
developed by the Spark community.
-  Cassandra connector powered by Datastax-Spark-Connector

===================

Compiling Crossdata

    > mvn clean install -Pcrossdata-all

If you prefer to install solely some connectors::

    > mvn clean install -P crossdata-cassandra

Use Crossdata with a standard Spark distribution
========================================
TODO

Building a Spark Distribution with Crossdata
========================================

You can build a Spark Distribution with Crossdata libraries running the make-distribution-crossdata script:
    > cd crossdata-scripts
    > ./make-distribution-crossdata.sh

This will build Spark with the following options:
    - Crossdata with Cassandra support
    - Spark Version v1.5.0
    - Spark's Hadoop  Version 2.4.0
    - Yarn support
    - Hive integration for SparkSQL
     -Scala version 2.10

For others options run ./make-distribution-crossdata.sh --help

Running the crossdata-shell
===========================

Using a Crossdata's Spark Distribution with cassandra support:
    > bin/stratio-xd-shell --cassandra

Then you can do:

    >xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra
            OPTIONS (keyspace \"highschool\", table \"students\", cluster \"students\", pushdown \"true\",
            spark_cassandra_connection_host \"127.0.0.1\")".stripMargin)
    >xdContext.sql("SELECT * FROM students").collect()



Send issues to Jira
===================
You can send us issues in https://crossdata.atlassian.net/
You can also find help in https://groups.google.com/forum/#!forum/crossdata-users


Grammar
=======

TODO


Getting started
===============

TODO. See Crossdata examples.


Connectors
==========

TODO


Crossdata compatibility with connectors
========================================

+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| Crossdata       | Cassandra      | Commons        | MongoDB        | ElasticSearch  | SparkSQL       | Deep           | Streaming      | HDFS           |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.0           | 0.1.0-001      | 0.1.0          | 0.1.0          | 0.1.0          |  X             | 0.1.0          | 0.1.0          | X              |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.0-001       | 0.1.0-001      | 0.1.0          | 0.1.0          | 0.1.0          |  X             | 0.1.0          | 0.1.0          | X              |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.0-002       | 0.1.0-001      | 0.1.0          | 0.1.0          | 0.1.0          |  X             | 0.1.0          | 0.1.0          | X              |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.1           | 0.1.1          | 0.2.0          | 0.2.0          | 0.2.1          |  X             | 0.2.X          | 0.2.0          | 0.1.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.2.0           | 0.2.0          | 0.4.0-001      | 0.3.0          | 0.3.0          |  X             | 0.3.0          | 0.3.0          | 0.2.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.2.0-001       | 0.2.0-001      | 0.4.1          | 0.3.0-001      | 0.3.0-001      |  X             | 0.3.0-001      | 0.3.0          | 0.2.0-001      |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.2.1           | 0.2.0-001      | 0.4.1          | 0.3.0-001      | 0.3.0-001      |  X             | 0.3.0-001      | 0.3.0          | 0.3.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.3.0           | 0.3.0          | 0.5.0          | 0.4.0          | 0.4.0          | 0.1.0          | X              | 0.4.0          | 0.4.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.3.3           | 0.3.3          | 0.5.1          | 0.4.1          | 0.4.1          | 0.1.X          | X              | 0.4.1          | 0.4.X          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.3.4           | 0.3.3          | 0.5.1          | 0.4.1          | 0.4.1          | 0.1.X          | X              | 0.4.1          | 0.4.X          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+ 
| 0.4.0-SNAPSHOT  | 0.4.0-SNAPSHOT | 0.6.0-SNAPSHOT | 0.5.0-SNAPSHOT | 0.5.0-SNAPSHOT | 0.2.0-SNAPSHOT | X              | 0.5.0-SNAPSHOT | 0.5.0-SNAPSHOT |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+


Sandbox
=======

If you want to test Crossdata you can get our Sandbox follow the instructions of this `link <doc/src/site/sphinx/Sandbox.rst>`_

License
=======

Stratio Crossdata is licensed as `Apache2 <http://www.apache.org/licenses/LICENSE-2.0.txt>`_

Licensed to STRATIO (C) under one or more contributor license agreements.
See the NOTICE file distributed with this work for additional information 
regarding copyright ownership.  The STRATIO (C) licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
