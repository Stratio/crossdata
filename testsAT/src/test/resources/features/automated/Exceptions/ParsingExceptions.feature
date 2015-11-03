Feature: Parsing exceptions Tests statements

  Scenario Outline: Statements that produce an exception
    When I execute a bad formed statement: '<Statement>'
    Then an exception 'IS' thrown with class 'ParsingException'

    Examples: 
      | Statement                                                                                                                          |
      | DROP DATASTORE " ";                                                                                                                |
      | DROP DATASTORE "1PARSINGTESTDROPDATASTORE";                                                                                        |
      | DROP DATASTORE "_PARSINGTESTDATASTORE";                                                                                            |
      | DROP DATASTORE '*/-+:.";                                                                                                           |
      | DROP DATASTORE "DATASTORE_NAME";                                                                                                   |
      | DROP DATASTORE "DATASTORE_NAME;                                                                                                    |
      | DROP DATASTORE 'DATASTORE_NAME';                                                                                                   |
      | DROP DATASTORE 'DATASTORE_NAME;                                                                                                    |
      | DROP CONNECTOR ' ";                                                                                                                |
      | DROP CONNECTOR '1PARSINGTESTDROPCONNECTOR";                                                                                        |
      | DROP CONNECTOR '_PARSINGTESTDROPCONNECTOR";                                                                                        |
      | DROP CONNECTOR '*/-+:.";                                                                                                           |
      | DROP CONNECTOR 'CONNECTOR_NAME';                                                                                                   |
      | DROP CONNECTOR 'CONNECTOR_NAME;                                                                                                    |
      | DROP CONNECTOR 'CONNECTOR_NAME';                                                                                                   |
      | DROP CONNECTOR 'CONNECTOR_NAME;                                                                                                    |
      | ATTACH CLUSTER ' ' ON DATASTORE "cassandra" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};                                      |
      | ATTACH CLUSTER '1CLUSTER' ON DATASTORE "cassandra" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};                               |
      | ATTACH CLUSTER '_CLUSTER' ON DATASTORE "cassandra" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};                               |
      | ATTACH CLUSTER '*/-+:.' ON DATASTORE "cassandra" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160} ;                                |
      | ATTACH CLUSTER 'TEST_CLUSTER' ON DATASTORE " " WITH OPTIONS {'host': '127.0.0.1', 'port': 9160} ,                                  |
      | ATTACH CLUSTER 'TEST_CLUSTER' ON DATASTORE "1PARSINGTESTDROPDATASTORE" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};           |
      | ATTACH CLUSTER 'TEST_CLUSTER' ON DATASTORE "_PARSINGTESTDATASTORE" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};               |
      | ATTACH CLUSTER 'TEST_CLUSTER' ON DATASTORE "*/-+:." WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};                              |
      | ATTACH CLUSTER IF EXISTS 'TEST_CLUSTER' ON DATASTORE "cassandra" WITH OPTIONS {'host': '127.0.0.1', 'port': 9160};                 |
      #FALTARIAN LOS CASOS DE QUE EL JSON ESTE MAL FORMADO
      | ALTER CLUSTER ' ' ON DATASTORE "hdfs" WITH OPTIONS {'host': '127.0.0.2'};;                                                         |
      | ALTER CLUSTER '1CLUSTER' ON DATASTORE "hdfs" WITH OPTIONS {'host': '127.0.0.2'};                                                   |
      | ALTER CLUSTER '_CLUSTER' ON DATASTORE "hdfs" WITH OPTIONS {'host': '127.0.0.2'};                                                   |
      | ALTER CLUSTER '*/-+:.' ON DATASTORE "hdfs" WITH OPTIONS {'host': '127.0.0.2'};                                                     |
      | ALTER CLUSTER 'TEST_CLUSTER' ON DATASTORE " " WITH OPTIONS {'host': '127.0.0.2'}	;                                                 |
      | ALTER CLUSTER 'TEST_CLUSTER' ON DATASTORE "1PARSINGTESTDROPDATASTORE" WITH OPTIONS {'host': '127.0.0.2'};                          |
      | ALTER CLUSTER 'TEST_CLUSTER' ON DATASTORE "_PARSINGTESTDATASTORE" WITH OPTIONS {'host': '127.0.0.2'};                              |
      | ALTER CLUSTER 'TEST_CLUSTER' ON DATASTORE "*/-+:." WITH OPTIONS {'host': '127.0.0.2'};                                             |
      | ALTER CLUSTER IF NOT EXISTS 'TEST_CLUSTER' ON DATASTORE "DATASTORE_NAME" WITH OPTIONS {'host': '127.0.0.2'};                       |
      #FALTARIAN LOS CASOS DE QUE EL JSON ESTE MAL FORMADO
      | DETACH CLUSTER "";                                                                                                                 |
      | DETACH CLUSTER "1CLUSTER";                                                                                                         |
      | DETACH CLUSTER "_CLUSTER";                                                                                                         |
      | DETACH CLUSTER "*/-+:.";                                                                                                           |
      | DETACH CLUSTER "CLUSTER_NAME";                                                                                                     |
      | DETACH CLUSTER "CLUSTER_NAME;                                                                                                      |
      | DETACH CLUSTER 'CLUSTER_NAME';                                                                                                     |
      | DETACH CLUSTER 'CLUSTER_NAME;                                                                                                      |
      | ATTACH CONNECTOR " " TO "CLUSTER_NAME" WITH OPTIONS {“DefaultLimit”: 999};                                                         |
      | ATTACH CONNECTOR "1CONNECTOR" TO "CLUSTER_NAME" WITH OPTIONS {"DefaultLimit": 999};                                                |
      | ATTACH CONNECTOR "_CONNECTOR" TO "CLUSTER_NAME" WITH OPTIONS {"DefaultLimit": 999};                                                |
      | ATTACH CONNECTOR "*/-+:." TO "CLUSTER_NAME" WITH OPTIONS {"DefaultLimit": 999};                                                    |
      | ATTACH CONNECTOR "CONNECTOR_NAME" TO " " WITH OPTIONS {"DefaultLimit": 999};                                                       |
      | ATTACH CONNECTOR "CONNECTOR_NAME" TO "1CLUSTER" WITH OPTIONS {"DefaultLimit": 999};                                                |
      | ATTACH CONNECTOR "CONNECTOR_NAME" TO "_CLUSTER" WITH OPTIONS {"DefaultLimit": 999};                                                |
      | ATTACH CONNECTOR "CONNECTOR_NAME" TO "*/-+:." WITH OPTIONS {"DefaultLimit": 999};                                                  |
      #FALTARIAN LOS CASOS DE QUE EL JSON ESTE MAL FORMADO
      | DETACH CONNECTOR "" FROM "CLUSTER_NAME";                                                                                           |
      | DETACH CONNECTOR "1CONNECTOR" FROM "CLUSTER_NAME";                                                                                 |
      | DETACH CONNECTOR "_CONNECTOR" FROM "CLUSTER_NAME";                                                                                 |
      | DETACH CONNECTOR "*/-+:." FROM "CLUSTER_NAME";                                                                                     |
      | DETACH CONNECTOR "CONNECTOR_NAME" FROM " ";                                                                                        |
      | DETACH CONNECTOR "CONNECTOR_NAME" FROM "1CLUSTER";                                                                                 |
      | DETACH CONNECTOR "CONNECTOR_NAME" FROM "_CLUSTER";                                                                                 |
      | DETACH CONNECTOR "CONNECTOR_NAME" FROM "*/-+:.";                                                                                   |
      | CREATE CATALOG ;                                                                                                                   |
      | CREATE CATALOG 1CATALOG_NAME;                                                                                                      |
      | CREATE CATALOG _CATALOG_NAME;                                                                                                      |
      | CREATE CATALOG */-+:.;                                                                                                             |
      | CREATE CATALOG IF EXISTS CATALOG_NAME;                                                                                             |
      #FALTAN CASOS CON LAS PROPERTIES
      | ALTER CATALOG ;                                                                                                                    |
      | ALTER CATALOG 1CATALOG_NAME;                                                                                                       |
      | ALTER CATALOG _CATALOG_NAME;                                                                                                       |
      | ALTER CATALOG */-+:.;                                                                                                              |
      | ALTER CATALOG IF NOT EXISTS CATALOG_NAME;                                                                                          |
      #FALTAN CASOS CON LAS PROPERTIES
      | DROP CATALOG ;                                                                                                                     |
      | DROP CATALOG 1CATALOG_NAME;                                                                                                        |
      | DROP CATALOG _CATALOG_NAME;                                                                                                        |
      | DROP CATALOG */-+:.;                                                                                                               |
      | DROP CATALOG IF NOT EXISTS CATALOG_NAME;                                                                                           |
      | USE ;                                                                                                                              |
      | USE 1PARSINGTESTCATALOG;                                                                                                           |
      | USE _PARSINGTESTCATALOG;                                                                                                           |
      | USE */-+:.;                                                                                                                        |
      | USE "CATALOG_NAME";                                                                                                                |
      | USE "CATALOG_NAME;                                                                                                                 |
      | USE 'CATALOG_NAME';                                                                                                                |
      | USE 'CATALOG_NAME;                                                                                                                 |
      | CREATE TABLE  ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                           |
      | CREATE TABLE 1TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                     |
      | CREATE TABLE _TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                     |
      | CREATE TABLE */-+:. ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                     |
      | CREATE TABLE CATALOGNAME. ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                               |
      | CREATE TABLE CATALOGNAME.1TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                         |
      | CREATE TABLE CATALOGNAME._TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                         |
      | CREATE TABLE CATALOGNAME.*/-+:. ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                         |
      | CREATE TABLE .TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                     |
      | CREATE TABLE 1CATALOG.TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                             |
      | CREATE TABLE _CATALOG.TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                             |
      | CREATE TABLE */-+:..TABLE ON CLUSTER CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                               |
      | CREATE TABLE TABLE_NAME ON CLUSTER  (NAME VARCHAR, PRIMARY KEY(NAME));                                                             |
      | CREATE TABLE TABLE_NAME ON CLUSTER 1CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                |
      | CREATE TABLE TABLE_NAME ON CLUSTER _CLUSTER_NAME (NAME VARCHAR, PRIMARY KEY(NAME));                                                |
      | CREATE TABLE TABLE_NAME ON CLUSTER */-+:. (NAME VARCHAR, PRIMARY KEY(NAME));                                                       |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( VARCHAR, PRIMARY KEY(NAME));                                                     |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( 1COlUMN_NAME VARCHAR, PRIMARY KEY(NAME));                                        |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( _COLUMN_NAME VARCHAR, PRIMARY KEY(NAME));                                        |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( */-+:. VARCHAR, PRIMARY KEY(NAME));                                              |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( COLUMN_NAME VARCHAR, PRIMARY KEY( ));                                            |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( COLUMN_NAME VARCHAR, PRIMARY KEY(1COLUMN));                                      |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( COLUMN_NAME VARCHAR, PRIMARY KEY(_COLUMN));                                      |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( COLUMN_NAME VARCHAR, PRIMARY KEY(*/-+:.));                                       |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( COLUMN_NAME VARCHAR);                                                            |
      | CREATE TABLE TABLE_NAME ON CLUSTER CLUSTER_NAME ( COLUMN_NAME TYPE);                                                               |
      | ALTER TABLE  ALTER COLUMN_NAME TYPE VARCHAR;                                                                                       |
      | ALTER TABLE  1TABLE ALTER COLUMN_NAME TYPE VARCHAR;                                                                                |
      | ALTER TABLE  _TABLE ALTER COLUMN_NAME TYPE VARCHAR;                                                                                |
      | ALTER TABLE  */-+:. ALTER COLUMN_NAME TYPE VARCHAR;                                                                                |
      | ALTER TABLE TABLENAME ALTER TYPE VARCHAR;                                                                                          |
      | ALTER TABLE TABLENAME 1COLUMN ALTER TYPE VARCHAR;                                                                                  |
      | ALTER TABLE TABLENAME _COLUMN ALTER TYPE VARCHAR;                                                                                  |
      | ALTER TABLE TABLENAME */-+:. ALTER TYPE VARCHAR;                                                                                   |
      | ALTER TABLE TABLENAME ALTER COLUMN1 TYPE ;                                                                                         |
      | ALTER TABLE TABLENAME ALTER COLUMN1 TYPE 1VARCHAR;                                                                                 |
      | ALTER TABLE TABLENAME ALTER COLUMN1 TYPE _VARCHAR;                                                                                 |
      | ALTER TABLE TABLENAME ALTER COLUMN1 TYPE */-+:.;                                                                                   |
      | ALTER TABLE TABLENAME ALTER COLUMN1 TYPE EJEMPLO;                                                                                  |
      | ALTER TABLE  ADD COLUMN_NAME VARCHAR;                                                                                              |
      | ALTER TABLE 1TABLE ADD COLUMN_NAME VARCHAR;                                                                                        |
      | ALTER TABLE _TABLE ADD COLUMN_NAME VARCHAR;                                                                                        |
      | ALTER TABLE */-+:.  ADD COLUMN_NAME VARCHAR;                                                                                       |
      | ALTER TABLE CATALOGNAME. ADD COLUMN_NAME VARCHAR;                                                                                  |
      | ALTER TABLE CATALOGNAME.1TABLE ADD COLUMN_NAME VARCHAR;                                                                            |
      | ALTER TABLE CATALOGNAME._TABLE ADD COLUMN_NAME VARCHAR;                                                                            |
      | ALTER TABLE CATALOGNAME.*/*-:. ADD COLUMN_NAME VARCHAR;                                                                            |
      | ALTER TABLE TABLE1 ADD  VARCHAR;                                                                                                   |
      | ALTER TABLE TABLE1 ADD 1COLUMN VARCHAR;                                                                                            |
      | ALTER TABLE TABLE1 ADD _COLUMN VARCHAR;                                                                                            |
      | ALTER TABLE TABLE1 ADD */-+:. VARCHAR;                                                                                             |
      | ALTER TABLE TABLE1 DROP ;                                                                                                          |
      | ALTER TABLE TABLE1 DROP 1COLUMN;                                                                                                   |
      | ALTER TABLE TABLE1 DROP _COLUMN;                                                                                                   |
      | ALTER TABLE TABLE1 DROP */-+:.;                                                                                                    |
      #FALTAN ALTER TABLE WITH OPTIONS AND OPTIONS...;
      | DROP TABLE ;                                                                                                                       |
      | DROP TABLE 1TABLENAME;                                                                                                             |
      | DROP TABLE _TABLENAME;                                                                                                             |
      | DROP TABLE */-+:.;                                                                                                                 |
      | DROP TABLE CATALOGNAME. ;                                                                                                          |
      | DROP TABLE CATALOGNAME.1TABLENAME;                                                                                                 |
      | DROP TABLE CATALOGNAME._TABLENAME;                                                                                                 |
      | DROP TABLE CATALOGNAME.*/-+:.;                                                                                                     |
      | TRUNCATE ;                                                                                                                         |
      | TRUNCATE 1TABLENAME;                                                                                                               |
      | TRUNCATE _TABLENAME;                                                                                                               |
      | TRUNCATE */+:.;                                                                                                                    |
      | TRUNCATE CATALOGNAME. ;                                                                                                            |
      | TRUNCATE CATALOGNAME.1TABLENAME;                                                                                                   |
      | TRUNCATE CATALOGNAME._TABLENAME;                                                                                                   |
      | TRUNCATE CATALOGNAME.*/-+:.;                                                                                                       |
      | CREATE FREEINDEX INDEX INDEX1 ON TABLE1 (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};              |
      | CREATE DEFAULT INDEX  ON TABLE1 (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};                      |
      | CREATE DEFAULT INDEX 1INDEX ON TABLE1 (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};                |
      | CREATE DEFAULT INDEX _INDEX ON TABLE1 (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};                |
      | CREATE DEFAULT INDEX */+:. ON TABLE1 (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};                 |
      | CREATE DEFAULT INDEX INDEXNAME ON (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};                    |
      | CREATE DEFAULT INDEX INDEXNAME ON _TABLE (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};             |
      | CREATE DEFAULT INDEX INDEXNAME ON 1TABLE (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};             |
      | CREATE DEFAULT INDEX INDEXNAME ON */+-.; (COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};             |
      | CREATE DEFAULT INDEX INDEXNAME ON TABLE1 ( , COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};                |
      | CREATE DEFAULT INDEX INDEXNAME ON TABLE1 (_COL, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};             |
      | CREATE DEFAULT INDEX INDEXNAME ON TABLE1 (1COL, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};             |
      | CREATE DEFAULT INDEX INDEXNAME ON TABLE1 ( */+-:. , COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};         |
      | CREATE DEFAULT INDEX CATALOG1. ON TABLE1 (COL1 , COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};            |
      | CREATE DEFAULT INDEX CATALOG1.1INDEXNAME ON TABLE1 ( COL1, COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};  |
      | CREATE DEFAULT INDEX CATALOG1._INDEXNAME ON TABLE1 ( COL1 , COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'}; |
      | CREATE DEFAULT INDEX CATALOG1.*/+:-. ON TABLE1 ( COL1 , COL3) USING 'com.company.Index.class' WITH OPTIONS = {'key1': 'val1'};     |
      | DROP INDEX ;                                                                                                                       |
      | DROP INDEX 1INDEX;                                                                                                                 |
      | DROP INDEX _INDEX;                                                                                                                 |
      | DROP INDEX */+:.;                                                                                                                  |
      | DROP INDEX CATALOG1. ;                                                                                                             |
      | DROP INDEX CATALOG1.1INDEX;                                                                                                        |
      | DROP INDEX CATALOG1._INDEX;                                                                                                        |
      | DROP INDEX CATALOG1.*/+:.;                                                                                                         |
      #FALTAN DESCRIBE CLUSTERS #BUG 11
      | DESCRIBE CATALOGS TESTCATALOG;                                                                                                     |
      | DESCRIBE CATALOG ;                                                                                                                 |
      | DESCRIBE CATALOG 1TESTCATALOG;                                                                                                     |
      | DESCRIBE CATALOG _TESTCATALOG;                                                                                                     |
      | DESCRIBE TABLES TESTTABLES;                                                                                                        |
      | DESCRIBE TABLE ;                                                                                                                   |
      | DESCRIBE TABLE 1TESTTABLE;                                                                                                         |
      | DESCRIBE TABLE _TESTTABLE;                                                                                                         |
      | DESCRIBE TABLE */-:.;                                                                                                              |
      | INSERT INTO  ( COL1, COL2 ) VALUES (-3.75 , 'term');                                                                               |
      | INSERT INTO _TABLENAME ( COL1, COL2 ) VALUES (-3.75 , 'term');                                                                     |
      | INSERT INTO 1TABLENAME ( COL1, COL2 ) VALUES (-3.75 , 'term');                                                                     |
      | INSERT INTO */-:. ( COL1, COL2 ) VALUES (-3.75 , 'term');                                                                          |
      | INSERT INTO TABLENAME ( _COL1, COL2 ) VALUES (-3.75 , 'termino');                                                                  |
      | INSERT INTO TABLENAME ( 1COL1, COL2 ) VALUES (-3.75 , 'termino');                                                                  |
      | INSERT INTO TABLENAME ( */-: , COL2 ) VALUES (-3.75 , 'termino');                                                                  |
      | INSERT INTO TABLENAME ( COL1 ) VALUES ('termin);                                                                                   |
      | INSERT INTO TABLENAME ( COL1 ) VALUES (3.);                                                                                        |
      | INSERT INTO TABLENAME ( COL1 ) VALUES (-3.);                                                                                       |
      | INSERT INTO .TABLENAME ( COL1 ) VALUES ( 'TERM1' );                                                                                |
      | INSERT INTO 1CATALOG.TABLENAME ( COL1 ) VALUES ('TERM1');                                                                          |
      | INSERT INTO _CATALOG.TABLENAME ( COL1 ) VALUES ('TERM1');                                                                          |
      | INSERT INTO */-:.TABLENAME ( COL1 ) VALUES ('TERM1');                                                                              |
      | INSERT INTO . (COL1) VALUES ('TERM1');                                                                                             |
      | INSERT INTO CATALOG1.TABLENAME ( COL1 ) VALUES ( );                                                                                |
      | INSERT INTO CATALOG1.TABLENAME ( ) VALUES ('TERM1');                                                                               |
      | INSERT INTO TABLENAME ( COL1, COL2 ) VALUES ('term');                                                                              |
      | INSERT INTO TABLENAME ( COL2 ) VALUES (1+2);                                                                                       |
      #################################################################
      | UPDATE SET count= count * 2 / expenses WHERE COL1 = 'value';                                                                       |
      | UPDATE 1TABLENAME SET count= count * 2 / expenses WHERE COL1 = 'value';                                                            |
      | UPDATE _TABLENAME SET count= count * 2 / expenses WHERE COL1 = 'value';                                                            |
      | UPDATE */-: SET count= count * 2 / expenses WHERE COL1 = 'value';                                                                  |
      | UPDATE TABLENAME SET  = COL1 - COL2 WHERE COL1 = 'value';                                                                          |
      | UPDATE TABLENAME SET  WHERE COL1 = 'value';                                                                                        |
      | UPDATE TABLENAME SET COL1 = COL2 +  WHERE COL1 = 'value';                                                                          |
      | UPDATE TABLENAME SET COL1 = 2 WHERE COL1  'value';                                                                                 |
      | DELETE FROM  WHERE COL2 = true;                                                                                                    |
      | DELETE FROM  1TAB1WHERE COL2 = true;                                                                                               |
      | DELETE FROM  _TAB1 WHERE COL2 = true;                                                                                              |
      | DELETE FROM  */-: WHERE COL2 = true;                                                                                               |
      | DELETE FROM TAB1 WHERE COL2 true;                                                                                                  |
      | DELETE FROM CAT1.TAB1 WHERE 1234 = true;                                                                                           |
      | SELECT FROM TABLE1;                                                                                                                |
      | SELECT _COL1 FROM TABLE1;                                                                                                          |
      | SELECT 1COL1 FROM TABLE1;                                                                                                          |
      | SELECT */+- FROM TABLE1;                                                                                                           |
      | SELECT * , COL2 FROM TABLE1;                                                                                                       |
      | SELECT COL1 AS _ALIASFROM TABLE1;                                                                                                  |
      | SELECT COL1 AS 1ALIAS FROM TABLE1;                                                                                                 |
      | SELECT COL1 AS */- FROM TABLE1;                                                                                                    |
      | SELECT TAB2.COL1 AS ALIAS, AS ALIAS2 FROM TABLE1;                                                                                  |
