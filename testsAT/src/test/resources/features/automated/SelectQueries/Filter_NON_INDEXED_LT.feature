Feature: Basic Select queries with FILTER_NON_INDEXED_LT

  Scenario: SELECT * FROM catalogTest.tableTest WHERE age < 50;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE age < 50;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
      | 2                                   | name_2                                 | 20                                    | 20000000                                     | 2222.22                                    | 12.11                                   | false                                 |
      | 3                                   | name_3                                 | 30                                    | 30000000                                     | 3333.33                                    | 13.11                                   | true                                  |
      | 4                                   | name_4                                 | 40                                    | 40000000                                     | 4444.44                                    | 14.11                                   | false                                 |
      | 10                                  | name_10                                | 1                                     | 10000000                                     | 1111.11                                    | 20.11                                   | false                                 |

  Scenario: SELECT id FROM catalogTest.tableTest WHERE age < 50;
    When I execute a query: 'SELECT id FROM catalogTest.tableTest WHERE age < 50;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 1                                   |
      | 2                                   |
      | 3                                   |
      | 4                                   |
      | 10                                  |

  Scenario: SELECT age AS identificador FROM catalogTest.tableTest WHERE identificador < 'Carlos';
    When I execute a query: 'SELECT age AS identificador FROM catalogTest.tableTest WHERE identificador < 'Carlos';'
    Then an exception 'IS' thrown

  Scenario: SELECT * AS identificador FROM catalogTest.tab2 WHERE surname < 'Dominguez';
    When I execute a query: 'SELECT * AS identificador FROM catalogTest.tab2 WHERE surname < 'Dominguez';'
    Then the result has to be:
      | catalogTest.tab2.name-name-String | catalogTest.tab2.surname-surname-String |
      | Antonio                           | Alcocer                                 |

  Scenario: SELECT * AS identificador FROM catalogTest.tab2 WHERE surname < 'ZZZZ';
    When I execute a query: 'SELECT * AS identificador FROM catalogTest.tab2 WHERE surname < 'ZZZZ';'
    Then the result has to be:
      | catalogTest.tab2.name-name-String | catalogTest.tab2.surname-surname-String |
      | Antonio                           | Alcocer                                 |
      | Hugo                              | Dominguez                               |
      | Carlos                            | Hernandez                               |
      | Miguel                            | Fernandez                               |

  Scenario: SELECT * AS identificador FROM catalogTest.tab2 WHERE surname < 25;
    When I execute a query: 'SELECT * AS identificador FROM catalogTest.tab2 WHERE surname < 25;'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age < 0;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age < 0;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age < 30;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age < 30;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |
      | 1                                 | 10                                  |
      | 2                                 | 20                                  |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age < -50;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age < -50;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age < -3 ;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age < -3;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id < 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE reten < 'Hugo';'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten < 0.0;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten < 0.0;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten < 40.40;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten < 40.40;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |
      | 1.1                                 | 10.10                               |
      | 2.2                                 | 20.20                               |
      | 3.3                                 | 30.30                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten < -40.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten < -40.40;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten < 30.301;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten < 30.301;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |
      | 1.1                                 | 10.10                               |
      | 2.2                                 | 20.20                               |
      | 3.3                                 | 30.30                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten < 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten < 'Hugo';'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten < 0.0;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten < 0.0;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten < 40;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten < 40;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |
      | 1.1                                | 10.10                              |
      | 2.2                                | 20.20                              |
      | 3.3                                | 30.30                              |

  Scenario: (CROSSDATA-516)SELECT * FROM catalogTest.tab5 WHERE reten < -40.40;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten < -40.40;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten < 40.40;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten < 40.40;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |
      | 1.1                                | 10.10                              |
      | 2.2                                | 20.20                              |
      | 3.3                                | 30.30                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten < 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten < 'Hugo';'
    Then an exception 'IS' thrown
