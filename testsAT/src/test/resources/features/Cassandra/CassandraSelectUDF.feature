Feature: CassandraSelectUDFS
  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT * FROM tabletest;
    When I execute 'SELECT ident, now() FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer| _c1-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT * FROM tabletest;
    When I execute 'SELECT ident, now() AS actual FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|actual-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT * FROM tabletest;
    When I execute 'SELECT ident, dateOf(now()) AS now FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|now-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT * FROM tabletest;
    When I execute 'SELECT ident, unixTimestampOf(now()) AS now FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|now-long     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT * FROM tabletest;
    When I execute 'SELECT toDate(now()) as t FROM sample_times'
    Then The result has to have '1' rows
      |toDate-string|
