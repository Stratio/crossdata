Feature: Select with funciont count

  Scenario: SELECT count(*) FROM catalogTest.tableTest;
    When I execute a query: 'SELECT count(*) FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.count-count-Integer |
      | 10                                        |
