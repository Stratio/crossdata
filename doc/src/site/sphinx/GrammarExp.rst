CROSSDATA Expanded Grammar
**************************

Introduction
============

        This document describes the standard SparkSQL grammar expansion provided by CROSSDATA.
that means that any SQL sentences accepted by SparkSQL will be compatible with CROSSDATA.

        Through the following lines you will find a description of those sentences provided by CROSSDATA which
are not supported by Spark.


Expansion main features
=======================

-   100% SparkSQL compatible.
-   Added new table import capabilities:
        -   `IMPORT TABLES`: Catalog registration of every single table existing accessible by a concrete provider.

Statements
----------

The language supports the following set of operations based on the SQL
language.

        The following elements are defined as:

-   Identifier: Used to identify providers and, databases and tables.
    An identifier is a token matching the regular expression
    ([a-zA-Z0-9\_]+.)*[a-zA-Z0-9\_]+
-   Values: A value is a text representation of any of the supported
    data types.

        The following non-terminal symbols appear in the grammar:

-   \<simple\_identifier\> ::= LETTER (LETTER | DIGIT | '\_')\*
-   \<identifier\> ::= (\<simple\_identifier\>'.')\*\<simple\_identifier\>
-   \<literal\> ::= “ (\~”)\* ” | ‘ (\~')\* '
-   \<provider\> ::= \<identifier\>
-   \<database\> ::= \<simple\_identifier\>
-   \<tablename\> ::= \<identifier\>
-   \<property\> ::= \<identifier\> ' '\+ ( \<literal\> | \<constant\> )

Supported types
---------------

Those supported by SparkSQL

IMPORT TABLES
-------------
IMPORT TABLES USING \<provider\> OPTIONS '(' (\<property\>',)\+'\<property\> ')'

Example:

    IMPORT TABLES
    USING com.stratio.crossdata.connector.cassandra
    OPTIONS (
        cluster "Test Cluster",
        spark_cassandra_connection_host '127.0.0.1'
    )


