Feature: [CROSSDATA-181]
  Scenario: Describe  table with a subfield
    When I describe table 'tablesubfield'
    Then The table has to have '2' columns
      |ident        | integer |
      |persona.name | string  |

  Scenario: Describe  table with a subfield and array
    When I describe table 'composetable'
    Then The table has to have '4' columns
      |ident        | integer |
      |person.name   | string  |
      |perso.daughter.name   | string  |
      |perso.daughter.age   | integer  |
