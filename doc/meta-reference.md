---
title: Meta Reference
---

Table of Contents
=================

-   [The META Console](#the-meta-console)
-   [Statements](#statements):
    -   [CREATE](#create):
        -   [CREATE KEYSPACE](#create-keyspace)
        -   [CREATE TABLE](#create-table)
        -   [CREATE DEFAULT INDEX](#create-default-index)
        -   [CREATE LUCENE INDEX](#create-lucene-index)
    -   [DELETE](#delete)
    -   [DESCRIBE](#describe)
    -   [DROP](#drop):
        -   [DROP KEYSPACE](#drop-keyspace)
        -   [DROP TABLE](#drop-table)
        -   [DROP INDEX](#drop-index)
    -   [INSERT INTO](#insert-into)
    -   [LIST PROCESS](#list-process)
    -   [SELECT](#select)
        -   [Persistent tables](#persistent-tables):
            -   [INNER JOIN](#inner-join)
            -   [WHERE CLAUSE](#where-clause)
        -   [Ephemeral tables](#ephemeral-tables)
    -   [STOP PROCESS](#stop-process)
    -   [TRUNCATE](#truncate)
    -   [USE](#use)
-   [Datatypes](#datatypes)

The META Console
================

To start the META console:

~~~~ {.code}
$ metash
~~~~

Available commands are:

-   help &lt;statement>: see below for a comprehensive list of META statements.
-   exit: to leave the console.

Statements
==========

CREATE
------

The CREATE command can be used with different targets. For more information check one of the following:

-   [CREATE KEYSPACE](#CREATE-KEYSPACE)
-   [CREATE TABLE](#CREATE-TABLE)
-   [CREATE DEFAULT INDEX](#CREATE-DEFAULT-INDEX)
-   [CREATE LUCENE INDEX](#CREATE-LUCENE-INDEX)

### CREATE KEYSPACE

Creates a new keyspace on the system specifying its replication properties.

Syntax:

~~~~ {.prettyprint .lang-meta}
CREATE KEYSPACE (IF NOT EXISTS)? &lt;keyspace_name>
WITH replication = &lt;options_map>
(AND durable_writes = &lt;boolean>)?;
~~~~

where durable_writes defaults to true.

As of Cassandra 2.0.x, the replication class can be SimpleStrategy for single datacenter deployments, or NetworkTopologyStrategy for a multi-datacenter setup.

Example:

~~~~ {.prettyprint .lang-meta}
CREATE KEYSPACE test
WITH replication = {class: SimpleStrategy, replication_factor: 3}
AND durable_writes = false;
~~~~

### CREATE TABLE

Creates a new table on the selected keyspace.

Syntax:

~~~~ {.prettyprint .lang-meta}
CREATE TABLE (IF NOT EXISTS)? &lt;tablename>
            '('&lt;definition> (',' &lt;definition>)*')'
             (WITH &lt;option> (AND &lt;option>)*)?;
~~~~

where:

~~~~ {.prettyprint .lang-meta}
&lt;column-definition> ::= &lt;identifier> &lt;type> (PRIMARY KEY)?
                      | PRIMARY KEY '('&lt;partition-key>(',' &lt;identifier>)*')'

&lt;partition-key> ::= &lt;partition-key>
                      | '('&lt;partition-key>(',' &lt;identifier>)*')'

&lt;partition-key> ::= &lt;identifier>
                      | '('&lt;identifier>(',' &lt;identifier>)*')'

&lt;option> ::= &lt;property>
               | COMPACT STORAGE
               | CLUSTERING ORDER

&lt;property> ::= 'EPHEMERAL = true'
~~~~

See [Apache Cassandra CQL3 documentation](http://cassandra.apache.org/doc/cql3/CQL.html#createTableStmt "Apache Cassandra CQL3 documentation") for a full list of available table properties.

The &lt;primary_key> can take two forms:

1.  A list of columns in the form of (column_name_0, …, column_name_m), where column_name_0 is the partition key, and columns 1 to n are part of the clustering key.
2.  A separated list of columns for the partition and clustering keys in the form of ((column_name_0, column_name_1), column_name_2, …, column_name_n), where columns 0 and 1 are part of the partition key, and columns 2 to n are part of the clustering key.

Example:

~~~~ {.prettyprint .lang-meta}
CREATE TABLE users (
      user_id      int,
      location_id  int,
      message      varchar,
      verified     boolean,
      email        varchar,
      PRIMARY KEY (user_id, location_id)
);
~~~~

~~~~ {.prettyprint .lang-meta}
CREATE TABLE wallet (
      wallet_id int,
      user_id int,
      amount int,
      city varchar,
      PRIMARY KEY(wallet_id,user_id)
);
~~~~

~~~~ {.prettyprint .lang-meta}
CREATE TABLE ratings (
      user_id int,
      rating double,
      member boolean,
      PRIMARY KEY(user_id)
) WITH EPHEMERAL = true;
~~~~

For more information about supported column types see [Datatypes](#datatypes "List of supported datatypes").

### CREATE DEFAULT INDEX

Creates an index on a set of columns of a table.

Syntax:

~~~~ {.prettyprint .lang-meta}
CREATE DEFAULT INDEX (IF NOT EXISTS)?
&lt;index_name>? ON &lt;tablename> '(' &lt;columname> (',' &lt;columnname>)* ')' ;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
CREATE DEFAULT INDEX users_index ON users (email);
~~~~

### CREATE LUCENE INDEX

Creates Lucene-based index on a set of columns. For each column, the Lucene index is created using 
the data type specified in the table schema. Additionally, the user can modify the mapping with the 
use of the WITH OPTIONS statement.

Syntax:

~~~~ {.prettyprint .lang-meta}
CREATE LUCENE INDEX (IF NOT EXISTS)?
&lt;index_name>? ON &lt;tablename> '(' &lt;columname> (',' &lt;columnname>)* ')' ;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
CREATE LUCENE INDEX users_index ON users (user_id, email, message);
~~~~

DELETE
------

Deletes row(s) of a table.

Syntax:

~~~~ {.prettyprint .lang-meta}
DELETE ( &lt;selection> (',' &lt;selection> )* )?
FROM &lt;tablename> WHERE &lt;where-clause>;
~~~~

where

~~~~ {.prettyprint .lang-meta}
&lt;selection>    ::= &lt;identifier> ( '[' &lt;term> ']' )?

&lt;where-clause> ::= &lt;relation> ( AND | OR &lt;relation> )*

&lt;relation>     ::= &lt;identifier> &lt;operator> &lt;term>
                     | &lt;identifier> (ALL | ANY) '(' ( &lt;term> (',' &lt;term>)* )? ')'
                     | &lt;identifier> (ALL | ANY) '?'

&lt;operator>     ::= ( '=' | '>' | '>=' | '&lt;' | '&lt;=' )
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
DELETE FROM users WHERE user_id = 100;
~~~~

DESCRIBE
--------

Provides information about the table or keyspace.

Syntax:

~~~~ {.prettyprint .lang-meta}
DESCRIBE ( KEYSPACE ( &lt;keyspace> )? | TABLE &lt;tablename> );
DESCRIBE ( KEYSPACES | TABLES );
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
DESCRIBE TABLE users;
~~~~

DROP
----

The DROP command can be used with different targets. For more information check one of the following:

-   [DROP KEYSPACE](#DROP-KEYSPACE)
-   [DROP TABLE](#DROP-TABLE)
-   [DROP INDEX](#DROP-INDEX)

### DROP KEYSPACE

Removes an existing keyspace.

Syntax:

~~~~ {.prettyprint .lang-meta}
DROP KEYSPACE (IF EXISTS)? &lt;keyspace>;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
DROP KEYSPACE IF EXISTS test;
~~~~

### DROP TABLE

Removes an existing table.

Syntax:

~~~~ {.prettyprint .lang-meta}
DROP TABLE (IF EXISTS)? &lt;tablename>;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
DROP TABLE IF EXISTS users;
~~~~

### DROP INDEX

Removes an existing index.

Syntax:

~~~~ {.prettyprint .lang-meta}
DROP INDEX (IF EXISTS)? ( &lt;keyspace>. )? &lt;index_name>;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
DROP INDEX users_index;
~~~~

INSERT INTO
-----------

Inserts new values in a table of the keyspace

Syntax:

~~~~ {.prettyprint .lang-meta}
INSERT INTO &lt;tablename> '(' &lt;identifier> ( ',' &lt;identifier> )* ')'
VALUES '(' &lt;term-or-literal> ( ',' &lt;term-or-literal> )* ')';
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
INSERT INTO users (user_id, location_id, email)
VALUES (100, 28010, 'jdoe@example.com');
~~~~

~~~~ {.prettyprint .lang-meta}
INSERT INTO wallet (wallet_id, user_id, amount, city)
VALUES (200, 100, 5000, 'Barcelona');
INSERT INTO wallet (wallet_id, user_id, amount, city)
VALUES (100, 100, 2000, 'Madrid');
INSERT INTO wallet (wallet_id, user_id, amount, city)
VALUES (100, 200, 4000, 'London');
~~~~

LIST PROCESS
------------

List active processes.

Syntax:

~~~~ {.prettyprint .lang-meta}
LIST PROCESS
~~~~

Example of output:

~~~~ {.prettyprint .lang-meta}
-------------------------------------------------------------------------------------------------
| QID                                  | Table   | Query                                        |
-------------------------------------------------------------------------------------------------
| fbafe0af-8bb3-4c45-98a4-288d1797f31b | ratings | SELECT * FROM ratings WITH WINDOW 6 SECONDS  |
-------------------------------------------------------------------------------------------------
~~~~

SELECT
------

### Persistent tables

Retrieves a subset of data from an existing table.

Syntax:

~~~~ {.prettyprint .lang-meta}
SELECT &lt;select-clause>
FROM &lt;tablename> ( &lt;alias> )?
( INNER JOIN &lt;tablename> ON '('? &lt;field1> = &lt;field2> ')'? )?
( WHERE &lt;where-clause> )?
( ORDER BY &lt;identifier> ('ASC'|'DESC')? (, &lt;identifier> ('ASC'|'DESC')? )* )?
( GROUP BY &lt;identifier> (, &lt;identifier>)* )?
~~~~

where

~~~~ {.prettyprint .lang-meta}
&lt;select-clause>  ::= DISTINCT? &lt;selection-list>
                      | COUNT '(' ( '*' | '1' ) ')' (AS &lt;identifier>)?

&lt;selection-list> ::= &lt;selector> (AS &lt;identifier>)?
                       ( ',' &lt;selector> (AS &lt;identifier>)? )*
                       | '*'

&lt;selector>       ::= &lt;identifier>
                       | &lt;agg_function> '(' (&lt;selector> (',' &lt;selector>)*)? ')'

&lt;agg_function>   ::= 'COUNT' | 'AVG' | 'SUM' | 'MIN' | 'MAX'

&lt;where-clause>   ::= &lt;condition> ( AND &lt;condition> )*

&lt;condition>      ::= &lt;identifier> &lt;operator> &lt;term> |
                     &lt;identifier> BETWEEN &lt;term1> AND &lt;term2> |
                     &lt;identifier> IN '(' &lt;term1> (, &lt;term2>)* ')'

&lt;operator>       ::= '=' | '>' | '>=' | '&lt;' | '&lt;=' | '&lt;>' | 'MATCH'
~~~~

INNER JOIN permits to combine two tables A and B by comparing each row in A with each one in B selecting
 all rows that match the join-predicate.

WHERE &lt;where-clause> specifies the conditions that a row in &lt;tablename> needs to fulfil in order to
 be selected. It can contain a condition on a non-indexed column.

ORDER BY will sort the results in ascending order by default.

Example:

~~~~ {.prettyprint .lang-meta}
SELECT email FROM users
WHERE user_id > 100
ORDER BY email DESC;
~~~~

#### INNER JOIN

Example:

~~~~ {.prettyprint .lang-meta}
SELECT user_id, email, wallet_id, amount, city
FROM test.users
INNER JOIN test.wallet ON users.user_id = wallet.user_id;

---------------------------------------------------------------
| user_id | email            | wallet_id | amount | city      |
---------------------------------------------------------------
| 100     | jdoe@example.com | 200       | 5000   | Barcelona |
| 100     | jdoe@example.com | 100       | 2000   | Madrid    |
| 100     | jdoe@example.com | 300       | 4000   | Bilbao    |
---------------------------------------------------------------
~~~~

#### WHERE CLAUSE

The WHERE clause can contain a non-indexed column.

Example:

~~~~ {.prettyprint .lang-meta}
SELECT user_id, email, wallet_id, amount, city
FROM test.users
INNER JOIN test.wallet ON users.user_id = wallet.user_id
WHERE wallet.city = 'Madrid';

------------------------------------------------------------
| user_id | email            | wallet_id | amount | city   |
------------------------------------------------------------
| 100     | jdoe@example.com | 100       | 2000   | Madrid |
------------------------------------------------------------
~~~~

### Ephemeral tables

Syntax:

~~~~ {.prettyprint .lang-meta}
SELECT &lt;select-clause>
FROM &lt;tablename>
WITH WINDOW &lt;window_length> ;
~~~~

where

~~~~ {.prettyprint .lang-meta}
&lt;select-clause>  ::= '*' | &lt;selection-list>

&lt;window_length>  ::= &lt;size> &lt;unit>

&lt;unit>           ::= ('year' | 'years') |
                     ('month' | 'months') |
                     ( 'week' | 'weeks' ) |
                     ( 'day' | 'days' ) |
                     ( 'hour' | 'hours' ) |
                     ( 'minute' | 'minutes' | 'min' ) |
                     ( 'second' | 'seconds' | 'sec' ) |
                     ( 'millisecond' | 'milliseconds' )
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
SELECT * FROM ratings WITH WINDOW 6 sec;

---------------------------------
| user_id | rating     | member |
--------------------------------
| 103     | 19.577045  | false  |
| 104     | 5.486732   | true   |
| 105     | 55.3958906 | true   |
| 106     | 67.00956   | false  |
| 107     | 25.745227  | false  |
---------------------------------
~~~~

STOP PROCESS
------------

Terminate an active process.

Syntax:

~~~~ {.prettyprint .lang-meta}
STOP PROCESS &lt;process_id>
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
STOP PROCESS b4d2c152-5a11-4f6b-9efe-caa5f6aa90d9;
~~~~

TRUNCATE
--------

Deletes all data in a table.

Syntax:

~~~~ {.prettyprint .lang-meta}
TRUNCATE &lt;tablename>;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
TRUNCATE users;
~~~~

USE
---

Sets the working keyspace.

Syntax:

~~~~ {.prettyprint .lang-meta}
USE &lt;keyspace>;
~~~~

Example:

~~~~ {.prettyprint .lang-meta}
USE test;
~~~~

Datatypes
=========

Available datatypes
-------------------

|META type|Description|
|:--------|:----------|
|ascii|*Use text or varchar instead.*|
|bigint|64-bit signed long|
|boolean|true or false|
|counter|Distributed counter value (64-bit long)|
|decimal|Variable-precision decimal|
|double|64-bit IEEE-754 floating point|
|float|32-bit IEEE-754 floating point|
|int|32-bit signed integer|
|varchar|UTF-8 encoded string|

Coming Soon
-----------

|META type|Description|
|:--------|:----------|
|blob|Arbitrary bytes expressed as hexadecimal|
|inet|IP address string in IPv4 or IPv6 format|
|list&lt;T>|A collection of one or more ordered elements|
|map&lt;K,V>|A JSON-style array of literals|
|set&lt;T>|A collection of one or more elements|
|timestamp|Date plus time, encoded as 8 bytes since epoch|
|uuid|Type 1 or type 4 UUID|
|text|UTF-8 encoded string|
|timeuuid|Type 1 UUID only|
|varint|Arbitrary-precision integer|
