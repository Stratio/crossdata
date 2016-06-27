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

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable5 over MONGO ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable5 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertintotableMongo5')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable5 (ident,name) VALUES (0, ['name_0'])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable5'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable6 over MONGO ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable6 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertintotableMongo6')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable6 (ident,name) VALUES (0, [])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable6'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable7 over MONGO ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable7 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertintotableMongo7')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable7 (ident,name) VALUES (0, [])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable7'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable8 over MONGO ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable8 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertintotableMongo8')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable8 (ident,name) VALUES (0, ['name_0','name_1'])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable8'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario Outline: [CROSSDATA-465] INSERT INTO insertintotable7 over Cassandra LIST
    When I execute 'CREATE EXTERNAL TABLE insertintotable<tablenumber> (ident INT, namesphone MAP<STRING, LONG>) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'databasetest', collection 'insertintotableMongo<tablenumber>')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable<tablenumber> (ident,namesphone) VALUES (0, <map>)'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable<tablenumber>'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

    Examples:
      | tablenumber  |map                |
      |     9        |('name_0'-> 00)           |
      |     10       |()                   |
      |     11       |('name_0'-> 00,'name_1'-> 10)  |