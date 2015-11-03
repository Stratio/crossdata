Feature: Datastore operations feature

  Scenario: Droping a datastore that not exists
    When I execute a DROP_DATASTORE query of the datastore 'Cassandra'
    Then an exception 'IS' thrown
    Then The result of drop a datastore is 'true'