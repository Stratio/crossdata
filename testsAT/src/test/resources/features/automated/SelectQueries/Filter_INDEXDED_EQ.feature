Feature: Select query with filter indexed equals

  Scenario: SELECT * FROM catalogTest.tab1 WHERE age = 10;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE age = 10;'
    Then the result has to be contained:
      | catalogTest.tab1.id-id-Integer | catalogTest.tab1.age-age-Integer |
      | 1                              | 10                               |

  Scenario: SELECT * FROM catalogTest.tab1 WHERE age = -40;
    When I execute a query: 'SELECT * FROM catalogTest.tab1 WHERE age =-40;'
    Then the result has to be contained:
      | catalogTest.tab1.id-id-Integer | catalogTest.tab1.age-age-Integer |
      | -4                             | -40                              |

  Scenario: SELECT * FROM catalogTest.tab2 WHERE surname = 'Dominguez';
    When I execute a query: 'SELECT * FROM catalogTest.tab2 WHERE surname = 'Dominguez';'
    Then the result has to be contained:
      | catalogTest.tab2.name-name-String | catalogTest.tab2.surname-surname-String |
      | Hugo                              | Dominguez                               |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age = 10;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age = 10;'
    Then the result has to be contained:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | 1                                 | 10                                  |

  Scenario: SELECT * FROM catalogTest.tab3 WHERE age = -40;
    When I execute a query: 'SELECT * FROM catalogTest.tab3 WHERE age =-40;'
    Then the result has to be contained:
      | catalogTest.tab3.id-id-BigInteger | catalogTest.tab3.age-age-BigInteger |
      | -4                                | -40                                 |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten = 10.10;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten = 10.10;'
    Then the result has to be contained:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | 1.1                                 | 10.10                               |

  Scenario: SELECT * FROM catalogTest.tab4 WHERE reten = -40.40;
    When I execute a query: 'SELECT * FROM catalogTest.tab4 WHERE reten =-40.40;'
    Then the result has to be contained:
      | catalogTest.tab4.money-money-Double | catalogTest.tab4.reten-reten-Double |
      | -4.4                                | -40.40                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten = 10.10;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten = 10.10;'
    Then the result has to be contained:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | 1.1                                | 10.10                              |

  Scenario: SELECT * FROM catalogTest.tab5 WHERE reten = -40;
    When I execute a query: 'SELECT * FROM catalogTest.tab5 WHERE reten =-40.40;'
    Then the result has to be contained:
      | catalogTest.tab5.money-money-Float | catalogTest.tab5.reten-reten-Float |
      | -4.4                               | -40.40                             |

  Scenario: SELECT * FROM catalogTest.tab6 WHERE married = true;
    When I execute a query: 'SELECT * FROM catalogTest.tab6 WHERE new = true;'
    Then the result has to be contained:
      | catalogTest.tab6.married-married-Boolean | catalogTest.tab6.new-new-Boolean |
      | false                                    | true                             |

  Scenario: SELECT * FROM catalogTest.tab6 WHERE married = false;
    When I execute a query: 'SELECT * FROM catalogTest.tab6 WHERE new = false;'
    Then the result has to be contained:
      | catalogTest.tab6.married-married-Boolean | catalogTest.tab6.new-new-Boolean |
      | true                                     | false                            |
