---
title: First steps with Stratio Meta
---

Meta is a simple and elegant framework designed to unify batch and stream processing using a common language. The Meta language maintains the friendliness of SQL and CQL while providing streaming processing capabilities.

More information about Stratio META can be found at [Stratio Meta Overview](http://www.openstratio.org/about/stratio-meta/ "Stratio Meta").

Table of Contents
=================

-   [Before you start](#befyoustart)
    -   [Prerequisites](#prerequisites)
    -   [Configuration](#configuration)
-   [Creating the keyspace and table](#creatint-the-keyspace-and-table)
    -   [Step 1: Create the keyspace](#step-1-create-the-keyspace)
    -   [Step 2: Create the table schemas](#step-2-create-the-table-schemas)
    -   [Step 3: Create indexes](#step-3-create-indexes)
        -   [Create a default index](#defaultindex)
        -   [Create a Lucene index](#luceneindex)
-   [Inserting Data](#inserttable)
    -   [Step 4: Insert into Table users](#insertusers)
    -   [Step 5: Insert into Table wallet](#insertwallet)
-   [Querying Data](#showdata)
    -   [Step 6: Select From](#showtables)
    -   [Step 7: Inner Join between tables](#joinbetweentables)
    -   [Step 8: Where clause on a non indexed column](#selectfromnotindexed)
-   [Delete Data and Remove Schemas](#deletedata)
    -   [Step 9: Delete](#delete)
    -   [Step 10: Drop Index](#dropindex)
    -   [Step 11: Drop Table](#droptable)
    -   [Step 12: Drop Keyspace](#dropkeyspace)
-   [Where to go from here](#wheretogofromhere)

Before you start
================

Prerequisites
-------------

-   A [Stratio installation](http://www.openstratio.org/getting-started/ "Getting Started")
-   Basic knowledge of CQL (or a SQL like language)

Configuration
-------------

Read this section carefully if you are on a multi nodes Cassandra cluster, otherwise you can skip it.

Make sure the cassandra.yaml configuration file is in the classpath and that the following properties are configured properly:

-   cluster\_name
-   listen\_address
-   storage\_port
-   rpc\_address
-   rpc\_port
-   seed\_provider, and seeds

Information about how to set these parameters can be found in the [Cassandra documentation](http://www.datastax.com/documentation/cassandra/2.0/webhelp/index.html#cassandra/configuration/../../cassandra/configuration/configCassandra_yaml_r.html "Cassandra Configuration Documentation").

Creating the keyspace and table
===============================

Step 1: Create the keyspace
---------------------------

Statement

~~~~ {.prettyprint .lang-meta}
CREATE KEYSPACE IF NOT EXISTS test
WITH replication = {class: SimpleStrategy, replication_factor: 3}
AND durable_writes = false;
~~~~

Result

~~~~ {.code}
metash-sh:USER> CREATE KEYSPACE IF NOT EXISTS test
metash-sh:USER> WITH replication = {class: SimpleStrategy, replication_factor: 3}
metash-sh:USER> AND durable_writes = false;
Result: QID: ca49b15f-6fe8-4c84-b003-47849c97ac29
OK
Result page: 0
~~~~

Statement

~~~~ {.prettyprint .lang-meta}
USE test;
~~~~

Result

~~~~ {.code}
metash-sh:USER> USE test;
QID: 23465c55-4e8e-4a6a-8a54-73f0597966e2
~~~~

Step 2: Create the table schemas
--------------------------------

Statement

~~~~ {.prettyprint .lang-meta}
CREATE TABLE IF NOT EXISTS users (
      user_id      int,
      location_id  int,
      message      varchar,
      verified     boolean,
      email        varchar,
      PRIMARY KEY (user_id, location_id)
);
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> CREATE TABLE IF NOT EXISTS users (
metash-sh:USER:test>       user_id      int,
metash-sh:USER:test>       location_id  int,
metash-sh:USER:test>       message      varchar,
metash-sh:USER:test>       verified     boolean,
metash-sh:USER:test>       email        varchar,
metash-sh:USER:test>       PRIMARY KEY (user_id, location_id)
metash-sh:USER:test> );
QID: 151d6d8d-1d69-4748-a723-51a0cfbff9ad
Result: QID: 151d6d8d-1d69-4748-a723-51a0cfbff9ad
OK
Result page: 0
~~~~

Statement

~~~~ {.prettyprint .lang-meta}
CREATE TABLE IF NOT EXISTS wallet (
      wallet_id int,
      user_id int,
      amount int,
      city varchar,
      PRIMARY KEY(wallet_id,user_id)
);
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> CREATE TABLE IF NOT EXISTS wallet (
metash-sh:USER:test>       wallet_id int,
metash-sh:USER:test>       user_id int,
metash-sh:USER:test>       amount int,
metash-sh:USER:test>       city varchar,
metash-sh:USER:test>       PRIMARY KEY(wallet_id,user_id)
metash-sh:USER:test> );
QID: 553de77c-38c6-4de2-b8d5-1f2a0d3f6236
Result: QID: 553de77c-38c6-4de2-b8d5-1f2a0d3f6236

OK
Result page: 0
~~~~

Step 3: Create Indexes
----------------------

### Create a default index

Statement

~~~~ {.prettyprint .lang-meta}
CREATE DEFAULT INDEX IF NOT EXISTS users_index ON users (email);
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> CREATE DEFAULT INDEX IF NOT EXISTS users_index ON users (email);
QID: 47da4048-7ed7-47c3-91f2-285985f5d872

Result: QID: 47da4048-7ed7-47c3-91f2-285985f5d872
OK
Result page: 0
~~~~

### Create a Lucene index

Statement

~~~~ {.prettyprint .lang-meta}
CREATE LUCENE INDEX IF NOT EXISTS lucene_index
ON users (user_id, email, message);
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> CREATE LUCENE INDEX IF NOT EXISTS lucene_index ON users (user_id, email, message);
QID: 9c2fdcd7-0ed7-4aae-b6e3-598a6aa0c62c

Result: QID: 9c2fdcd7-0ed7-4aae-b6e3-598a6aa0c62c
OK
Result page: 0
~~~~

Inserting Data
==============

Step 4: Insert into Table users
-------------------------------

Statement

~~~~ {.prettyprint .lang-meta}
INSERT INTO users (user_id, location_id, email)
VALUES (100, 28010, 'jdoe@example.com');
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> INSERT INTO users (user_id, location_id, email)
metash-sh:USER:test> VALUES (100, 28010, 'jdoe@example.com');
QID: 1803ac45-2d1f-433b-b778-bd3acb44a3dd

Result: QID: 1803ac45-2d1f-433b-b778-bd3acb44a3dd
OK
Result page: 0
~~~~

Step 5: Insert into Table wallet
--------------------------------

Statement

~~~~ {.prettyprint .lang-meta}
INSERT INTO wallet (wallet_id, user_id, amount, city)
VALUES (200, 100, 5000, 'Barcelona');
INSERT INTO wallet (wallet_id, user_id, amount, city)
VALUES (100, 100, 2000, 'Madrid');
INSERT INTO wallet (wallet_id, user_id, amount, city)
VALUES (100, 200, 4000, 'London');
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> INSERT INTO wallet (wallet_id, user_id, amount, city)
metash-sh:USER:test> VALUES (200, 100, 5000, 'Barcelona');
QID: 881f66ca-6a02-45f2-bc6b-cd7790b1f115
metash-sh:USER:test> INSERT INTO wallet (wallet_id, user_id, amount, city)
metash-sh:USER:test> VALUES (100, 100, 2000, 'Madrid');
QID: 95acfcf1-6d56-4e86-afbb-17898226adcd
metash-sh:USER:test> INSERT INTO wallet (wallet_id, user_id, amount, city)
metash-sh:USER:test> VALUES (100, 200, 4000, 'London');
QID: 043bed5a-a8e5-475d-a81a-2313b710ba92
~~~~

Querying Data
=============

Step 6: Select From
-------------------

Statement

~~~~ {.prettyprint .lang-meta}
SELECT email
FROM users
WHERE user_id = 100;
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> SELECT email
metash-sh:USER:test> FROM users
metash-sh:USER:test> WHERE user_id = 100;
QID: 4b3efb67-f3dd-4ba2-a76b-a691d65832df
Partial result: true
--------------------
| email            |
--------------------
| jdoe@example.com |
--------------------

Result page: 0
~~~~

Step 7: Inner Join between tables
---------------------------------

Statement

~~~~ {.prettyprint .lang-meta}
SELECT u.user_id, u.location_id, u.email, w.wallet_id, w.amount, w.city
FROM test.users u
INNER JOIN test.wallet w ON u.user_id = w.user_id;
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> SELECT u.user_id, u.location_id, u.email, w.wallet_id, w.amount, w.city
metash-sh:USER:test> FROM test.users u
metash-sh:USER:test> INNER JOIN test.wallet w ON u.user_id = w.user_id;
QID: b3a18378-9f60-4dea-b257-c9567ea7d512

Partial result: true
-----------------------------------------------------------------------------
| user_id | location_id | email            | wallet_id | amount | city      |
-----------------------------------------------------------------------------
| 100     | 28010       | jdoe@example.com | 200       | 5000   | Barcelona |
| 100     | 28010       | jdoe@example.com | 100       | 2000   | Madrid    |
-----------------------------------------------------------------------------

Result page: 0
~~~~

Step 8: Where Clause on a non indexed Column
--------------------------------------------

Statement

~~~~ {.prettyprint .lang-meta}
SELECT u.user_id, u.email, w.wallet_id, w.amount, w.city
FROM test.users u
INNER JOIN test.wallet w ON u.user_id = w.user_id
WHERE w.city = 'Madrid';
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> SELECT u.user_id, u.email, w.wallet_id, w.amount, w.city
metash-sh:USER:test> FROM test.users u
metash-sh:USER:test> INNER JOIN test.wallet w ON u.user_id = w.user_id
metash-sh:USER:test> WHERE w.city = 'Madrid';
QID: d0f012f4-308e-4760-aabf-4f43d25f1a79

Partial result: true
------------------------------------------------------------
| user_id | email            | wallet_id | amount | city   |
------------------------------------------------------------
| 100     | jdoe@example.com | 100       | 2000   | Madrid |
------------------------------------------------------------

Result page: 0
~~~~

Delete Data and Remove Schemas
==============================

Step 9: Delete Data
-------------------

Statement

~~~~ {.prettyprint .lang-meta}
TRUNCATE users;
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> TRUNCATE users;
QID: eec4d679-3e3a-44d9-a718-30cd525c416a
~~~~

Step 10: Drop Index
-------------------

Statement

~~~~ {.prettyprint .lang-meta}
DROP INDEX IF EXISTS users_index;
DROP INDEX IF EXISTS lucene_index;
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> DROP INDEX IF EXISTS users_index;
QID: 4ce44df2-0ce0-4de3-9834-d903fff8d9f6
metash-sh:USER:test> DROP INDEX IF EXISTS lucene_index;
QID: e7cf3999-165d-4053-8b03-f30d4e084f5d
~~~~

Step 11: Drop Table
-------------------

Statement

~~~~ {.prettyprint .lang-meta}
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS wallet;
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> DROP TABLE IF EXISTS users;
QID: 2a97726d-ffb8-4618-a411-bc517f400db9
metash-sh:USER:test> DROP TABLE IF EXISTS wallet;
QID: 24f65aba-5c50-4731-a812-621b89778aa1
~~~~

Step 12: Drop Keyspace
----------------------

Statement

~~~~ {.prettyprint .lang-meta}
DROP KEYSPACE IF EXISTS test;
~~~~

Result

~~~~ {.code}
metash-sh:USER:test> DROP KEYSPACE IF EXISTS test;
QID: a63df932-296e-41ef-8e4d-d46c6c801911
~~~~

Where to go from here
=====================

To learn more about Stratio Meta, we recommend to visit the [Meta Reference](http://www.openstratio.org/manuals/meta-reference/ "Meta Reference").
