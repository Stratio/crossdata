Feature: Update_PK_EQ

  Scenario: UPDATE catalogTest.tableTests SET id = 25 WHERE id = 1;
    When I execute a query: 'UPDATE catalogTest.tableTests SET id = 25 WHERE id = 1;'
    When I execute a query: 'SELECT id FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 25                                  |
      | 2                                   |
      | 3                                   |
      | 4                                   |
      | 5                                   |
      | 6                                   |
      | 7                                   |
      | 8                                   |
      | 9                                   |
      | 10                                  |

  Scenario: UPDATE catalogTest.tableTests SET id = 26 WHERE name = 'name_1';
    When I execute a query: 'UPDATE catalogTest.tableTests SET id = 26 WHERE name = 'name_1';'
    When I execute a query: 'SELECT id FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 25                                  |
      | 26                                  |
      | 3                                   |
      | 4                                   |
      | 5                                   |
      | 6                                   |
      | 7                                   |
      | 8                                   |
      | 9                                   |
      | 10                                  |

  Scenario: UPDATE catalogTest.tableTests SET id = 27, name = 'name_27' WHERE id = 3;
    When I execute a query: 'UPDATE catalogTest.tableTests SET id = 27, name = 'name_27' WHERE id = 3;'
    When I execute a query: 'SELECT id,name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 25                                  | name_1                                 |
      | 26                                  | name_2                                 |
      | 27                                  | name_27                                |
      | 4                                   | name_4                                 |
      | 5                                   | name_5                                 |
      | 6                                   | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: UPDATE catalogTest.tableTests SET id = 28, name = 'name_28' WHERE id = 4, name = 'name_4';
    When I execute a query: 'UPDATE catalogTest.tableTests SET id = 28, name = 'name_28' WHERE id = 4, name = 'name_4';'
    When I execute a query: 'SELECT id,name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 25                                  | name_1                                 |
      | 26                                  | name_2                                 |
      | 27                                  | name_27                                |
      | 28                                  | name_28                                |
      | 5                                   | name_5                                 |
      | 6                                   | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: UPDATE catalogTest.tableTests SET id = id - 6 WHERE id = 5;
    When I execute a query: 'UPDATE catalogTest.tableTests SET id = id - 6 WHERE id = 5;'
    When I execute a query: 'SELECT id,name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 25                                  | name_1                                 |
      | 26                                  | name_2                                 |
      | 27                                  | name_27                                |
      | 28                                  | name_28                                |
      | -1                                  | name_5                                 |
      | 6                                   | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: UPDATE catalogTest.tableTests SET id = id - 6 WHERE id = 6;
    When I execute a query: 'UPDATE catalogTest.tableTests SET id = id + 6 WHERE id = 6;'
    When I execute a query: 'SELECT id,name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 25                                  | name_1                                 |
      | 26                                  | name_2                                 |
      | 27                                  | name_27                                |
      | 28                                  | name_28                                |
      | -1                                  | name_5                                 |
      | 12                                  | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: UPDATE catalogTest.tab3 SET id = id + 3 WHERE id = 1;
    When I execute a query: 'UPDATE catalogTest.tab3 SET id = id + 3 WHERE id = 1;'
    When I execute a query: 'SELECT id,age FROM catalogTest.tab3 WHERE id = 4;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | 4                                 | 10                                  |

  Scenario: UPDATE catalogTest.tab3 SET id = 5 WHERE id = 2;
    When I execute a query: 'UPDATE catalogTest.tab3 SET id = 5 WHERE id = 2;'
    When I execute a query: 'SELECT id,age FROM catalogTest.tab3 WHERE id = 5;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | 5                                 | 20                                  |

  Scenario: UPDATE catalogTest.tab3 SET age = age + 1 WHERE id = 3;
    When I execute a query: 'UPDATE catalogTest.tab3 SET age = age + 1 WHERE id = 3;'
    When I execute a query: 'SELECT id,age FROM catalogTest.tab3 WHERE id = 3;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | 3                                 | 31                                  |

  Scenario: UPDATE catalogTest.tab3 SET age = 100 WHERE id = -4;
    When I execute a query: 'UPDATE catalogTest.tab3 SET age = 100 WHERE id = -4;'
    When I execute a query: 'SELECT id,age FROM catalogTest.tab3 WHERE id = -4;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | 100                                 |

  Scenario: UPDATE catalogTest.tab4 SET money = 3 WHERE money = 1.1;
    When I execute a query: 'UPDATE catalogTest.tab4 SET money = 3 WHERE money = 1.1;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab4 WHERE money = 3;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | 3                                   | 10.10                               |

  Scenario: UPDATE catalogTest.tab4 SET money = money +  3 WHERE money = 2.2;
    When I execute a query: 'UPDATE catalogTest.tab4 SET money = money + 3 WHERE money = 2.2;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab4 WHERE money = 5.2;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | 5.2                                 | 20.20                               |

  Scenario: UPDATE catalogTest.tab4 SET reten = 0.05 WHERE money = 1.1;
    When I execute a query: 'UPDATE catalogTest.tab4 SET reten = 3 WHERE money = 3.3;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab4 WHERE money = 3.3;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | 3.3                                 | 0.05                                |

  Scenario: UPDATE catalogTest.tab4 SET reten = reten + 3.2 WHERE money = -4.4;
    When I execute a query: 'UPDATE catalogTest.tab4 SET reten = reten + 3 WHERE money = -4.4;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab4 WHERE money = -4.4;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -37.20                              |

  Scenario: UPDATE catalogTest.tab5 SET money = 3 WHERE money = 1.1;
    When I execute a query: 'UPDATE catalogTest.tab5 SET money = 3 WHERE money = 1.1;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab5 WHERE money = 3;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | 3                                  | 10.10                              |

  Scenario: UPDATE catalogTest.tab5 SET money = money +  3 WHERE money = 2.2;
    When I execute a query: 'UPDATE catalogTest.tab5 SET money = money + 3 WHERE money = 2.2;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab5 WHERE money = 5.2;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | 5.2                                | 20.20                              |

  Scenario: UPDATE catalogTest.tab5 SET reten = 0.05 WHERE money = 1.1;
    When I execute a query: 'UPDATE catalogTest.tab5 SET reten = 3 WHERE money = 3.3;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab5 WHERE money = 3.3;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | 3.3                                | 0.05                               |

  Scenario: UPDATE catalogTest.tab5 SET reten = reten + 3.2 WHERE money = -4.4;
    When I execute a query: 'UPDATE catalogTest.tab5 SET reten = reten + 3 WHERE money = -4.4;'
    When I execute a query: 'SELECT money,reten FROM catalogTest.tab5 WHERE money = -4.4;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -37.20                             |
