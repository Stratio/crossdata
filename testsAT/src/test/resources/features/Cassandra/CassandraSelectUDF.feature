Feature: CassandraSelectUDFS
  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT ident, now() FROM tabletest;
    When I execute 'SELECT ident, now() FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer| _c1-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31]SELECT ident, now() AS actual FROM tabletest;
    When I execute 'SELECT ident, now() AS actual FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|actual-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT ident, dateOf(now()) AS now FROM tabletest
    When I execute 'SELECT ident, dateOf(now()) AS now FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|now-timestamp    |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT ident, unixTimestampOf(now()) AS now FROM tabletest
    When I execute 'SELECT ident, unixTimestampOf(now()) AS now FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|now-long     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT dateOf(now()) as t FROM tabletest
    When I execute 'SELECT dateOf(now()) as t FROM tabletest'
    Then The result has to have '10' rows
      |t-timestamp|