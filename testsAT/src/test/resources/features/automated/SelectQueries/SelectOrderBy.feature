Feature: Select query with ORDER BY clause

  Scenario: SELECT company FROM catalogTest.usersorderby ORDER BY company ASC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company FROM catalogTest.usersorderby ORDER BY company ASC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String |
      | Company01                                       |
      | Company01                                       |
      | Company01                                       |
      | Company01                                       |
      | Company02                                       |
      | Company02                                       |
      | Company02                                       |
      | Company02                                       |
      | Company03                                       |
      | Company03                                       |
      | Company03                                       |
      | Company03                                       |
      | Company04                                       |
      | Company04                                       |
      | Company04                                       |
      | Company04                                       |
      | Company05                                       |
      | Company05                                       |
      | Company05                                       |
      | Company05                                       |

  Scenario: SELECT company FROM catalogTest.usersorderby ORDER BY company;
    When I execute a query: 'SELECT catalogTest.usersorderby.company FROM catalogTest.usersorderby ORDER BY company;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String |
      | Company01                                       |
      | Company01                                       |
      | Company01                                       |
      | Company01                                       |
      | Company02                                       |
      | Company02                                       |
      | Company02                                       |
      | Company02                                       |
      | Company03                                       |
      | Company03                                       |
      | Company03                                       |
      | Company03                                       |
      | Company04                                       |
      | Company04                                       |
      | Company04                                       |
      | Company04                                       |
      | Company05                                       |
      | Company05                                       |
      | Company05                                       |
      | Company05                                       |

  Scenario: SELECT company FROM catalogTest.usersorderby ORDER BY company ASC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company FROM catalogTest.usersorderby ORDER BY company DESC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String |
      | Company05                                       |
      | Company05                                       |
      | Company05                                       |
      | Company05                                       |
      | Company04                                       |
      | Company04                                       |
      | Company04                                       |
      | Company04                                       |
      | Company03                                       |
      | Company03                                       |
      | Company03                                       |
      | Company03                                       |
      | Company02                                       |
      | Company02                                       |
      | Company02                                       |
      | Company02                                       |
      | Company01                                       |
      | Company01                                       |
      | Company01                                       |
      | Company01                                       |

  Scenario: SELECT company, client FROM catalogTest.usersorderby ORDER BY company ASC, client ASC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company, catalogTest.usersorderby.client FROM catalogTest.usersorderby ORDER BY company ASC, client ASC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String | catalogTest.usersorderby.client-client-String |
      | Company01                                       | Client01                                      |
      | Company01                                       | Client02                                      |
      | Company01                                       | Client05                                      |
      | Company01                                       | Client06                                      |
      | Company02                                       | Client01                                      |
      | Company02                                       | Client02                                      |
      | Company02                                       | Client07                                      |
      | Company02                                       | Client08                                      |
      | Company03                                       | Client03                                      |
      | Company03                                       | Client04                                      |
      | Company03                                       | Client07                                      |
      | Company03                                       | Client08                                      |
      | Company04                                       | Client03                                      |
      | Company04                                       | Client04                                      |
      | Company04                                       | Client09                                      |
      | Company04                                       | Client10                                      |
      | Company05                                       | Client05                                      |
      | Company05                                       | Client06                                      |
      | Company05                                       | Client09                                      |
      | Company05                                       | Client10                                      |

  Scenario: SELECT company, client FROM catalogTest.usersorderby ORDER BY company DESC, client ASC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company ,catalogTest.usersorderby.client FROM catalogTest.usersorderby ORDER BY company DESC, client ASC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String | catalogTest.usersorderby.client-client-String |
      | Company05                                       | Client05                                      |
      | Company05                                       | Client06                                      |
      | Company05                                       | Client09                                      |
      | Company05                                       | Client10                                      |
      | Company04                                       | Client03                                      |
      | Company04                                       | Client04                                      |
      | Company04                                       | Client09                                      |
      | Company04                                       | Client10                                      |
      | Company03                                       | Client03                                      |
      | Company03                                       | Client04                                      |
      | Company03                                       | Client07                                      |
      | Company03                                       | Client08                                      |
      | Company02                                       | Client01                                      |
      | Company02                                       | Client02                                      |
      | Company02                                       | Client07                                      |
      | Company02                                       | Client08                                      |
      | Company01                                       | Client01                                      |
      | Company01                                       | Client02                                      |
      | Company01                                       | Client05                                      |
      | Company01                                       | Client06                                      |

  Scenario: SELECT company, client FROM catalogTest.usersorderby ORDER BY company ASC, client DESC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company, catalogTest.usersorderby.client FROM catalogTest.usersorderby ORDER BY company ASC, client DESC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String | catalogTest.usersorderby.client-client-String |
      | Company01                                       | Client06                                      |
      | Company01                                       | Client05                                      |
      | Company01                                       | Client02                                      |
      | Company01                                       | Client01                                      |
      | Company02                                       | Client08                                      |
      | Company02                                       | Client07                                      |
      | Company02                                       | Client02                                      |
      | Company02                                       | Client01                                      |
      | Company03                                       | Client08                                      |
      | Company03                                       | Client07                                      |
      | Company03                                       | Client04                                      |
      | Company03                                       | Client03                                      |
      | Company04                                       | Client10                                      |
      | Company04                                       | Client09                                      |
      | Company04                                       | Client04                                      |
      | Company04                                       | Client03                                      |
      | Company05                                       | Client10                                      |
      | Company05                                       | Client09                                      |
      | Company05                                       | Client06                                      |
      | Company05                                       | Client05                                      |

  Scenario: SELECT company, client FROM catalogTest.usersorderby ORDER BY company DESC, client DESC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company,catalogTest.usersorderby.client FROM catalogTest.usersorderby ORDER BY company DESC, client DESC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String | catalogTest.usersorderby.client-client-String |
      | Company05                                       | Client10                                      |
      | Company05                                       | Client09                                      |
      | Company05                                       | Client06                                      |
      | Company05                                       | Client05                                      |
      | Company04                                       | Client10                                      |
      | Company04                                       | Client09                                      |
      | Company04                                       | Client04                                      |
      | Company04                                       | Client03                                      |
      | Company03                                       | Client08                                      |
      | Company03                                       | Client07                                      |
      | Company03                                       | Client04                                      |
      | Company03                                       | Client03                                      |
      | Company02                                       | Client08                                      |
      | Company02                                       | Client07                                      |
      | Company02                                       | Client02                                      |
      | Company02                                       | Client01                                      |
      | Company01                                       | Client06                                      |
      | Company01                                       | Client05                                      |
      | Company01                                       | Client02                                      |
      | Company01                                       | Client01                                      |

  Scenario: SELECT client, company FROM catalogTest.usersorderby ORDER BY client ASC, company ASC;
    When I execute a query: 'SELECT catalogTest.usersorderby.client, catalogTest.usersorderby.company FROM catalogTest.usersorderby ORDER BY  client ASC, company ASC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.client-client-String | catalogTest.usersorderby.company-company-String |
      | Client01                                      | Company01                                       |
      | Client01                                      | Company02                                       |
      | Client02                                      | Company01                                       |
      | Client02                                      | Company02                                       |
      | Client03                                      | Company03                                       |
      | Client03                                      | Company04                                       |
      | Client04                                      | Company03                                       |
      | Client04                                      | Company04                                       |
      | Client05                                      | Company01                                       |
      | Client05                                      | Company05                                       |
      | Client06                                      | Company01                                       |
      | Client06                                      | Company05                                       |
      | Client07                                      | Company02                                       |
      | Client07                                      | Company03                                       |
      | Client08                                      | Company02                                       |
      | Client08                                      | Company03                                       |
      | Client09                                      | Company04                                       |
      | Client09                                      | Company05                                       |
      | Client10                                      | Company04                                       |
      | Client10                                      | Company05                                       |

  Scenario: SELECT company, client FROM catalogTest.usersorderby ORDER BY client ASC, company ASC;
    When I execute a query: 'SELECT catalogTest.usersorderby.company, catalogTest.usersorderby.client FROM catalogTest.usersorderby ORDER BY client ASC, company ASC;'
    Then the result has to be exactly:
      | catalogTest.usersorderby.company-company-String | catalogTest.usersorderby.client-client-String |
      | Company01                                       | Client01                                      |
      | Company02                                       | Client01                                      |
      | Company01                                       | Client02                                      |
      | Company02                                       | Client02                                      |
      | Company03                                       | Client03                                      |
      | Company04                                       | Client03                                      |
      | Company03                                       | Client04                                      |
      | Company04                                       | Client04                                      |
      | Company01                                       | Client05                                      |
      | Company05                                       | Client05                                      |
      | Company01                                       | Client06                                      |
      | Company05                                       | Client06                                      |
      | Company02                                       | Client07                                      |
      | Company03                                       | Client07                                      |
      | Company02                                       | Client08                                      |
      | Company03                                       | Client08                                      |
      | Company04                                       | Client09                                      |
      | Company05                                       | Client09                                      |
      | Company04                                       | Client10                                      |
      | Company05                                       | Client10                                      |

  Scenario: SELECT id FROM catalogTest.tab1 ORDER BY id ASC;
    When I execute a query: 'SELECT catalogTest.tab1.id FROM catalogTest.tab1 ORDER BY id ASC;'
    Then the result has to be exactly:
      | catalogTest.tab1.id-id-Integer |
      | -4                             |
      | 1                              |
      | 2                              |
      | 3                              |

  Scenario: SELECT id FROM catalogTest.tab1 ORDER BY id DESC;
    When I execute a query: 'SELECT catalogTest.tab1.id FROM catalogTest.tab1 ORDER BY id DESC;'
    Then the result has to be exactly:
      | catalogTest.tab1.id-id-Integer |
      | 3                              |
      | 2                              |
      | 1                              |
      | -4                             |

  Scenario: SELECT id FROM catalogTest.tab3 ORDER BY id ASC;
    When I execute a query: 'SELECT catalogTest.tab3.id FROM catalogTest.tab3 ORDER BY id ASC;'
    Then the result has to be exactly:
      | catalogTest.tab3.id-id-BigInteger |
      | -4                                |
      | 1                                 |
      | 2                                 |
      | 3                                 |

  Scenario: SELECT id FROM catalogTest.tab3 ORDER BY id DESC;
    When I execute a query: 'SELECT catalogTest.tab3.id FROM catalogTest.tab3 ORDER BY id DESC;'
    Then the result has to be exactly:
      | catalogTest.tab3.id-id-BigInteger |
      | 3                                 |
      | 2                                 |
      | 1                                 |
      | -4                                |

  Scenario: SELECT money FROM catalogTest.tab4 ORDER BY id ASC;
    When I execute a query: 'SELECT catalogTest.tab4.money FROM catalogTest.tab4 ORDER BY money ASC;'
    Then the result has to be exactly:
      | catalogTest.tab4.money-money-Double |
      | -4.4                                |
      | 1.1                                 |
      | 2.2                                 |
      | 3.3                                 |

  Scenario: SELECT money FROM catalogTest.tab4 ORDER BY money DESC;
    When I execute a query: 'SELECT catalogTest.tab4.money FROM catalogTest.tab4 ORDER BY money DESC;'
    Then the result has to be exactly:
      | catalogTest.tab4.money-money-Double |
      | 3.3                                 |
      | 2.2                                 |
      | 1.1                                 |
      | -4.4                                |

  Scenario: SELECT money FROM catalogTest.tab5 ORDER BY money ASC;
    When I execute a query: 'SELECT catalogTest.tab5.money FROM catalogTest.tab5 ORDER BY money ASC;'
    Then the result has to be exactly:
      | catalogTest.tab5.money-money-Float |
      | -4.4                               |
      | 1.1                                |
      | 2.2                                |
      | 3.3                                |

  Scenario: SELECT money FROM catalogTest.tab5 ORDER BY money DESC;
    When I execute a query: 'SELECT catalogTest.tab5.money FROM catalogTest.tab5 ORDER BY money DESC;'
    Then the result has to be exactly:
      | catalogTest.tab5.money-money-Float |
      | 3.3                                |
      | 2.2                                |
      | 1.1                                |
      | -4.4                               |

  Scenario: SELECT married FROM catalogTest.tab6 ORDER BY married ASC;
    When I execute a query: 'SELECT catalogTest.tab6.married FROM catalogTest.tab6 ORDER BY married ASC;'
    Then the result has to be exactly:
      | catalogTest.tab6.married-married-Boolean |
      | false                                     |
      | true                                    |

  Scenario: SELECT married FROM catalogTest.tab6 ORDER BY married DESC;
    When I execute a query: 'SELECT catalogTest.tab6.married FROM catalogTest.tab6 ORDER BY married DESC;'
    Then the result has to be exactly:
      | catalogTest.tab6.married-married-Boolean |
      | true                                    |
      | false                                     |
