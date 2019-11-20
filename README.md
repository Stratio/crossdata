
Discontinued
===================

This repository has been discontinued. [Stratio Crossdata](https://www.stratio.com/a-data-centric-product/) has moved to a commercial license. Please [contact Stratio Big Data Inc.](https://www.stratio.com/contact/) for further info.

Introduction
============
![Project unmaintained](https://img.shields.io/badge/project-unmaintained-red.svg) [![GitterIL]( https://badges.gitter.im/Stratio/Crossdata.svg?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)](https://gitter.im/Stratio/Crossdata) [![Coverage Status](https://coveralls.io/repos/github/Stratio/Crossdata/badge.svg?branch=master)](https://coveralls.io/github/Stratio/Crossdata?branch=master)

Crossdata is a distributed framework and a fast and general-purpose computing system powered by Apache Spark. It unifies the interaction with different sources supporting multiple datastore technologies thanks to its generic architecture and a custom SQL-like language using SparkSQL as the core of the project. In addition, Crossdata supports batch and streaming processing so that you can mix data from both input technologies. Supporting multiple architectures imposes two main challenges: how to normalize the access to the datastores, and how to cope with datastore limitations. Crossdata provides connectors which can access to multiple datastores natively, speeding up the queries by avoiding the overhead and the block of resources of the Spark Cluster when possible. We offer a shell, Java and Scala APIs, JDBC and ODBC for BI tools.

This project is aimed for those who want to manage only one API to access to multiple datastores with different nature, get rid of the drawbacks of Apache Spark, perform analytics from a BI tool and speed up your queries effortlessly.

Crossdata is broken up into the following components:

- Crossdata Core: It is a library that you can deploy in any existent system using Spark with no changes, just add the Crossdata jar file. SparkSQL extension with improvements in the DataSource API and new features. Crossdata expands the functionalities of Apache Spark in order to provide a richer SQL-like language, to improve some aspects (metastore, execution trees, ...)
- Crossdata Server: Provides a multi-user environment to SparkSQL, giving a reliable architecture with high-availability and scalability out of the box.
- Crossdata Driver: Entry point with an API for both Scala and Java. Crossdata ODBC/JDBC uses this driver. - Crossdata Connectors: Take advantage of the Crossdata DataSource API to speed up the queries in specific datasources and provide new features.

We include some Spark connectors optimized to access to each datasource, but Crossdata is fully compatible with any connector developed by the Spark community.

- Apache Cassandra connector powered by Datastax-Spark-Connector
- MongoDB connector powered by Stratio-Spark-Connector
- ElasticSearch connector powered by Elastic-Spark-Connector

Moreover, some datasources are already included, avoiding to import them manually:

- Spark-CSV
- Spark-Avro

Main Crossdata's advantages over other options:

- JDBC/ODBC self-contained. Other solutions require Hive.
- Faster queries using native access (including subdocuments and array elements).
- Streaming queries from a SQL-like interface.
- Metadata discovery.
- Datasource functions (Spark only can execute its own UDFs).
- High-availability and load balancing.
- Logical views.
- Full SQL interface for documents with nested subdocuments and nested arrays.
- Persistent metadata catalog.
- Common interface for datasources management.
- Creation of tables in the datastores.
- Drop of tables from the datastores.
- Insert with values queries as in typical SQL.
- Service Discovery

===================
Spark Compatibility
===================

| Crossdata Version | Spark Version |
|-------------------|:--------------|
| 1.7.X	            | 1.6.X         |
| 1.6.X	            | 1.6.X         |
| 1.5.X	            | 1.6.X         |
| 1.4.X	            | 1.6.X         |
| 1.3.X	            | 1.6.X         |
| 1.2.X             |	1.5.X         |
| 1.1.X             |	1.5.X         |
| 1.0.X             |	1.5.X         |


===========
Get support
===========

You can send us issues in https://crossdata.atlassian.net.

You can also find help in https://groups.google.com/forum/#!forum/crossdata-users.

There is also a gitter channel available: https://gitter.im/Stratio/Crossdata.

Alternatively, you can try to reach us at gitter or our IRC channel [#stratio-crossdata](http://webchat.freenode.net/?channels=#stratio-crossdata). Feel free to ask, if we are available we'll try to help you.


=============
Release notes
=============

Features and changes are detailed in the [changelog](CHANGELOG.md).

=======
License
=======

Stratio Crossdata is licensed as [Apache2](http://www.apache.org/licenses/LICENSE-2.0.txt)

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
