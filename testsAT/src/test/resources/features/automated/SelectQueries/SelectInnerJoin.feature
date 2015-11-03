Feature: Basic Select queries and inner join clause(without filters)

  Scenario: SELECT catalogTest.tableTest.id, catalogTest.tab1.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab1 ON catalogTest.tableTest.id = catalogTest.tab1.id;
    When I execute a query: 'SELECT catalogTest.tableTest.id, catalogTest.tab1.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab1 ON catalogTest.tableTest.id = catalogTest.tab1.id;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tab1.age-age-Integer |
      | 1                                   | 10                               |
      | 2                                   | 20                               |
      | 3                                   | 30                               |

  Scenario: SELECT catalogTest.tableTest.id, catalogTest.tab3.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab3 ON catalogTest.tableTest.id = catalogTest.tab3.id;
    When I execute a query: 'SELECT catalogTest.tableTest.id, catalogTest.tab3.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab3 ON catalogTest.tableTest.id = catalogTest.tab3.id;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tab3.age-age-BigInteger |
      | 1                                   | 10                                  |
      | 2                                   | 20                                  |
      | 3                                   | 30                                  |

  Scenario: SELECT catalogTest.tableTest.id, catalogTest.tab3.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab3 ON catalogTest.tableTest.id = catalogTest.tab3.id;
    When I execute a query: 'SELECT catalogTest.tableTest.id, catalogTest.tab3.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab3 ON catalogTest.tableTest.name = catalogTest.tab3.id;'
    Then an exception 'IS' thrown
  
  Scenario: SELECT catalogTest.tableTest.id, catalogTest.tab3.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab3 ON catalogTest.tableTest.id = catalogTest.tab1.id;
    When I execute a query: 'SELECT catalogTest.tableTest.id, catalogTest.tab3.age FROM catalogTest.tableTest INNER JOIN catalogTest.tab3 ON catalogTest.tableTest.id = catalogTest.tab1.id;'
    Then an exception 'IS' thrown
    
  Scenario: SELECT catalogTest.tab4.money, catalogTest.tab5.reten FROM catalogTest.tab4 INNER JOIN catalogTest.tab5 ON catalogTest.tab4.money = catalogTest.tab5.money;
    When I execute a query: ' SELECT catalogTest.tab4.money, catalogTest.tab5.reten FROM catalogTest.tab4 INNER JOIN catalogTest.tab5 ON catalogTest.tab4.money = catalogTest.tab5.money;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double| catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |
      | 1.1                                | 10.10                              |
      | 2.2                                | 20.20                              |
      | 3.3                                | 30.30                              |