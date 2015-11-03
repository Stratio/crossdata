Feature: Delete_PK_EQ

  Scenario: DELETE FROM catalogTest.tableTest WHERE id = 1;
    When I execute a query: 'DELETE FROM catalogTest.tableTest WHERE id = 1;'
    When I execute a query: 'SELECT id FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer |
      | 2                                   |
      | 3                                   |
      | 4                                   |
      | 5                                   |
      | 6                                   |
      | 7                                   |
      | 8                                   |
      | 9                                   |
      | 10                                  |

  Scenario: DELETE FROM catalogTest.tableTest WHERE id = 2;
    When I execute a query: 'DELETE FROM catalogTest.tableTest WHERE id = 2;'
    When I execute a query: 'SELECT name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.name-name-String |
      | name_3                                 |
      | name_4                                 |
      | name_5                                 |
      | name_6                                 |
      | name_7                                 |
      | name_8                                 |
      | name_9                                 |
      | name_10                                |

  Scenario: DELETE FROM catalogTest.tableTest WHERE id = 3;
    When I execute a query: 'DELETE FROM catalogTest.tableTest WHERE id = 3;'
    When I execute a query: 'SELECT id , name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 4                                   | name_4                                 |
      | 5                                   | name_5                                 |
      | 6                                   | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: DELETE FROM catalogTest.tableTest WHERE id = 3;
    When I execute a query: 'DELETE FROM catalogTest.tableTest WHERE  id = 3;'
    When I execute a query: 'SELECT id , name FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String |
      | 4                                   | name_4                                 |
      | 5                                   | name_5                                 |
      | 6                                   | name_6                                 |
      | 7                                   | name_7                                 |
      | 8                                   | name_8                                 |
      | 9                                   | name_9                                 |
      | 10                                  | name_10                                |

  Scenario: DELETE FROM catalogTests.tab3 WHERE id = 1;
    When I execute a query: 'DELETE FROM catalogTest.tab3 WHERE id = 1;'
    When I execute a query: 'SELECT id FROM catalogTest.tab3;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger |
      | 2                                 |
      | 3                                 |
      | -4                                |

  Scenario: DELETE FROM catalogTests.tab3 WHERE id = -4;
    When I execute a query: 'DELETE FROM catalogTest.tab3 WHERE id = -4;'
    When I execute a query: 'SELECT id FROM catalogTest.tab3;'
    Then the result has to be:
      | catalogTest.tab3.id-id-BigInteger |
      | 2                                 |
      | 3                                 |

  Scenario: DELETE FROM catalogTests.tab4 WHERE money = 1.1;
    When I execute a query: 'DELETE FROM catalogTest.tab4 WHERE money = 1.1;'
    When I execute a query: 'SELECT money FROM catalogTest.tab4;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double |
      | 2.2                                 |
      | 3.3                                 |
      | -4.4                                |

  Scenario: DELETE FROM catalogTests.tab4 WHERE money = -4.4;
    When I execute a query: 'DELETE FROM catalogTest.tab4 WHERE money = -4.4;'
    When I execute a query: 'SELECT money FROM catalogTest.tab4;'
    Then the result has to be:
      | catalogTest.tab4.money-money-Double |
      | 2.2                                 |
      | 3.3                                 |

  Scenario: DELETE FROM catalogTests.tab5 WHERE money = 1.1;
    When I execute a query: 'DELETE FROM catalogTest.tab5 WHERE money = 1.1;'
    When I execute a query: 'SELECT money FROM catalogTest.tab5;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float |
      | 2.2                                |
      | 3.3                                |
      | -4.4                               |

  Scenario: DELETE FROM catalogTests.tab5 WHERE money = -4.4;
    When I execute a query: 'DELETE FROM catalogTest.tab5 WHERE money = -4.4;'
    When I execute a query: 'SELECT money FROM catalogTest.tab5;'
    Then the result has to be:
      | catalogTest.tab5.money-money-Float |
      | 2.2                                |
      | 3.3                                |

  Scenario: DELETE FROM catalogTests.tab1 WHERE id = 'Incomplatible';
    When I execute a query: 'DELETE FROM catalogTests.tab1 WHERE id = 'Incomplatible';'
    Then an exception 'IS' thrown

  Scenario: DELETE FROM catalogTests.notExists WHERE id = 1;
    When I execute a query: 'DELETE FROM catalogTests.notExists WHERE id = 1;'
    Then an exception 'IS' thrown

  Scenario: DELETE FROM notExists.tab1 WHERE id = 1;
    When I execute a query: 'DELETE FROM notExists.notExists WHERE id = 1;'
    Then an exception 'IS' thrown
