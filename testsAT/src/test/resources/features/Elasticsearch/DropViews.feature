Feature: DROP VIEWS

  Scenario: [CROSSDATA-330] DROP VIEW dropViewTest;
    When I execute 'CREATE VIEW dropViewTest AS SELECT * FROM tabletest'
    Then I execute 'SELECT * FROM dropViewTest'
    When I execute 'DROP VIEW dropViewTest'
    Then an exception 'IS NOT' thrown
    When I execute 'SELECT * FROM dropViewTest'
    Then an exception 'IS' thrown

  Scenario: [CROSSDATA-330, CROSSDATA-379] DROP VIEW dropViewNotExists(not exists)
    When I execute 'DROP VIEW dropViewNotExists'
    Then an exception 'IS' thrown
