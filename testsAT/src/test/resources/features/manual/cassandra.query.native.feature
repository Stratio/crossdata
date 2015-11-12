@ignore @unimplemented
Feature: Test crossdata shell queries operations

  # testTable and testTableJoin are the same
  # keyspace name in C* must be the same as XD catalog name
  Background:
    Given I populate a Cassandra DATASTORE with keyspace testCatalog and table testTable
    And I run command ATTACH CLUSTER testCluster ON DATASTORE Cassandra WITH OPTIONS "{'Hosts': '[${HOSTS}]',
  'Port': ${PORT}};"
    And I run command ATTACH CONNECTOR CassandraConnector TO testCluster WITH OPTIONS {'DefaultLimit': '2'};
    And I run command CREATE CATALOG testCatalog;
    And I run command USE testCatalog;
    And I run command IMPORT TABLE testTable FROM CLUSTER testCluster;
    And I run command IMPORT TABLE testTableJoins FROM CLUSTER testCluster;
    And I run command CREATE FULL_TEXT INDEX stringIndex ON testTable (catalogTest.testTable.name-name-String);

  # SELECT_OPERATOR
  Scenario: Retrieve all columns
    When I execute a query: 'SELECT * FROM testTable;'
    Then the result has to be:
      | catalogTest.testTable.id-id-Integer | catalogTest.testTable.name-name-String | catalogTest.testTable.age-age-Integer | catalogTest.testTable.phone-phone-BigInteger | catalogTest.testTable.salary-salary-Double | catalogTest.testTable.reten-reten-Float | catalogTest.testTable.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
      | 2                                   | name_2                                 | 20                                    | 20000000                                     | 2222.22                                    | 12.11                                   | false                                 |

  # NOTE: Take care about case sensitive names => You must set ""
  Scenario: Retrieve single column
    When I execute a query: 'SELECT phone-phone-BigInteger FROM testTable;'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  Scenario: Retrieve twice columns
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable;'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger | catalogTest.testTable.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |
      | 20000000                                     | 2222.22                                    |

  Scenario: Retrieve non-existing columns
    When I execute a query: 'SELECT error FROM testTable;'
    Then I expect a 'testCatalog.testTable.error is not valid column in this sentence' message.

  Scenario: Retrieve no columns
    When I execute a query: 'SELECT FROM testTable;'
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
      | catalogTest.testTable.phone-phone-BigInteger | catalogTest.testTable.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |

  Scenario: Select same amount as connector.DefaultLimit
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable;'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger | catalogTest.testTable.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |
      | 20000000                                     | 2222.22                                    |

  Scenario: Select greater amount than connector.DefaultLimit
    When I execute a query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable LIMIT 3;'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger | catalogTest.testTable.salary-salary-Double |
      | 10000000                                     | 1111.11                                    |
      | 20000000                                     | 2222.22                                    |
      | 30000000                                     | 3333.33                                    |

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
    Then I expect a 'Cannot determine execution path as no connector supports ORDER BY 'non-existing'' message.

  Scenario: Existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable ORDER BY phone-phone-BigInteger;'
    Then I expect a 'Cannot determine execution path as no connector supports ORDER BY 'phone-phone-BigInteger'' message.

  Scenario: Existing column with reserved keyword
    When I execute query: 'SELECT phone-phone-BigInteger, "double" FROM testTable ORDER BY "double";'
    Then I expect a 'Cannot determine execution path as no connector supports ORDER BY 'double'' message.

  # SELECT_GROUP_BY
  Scenario: Non-existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable GROUP BY non-existing;'
    Then I expect a 'Cannot determine execution path as no connector supports GROUP BY 'non-existing'' message.

  Scenario: Existing column
    When I execute query: 'SELECT phone-phone-BigInteger, salary-salary-Double FROM testTable GROUP BY phone-phone-BigInteger;'
    Then I expect a 'Cannot determine execution path as no connector supports GROUP BY 'phone-phone-BigInteger'' message.

  Scenario: Existing column with reserved keyword
    When I execute query: 'SELECT phone-phone-BigInteger, "double" FROM testTable GROUP BY "double";'
    Then I expect a 'Cannot determine execution path as no connector supports GROUP BY 'double'' message.

  # SELECT_CASE_WHEN
  Scenario: Non-existing column
    When I execute query: 'SELECT phone-phone-BigInteger, SALARY = CASE non-existing WHEN non-existing < 2000.00 THEN 'Low' WHEN non-existing > 5000.01 THEN 'High' ELSE 'Unknown' END, FROM testTable;'
    Then I expect a 'Error recognized: Unknown parser error: Column name not found' message.

  Scenario: Existing column
    When I execute query: 'SELECT phone-phone-BigInteger, CASE WHEN salary-salary-Double WHEN salary-salary-Double < 2000.00 THEN 'Low' WHEN salary-salary-Double > 2000.01 THEN 'High' ELSE 'Unknown' END, FROM testTable;'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger | salary-salary-Double   |
      | 10000000                                     | Low                    |
      | 20000000                                     | High                   |

  # SELECT_FUNCTIONS
  # DEFINED BY CONNECTOR
  # => CASE SENSITIVE
  # C* Connector. [range and should]
  # C* DataStore. [count, now, ttl, writetime and dateOf]
  Scenario: Query with RANGE
    When I execute query: 'SELECT catalogTest.testTable.id-id-Integer, catalogTest.testTable.name-name-String FROM testtable WHERE id=range(1,2);'
    Then the result has to be:
    | catalogTest.testTable.id-id-Integer | catalogTest.testTable.name-name-String |
    | 1                                   | name_1                                 |
    | 2                                   | name_2                                 |

  Scenario: Query with SHOULD
    When I execute query: 'SELECT catalogTest.testTable.id-id-Integer, catalogTest.testTable.name-name-String FROM testTable WHERE id=should(1, 2);'
    Then the result has to be:
    | catalogTest.testTable.id-id-Integer | catalogTest.testTable.name-name-String |
    | 1                                   | name_1                                 |
    | 2                                   | name_2                                 |


  # This scenario depends on the connector.DefaultLimit
  Scenario: Query with COUNT
    When I execute query: 'SELECT count(*) FROM testTable;'
    Then the result has to be:
      | count(*)        |
      | 2               |

  Scenario: Query with COUNT and LIMIT
    When I execute query: 'SELECT count(*) FROM testTable LIMIT 3;'
    Then the result has to be:
      | count(*)        |
      | 3               |

  Scenario: Query with NOW
    When I execute query: 'SELECT now() FROM testTable LIMIT 1;'
    Then the result has to be:
      | now()                                              |
      | 19d07c30-8890-11e5-ab99-99a70c254b38               |

  Scenario: Query with TTL
    When I execute query: 'SELECT ttl(catalogTest.testTable.name-name-String) FROM testTable LIMIT 1;'
    Then the result has to be:
      | ttl(catalogTest.testTable.name-name-String)        |
      | 0                                                  |

  Scenario: Query with WRITETIME
    When I execute query: 'SELECT writetime(catalogTest.testTable.name-name-String) FROM testTable LIMIT 1;'
    Then the result has to be:
      | writetime(catalogTest.testTable.name-name-String)        |
      | 1447238887744173                                         |

  Scenario: Query with DATEOF
    When I execute query: 'SELECT dateOf(now()) FROM testTable LIMIT 1;'
    Then the result has to be:
      | dateOf(now())                          |
      | 2015-11-11 16:55:04+0000               |

  Scenario: Query with NON_FUNCTION
    When I execute query: 'SELECT non_function() FROM testTable LIMIT 1;'
    Then I expect a 'Error recognized: Unknown parser error: Function not found' message.

  # SELECT_SUBQUERY
  Scenario: Query with SUBQUERY
    When I execute query: 'SELECT phone-phone-BigInteger FROM testTable WHERE salary-salary-Double = (SELECT salary-salary-Double FROM testTable)'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  # FILTER_<column_type>_<relationship => It is as where clause:
  ## FILTER_PK_EQ
  ## FILTER_PK_IN
  ## FILTER_PK_MATCH
  ## FILTER_INDEXED_EQ
  ## FILTER_INDEXED_IN
  ## FILTER_INDEXED_MATCH
  ## IT WILL BE REMOVED: FILTER_INDEXED_LIKE

  Scenario: Filter FILTER_PK_EQ
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.id-id-Integer = 1'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |

  Scenario: Filter FILTER_PK_IN
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.id-id-Integer IN[1,2]'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  # must be a string type field
  Scenario: Filter FILTER_PK_MATCH
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.id-id-Integer MATCH(1)'
    Then I expect a 'No mapper found for field 'id'' message.

  # MUST BE INDEXED A FIELD
  Scenario: Filter FILTER_INDEXED_EQ
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String = 'name_1''
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |

  Scenario: Filter FILTER_INDEXED_IN
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String IN('name_1','name_2')'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  Scenario: Filter FILTER_INDEXED_MATCH
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String MATCH('name_1')'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |

  Scenario: Filter FILTER_INDEXED_MATCH end with
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String MATCH('*_1')'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |

  Scenario: Filter FILTER_INDEXED_MATCH start with
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String MATCH('name*')'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  Scenario: Filter FILTER_INDEXED_MATCH contains
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String MATCH('*ame_*')'
    Then the result has to be:
      | catalogTest.testTable.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |

  Scenario: Filter FILTER_INDEXED_MATCH no results
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String MATCH('*no_results*')'
    Then I expect a '0 results' message.

  Scenario: Filter FILTER_INDEXED_LIKE literal
    When I execute query: 'SELECT catalogTest.testTable.phone-phone-BigInteger FROM testTable WHERE catalogTest.testTable.name-name-String LIKE('name_1')'
    Then I expect a 'Function FILTER_INDEXED_LIKE is not supported' message.