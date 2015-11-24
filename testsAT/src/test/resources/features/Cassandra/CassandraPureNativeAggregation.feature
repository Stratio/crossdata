Feature:[CROSSDATA-34]Cassandra Pure Native Aggregation
  Scenario: [CROSSDATA-36] SELECT count(*) FROM tabletest;
    When I execute 'SELECT count(*) FROM tabletest'
    Then The result has to have '1' rows
      |_c0-long|
      | 10     |

  Scenario: [CROSSDATA-36] SELECT count(*) AS count FROM tabletest;
    When I execute 'SELECT count(*) AS count  FROM tabletest'
    Then The result has to have '1' rows
      |count-long|
      | 10     |

