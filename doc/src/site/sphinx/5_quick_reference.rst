===============
Quick Reference
===============

Quick SQL Statement reference. More details can be found at `reference guide. <../6_reference_guide.rst>`__

CREATE TABLE
------------

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS \<options\>

Example:
::

    CREATE TABLE IF NOT EXISTS tablename ( id string, eventdate date)
    USING com.databricks.spark.csv
    OPTIONS (path "events.csv", header "true")

TABLE CACHING
-------------

- CACHE [LAZY] TABLE \<tablename\>

- UNCACHE TABLE \<tablename\>

- CLEAR CACHE

Example:
::

    CACHE LAZY TABLE table1

SELECT STATEMENTS
-----------------
::

  SELECT [DISTINCT] \<projections\>
  FROM   ( \<relations\> | \<joinexpressions\> )
  [WHERE \<filters\>]
  [GROUP BY \<expressions\> [ HAVING \<havingexpressions\>]]
  [(ORDER BY | SORT BY) \<orderexpressions\>]
  [LIMIT \<numLiteral\>]

  \<joinexpression\> ::= \<relation\> [ \<jointype\>] JOIN \<relation\> [ ON \<expression\> ]
  \<jointype\> ::= ( INNER        |
                    LEFT SEMI    |
                    LEFT [OUTER] |
                    RIGHT [OUTER]|
                    FULL  [OUTER]
                  )

Examples:
::

    - SELECT t1.product, gender, count(*) AS amount, sum(t1.quantity) AS total_quantity
    FROM (SELECT product, client_id, quantity FROM lineshdfsdemo) t1
    INNER JOIN clients ON client_id=id
    GROUP BY gender, product;


    - SELECT ol_cnt, sum(carrier) AS high_line_count
    FROM testmetastore.orders
    WHERE ol_delivery_d BETWEEN '2013-07-09' AND '2013-08-09' AND country LIKE "C%"
    GROUP BY o_ol_cnt
    ORDER BY high_line_count DESC, low_line_count
    LIMIT 10



ADMIN COMMANDS
--------------

SHOW TABLES [IN \<database\>]

SHOW FUNCTIONS [\<functionid\>]

DESCRIBE [EXTENDED] \<tablename\>

DESCRIBE FUNCTION [EXTENDED] \<functionid\>

Examples:
::

    SHOW TABLES IN mydatabase
    SHOW FUNCTIONS
    DESCRIBE TABLE mydatabase.mytable