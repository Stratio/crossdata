Feature: Basic Select queries (without filters)

  Scenario: (CROSSDATA-312)SELECT * FROM catalogTest.tableTest;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
      | 2                                   | name_2                                 | 20                                    | 20000000                                     | 2222.22                                    | 12.11                                   | false                                 |
      | 3                                   | name_3                                 | 30                                    | 30000000                                     | 3333.33                                    | 13.11                                   | true                                  |
      | 4                                   | name_4                                 | 40                                    | 40000000                                     | 4444.44                                    | 14.11                                   | false                                 |
      | 5                                   | name_5                                 | 50                                    | 50000000                                     | 5555.55                                    | 15.11                                   | true                                  |
      | 6                                   | name_6                                 | 60                                    | 60000000                                     | 6666.66                                    | 16.11                                   | false                                 |
      | 7                                   | name_7                                 | 70                                    | 70000000                                     | 7777.77                                    | 17.11                                   | true                                  |
      | 8                                   | name_8                                 | 80                                    | 80000000                                     | 8888.88                                    | 18.11                                   | false                                 |
      | 9                                   | name_9                                 | 90                                    | 90000000                                     | 9999.99                                    | 19.11                                   | true                                  |
      | 10                                  | name_10                                | 1                                     | 10000000                                     | 1111.11                                    | 20.11                                   | false                                 |

  Scenario: SELECT id FROM catalogTest.tableTest;
    When I execute a query: 'SELECT id FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 1                                   |
      | 2                                   |
      | 3                                   |
      | 4                                   |
      | 5                                   |
      | 6                                   |
      | 7                                   |
      | 8                                   |
      | 9                                   |
      | 10                                  |

  Scenario: SELECT name FROM catalogTest.tableTest;
    When I execute a query: 'SELECT name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.name-name-String |
      | name_1                                 |
      | name_2                                 |
      | name_3                                 |
      | name_4                                 |
      | name_5                                 |
      | name_6                                 |
      | name_7                                 |
      | name_8                                 |
      | name_9                                 |
      | name_10                                |

  Scenario: SELECT phone FROM catalogTest.tableTest;
    When I execute a query: 'SELECT phone FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.phone-phone-BigInteger |
      | 10000000                                     |
      | 20000000                                     |
      | 30000000                                     |
      | 40000000                                     |
      | 50000000                                     |
      | 60000000                                     |
      | 70000000                                     |
      | 80000000                                     |
      | 90000000                                     |
      | 10000000                                     |

  Scenario: SELECT salary FROM catalogTest.tableTest;
    When I execute a query: 'SELECT salary FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.salary-salary-Double |
      | 1111.11                                    |
      | 2222.22                                    |
      | 3333.33                                    |
      | 4444.44                                    |
      | 5555.55                                    |
      | 6666.66                                    |
      | 7777.77                                    |
      | 8888.88                                    |
      | 9999.99                                    |
      | 1111.11                                    |

  Scenario: SELECT reten FROM catalogTest.tableTest;
    When I execute a query: 'SELECT reten FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.reten-reten-Float |
      | 11.11                                   |
      | 12.11                                   |
      | 13.11                                   |
      | 14.11                                   |
      | 15.11                                   |
      | 16.11                                   |
      | 17.11                                   |
      | 18.11                                   |
      | 19.11                                   |
      | 20.11                                   |

  Scenario: SELECT new FROM catalogTest.tableTest;
    When I execute a query: 'SELECT new FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.new-new-Boolean |
      | true                                  |
      | false                                 |
      | true                                  |
      | false                                 |
      | true                                  |
      | false                                 |
      | true                                  |
      | false                                 |
      | true                                  |
      | false                                 |

  Scenario: SELECT id, name FROM catalogTest.tableTest;
    When I execute a query: 'SELECT id, name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 1                                   | name_1                                 |
      | 2                                   | name_2                                 |
      | 3                                   | name_3                                 |
      | 4                                   | name_4                                 |
      | 5                                   | name_5                                 |
      | 6                                   | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: SELECT name, id FROM catalogTest.tableTest;
    When I execute a query: 'SELECT name, id FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.name-name-String | catalogTest.tableTest.id-id-Integer |
      | name_1                                 | 1                                   |
      | name_2                                 | 2                                   |
      | name_3                                 | 3                                   |
      | name_4                                 | 4                                   |
      | name_5                                 | 5                                   |
      | name_6                                 | 6                                   |
      | name_7                                 | 7                                   |
      | name_8                                 | 8                                   |
      | name_9                                 | 9                                   |
      | name_10                                | 10                                  |

  Scenario: SELECT id, id FROM catalogTest.tableTest;
    When I execute a query: 'SELECT id, id FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.id-id-Integer |
      | 1                                   | 1                                   |
      | 2                                   | 2                                   |
      | 3                                   | 3                                   |
      | 4                                   | 4                                   |
      | 5                                   | 5                                   |
      | 6                                   | 6                                   |
      | 7                                   | 7                                   |
      | 8                                   | 8                                   |
      | 9                                   | 9                                   |
      | 10                                  | 10                                  |

  Scenario: SELECT 1 FROM catalogTest.tableTest;
    When I execute a query: 'SELECT 1 FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.1-1-Integer |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |
      | 1                                 |

  Scenario: SELECT new FROM notExistsCatalog.tableTest;
    When I execute a query: 'SELECT new FROM notExistsCatalog.tableTest;'
    Then an exception 'IS' thrown

  Scenario: SELECT notExistsColumn FROM catalogTest.tableTest;
    When I execute a query: 'SELECT notExistsColumn FROM catalogTest.tableTest;'
    Then an exception 'IS' thrown

  Scenario: SELECT new FROM catalogTest.notExistsTable;
    When I execute a query: 'SELECT new FROM catalogTest.notExistsTable;'
    Then an exception 'IS' thrown
