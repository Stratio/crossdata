============
Introduction
============

|GitterIL|_

.. |GitterIL| image:: https://badges.gitter.im/Stratio/Crossdata.svg?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
.. _GitterIL: https://gitter.im/Stratio/Crossdata

Crossdata is a distributed framework and a fast and general-purpose computing system powered by Apache Spark. It
unifies the interaction with different sources supporting multiple datastore technologies thanks to its generic
architecture and a custom SQL-like language using SparkSQL as the core of the project. Supporting multiple
architectures imposes two main challenges: how to normalize the access to the datastores, and how to cope with
datastore limitations. Crossdata provides connectors which can access to multiple datastores natively, speeding up
the queries by avoiding the overhead and the block of resources of the Spark Cluster when possible. We offer a shell,
Java and Scala APIs, JDBC and ODBC for BI tools.

This project is aimed for those who want to manage only one API to access to multiple datastores with different nature,
get rid of the drawbacks of Apache Spark, perform analytics from a BI tool and speed up your queries effortlessly.

Crossdata is broken up into the following components:

- Crossdata Core: It is a library that you can deploy in any existent system using Spark with no changes, just add the Crossdata jar file. SparkSQL extension with improvements in the DataSource API and new features. Crossdata expands the functionalities of Apache Spark in order to provide a richer SQL-like language, to improve some aspects (metastore, execution trees, ...)
- Crossdata Server: Provides a multi-user environment to SparkSQL, giving a reliable architecture with high-availability and scalability out of the box.
- Crossdata Driver: Entry point with an API for both Scala and Java. Crossdata ODBC/JDBC uses this driver.
- Crossdata Connectors: Take advantage of the Crossdata DataSource API to speed up the queries in specific datasources
and provide new features.

We include some Spark connectors optimized to access to each datasource, but Crossdata is fully compatible with any
connector developed by the Spark community.

- Apache Cassandra connector powered by Datastax-Spark-Connector
- MongoDB connector powered by Stratio-Spark-Connector
- ElasticSearch connector powered by Elastic-Spark-Connector


Main Crossdata's advantages over other options:

- JDBC/ODBC self-contained. Other solutions require Hive.
- Faster queries using native access.
- Metadata discovery.
- Datasource functions (Spark only can execute its own UDFs).
- High-availability.


=============
Documentation
=============

For a complete documentation, please, visit https://stratio.atlassian.net/wiki/display/CROSSDATA1x1/Home.

===========
Get support
===========

You can send us issues in https://crossdata.atlassian.net.

You can also find help in https://groups.google.com/forum/#!forum/crossdata-users.

Alternatively, you can try to reach us at gitter or our IRC channel `#stratio-crossdata <http://webchat.freenode.net/?channels=#stratio-crossdata>`_. Feel free to ask,
if we are available we'll try to help you.


=============
Release notes
=============

Features and changes are detailed in the `changelog <CHANGELOG.md>`_.

=======
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

