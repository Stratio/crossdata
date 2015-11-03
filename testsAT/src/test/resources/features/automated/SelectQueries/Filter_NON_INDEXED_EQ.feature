Feature: Select query with filter_non_indexded_eq

  Scenario: SELECT * FROM catalogTest.tableTest WHERE age = 10;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE age = 10;'
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
  Scenario: SELECT * FROM catalogTest.tableTest WHERE age = 10 AND phone = 10000000;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE age = 10 AND phone = 10000000;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |

  Scenario: SELECT * FROM catalogTest.tab1 WHERE age = -40;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE age = -40;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab1.id-id-Integer | catalogTest.tab1.age-age-Integer |
      | -4                             | -40                              |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age = 10;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age = 10;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | 1                                 | 10                                  |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten = 10.10;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten = 10.10;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | 1.1                                 | 10.10                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten = -40.40;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten = -40.40;'
    Then the result has to be contained:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten = 10.10;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten = 10.10;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | 1.1                                | 10.10                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten = -40.40;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten = -40.40;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |

  Scenario: SELECT * FROM catalogTest.tab6 WHERE new = false;
    When I execute a query: 'SELECT * FROM catalogTest.tab6 WHERE new = false;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab6.married-married-Boolean | catalogTest.tab6.new-new-Boolean |
      | true                                     | false                            |

  Scenario: SELECT * FROM catalogTest.tab6 WHERE new = true;
    When I execute a query: 'SELECT * FROM catalogTest.tab6 WHERE new = true;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab6.married-married-Boolean | catalogTest.tab6.new-new-Boolean |
      | false                                    | true                             |

  Scenario: SELECT * FROM catalogTest.tab1 WHERE age = 25;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE age = 25;'
    Then an exception 'IS NOT' thrown
    Then the result has to be contained:
      | catalogTest.tab1.id-id-Integer | catalogTest.tab1.age-age-Integer |

  Scenario: SELECT * FROM catalogTest.tab1 WHERE auxiliar = 25;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE auxiliar = 25;'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tableTest WHERE age = 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE age = 'Hugo';'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tableTest WHERE id = 1 AND new = 25;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE age = 1 AND  new= 25;'
    Then an exception 'IS' thrown
