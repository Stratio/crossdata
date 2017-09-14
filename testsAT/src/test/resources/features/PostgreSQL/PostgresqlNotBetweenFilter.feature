Feature: PostgreSQL Not Between Filter

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_1 not between -32768 and 0
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_1 not between -32768 and 0'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_2 not between -2147483648 and 0
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_2 not between -2147483648 and 0'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_3 not between -9223372036854775808 and 0
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_3 not between -9223372036854775808 and 0'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_4 not between -1000.0001 and 0E-18
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_4 not between -1000.0001 and 0E-18'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_5 not between -1000.0001 and 0E-18
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_5 not between -1000.0001 and 0E-18'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_6 not between 20.0 and 200.0
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_6 not between 20.0 and 200.0'
    Then The result has to have '8' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_7 not between 2.0 and 2.0E9
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_7 not between 2.0 and 2.0E9'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_7 not between 2.0 and 2.0E9
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_7 not between 2.0 and 2.0E9'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_8 not between 1 and 10000
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_8 not between 1 and 10000'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_9 not between 1 and 10000
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_9 not between 1 and 10000'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers where col_10 not between 1 and 10000
    When I execute 'SELECT * FROM databasetest.crossdatanumbers where col_10 not between 1 and 10000'
    Then The result has to have '5' rows ignoring the order:
      |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)       | col_5-decimal(38,18)      |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long           |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |
      |32767 |2147483647 |9223372036854775807 |10000.000010000000000000|10000.000010000000000000|2000000.0|2.0E14   |32767|2147483647|9223372036854775807|

  Scenario: [POSTGRESQL NATIVE-CHARACTER TYPES] Select * FROM databasetest.crossdatacharacter where col_1 not between 'example_1' and 'example_5'
    When I execute 'SELECT * FROM databasetest.crossdatacharacter where col_1 not between 'example_1' and 'example_5''
    Then The result has to have '4' rows ignoring the order:
      |col_1-string   |col_2-string   |col_3-string   |col_4-string   |col_5-string   |
      |example_6|example_6|example_6|example_6 |example_6|
      |example_7|example_7|example_7|example_7 |example_7|
      |example_8|example_8|example_8|example_8 |example_8|
      |example_9|example_9|example_9|example_9 |example_9|

  Scenario:[POSTGRESQL NATIVE-TIMESTAMP]Select * FROM databasetest.crossdatatimestamp where col_1 not between '2016-12-15 15:12:32.459958' and '2016-12-15 15:12:32.45996'
    When I execute 'SELECT * FROM databasetest.crossdatatimestamp where col_1 not between '2016-12-15 15:12:32.459958' and '2016-12-15 15:12:32.45996''
    Then The result has to have '1' rows:
      | col_1-timestamp | col_2-timestamp |
      |2016-12-15 15:12:32.459957|2016-12-16 00:12:32.459957|

  Scenario:[POSTGRESQL NATIVE-DATE]Select * FROM databasetest.crossdatadate where col_1 not between '1999-01-09' and '1999-01-09'
    When I execute 'SELECT * FROM databasetest.crossdatadate where col_1 not between '1999-01-09' and '1999-01-12''
    Then The result has to have '15' rows:
      |col_1-date |
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-18|
      |2003-01-02|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-19|