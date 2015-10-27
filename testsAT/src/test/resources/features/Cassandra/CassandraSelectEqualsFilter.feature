Feature: CassandraSelectEqualsFilter

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tabletest WHERE ident = 0;
    When I execute 'SELECT * FROM tabletest WHERE ident = 0'
    Then The result has to have '1' rows:
      |ident-integer|date-timestamp     |money-double |name-string| new-boolean |
      |    0        |1999-11-30 00:00:00| 10.2        |name_0     |true         |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tabletest WHERE ident = 10;
    When I execute 'SELECT * FROM tabletest WHERE ident = 10'
    Then The result has to have '0' rows:
      |ident-integer|date-timestamp     |money-double |name-string| new-boolean |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tab1 WHERE name = 'name_0';
    When I execute 'SELECT * FROM tab1 WHERE name = 'name_0''
    Then The result has to have '1' rows:
     |name-string|
     |name_0     |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tab1 WHERE name = 'name_10';
    When I execute 'SELECT * FROM tab1 WHERE name = 'name_10''
    Then The result has to have '0' rows:
      |name-string|

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tab2 WHERE money = 10.2;
    When I execute 'SELECT * FROM tab2 WHERE money = 10.2'
    Then The result has to have '1' rows:
      |money-double|
      |10.2     |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tab2 WHERE money = 11.3;
    When I execute 'SELECT * FROM tab2 WHERE money = 11.3'
    Then The result has to have '0' rows:
      |money-double|

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tab3 WHERE new = true;
    When I execute 'SELECT * FROM tab3 WHERE new = true'
    Then The result has to have '1' rows:
      |new-boolean|
      |true     |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tab3 WHERE new = false;
    When I execute 'SELECT * FROM tab3 WHERE new = false'
    Then The result has to have '1' rows:
      |new-boolean|
      |false      |

  Scenario: [CROSSDATA-81 : CASSANDRA NATIVE] SELECT * FROM tab4 WHERE date = '1999-11-30 00:00:00';
    When I execute 'SELECT * FROM tab4 WHERE date = '1999-11-30 00:00:00''
    Then The result has to have '1' rows:
      |date-timestamp|
      |1999-11-30 00:00:00    |

  Scenario: [CROSSDATA-81 : CASSANDRA NATIVE] SELECT * FROM tab4 WHERE date = '1999-12-30 00:00:00';
    When I execute 'SELECT * FROM tab4 WHERE date = '1999-12-30 00:00:00''
    Then The result has to have '0' rows:
      |date-timestamp|
