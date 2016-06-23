Feature:[CROSSDATA-465]Cassandra InsertInto
  Scenario: [CROSSDATA-465] INSERT INTO insertIntotableElasticSearch (ident,name,money,new,date) VALUES (0, 'name_0', 10.2 , true, '1999-11-30')
    When I execute 'CREATE EXTERNAL TABLE insertIntotableES1 (ident INT, name STRING, money DOUBLE, new BOOLEAN) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES1', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertIntotableES1 (ident,name,money,new) VALUES (0, 'name_0', 10.2 ,true)'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertIntotableES1'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 1      |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable2 (ident,money,new,name) VALUES (0, 10.2 ,true,'name_0')
    When I execute 'CREATE EXTERNAL TABLE insertIntotableES2 (ident INT, name STRING, money DOUBLE, new BOOLEAN) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES2', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertIntotableES2 (ident,money,new,name) VALUES (0, 10.2 ,true,'name_0')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertIntotableES2'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 1     |

  Scenario: [CROSSDATA-465] INSERT INTO notexitstable (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')
    When I execute 'INSERT INTO notexitstable (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-465] INSERT INTO over CREATE TEMPORARY VIEW AS SELECT
    When I execute 'CREATE TEMPORARY VIEW temptable AS SELECT ident FROM insertIntotableES2'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO temptable(ident) VALUES (1)'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable4 two rows in the same sentence
    When I execute 'CREATE EXTERNAL TABLE insertIntotableES4 (ident INT, name STRING, money DOUBLE, new BOOLEAN) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES4', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertIntotableES4 (ident,money,new,name) VALUES (0, 10.2 ,true, 'name_0'), (1, 11.2 ,true, 'name_1')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertIntotableES4'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 2     |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable6 over ES ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable6 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES5', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable6 (ident,name) VALUES (0, [])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable6'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable7 over ES ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable7 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES6', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable7 (ident,name) VALUES (0, [])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable7'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable8 over ES ARRAY
    When I execute 'CREATE EXTERNAL TABLE insertintotable8 (ident INT, name ARRAY<STRING>) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES7', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO insertintotable8 (ident,name) VALUES (0, ['name_0','name_1'])'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable8'
    Then The spark result has to have '1' rows:
      |_c0-long       |
      | 1             |

  Scenario Outline: [CROSSDATA-465] INSERT INTO insertintotable<tablenumber> ES MAP
    When I execute 'CREATE EXTERNAL TABLE insertintotable<tablenumber> (ident INT, namesphone MAP<STRING, LONG>) USING com.stratio.crossdata.connector.elasticsearch OPTIONS (es.resource 'databasetest/insertIntotableES<tablenumber>', es.nodes '${ES_NODE}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
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