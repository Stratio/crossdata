Feature: Basic Select queries with FILTER_PK_LET

  Scenario: SELECT * FROM catalogTest.tableTest WHERE id <= 4;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE id <= 4;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
      | 2                                   | name_2                                 | 20                                    | 20000000                                     | 2222.22                                    | 12.11                                   | false                                 |
      | 3                                   | name_3                                 | 30                                    | 30000000                                     | 3333.33                                    | 13.11                                   | true                                  |
      | 4                                   | name_4                                 | 40                                    | 40000000                                     | 4444.44                                    | 14.11                                   | false                                 |

  Scenario: SELECT id FROM catalogTest.tableTest WHERE id <= 4;
    When I execute a query: 'SELECT * FROM catalogTest.tableTest WHERE id <= 4;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 1                                   |
      | 2                                   |
      | 3                                   |
      | 4                                   |

  Scenario: SELECT id AS identificador FROM catalogTest.tableTest WHERE identificador <= 4;
    When I execute a query: 'SELECT id AS identificador FROM catalogTest.tableTest WHERE identificador <= 4;'
    Then the result has to be:
      | catalogTest.tableTest.id-identificador-Integer |
      | 1                                              |
      | 2                                              |
      | 3                                              |
      | 4                                              |

  Scenario: SELECT id AS identificador FROM catalogTest.tableTest WHERE identificador <= 4.5;
    When I execute a query: 'SELECT id AS identificador FROM catalogTest.tableTest WHERE identificador <= 4.5;'
    Then the result has to be:
      | catalogTest.tableTest.id-identificador-Integer |
      | 1                                              |
      | 2                                              |
      | 3                                              |
      | 4                                              |

  Scenario: SELECT id AS identificador FROM catalogTest.tableTest WHERE identificador <= 'Carlos';
    When I execute a query: 'SELECT id AS identificador FROM catalogTest.tableTest WHERE identificador <= 'Carlos';'
    Then an exception 'IS' thrown

  Scenario: SELECT * AS identificador FROM catalogTest.tab2 WHERE name <= 'Carlos';
    When I execute a query: 'SELECT * AS identificador FROM catalogTest.tab2 WHERE name <= 'Carlos';'
    Then the result has to be:
      | catalogTest.tab2.name-name-String | catalogTest.tab2.surname-surname-String |
      | Antonio                           | Alcocer                                 |
      | Carlos                            | Hernandez                               |

  Scenario: SELECT * AS identificador FROM catalogTest.tab2 WHERE name <= 'ZZZZ';
    When I execute a query: 'SELECT * AS identificador FROM catalogTest.tab2 WHERE name <= 'ZZZZ';'
    Then the result has to be:
      | catalogTest.tab2.name-name-String | catalogTest.tab2.surname-surname-String |
      | Antonio                           | Alcocer                                 |
      | Hugo                              | Dominguez                               |
      | Carlos                            | Hernandez                               |
      | Miguel                            | Fernandez                               |

  Scenario: SELECT * AS identificador FROM catalogTest.tab2 WHERE name <= 25;
    When I execute a query: 'SELECT * AS identificador FROM catalogTest.tab2 WHERE name <= 25;'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id <= 0;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE id <= 0;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id <= 2;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE id <= 2;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |
      | 1                                 | 10                                  |
      | 2                                 | 20                                  |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id <= -5;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE id <= -5;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id <= -3 ;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE id <= -3;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE id <= 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE money <= 'Hugo';'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money <= 4.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money <= -4.4;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money <= 3.3;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money <= 3.3;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |
      | 1.1                                 | 10.10                               |
      | 2.2                                 | 20.20                               |
      | 3.3                                 | 30.30                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money < -4.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money < -4.4;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money <= 3.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money <= 3.4;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |
      | 1.1                                 | 10.10                               |
      | 2.2                                 | 20.20                               |
      | 3.3                                 | 30.30                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE money <= 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE money <= 'Hugo';'
    Then an exception 'IS' thrown

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money <= 4.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money <= -4.4;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Double | catalogTest.tab5.reten-reten-Double |
      | -4.4                                | -40.40                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money <= 3.3;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money <= 3.3;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Double | catalogTest.tab5.reten-reten-Double |
      | -4.4                                | -40.40                              |
      | 1.1                                 | 10.10                               |
      | 2.2                                 | 20.20                               |
      | 3.3                                 | 30.30                               |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money < -4.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money < -4.4;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Double | catalogTest.tab5.reten-reten-Double |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money <= 3.4;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money <= 3.4;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Double | catalogTest.tab5.reten-reten-Double |
      | -4.4                                | -40.40                              |
      | 1.1                                 | 10.10                               |
      | 2.2                                 | 20.20                               |
      | 3.3                                 | 30.30                               |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE money <= 'Hugo';
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE money <= 'Hugo';'
    Then an exception 'IS' thrown
