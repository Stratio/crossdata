============
Introduction
============

Crossdata is a distributed framework and a fast and general-purpose computing system powered by Apache Spark. It
unifies the interaction with different sources supporting multiple datastore technologies thanks to its generic
architecture and a custom SQL-like language using SparkSQL as the core of the project. Supporting multiple
architectures imposes two main challenges: how to normalize the access to the datastores, and how to cope with
datastore limitations. Crossdata provides connectors which can access to multiple datastores natively, speeding up
the queries by avoiding the overhead and the block of resources of the Spark Cluster when possible. We offer a shell,
 Java and Scala APIs, JDBC and ODBC for BI tools.

In addition, Crossdata expands the functionalities of Apache Spark in order to provide a richer SQL-like language, to
improve some aspects (metadastore, execution trees, ...) and to give a more reliable architecture with
high-availability and scalability out of the box.

This project is aimed for those who want to manage only one API to access to multiple datastores with different nature,
get rid of the drawbacks of Apache Spark, perform analytics from a BI tool and speed up your queries effortlessly.
Moreover, Crossdata is a library that you can deploy in any existent system using Spark with no changes, just add the
Crossdata jar file.

We include some Spark connectors optimized to access to each datasource, but Crossdata is fully compatible with any
connector developed by the Spark community.

- Apache Cassandra connector powered by Datastax-Spark-Connector
- MongoDB connector powered by Stratio-Spark-Connector
- (Coming soon) ElasticSearch powered by

(Coming soon) Crossdata will allow us to merge data from batch and streaming resources using the extended SQL-like
grammar of Spark and the Crossdata API.


----


=============
Documentation
=============

* [Feature guide](doc/src/site/sphinx/0_feature_guide.rst)
* [Architecture guide](doc/src/site/sphinx/1_architecture_guide.rst)
* [Getting started](doc/src/site/sphinx/2_getting_started.rst)
* [Configuration](doc/src/site/sphinx/3_configuration.rst)
* [Using Crossdata](doc/src/site/sphinx/4_using_crossdata.rst)
* [Quick reference](doc/src/site/sphinx/5_quick_reference.rst)
* [Reference guide](doc/src/site/sphinx/6_reference_guide.rst)
* [Crossdata in production](doc/src/site/sphinx/7_crossdata_production.rst)
* [Best practices and recommendations](doc/src/site/sphinx/8_best_practices.rst)
* [Crossdata in the Stratio Platform](doc/src/site/sphinx/9_stratio_platform.rst)
* [Benchmarks](doc/src/site/sphinx/10_crossdata_benchmarks.rst)
* [FAQs](doc/src/site/sphinx/11_crossdata_faqs.rst)


----


===========
Get support
===========

You can send us issues in https://crossdata.atlassian.net/

You can also find help in https://groups.google.com/forum/#!forum/crossdata-users

TODO:


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

