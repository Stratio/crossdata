
CROSSDATA Grammar
---

Language definition

Version: 0.1.1

Date: 27, Oct, 2014


* * * * *

Table of contents

-   [Notes] (#notes)
-   [Introduction](#introduction)
-   [Main characteristics](#main-characteristics)
-   [Language Features](#language-features)
    -   [Statements](#statements)
        -   [ADD DATASTORE](#add-dataStore)
        -   [ADD CONNECTOR](#add-connector)
        -   [ATTACH CLUSTER](#attach-cluster)
        -   [ATTACH CONNECTOR](#attach-connector)
        -   [CREATE CATALOG](#create-catalog)
        -   [CREATE TABLE](#create-table)
        -   [INSERT](#insert)
        -   [SELECT](#select)
        -   [LIST](#list)
        -   [RESET](#reset)

* * * * *

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

* * * * *

Introduction
============

        This document describes the main features of CROSSDATA and the syntax
of the supported operations. The CROSSDATA language is a SQL-like language that unifies the
treatment of streaming and batch data into a single language.

* * * * *

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


* * * * *

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
-   \<properties\> ::= \<property\> (AND \<property\>)\*
-   \<property\> ::= (\<literal\> | \<identifier\> ) '=' ( \<literal\> | \<constant\> )
-   \<data-types\> = TEXT | BIGINT | INT | DOUBLE | FLOAT | BOOLEAN

Supported types
----------

-   TEXT
-   BIGINT
-   INT
-   DOUBLE
-   FLOAT
-   BOOLEAN

### ADD DATASTORE

ADD DATASTORE \<path\> ';'

Example:

    ADD DATASTORE “/home/stratio/crossdata/cassandra.xml”;

### ADD CONNECTOR

ADD CONNECTOR \<path\> ';'

Examples:

    ADD CONNECTOR “/home/stratio/crossdata/connectors/connector\_native\_cassandra.xml”;

### ATTACH CLUSTER

ATTACH CLUSTER (IF NOT EXISTS)? \<cluster\_name\> ON DATASTORE \<datastore\_name\> WITH OPTIONS \<JSON\> ';'

Example:

    ATTACH CLUSTER production\_madrid ON DATASTORE cassandra WITH OPTIONS {'Hosts': '[127.0.0.1]', 'port': 9160}

### ATTACH CONNECTOR

ATTACH CONNECTOR \<connector-name\> TO \<cluster-name\> WITH OPTIONS \<JSON\>‘;'

Example:

    ATTACH CONNECTOR con\_native\_cassandra TO cassandra\_production WITH OPTIONS {'DefaultLimit': '1000'};

### CREATE CATALOG

CREATE CATALOG (IF NOT EXISTS)? \<catalog\_name\> (WITH \<JSON\>)? ';'

Example:

    CREATE CATALOG catalog1 WITH {"comment": "This is a comment"}                

### USE

USE \<catalog\_name\> ';'

Example:

    USE sales;

### CREATE TABLE

CREATE TABLE (IF NOT EXISTS)? \<tablename\> ON CLUSTER \<clusterName\> '('\<column-definition\> (',' \<column-definition\> )\* ')' (WITH \<JSON\>)? ';'

\<column-definition\> ::= \<identifier\> \<type\> ( PRIMARY KEY )? | PRIMARY KEY '(' \<partition-key\> (',' \<identifier\> )\* ')'

\<partition-key\> ::= \<partition-key\> | '(' \<partition-key\> ( ',' \<identifier\> )\* ')'        

Example:

    CREATE TABLE tableTest ON CLUSTER cassandra_prod (id int PRIMARY KEY, name text);
        
### INSERT

INSERT INTO \<tablename\> '('\<identifier\> (',' \<identifier\> )\*')' VALUES '('\<data-types\> (',' \<term-or-literal\> )\* ')' (IF NOT EXISTS)? ';'

Example:

    INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (-3.75, 'term2') IF NOT EXISTS;

### SELECT

SELECT \<select-list\> FROM \<tablename\> (AS \<identifier\>)? (WITH WINDOW \<integer\> \<time-unit\>)? (INNER JOIN
\<tablename\> (AS \<identifier\>) ON \<field1\>=\<field2\>)? (WHERE \<where-clause\>)? (LIMIT \<integer\>)? ';'

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

    SELECT field1, field2 FROM demo.clients AS table1 INNER JOIN sales AS table2 ON identifier = codeID

### LIST

List the existing connectors in the system and information about them.

LIST CONNECTORS;

Example:

    LIST CONNECTORS;

### RESET

Remove all metadata stored in the system.

RESET METADATA;

Example:

    RESET METADATA;

