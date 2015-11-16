============
Introduction
============

|TravisIL|_

.. |TravisIL| image:: https://api.travis-ci.org/Stratio/Crossdata.svg?branch=master
.. _TravisIL: https://travis-ci.org/Stratio/Crossdata?branch=master

|GitterIL|_

.. |GitterIL| image:: https://badges.gitter.im/Stratio/Crossdata.svg
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

- Crossdata Core: It is a library that you can deploy in any existent system using Spark with no changes, just add the Crossdata jar file.
SparkSQL extension with improvements in the DataSource API and new features. Crossdata expands the functionalities
of Apache Spark in order to provide a richer SQL-like language, to improve some aspects (metastore, execution trees, ...)
- Crossdata Server: Provides a multi-user environment to SparkSQL, giving a reliable architecture with
high-availability and scalability out of the box.
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

- `Feature guide <doc/src/site/sphinx/0_feature_guide.rst>`__
- `Architecture guide <doc/src/site/sphinx/1_architecture_guide.rst>`__
- `Getting started <doc/src/site/sphinx/2_getting_started.rst>`__
- `Configuration <doc/src/site/sphinx/3_configuration.rst>`__
- `Using Crossdata <doc/src/site/sphinx/4_using_crossdata.rst>`__
- `Quick reference <doc/src/site/sphinx/5_quick_reference.rst>`__
- `Reference guide <doc/src/site/sphinx/6_reference_guide.rst>`__
- `Crossdata in production <doc/src/site/sphinx/7_crossdata_production.rst>`__
- `Best practices and recommendations <doc/src/site/sphinx/8_best_practices.rst>`__
- `Crossdata in the Stratio Platform <doc/src/site/sphinx/9_stratio_platform.rst>`__
- `Benchmarks <doc/src/site/sphinx/10_crossdata_benchmarks.rst>`__
- `FAQs <doc/src/site/sphinx/11_crossdata_faqs.rst>`__


===========
Get support
===========

You can send us issues in https://crossdata.atlassian.net/

You can also find help in https://groups.google.com/forum/#!forum/crossdata-users

Alternatively, you can try to reach us at our IRC channel `#stratio-crossdata <http://webchat.freenode.net/?channels=#stratio-crossdata>`_ . Feel free to ask, if we are available we'll try to help you.

=============
Release notes
=============

TODO


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



.. image:: https://badges.gitter.im/Join%20Chat.svg
   :alt: Join the chat at https://gitter.im/Stratio/Crossdata
   :target: https://gitter.im/Stratio/Crossdata?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge