Feature: PostgreSQL In Filter

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_1 IN (0,1)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_1 IN (0,1)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_3 IN (-9223372036854775808, 9223372036854775807)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_3 IN (-9223372036854775808, 9223372036854775807)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_4 IN (10.01, 100.001)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_4 IN (10.01, 100.001)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_5 IN (10.01, 100.001)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_5 IN (10.01, 100.001)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |


  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_6 IN (20.0, 200.0)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_6 IN (20.0, 200.0)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_7 IN (2.0E9, 2.0E10)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_7 IN (2.0E9, 2.0E10)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_8 IN (10000, 20000)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_8 IN (10000, 20000)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_9 IN (10000, 100000)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_9 IN (10000, 100000)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |


  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_10 IN (10000, 100000)
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_10 IN (10000, 100000)'
    Then The result has to have '2' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |

  Scenario: [POSTGRESQL NATIVE-CHARACTER TYPES] Select * FROM databasetest.crossdatacharacter where col_1 IN ('example_1', 'example_2')
    When I execute 'SELECT * FROM databasetest.crossdatacharacter where col_1 IN ('example_1', 'example_2')'
    Then The result has to have '2' rows ignoring the order:
      |col_1-string   |col_2-string   |col_3-string   |col_4-string   |col_5-string   |
      |example_1|example_1|example_1|example_1 |example_1|
      |example_2|example_2|example_2|example_2 |example_2|

  Scenario:[POSTGRESQL NATIVE-BOOLEAN TYPES] Select * FROM databasetest.crossdataboolean where col_1 IN (true, false)
    When I execute 'SELECT * FROM databasetest.crossdataboolean'
    Then The result has to have '14' rows ignoring the order:
      |col_1-boolean  |
      | true          |
      | true          |
      | true          |
      | true          |
      | true          |
      | true          |
      | true          |
      | false         |
      | false         |
      | false         |
      | false         |
      | false         |
      | false         |
      | false         |

  Scenario:[POSTGRESQL NATIVE-CROSSDATA TYPES] Select * FROM databasetest.crossdatauuid where col_1 IN ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')
    When I execute 'SELECT * FROM databasetest.crossdatauuid where col_1 IN ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')'
    Then The result has to have '5' rows ignoring the order:
      |col_1-string                        |
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|
      |a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11|

  Scenario:[POSTGRESQL NATIVE-TIMESTAMP] SELECT * FROM databasetest.crossdatatimestamp where col_1 IN ('2016-12-15 15:12:32.459957', '2016-12-15 15:12:32.459958')
    When I execute 'SELECT * FROM databasetest.crossdatatimestamp where col_1 IN ('2016-12-15 15:12:32.459957', '2016-12-15 15:12:32.459958')'
    Then The result has to have '2' rows:
      | col_1-timestamp | col_2-timestamp |
      |2016-12-15 15:12:32.459957|2016-12-16 00:12:32.459957|
      |2016-12-15 15:12:32.459958 |2016-12-16 00:12:32.459958 |

  Scenario:[POSTGRESQL NATIVE-DATE]Select * FROM databasetest.crossdatadate where col_1 IN ('1999-01-11','1999-01-12')
    When I execute 'SELECT * FROM databasetest.crossdatadate where col_1 IN ('1999-01-11','1999-01-12')'
    Then The result has to have '2' rows:
      |col_1-date |
      |1999-01-11|
      |1999-01-12|
