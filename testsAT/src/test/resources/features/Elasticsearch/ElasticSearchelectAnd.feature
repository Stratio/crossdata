Feature: ElasticSearch Select And
  Scenario: [CROSSDATA-18 : ES NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_0';
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_0''
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-timestamp  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 00:00:00|

  Scenario: [CROSSDATA-18 : ES NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_8';
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_8''
    Then The result has to have '0' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-timestamp  |

  Scenario: [CROSSDATA-18 : ES NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2;
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2'
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-timestamp  |
      |    0          | name_0        | 10.2          |  true       | 1999-11-30 00:00:00|

  Scenario: [CROSSDATA-18 : ES NATIVE] SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND money = 10.2;
    When I execute 'SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND money = 10.2'
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-timestamp  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 00:00:00|

  Scenario: [CROSSDATA-18 : ES NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2 AND new = true;
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2 AND new = true'
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-timestamp  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 00:00:00|

  Scenario: [CROSSDATA-18 : ES NATIVE] SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true);
    When I execute 'SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true)'
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-timestamp  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 00:00:00|

  Scenario: [ES NATIVE] SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true);
    When I execute 'SELECT date FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true)'
    Then The result has to have '1' rows:
     | date-timestamp  |
     | 1999-11-30 00:00:00|