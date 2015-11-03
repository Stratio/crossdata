Feature: Select with funciont now

  Scenario: SELECT now(*) FROM catalogTest.tableTest;
    When I execute a query: 'SELECT now() FROM catalogTest.tableTest;'
    Then the number of rows of the table 'catalogTest.tableTest' has to be: '10'
