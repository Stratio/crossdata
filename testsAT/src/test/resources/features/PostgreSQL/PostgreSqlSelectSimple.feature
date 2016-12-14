Feature: PostgreSQL Simple Select

    Scenario: [POSTGRESQL NATIVE-NUMERIC TYPES] Select * FROM databasetest.crossdatanumbers
        When I execute 'SELECT * FROM databasetest.crossdatanumbers'
        Then The result has to have '10' rows ignoring the order:
            |col_1-integer  |col_2-integer  |col_3-long             |col_4-decimal(38,18)   | col_5-decimal(38,18)  |col_6-double  |col_7-double       |col_8-integer  |col_9-integer  | col_10-long        |
            |-32768         |-2147483648    |-9223372036854775808   |-1000.0001             |-1000.0001             |20000.00      |2.00000000000000   |1              |1              |1                      |
            |-100           |-100           |-100                   |-100.001               |-100.001               |20000.00      |2000000.00000000   |10             |10             |10                     |
            |-10            |-10            |-10                    |-10.01                 |-10.01                 |20000.00      |20000000.0000000   |100            |100            |100                    |
            |-1             |-1             |-1                     |-1.1                   |-1.1                   |2.000000      |200000000.000000   |1000           |1000           |1000                   |
            |0              |0              |0                      |0.0                    |0.0                    |20.00000      |2000000000.00000   |10000          |10000          |10000                  |
            |1              |1              |1                      |1.1                    |1.1                    |200.0000      |20000000000.0000   |20000          |100000         |100000                 |
            |10             |10             |10                     |10.01                  |10.01                  |2000.000      |200000000000.000   |25000          |1000000        |1000000                |
            |100            |100            |100                    |100.001                |100.001                |20000.00      |2000000000000.00   |26000          |10000000       |10000000               |
            |1000           |1000           |1000                   |1000.0001              |1000.0001              |200000.0      |20000000000000.0   |30000          |100000000      |100000000              |
            |32767          |2147483647     |9223372036854775807    |10000.00001            |10000.00001            |2000000       |200000000000000    |32767          |2147483647     |9223372036854775807    |


    Scenario: [POSTGRESQL NATIVE-MONETARY TYPES] Select * FROM databasetest.crossdatamonetary
        When I execute 'SELECT * FROM databasetest.crossdatamonetary'
        Then The result has to have '3' rows ignoring the order:
            |col_1-double   |
            | -10.08        |
            | 0.00          |
            | 10.08         |

    Scenario: [POSTGRESQL NATIVE-CHARACTER TYPES] Select * FROM databasetest.crossdatacharacter
        When I execute 'SELECT * FROM databasetest.crossdatacharacter'
        Then The result has to have '10' rows ignoring the order:
            |col_1-string   |col_2-string   |col_3-string   |col_4-string   |col_5-string   |
            |example_1|example_1|example_1|example_1 |example_1|
            |example_2|example_2|example_2|example_2 |example_2|
            |example_3|example_3|example_3|example_3 |example_3|
            |example_4|example_4|example_4|example_4 |example_4|
            |example_5|example_5|example_5|example_5 |example_5|
            |example_6|example_6|example_6|example_6 |example_6|
            |example_7|example_7|example_7|example_7 |example_7|
            |example_8|example_8|example_8|example_8 |example_8|
            |example_9|example_9|example_9|example_9 |example_9|
            |example_10|example_10|example_10|example_10|example_10|

    Scenario:
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