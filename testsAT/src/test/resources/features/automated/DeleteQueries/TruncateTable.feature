Feature: (CROSSDATA-150)Truncate table tests

  Scenario: TRUNCATE TABLE catalogTest.tableTest;
    When I execute a query: 'TRUNCATE catalogTest.tableTest;'
    Then the number of rows of the table 'catalogTest.tableTest' has to be: '0'

  Scenario: TRUNCATE TABLE catalogTest.notExists;
    When I execute a query: 'TRUNCATE catalogTest.notExists;'
    Then an exception 'IS' thrown

  Scenario: TRUNCATE TABLE notExists.tableTest;
    When I execute a query: 'TRUNCATE notExists.tableTest;'
    Then an exception 'IS' thrown
