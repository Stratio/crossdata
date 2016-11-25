Feature: CassandraSelectUDFS
  Scenario: [CASSANDRA NATIVE, CROSSDATA-31] SELECT ident, now() FROM tabletest;
    When I execute 'SELECT ident, now() FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer| _c1-timestamp     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31]SELECT ident, now() AS actual FROM tabletest;
    When I execute 'SELECT ident, now() AS actual FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|actual-timestamp     |
    
  Scenario: [CASSANDRA NATIVE, CROSSDATA-31, CROSSDATA-89] SELECT ident, cassandra_now() FROM tabletest;
    When I execute 'SELECT ident, cassandra_now() FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer| _c1-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31, CROSSDATA-89]SELECT ident, cassandra_now() AS actual FROM tabletest;
    When I execute 'SELECT ident, cassandra_now() AS actual FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|actual-string     |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31, CROSSDATA-89] SELECT ident, cassandra_dateOf(cassandra_now()) AS now FROM tabletest
    When I execute 'SELECT ident, cassandra_dateOf(cassandra_now()) AS now FROM tabletest'
    Then The result has to have '10' rows
      |ident-integer|now-timestamp    |

  Scenario: [CASSANDRA NATIVE, CROSSDATA-31 , CROSSDATA-89] SELECT cassandra_dateOf(cassandra_now()) as t FROM tabletest
    When I execute 'SELECT cassandra_dateOf(cassandra_now()) as t FROM tabletest'
    Then The result has to have '10' rows
      |t-timestamp|