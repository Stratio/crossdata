Feature: [CROSSDATA-181]
  Scenario: First example
    When I describe table 'tablesubfield'
    Then The table has to have '2' columns
      |ident        | integer |
      |persona.name | string  |