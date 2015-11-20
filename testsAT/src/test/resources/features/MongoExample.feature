    Feature: MongoExample
      Scenario: First example
        When I execute 'SELECT ident AS hugo, name, money  FROM tabletest LIMIT 2'
        Then The result has to have '10' rows:
          | hugo-integer | name-string   | money-double  |
          |    0          |name_0         | 10.2          |
          |    1          |name_1         | 11.2          |
          |    2          |name_2         | 12.2          |
          |    3          |name_3         | 13.2          |
          |    4          |name_4         | 14.2          |
          |    5          |name_5         | 15.2          |
          |    6          |name_6         | 16.2          |
          |    7          |name_7         | 17.2          |
          |    8          |name_8         | 18.2          |
          |    9          |name_9         | 19.2          |

      Scenario: Second example
        When I execute 'SELECT ident, name, money  FROM tabletest WHERE ident = 0'
        Then The result has to have '1' rows:
          | ident-integer | name-string   | money-double  |
          |    0          |name_0         | 10.2          |


