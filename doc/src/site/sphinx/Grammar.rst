CROSSDATA Grammar
*****************


Notes
=======

-   In general, a quoted (single or double) string refers to a literal
    string whereas a string without quotation marks refers to a column
    name.

Example:

    -   Column name:
        -   total
        -   myTable.total
        -   myCatalog.myTable.total
    -   Literal:
        -   “Madrid”
        -   ‘California'
        -   “New York City”


-   In the near future, many statements and statement extensions will be supported.



Introduction
============

        This document describes the main features of CROSSDATA and the syntax
of the supported operations. The CROSSDATA language is a SQL-like language that unifies the
treatment of streaming and batch data into a single language.



Main characteristics
====================

        The CROSSDATA language provides the following benefits over the
existing SQL implementation.

-   Unified language
    -   Real time and batch support in a single platform.
-   Improved INSERT INTO
    -   Primary key and associated indexes are defined when creating the
    table.
-   Improved SELECT queries
    -   Selection windows
    -   Joins
-   Centralized schema
-   Mutable data
-   P2P storage backend (if Stratio Cassandra is used as datastore)




Language Features
=================

        This section describes the features of the CROSSDATA language. The
language has been defined by extending the current SQL
grammar to support the required streaming features, and
to provide new query capabilities. This section introduces the syntax of the supported statements highlighting
the new additions.

Statements
----------
       
The language supports the following set of operations based on the SQL
language .

        The following elements are defined as:

-   Identifier: Used to identify catalogs, tables, columns and other
    objects. An identifier is a token matching the regular expression
    [a-zA-Z0-9\_]\*.
-   Values: A value is a text representation of any of the supported
    data types.

        The following non-terminal symbols appear in the grammar:

-   \<identifier\> ::= LETTER (LETTER | DIGIT | '\_')\*
-   \<literal\> ::= “ (\~”)\* ” | ‘ (\~')\* '
-   \<JSON\> ::= '{' (\<literal\> ':' \<JSON-value\>)\* '}'
-   \<JSON-value\> ::= ( \<literal\> | \<boolean\> | \<integer\> | \<decimal\> )
-   \<catalog\_name\> ::= \<identifier\>
-   \<tablename\> ::= (\<catalog\_name\> '.')? \<identifier\>
-   \<columnname\> ::= (\<tablename\> '.')? \<identifier\>
-   \<indexname\> ::= \<tablename\> '.' \<identifier\>
-   \<property\> ::= (\<literal\> | \<identifier\> ) '=' ( \<literal\> | \<constant\> )
-   \<data-types\> = TEXT | BIGINT | INT | DOUBLE | FLOAT | BOOLEAN

Supported types
---------------

-   TEXT
-   BIGINT
-   INT
-   DOUBLE
-   FLOAT
-   BOOLEAN

ADD DATASTORE
-------------
ADD DATASTORE \<path\> ';'

Example:

    ADD DATASTORE “/home/stratio/crossdata/cassandra.xml”;

DROP DATASTORE
--------------
DROP DATASTORE \<datastore-name\> ';'

Example:

    DROP DATASTORE cassandra;

ADD CONNECTOR
-------------
ADD CONNECTOR \<path\> ';'

Examples:

    ADD CONNECTOR “/home/stratio/crossdata/connectors/connector_native_cassandra.xml”;

DROP CONNECTOR
--------------
DROP CONNECTOR \<connector-name\> ';'

Examples:

    DROP CONNECTOR cassandra_connector;

ATTACH CLUSTER
--------------
ATTACH CLUSTER (IF NOT EXISTS)? \<cluster\_name\> ON DATASTORE \<datastore\_name\> WITH OPTIONS \<JSON\> ';'

Example:

    ATTACH CLUSTER production_madrid ON DATASTORE cassandra WITH OPTIONS {'Hosts': '[127.0.0.1]', 'port': 9160};

ALTER CLUSTER
-------------
ALTER CLUSTER (IF EXISTS)? \<cluster\_name\> WITH \<JSON\> ';'

Example:

    ALTER CLUSTER production_madrid WITH {"port": 9161};

DETACH CLUSTER
--------------
DETACH CLUSTER \<cluster\_name\>';'

Example:

    DETACH CLUSTER production_madrid;

ATTACH CONNECTOR
----------------
ATTACH CONNECTOR \<connector-name\> TO \<cluster-name\> WITH OPTIONS \<JSON\>';'

Example:

    ATTACH CONNECTOR con_native_cassandra TO cassandra_production WITH OPTIONS {'DefaultLimit': '1000'};

DETACH CONNECTOR
----------------
DETACH CONNECTOR \<connector-name\> FROM \<cluster-name\>';'

Example:

    DETACH CONNECTOR con_native_cassandra FROM cassandra_production;

CREATE CATALOG
--------------
CREATE CATALOG (IF NOT EXISTS)? \<catalog\_name\> (WITH \<JSON\>)? ';'

Example:

    CREATE CATALOG catalog1 WITH {"comment": "This is a comment"};                

ALTER CATALOG
-------------
ALTER CATALOG (IF NOT EXISTS)? \<catalog\_name\> (WITH \<JSON\>)? ';'

Example:

    ALTER CATALOG catalog1 WITH {"comment": "This is a comment"};

DROP CATALOG
------------
DROP CATALOG (IF EXISTS)? \<catalog\_name\> ';'

Example:

    DROP CATALOG catalog1;  

USE
----
USE \<catalog\_name\> ';'

Example:

    USE sales;

CREATE TABLE
------------
CREATE TABLE (IF NOT EXISTS)? \<tablename\> ON CLUSTER \<clusterName\> '('\<column-definition\> (',' \<column-definition\> )\* ')' (WITH \<JSON\>)? ';'

\<column-definition\> ::= \<identifier\> \<type\> ( PRIMARY KEY )? | PRIMARY KEY '(' \<partition-key\> (',' \<identifier\> )\* ')'

\<partition-key\> ::= \<partition-key\> | '(' \<partition-key\> ( ',' \<identifier\> )\* ')'        

Example:

    CREATE TABLE tableTest ON CLUSTER cassandra_prod (id int PRIMARY KEY, name text);
        
ALTER TABLE
-----------
ALTER TABLE \<tablename\>
        (ALTER \<column-name\> \<data-types\>
        |ADD \<column-name\> \<data-types\>
        |DROP \<column-name\>)?
        (WITH \<JSON\>)?';'   

Example:

    ALTER TABLE tableTest ADD timestamp INT;

UPDATE TABLE
------------
UPDATE \<tablename\>
    (USING option (AND option)\*)?
    SET assignment (COMMA assignment)\*
    (WHERE \<where-clause\>)?
    (WITH \<JSON\>)? ';'      

\<assignment\> ::= \<column-name\> = \<value-assignment\>

\<value-assignment\> ::= \<value\> | \<column-name\> \<operator\> \<value-assignment\>

\<where-clause\> ::= \<relation\> ( AND \<relation\> )\*

\<relation\> ::= \<identifier\> ('=' | '\<' | '\>' | '\<=' | '\>=' | '\<\>'  | 'MATCH') \<data-types\>  

Example:

    UPDATE tableTest SET value = value + 900 WHERE age > 30;

DROP TABLE
----------
DROP TABLE (IF EXISTS)? \<tablename\> ';'

Example:

    DROP TABLE tableTest;

TRUNCATE TABLE
--------------
TRUNCATE \<tablename\> ';'

Example:

    TRUNCATE tableTest;

DELETE
------
DELETE FROM \<tablename\> (WHERE \<where-clause\>)? ';'

\<where-clause\> ::= \<relation\> ( AND \<relation\> )\*

\<relation\> ::= \<identifier\> ('=' | '\<' | '\>' | '\<=' | '\>=' | '\<\>'  | 'MATCH') \<data-types\>  

Example:

    DELETE FROM tableTest WHERE income < 100;

INSERT
------
INSERT INTO \<tablename\> '('\<identifier\> (',' \<identifier\> )\*')' VALUES '('\<data-types\> (',
' \<term-or-literal\> )\* ')' (IF NOT EXISTS)? WHEN \<where-clause\> (WITH \<JSON\>)? ';'

Example:

    INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (-3.75, 'term2') IF NOT EXISTS;

CREATE INDEX
------------
CREATE (\<index-type\>)? INDEX (IF NOT EXISTS)? \<index-name\> ON \<table-name\> '(' \<column-names\> ')' (WITH \<JSON\>)? ';'

\<index-type\> ::= DEFAULT | FULL\_TEXT | CUSTOM

Example:

    CREATE FULL_TEXT INDEX revenueIndex ON tabletest (revenue);

DROP INDEX
----------
DROP INDEX (IF EXISTS)? \<indexname\> ';'

Example:

    DROP INDEX IF EXISTS tabletest.revenueIndex;

SELECT
------
SELECT \<select-list\> FROM \<tablename\> (AS \<identifier\>)? (WITH WINDOW \<integer\> \<time-unit\>)? ((INNER)? JOIN
\<tablename\> (AS \<identifier\>) ON \<field1\>=\<field2\>)? (WHERE \<where-clause\>)? (ORDER BY \<select-list\>)?
(GROUP BY \<select-list\>)? (LIMIT \<integer\>)? ';'

\<selection-list\> ::= \<identifier\> (AS \<identifier\>)? (',' \<selector\> (AS \<identifier\>)? )\* | '\*'

\<where-clause\> ::= \<relation\> ( AND \<relation\> )\*

\<relation\> ::= \<identifier\> ('=' | '\<' | '\>' | '\<=' | '\>=' | '\<\>'  | 'MATCH') \<data-types\>      
    
Modifications:

-   The SELECT statement has been extended to support the following
    features:

    -   Inner join: Inner join creates a new result table by combining
        column values of two tables (A and B) based upon the join-predicate.
        The query compares each row of A with each row of B to find all
        pairs of rows which satisfy the join-predicate. When the
        join-predicate is satisfied, column values for each matched pair of
        rows of A and B are combined into a result row.
    -   Window: The user is able to specify the selection window for
        streaming queries. The window can be either an absolute number of
        tuples or a time window.
    -   New comparison operators (\<\>, LIKE, and MATCH)

Example:

    SELECT field1, field2 FROM demo.clients AS table1 INNER JOIN sales AS table2 ON identifier = codeID;

Implicit joins are also supported:

SELECT \<select-list\> FROM \<tablename\> (AS \<identifier\>)? ',' \<tablename\> (AS \<identifier\>)? (WITH WINDOW \<integer\> \<time-unit\>)? ON
\<field1\>=\<field2\> (WHERE \<where-clause\>)? (ORDER BY \<select-list\>)? (GROUP BY \<select-list\>)? (LIMIT
\<integer\>)? ';'

Example:

    SELECT * FROM demo.clients, demo.sales ON clients.identifier = sales.codeID;


EXPLAIN PLAN
------------
Explain plan for a specific command according to the current state of the system.

EXPLAIN PLAN FOR \<crossdata-statement\> ';'

Example:

    EXPLAIN PLAN FOR Select * from demoCatalog.demoCatalog;

RESET SERVERDATA
----------------
Remove all data stored in the system (in all servers), including information related to datastores, clusters and connectors.
Connectors status are the only metadata kept by the system after issuing this command.

RESET SERVERDATA ';'

Example:

    RESET SERVERDATA;

CLEAN METADATA
--------------
Remove all metadata related to catalogs, tables, indexes and columns.

CLEAN METADATA ';'

Example:

    CLEAN METADATA;

DESCRIBE SYSTEM
---------------
Describe all the information related to datastores, clusters and connectors.

DESCRIBE SYSTEM ';'

Example:

    DESCRIBE SYSTEM;

DESCRIBE DATASTORES
-------------------
Describe all the datastores registered in the system.

DESCRIBE DATASTORES ';'

Example:

    DESCRIBE DATASTORES;

DESCRIBE DATASTORE
------------------
Describe information related to a specific datastore.

DESCRIBE DATASTORE \<datastore-name\> ';'

Example:

    DESCRIBE DATASTORE cassandra;

DESCRIBE CLUSTERS
-----------------
Describe all the clusters registered in the system.

DESCRIBE CLUSTERS ';'

Example:

    DESCRIBE CLUSTERS;

DESCRIBE CLUSTER
----------------
Describe information related to a specific cluster.

DESCRIBE CLUSTER \<cluster-name\> ';'

Example:

    DESCRIBE CLUSTER production;

DESCRIBE CONNECTORS
-------------------
Describe all the connectors registered in the system.

DESCRIBE CONNECTORS;

Example:

    DESCRIBE CONNECTORS;

DESCRIBE CONNECTOR
------------------
Describe the specified connector.

DESCRIBE CONNECTOR \<connector-name\> ';'

Example:

    DESCRIBE CONNECTOR cassandra_connector;

DESCRIBE CATALOGS
-----------------
List of the catalogs created in the system.

DESCRIBE CATALOGS ';'

Example:

    DESCRIBE CATALOGS;

DESCRIBE CATALOG
----------------
Describe the specified catalog.

DESCRIBE CATALOG \<catalog-name\> ';'

Example:

    DESCRIBE CATALOG catalog1;

DESCRIBE TABLES
---------------
List of the tables created in a specific catalog.

DESCRIBE TABLES (FROM \<catalog-name\>)? ';'

Example:

    DESCRIBE TABLES FROM myCatalog;


DESCRIBE TABLE
--------------
Describe the specified table.

DESCRIBE TABLE \<table-name\> ';'

Example:

    DESCRIBE TABLE catalog1.table;

DISCOVER METADATA
-----------------
Discover metadata from a specified cluster. This command provides information about catalogs and tables already
existent on a cluster.

DISCOVER METADATA ON CLUSTER \<cluster-name\> ';'

Example:

    DISCOVER METADATA ON CLUSTER productionCluster;

IMPORT CATALOGS
---------------
Import all the metadata from a specific cluster. This command incorporates to the Crossdata servers all the catalogs
metadata and their underlying metadata.

IMPORT CATALOGS FROM CLUSTER \<cluster-name\> ';'

Example:

    IMPORT CATALOGS FROM CLUSTER cluster_name;

IMPORT CATALOG
--------------
Import all the metadata from a specific catalog. This command incorporates to the the Crossdata servers a
catalog metadata and its underlying metadata.

IMPORT CATALOG \<catalog-name\> FROM CLUSTER \<cluster-name\> ';'

Example:

    IMPORT CATALOG catalog_name FROM CLUSTER cluster_name;

IMPORT TABLE
------------
Import metadata of a specific table. This command incorporates to the the Crossdata servers a
table metadata and its underlying metadata.

IMPORT TABLE \<table-name\> FROM CLUSTER \<cluster-name\> ';'

Example:

    IMPORT TABLE myCatalog.myTable FROM CLUSTER myCluster;



Shell Features
==============

        This section describes the specific and special features of the CROSSDATA shell:

*   Shell accepts comments:
    *   One line comment: line starts with "//" or "#".
        *   ``` > // This is a one line comment```
        *   ``` > /# This is also a one line comment```
    *   Multiline comment: starts with a line starting with "/*" and ends with a line ending with "*/"
        *   ``` > /* This is a ```
        *   ``` > multiline ```
        *   ``` > comment */ ```
*   How to exit from the shell:
    *   ``` > exit```
    *   ``` > quit```
*   Help:
    *   A help entry is available for every command, just type "help \<command\>"
        *   ``` > help create```
*   Script:
    *   You can execute a script upon launching the shell. The script will be executed first,
    and the prompt will be shown afterwards. Run the shell with an input argument "--script <path-to-xdql-file>"
        *   ``` > mvn exec:java -pl crossdata-shell -Dexec.mainClass="com.stratio.crossdata.sh.Shell" -Dexec.args="--script /path/script.xdql"```
