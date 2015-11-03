Feature: Select query with filter primary key equals

  Scenario: SELECT * FROM catalogTest.tableTest WHERE id = 1;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE id = 1;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |

  #Scenario: SELECT * FROM catalogTest.tableTest WHERE name = 'name_1';
  #    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE name = 'name_1';'
  # Then an exception 'IS NOT' thrown
  #Then the result has to be contained:
  #| catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
  #| 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
 
  Scenario: SELECT * FROM catalogTest.tab1 WHERE id = -4;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE id = -4;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab1.id-id-Integer | catalogTest.tab1.age-age-Integer |
      | -4                             | -40                              |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id = 1;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE id = 1;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | 1                                 | 10                                  |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money = 1.1;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money = 1.1;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | 1.1                                 | 10.10                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money = -4.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money = -4.4;'
    Then the result has to be contained:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money = 1.1;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money = 1.1;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | 1.1                                | 10.10                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money = -4.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money = -4.4;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |

  Scenario: SELECT * FROM catalogTest.tab6 WHERE married = true;
    When I execute a query: 'SELECT * FROM catalogTest.tab6 WHERE married = true;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab6.married-married-Boolean | catalogTest.tab6.new-new-Boolean |
      | true                                     | false                            |

  Scenario: SELECT * FROM catalogTest.tab6 WHERE married = false;
    When I execute a query: 'SELECT * FROM catalogTest.tab6 WHERE married = false;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab6.married-married-Boolean | catalogTest.tab6.new-new-Boolean |
      | false                                    | true                             |

  Scenario: SELECT * FROM catalogTest.tab1 WHERE id = 25;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE id = 25;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab1.id-id-Integer | catalogTest.tab1.age-age-Integer |

  Scenario: SELECT * FROM catalogTest.tab1 WHERE auxiliar = 25;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE auxiliar = 25;'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tableTest WHERE id = 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE id = 'Hugo';'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tableTest WHERE id = 1 AND name = 25;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE id = 1 AND name = 25;'
    Then an exception 'IS' thrown
