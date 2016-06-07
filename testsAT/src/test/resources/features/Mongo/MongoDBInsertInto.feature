Feature:[CROSSDATA-465]Mongo InsertInto
  Scenario: [CROSSDATA-465] INSERT INTO insertIntotableMongo (ident,name,money,new,date) VALUES (0, 'name_0', 10.2 , true, '1999-11-30')
    When I execute 'CREATE EXTERNAL TABLE insertIntotableMongo1 (ident INT, name STRING, money DOUBLE, new BOOLEAN, date TIMESTAMP) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertIntotableMongo1')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertIntotableMongo1 (ident,name,money,new,date) VALUES (0, 'name_0', 10.2 ,true, '1999-11-30 00:00:00')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertIntotableMongo1'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 1     |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable2 (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')
    When I execute 'CREATE EXTERNAL TABLE insertIntotableMongo2 (ident INT, name STRING, money DOUBLE, new BOOLEAN, date TIMESTAMP) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertIntotableMongo2')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertIntotableMongo2 (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertIntotableMongo2'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 1     |

  Scenario: [CROSSDATA-465] INSERT INTO notexitstable (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')
    When I execute 'INSERT INTO notexitstable (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-465] INSERT INTO over CREATE TEMPORARY VIEW AS SELECT
    When I execute 'CREATE TEMPORARY VIEW temptable AS SELECT ident FROM insertIntotableMongo2'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO temptable(ident) VALUES (1)'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-465] INSERT INTO insertIntotableMongo4 two rows in the same sentence
    When I execute 'CREATE EXTERNAL TABLE insertIntotableMongo4 (ident INT, name STRING, money DOUBLE, new BOOLEAN, date TIMESTAMP) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertIntotableMongo4')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertIntotableMongo4 (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0'), (1, 11.2 ,true, '1999-11-30 00:00:00', 'name_1')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertIntotableMongo4'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 2     |