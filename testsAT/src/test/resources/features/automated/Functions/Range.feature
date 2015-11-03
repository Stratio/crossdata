Feature: Select with funciont range

  Scenario: SELECT concat(id) FROM catalogTest.tableTest WHERE name = range('name_1', 'name_9');
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE name = range('name_1', 'name_9');'
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

  Scenario: SELECT id FROM catalogTest.tableTest WHERE name = range('name_1', 'name_1');
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE name = range('name_1', 'name_1');'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 1                                   |

  Scenario: SELECT id FROM catalogTest.tableTest WHERE name = range('name_00', 'name_99');
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE name = range('name_00', 'name_99');'
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

  Scenario: SELECT id FROM catalogTest.tableTest WHERE name = range('name_00');'
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE name = range('name_00');'
    Then an exception 'IS' thrown
  Scenario: SELECT id FROM catalogTest.tableTest WHERE name = name = range();
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE name = range();'
    Then an exception 'IS' thrown
    
    
   Scenario: SELECT id FROM catalogTest.tableTest WHERE name = range(name_1, name_2);
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE name = range(catalogTest.tableTest.name, catalogTest.tableTest.name);'
    Then an exception 'IS' thrown