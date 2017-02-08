Feature: PostgreSQL Simple Select with limit

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers limit 2
    When I execute 'SELECT * FROM databasetest.crossdatanumbers limit 2'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |

  Scenario: [POSTGRESQL NATIVE-CHARACTER TYPES] Select * FROM databasetest.crossdatacharacter  limit 2
    When I execute 'SELECT * FROM databasetest.crossdatacharacter  limit 2'
    Then The result has to have '2' rows ignoring the order:
      |col_1-string   |col_2-string   |col_3-string   |col_4-string   |col_5-string   |
      |example_1|example_1|example_1|example_1 |example_1|
      |example_2|example_2|example_2|example_2 |example_2|

  Scenario:[POSTGRESQL NATIVE-BOOLEAN TYPES] Select * FROM databasetest.crossdataboolean limit 2
    When I execute 'SELECT * FROM databasetest.crossdataboolean limit 2'
    Then The result has to have '2' rows ignoring the order:
      |col_1-boolean  |
      | true          |
      | true          |

  Scenario:[POSTGRESQL NATIVE-CROSSDATA TYPES] Select * FROM databasetest.crossdatauuid limit 2
    When I execute 'SELECT * FROM databasetest.crossdatauuid limit 2'
    Then The result has to have '2' rows ignoring the order:
      |col_1-string                        |
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|


  Scenario:[POSTGRESQL NATIVE-ARRAYS TYPES]Select * FROM databasetest.crossdataarray limit 1
    When I execute 'SELECT col_1, col_2 FROM databasetest.crossdataarray limit 1'
    Then The result has to have '1' rows:
      | col_1-string | col_2-array<int> |
      | Bill  | 10000,10000,10000,10000 |

  Scenario:[POSTGRESQL NATIVE-TIMESTAMP]Select * FROM databasetest.crossdatatimestamp limit 2
    When I execute 'SELECT * FROM databasetest.crossdatatimestamp limit 2'
    Then The result has to have '2' rows:
      | col_1-timestamp | col_2-timestamp |
      |2016-12-15 15:12:32.459957|2016-12-16 00:12:32.459957|
      |2016-12-15 15:12:32.459958|2016-12-16 00:12:32.459958|

  Scenario:[POSTGRESQL NATIVE-DATE]Select * FROM databasetest.crossdatadate  limit 2
    When I execute 'SELECT * FROM databasetest.crossdatadate limit 2'
    Then The result has to have '2' rows:
      |col_1-date |
      |1999-01-08|
      |1999-01-08|

  Scenario:[POSTGRESQL NATIVE-TIME]Select * FROM databasetest.crossdatatime limit 2
    When I execute 'SELECT * FROM databasetest.crossdatatime limit 2'
    Then The result has to have '2' rows:
      | col_1-timestamp | col_2-timestamp |
      | 1970-01-01 04:05:06.789 | 1970-01-01 13:05:06.0 |
      | 1970-01-01 04:05:06.0  | 1970-01-01 09:05:06.0  |