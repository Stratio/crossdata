@ignore @unimplemented
Feature: Test crossdata shell attach/add operations

  # ATTACH CLUSTER
  Scenario: Use empty cluster name
    Given I run the shell command "ATTACH CLUSTER ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': 9042};"
    Then I expect a 'Parser exception' message

  Scenario: Use non-existing cluster name
    Given I run the shell command "ATTACH CLUSTER %hh ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': 9042};"
    Then I expect a 'Parser exception' message

  Scenario: Use empty datastore name
    Given I run the shell command "ATTACH CLUSTER exampleCluster ON DATASTORE WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': 9042};"
    Then I expect a 'Parser exception' message

  Scenario: Use non-existing datastore name
    Given I run the shell command "ATTACH CLUSTER exampleCluster ON DATASTORE exampleDatastore WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': 9042};"
    Then I expect a '[datastore.exampleDatastore]  doesn't exist yet' message

  Scenario: Use empty options
    Given I run the shell command "ATTACH CLUSTER exampleCluster ON DATASTORE elasticsearch WITH OPTIONS {};"
    Then I expect a 'Some required properties are missing' message

  Scenario: Use invalid options
    Given I run the shell command "ATTACH CLUSTER exampleCluster ON DATASTORE elasticsearch WITH OPTIONS {'myOption': 'myValue'};"
    Then I expect a 'Some required properties are missing' message

  Scenario: Attach valid cluster
    Given I run the shell command "ATTACH CLUSTER esTestCluster ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[172.31.13.55,172.31.2.182,172.31.5.193]', 'Native Ports': '[9300,9300,9300]', 'Restful Ports':'[9200,9200,9200]', 'Cluster Name':'Stratio ElasticSearch'};"
    Then I expect a 'Cluster attached successfully' message

  Scenario: Attach same cluster on same datastore
    Given I run the shell command "ATTACH CLUSTER esTestCluster ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[172.31.13.55,172.31.2.182,172.31.5.193]', 'Native Ports': '[9300,9300,9300]', 'Restful Ports':'[9200,9200,9200]', 'Cluster Name':'Stratio ElasticSearch'};"
    Then I expect a '[cluster.testCluster] exists already' message

  # ATTACH CONNECTOR
  Scenario: Use empty connector
    Given I run the shell command "ATTACH CONNECTOR TO testCluster WITH OPTIONS {};"
    Then I expect a 'Parser exception' message

  Scenario: Use non-existing connector
    Given I run the shell command "ATTACH CONNECTOR invalidConnector TO testCluster WITH OPTIONS {};"
    Then I expect a '[connector.invalidConnector]  doesn't exist yet' message

  Scenario: Use empty cluster
    Given I run the shell command "ATTACH CONNECTOR elasticsearchconnector TO WITH OPTIONS {};"
    Then I expect a 'Parser exception' message

  Scenario: Use non-existing cluster
    Given I run the shell command "ATTACH CONNECTOR elasticsearchconnector TO invalidCluster WITH OPTIONS {};"
    Then I expect a '[cluster.invalidCluster]  doesn't exist yet' message

  Scenario: Attach valid connector
    Given I run the shell command "ATTACH CONNECTOR elasticsearchconnector TO testCluster WITH OPTIONS {};"
    Then I expect a 'Connected to cluster successfully' message

  Scenario: Attach same valid connector
    Given I run the shell command "ATTACH CONNECTOR elasticsearchconnector TO testCluster WITH OPTIONS {};"
    Then I expect a 'ERROR: Couldn't connect to cluster: The connection [testCluster] already exists.' message

  Scenario: [CROSSDATA-107] Detach cluster before detaching connector causes corruption in infinispan
    When I run the shell command "ATTACH CLUSTER esTestCluster ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[172.31.13.55,172.31.2.182,172.31.5.193]', 'Native Ports': '[9300,9300,9300]', 'Restful Ports':'[9200,9200,9200]', 'Cluster Name':'Stratio ElasticSearch'};"
    Then I expect a 'Cluster attached successfully' message
    When  I run the shell command "ATTACH CONNECTOR elasticsearchconnector TO esCluster;"
    Then I expect a 'Connected to cluster successfully' message
    When I run the shell command 'DETACH CLUSTER esCluster;'
    Then I expect a 'Cluster cannot be detached while it has a connector attached';