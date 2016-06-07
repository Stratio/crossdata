Feature:[CROSSDATA-465]Cassandra InsertInto
  Scenario: [CROSSDATA-465] INSERT INTO insertIntotable (ident,name,money,new,date) VALUES (0, 'name_0', 10.2 ,true, '1999-11-30')
    When I execute 'INSERT INTO insertintotable1 (ident,name,money,new,date) VALUES (0, 'name_0', 10.2 ,true, '1999-11-30 00:00:00')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable1'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 1     |

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable2 (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')
    When I execute 'INSERT INTO insertintotable2 (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable2'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 1     |

  Scenario: [CROSSDATA-465] INSERT INTO notexitstable (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')
    When I execute 'INSERT INTO notexitstable (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0')'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-465] INSERT INTO over TEMPORARY TABLE must return an exception
    When I execute 'INSERT INTO tab1(name) VALUES ('name_2')'
    Then an exception 'IS NOT' thrown


  Scenario: [CROSSDATA-465] INSERT INTO over CREATE TEMPORARY VIEW AS SELECT
    When I execute 'CREATE TEMPORARY VIEW temptable AS SELECT ident FROM insertintotable3'
    Then an exception 'IS NOT' thrown
    When I execute 'INSERT INTO temptable(ident) VALUES (1)'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-465] INSERT INTO insertintotable4 two rows in the same sentence
    When I execute 'INSERT INTO insertintotable4 (ident,money,new,date,name) VALUES (0, 10.2 ,true, '1999-11-30 00:00:00', 'name_0'), (1, 11.2 ,true, '1999-11-30 00:00:00', 'name_1')'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT count(*) FROM insertintotable4'
    Then The spark result has to have '1' rows:
      |_c0-long|
      | 2     |