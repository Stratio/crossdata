Feature: PostgreSQL Less Filter

  # NUMBERS
  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_1 < 32767;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_1 < 32767'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_2 < 2147483647;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_2 < 2147483647'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_3 < 9223372036854775807;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_3 < 9223372036854775807'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_4 < 10000.00001;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_4 < 10000.00001'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_5 < 10000.00001;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_5 < 10000.00001'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_6 < 2000000.00;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_6 < 2000000.00'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_7 < 2.0E14;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_7 < 2.0E14'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_8 < 32767;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_8 < 32767'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_9 < 2147483647;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_9 < 2147483647'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatanumbers WHERE col_10 < 9223372036854775807;
    When I execute 'SELECT * FROM databasetest.crossdatanumbers WHERE col_10 < 9223372036854775807'
    Then The result has to have '9' rows:
      | col_1-integer | col_2-integer     | col_3-long          | col_4-decimal(38,18)      | col_5-decimal(38,18)    | col_6-double  | col_7-double    | col_8-integer  | col_9-integer  | col_10-long   |
      |-32768|-2147483648|-9223372036854775808|-1000.000100000000000000|-1000.000100000000000000|20000.0  |2.0      |1    |1         |1                  |
      |-100  |-100       |-100                |-100.001000000000000000 |-100.001000000000000000 |20000.0  |2000000.0|10   |10        |10                 |
      |-10   |-10        |-10                 |-10.010000000000000000  |-10.010000000000000000  |20000.0  |2.0E7    |100  |100       |100                |
      |-1    |-1         |-1                  |-1.100000000000000000   |-1.100000000000000000   |20000.0  |2.0E8    |1000 |1000      |1000               |
      |0     |0          |0                   |0E-18                   |0E-18                   |20.0     |2.0E9    |10000|10000     |10000              |
      |1     |1          |1                   |1.100000000000000000    |1.100000000000000000    |200.0    |2.0E10   |20000|100000    |100000             |
      |10    |10         |10                  |10.010000000000000000   |10.010000000000000000   |2000.0   |2.0E11   |25000|1000000   |1000000            |
      |100   |100        |100                 |100.001000000000000000  |100.001000000000000000  |20000.0  |2.0E12   |26000|10000000  |10000000           |
      |1000  |1000       |1000                |1000.000100000000000000 |1000.000100000000000000 |200000.0 |2.0E13   |30000|100000000 |100000000          |

  # CHARACTERS
  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatacharacter WHERE col_1 < 'example_9';
    When I execute 'SELECT * FROM databasetest.crossdatacharacter WHERE col_1 < 'example_9''
    Then The result has to have '8' rows:
      | col_1-string     | col_2-string     | col_3-string     | col_4-string     | col_5-string     |
      |example_1|example_1|example_1|example_1|example_1|
      |example_2|example_2|example_2|example_2|example_2|
      |example_3|example_3|example_3|example_3|example_3|
      |example_4|example_4|example_4|example_4|example_4|
      |example_5|example_5|example_5|example_5|example_5|
      |example_6|example_6|example_6|example_6|example_6|
      |example_7|example_7|example_7|example_7|example_7|
      |example_8|example_8|example_8|example_8|example_8|

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatacharacter WHERE col_2 < 'example_9';
    When I execute 'SELECT * FROM databasetest.crossdatacharacter WHERE col_2 < 'example_9''
    Then The result has to have '8' rows:
      | col_1-string     | col_2-string     | col_3-string     | col_4-string     | col_5-string     |
      |example_1|example_1|example_1|example_1|example_1|
      |example_2|example_2|example_2|example_2|example_2|
      |example_3|example_3|example_3|example_3|example_3|
      |example_4|example_4|example_4|example_4|example_4|
      |example_5|example_5|example_5|example_5|example_5|
      |example_6|example_6|example_6|example_6|example_6|
      |example_7|example_7|example_7|example_7|example_7|
      |example_8|example_8|example_8|example_8|example_8|

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatacharacter WHERE col_3 < 'example_9';
    When I execute 'SELECT * FROM databasetest.crossdatacharacter WHERE col_3 < 'example_9''
    Then The result has to have '8' rows:
      | col_1-string     | col_2-string     | col_3-string     | col_4-string     | col_5-string     |
      |example_1|example_1|example_1|example_1|example_1|
      |example_2|example_2|example_2|example_2|example_2|
      |example_3|example_3|example_3|example_3|example_3|
      |example_4|example_4|example_4|example_4|example_4|
      |example_5|example_5|example_5|example_5|example_5|
      |example_6|example_6|example_6|example_6|example_6|
      |example_7|example_7|example_7|example_7|example_7|
      |example_8|example_8|example_8|example_8|example_8|

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatacharacter WHERE col_4 < 'example_9';
    When I execute 'SELECT * FROM databasetest.crossdatacharacter WHERE col_4 < 'example_9''
    Then The result has to have '8' rows:
      | col_1-string     | col_2-string     | col_3-string     | col_4-string     | col_5-string     |
      |example_1|example_1|example_1|example_1|example_1|
      |example_2|example_2|example_2|example_2|example_2|
      |example_3|example_3|example_3|example_3|example_3|
      |example_4|example_4|example_4|example_4|example_4|
      |example_5|example_5|example_5|example_5|example_5|
      |example_6|example_6|example_6|example_6|example_6|
      |example_7|example_7|example_7|example_7|example_7|
      |example_8|example_8|example_8|example_8|example_8|

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatacharacter WHERE col_5 < 'example_9';
    When I execute 'SELECT * FROM databasetest.crossdatacharacter WHERE col_5 < 'example_9''
    Then The result has to have '8' rows:
      | col_1-string     | col_2-string     | col_3-string     | col_4-string     | col_5-string     |
      |example_1|example_1|example_1|example_1|example_1|
      |example_2|example_2|example_2|example_2|example_2|
      |example_3|example_3|example_3|example_3|example_3|
      |example_4|example_4|example_4|example_4|example_4|
      |example_5|example_5|example_5|example_5|example_5|
      |example_6|example_6|example_6|example_6|example_6|
      |example_7|example_7|example_7|example_7|example_7|
      |example_8|example_8|example_8|example_8|example_8|

  #BOOLEAN
  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdataboolean WHERE col_1 < TRUE;
    When I execute 'SELECT * FROM databasetest.crossdataboolean WHERE col_1 < TRUE'
    Then The result has to have '7' rows:
      | col_1-boolean |
      |false|
      |false|
      |false|
      |false|
      |false|
      |false|
      |false|

    #UUID
  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatauuid WHERE col_1 < 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11';
    When I execute 'SELECT * FROM databasetest.crossdatauuid WHERE col_1 < 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11''
    Then The result has to have '0' rows:
      | col_1-string                       |


    #TIMESTAMP
  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatatimestamp where col_1 < '2016-12-15 15:12:32.45996';
    When I execute 'SELECT * FROM databasetest.crossdatatimestamp where col_1 < '2016-12-15 15:12:32.45996''
    Then The result has to have '3' rows:
      | col_1-timestamp          | col_2-timestamp          |
      |2016-12-15 15:12:32.459957|2016-12-16 00:12:32.459957|
      |2016-12-15 15:12:32.459958|2016-12-16 00:12:32.459958|
      |2016-12-15 15:12:32.459959|2016-12-16 00:12:32.459959|

  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatatimestamp where col_2 < '2016-12-16 00:12:32.45996';
    When I execute 'SELECT * FROM databasetest.crossdatatimestamp where col_2 < '2016-12-16 00:12:32.45996' '
    Then The result has to have '3' rows:
      | col_1-timestamp                     | col_2-timestamp                    |
      |2016-12-15 15:12:32.459957|2016-12-16 00:12:32.459957|
      |2016-12-15 15:12:32.459958|2016-12-16 00:12:32.459958|
      |2016-12-15 15:12:32.459959|2016-12-16 00:12:32.459959|

    #DATE
  Scenario: [CROSSDATA-841 : POSTGRESQL NATIVE] SELECT * FROM databasetest.crossdatadate where col_1 < '2003-01-02';
    When I execute 'SELECT * FROM databasetest.crossdatadate where col_1 < '2003-01-02''
    Then The result has to have '17' rows:
      | col_1-date     |
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-18|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-08|
      |1999-01-09|
      |1999-01-19|
      |1999-01-11|
      |1999-01-12|
