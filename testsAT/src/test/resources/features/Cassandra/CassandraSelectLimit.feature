Feature: CassandraSelectLimit

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tabletest LIMIT 11;
    When I execute 'SELECT * FROM tabletest LIMIT 11'
    Then The result has to have '10' rows ignoring the order:
      |ident-integer|date-timestamp     |money-double |name-string| new-boolean |
      |    5        |2005-05-05 00:00:00| 15.2        |name_5     |true         |
      |    1        |2001-01-01 00:00:00| 11.2        |name_1     |true         |
      |    8        |2008-08-08 00:00:00| 18.2        |name_8     |true         |
      |    0        |1999-11-30 00:00:00| 10.2        |name_0     |true         |
      |    2        |2002-02-02 00:00:00| 12.2        |name_2     |true         |
      |    4        |2004-04-04 00:00:00| 14.2        |name_4     |true         |
      |    7        |2007-07-07 00:00:00| 17.2        |name_7     |true         |
      |    6        |2006-06-06 00:00:00| 16.2        |name_6     |true         |
      |    9        |2009-09-09 00:00:00| 19.2        |name_9     |true         |
      |    3        |2003-03-03 00:00:00| 13.2        |name_3     |true         |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tabletest LIMIT 0;
    When I execute 'SELECT * FROM tabletest LIMIT 0'
    Then The result has to have '0' rows ignoring the order:
      |ident-integer|date-timestamp     |money-double |name-string| new-boolean |

  Scenario: [CASSANDRA NATIVE] SELECT * FROM tabletest LIMIT 1;
    When I execute 'SELECT * FROM tabletest LIMIT 1'
    Then The result has to have '1' rows ignoring the order:
      |ident-integer|date-timestamp     |money-double |name-string| new-boolean |
      |    5        |2005-05-05 00:00:00| 15.2        |name_5     |true         |
