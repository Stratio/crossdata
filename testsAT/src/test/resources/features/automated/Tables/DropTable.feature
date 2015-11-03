Feature: Drop Table tests

  Scenario: Drop a simple table
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Drop a simple table (IF EXISTS)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'DROP TABLE IF EXISTS catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Drop a table that not exists
    When I execute a query: 'DROP TABLE catalogTest.noExists;'
    Then an exception 'IS' thrown

  Scenario: Drop a table that not exists(IF EXISTS)
    When I execute a query: 'DROP TABLE IF EXISTS catalogTest.noExists;'
    Then an exception 'IS' thrown


  Scenario: Drop a table over a catalog that not exists
    When I execute a query: 'DROP TABLE notExists.noExists;'
    Then an exception 'IS' thrown

  Scenario: Drop a table over a catalog that not exists(IF EXISTS)
    When I execute a query: 'DROP TABLE IF EXISTS notExists.noExists;'
    Then an exception 'IS' thrown
