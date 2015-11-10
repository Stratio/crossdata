@ignore @unimplemented
Feature: Test crossdata shell queries operations

  # testTable and testTableJoin are the same
  Background:
    Given I populate a Cassandra DATASTORE with keyspace testKeyspace and table testTable
    And I run command ATTACH CLUSTER testCluster ON DATASTORE Cassandra WITH OPTIONS "{'Hosts': '[${HOSTS}]',
  'Port': ${PORT}};"
    And I run command ATTACH CONNECTOR testConnector TO testCluster WITH OPTIONS {'DefaultLimit': '2'};
    And I run command CREATE CATALOG testCatalog;
    And I run command USE testCatalog;
    And I run command IMPORT TABLE testTable FROM CLUSTER testCluster;
    And I run command IMPORT TABLE testTableJoins FROM CLUSTER testCluster;


  # SELECT_OPERATOR
  Scenario: Retrieve all columns
    When I execute a query: 'SELECT * FROM testTable;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
      | 2                                   | name_2                                 | 20                                    | 20000000                                     | 2222.22                                    | 12.11                                   | false                                 |

  # NOTE: Take care about case sensitive names => You must set ""
  Scenario: Retrieve single column
    When I execute a query: 'SELECT phone-phone-BigInteger FROM testTable;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  Scenario: Retrieve twice columns
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |
      | 20000000                                     | 2222.22                                    |

  Scenario: Retrieve non-existing columns
    When I execute a query: 'SELECT error FROM testTable;'
    Then I expect a 'testCatalog.testTable.error is not valid column in this sentence' message.

  Scenario: Retrieve no columns
    When I execute a query: 'SELECT FROM tableTest;'
    Then I expect a 'Error recognized: Unknown parser error: Column name not found' message.

  # SELECT_WINDOW
  Scenario:
    When I execute a query: 'SELECT id FROM testtable WITH WINDOW 10 seconds;'
    Then I expect a 'Cannot determine execution path as no connector supports 10 SECONDS' message.

  # SELECT LIMIT
  Scenario: Select with negative limit
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable LIMIT -1;'
    Then I expect a 'Limit must be a positive number.' message.

  Scenario: Select with valid limit
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable LIMIT 1;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |

  Scenario: Select same amount as connector.DefaultLimit
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |
      | 20000000                                     | 2222.22                                    |

  # SELECT_<join_type>_JOIN
  # INNER | (RIGHT | LEFT | FULL) OUTER | CROSS
  Scenario: Select with INNER JOIN type
    When I execute query: 'SELECT table1.name-name-String, table2.salary-salary-Double FROM testtable AS table1 INNER JOIN testtablejoin AS table2 ON table1.id-id-Integer = table2.id-id-Integer;'
    Then I expect a 'Cannot determine execution path as no connector supports INNER JOIN ([testCatalog.testtable, testCatalog.testtablejoin]) ON [testCatalog.testtable.id-id-Integer = testCatalog.testtablejoin.id-id-Integer]' message.

  Scenario: Select with valid FULL OUTER JOIN type
    When I execute query: 'SELECT table1.name-name-String, table2.salary-salary-Double FROM testtable AS table1 FULL OUTER JOIN testtablejoin AS table2 ON table1.id-id-Integer = table2.id-id-Integer;'
    Then I expect a 'Cannot determine execution path as no connector supports FULL_OUTER JOIN ([testCatalog.testtable, testCatalog.testtablejoin]) ON [testCatalog.testtable.id-id-Integer = testCatalog.testtablejoin.id-id-Integer]' message.

  Scenario: Select with invalid join type
    When I execute query: 'SELECT table1.name-name-String, table2.salary-salary-Double FROM testtable AS table1 CENTER OUTER JOIN testtablejoin AS table2 ON table1.id-id-Integer = table2.id-id-Integer;'
    Then I expect a 'Error recognized: line 1:84: required (...)+ loop did not match anything at input 'CENTER'' message.

  # SELECT_<join_type>_JOIN_PARTIAL_RESULTS => C* + Decision
  # Scenario: Select with invalid join type
  # Scenario: Select with valid join type

  # SELECT_ORDER_BY
  Scenario: Non-existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable ORDER BY non-existing;'
    Then I expect a '' message.

  Scenario: Existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable ORDER BY phone-phone-BigInteger;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |
      | 20000000                                     | 2222.22                                    |

  # SELECT_GROUP_BY
  Scenario: Non-existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable GROUP BY non-existing;'
    Then I expect a '' message.

  Scenario: Existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable ORDER BY phone-phone-BigInteger;'
    Then I expect a '' message.

  # SELECT_CASE_WHEN
  Scenario: Non-existing column
    When I execute query: 'SELECT phone-phone-BigInteger, SALARY = CASE non-existing WHEN price < 2000.00 THEN 'Low' WHEN non-existing BETWEEN 2000.01 AND 5000.00 THEN 'Moderate' WHEN non-existing > 5000.01 THEN 'High' ELSE 'Unknown' END, FROM testTable;'
    Then I expect a '' message.

  Scenario: Existing column
    When I execute query: 'SELECT phone-phone-BigInteger, SALARY = CASE salary-salary-Double WHEN price < 2000.00 THEN 'Low' WHEN salary-salary-Double BETWEEN 2000.01 AND 5000.00 THEN 'Moderate' WHEN salary-salary-Double > 5000.01 THEN 'High' ELSE 'Unknown' END, FROM testTable;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger | SALARY   |
      | 10000000                                     | Low      |
      | 20000000                                     | Moderate |

  # SELECT_FUNCTIONS
  # DEFINED BY CONNECTOR
  # C* Connector. [range and should]
  # C* DataStore. [count, now, ttl, writetime and dateOf]
  Scenario: Query with RANGE
    When I execute query: 'SELECT RANGE(salary-salary-Double, 1000.00,2000.00) FROM testTable;'
    Then I expect a '' message.

  Scenario: Query with SHOULD
    When I execute query: 'SELECT SHOULD(salary-salary-Double, 1000.00,2000.00) FROM testTable;'
    Then I expect a '' message.

  Scenario: Query with COUNT
    When I execute query: 'SELECT COUNT(salary-salary-Double) FROM testTable;'
    Then I expect a '' message.

  Scenario: Query with NOW
    When I execute query: 'SELECT NOW FROM testTable;'
    Then I expect a '' message.

  Scenario: Query with TTL
    When I execute query: 'SELECT TTL FROM testTable;'
    Then I expect a '' message.

  Scenario: Query with WRITETIME
    When I execute query: 'SELECT WRITETIME FROM testTable;'
    Then I expect a '' message.

  Scenario: Query with DATEOF
    When I execute query: 'SELECT DATEOF FROM testTable;'
    Then I expect a '' message.

  # SELECT_SUBQUERY
  Scenario: Query with SUBQUERY
    When I execute query: 'SELECT phone-phone-BigInteger FROM testTable WHERE salary-salary-Double = (SELECT salary-salary-Double FROM testTable)'
    Then I expect a '' message.

  # FILTER_<column_type>_<relationship>
  ## EVALUATE AS WHERE CLAUSE:
  ## FILTER_PK_EQ
  ## FILTER_PK_IN
  ## FILTER_PK_MATCH
  ## FILTER_INDEXED_EQ
  ## FILTER_INDEXED_IN
  ## FILTER_INDEXED_MATCH
  ## FILTER_INDEXED_LIKE

  Scenario: Filter FILTER_PK_EQ
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.id-id-Integer = 1'
    Then I expect a '' message.

  Scenario: Filter FILTER_PK_IN
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.id-id-Integer IN(1,2)'
    Then I expect a '' message.

  Scenario: Filter FILTER_PK_MATCH
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.id-id-Integer MATCH(1,2)'
    Then I expect a '' message.

  # MUST BE INDEXED A FIELD
  Scenario: Filter FILTER_INDEXED_EQ
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.phone-phone-BigInteger = 10000000'
    Then I expect a '' message.

  Scenario: Filter FILTER_INDEXED_IN
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.phone-phone-BigInteger IN(10000000,20000000)'
    Then I expect a '' message.

  Scenario: Filter FILTER_INDEXED_MATCH
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.phone-phone-BigInteger MATCH(10000000,20000000)'
    Then I expect a '' message.

  Scenario: Filter FILTER_INDEXED_LIKE
    When I execute query: 'SELECT catalogTest.tableTest.phone-phone-BigInteger FROM testTable WHERE catalogTest.tableTest.phone-phone-BigInteger LIKE %10000000%'
    Then I expect a '' message.

  # Scenario: Filter from non-existing table

  # Scenario: Filter with non-existing column_type

  # Scenario: Filter with non-existing relation

  # Scenario: Filter with empty column_type

  # Scenario: Filter with empty relation