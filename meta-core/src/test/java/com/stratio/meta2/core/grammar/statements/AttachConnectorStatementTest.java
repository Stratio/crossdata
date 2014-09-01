/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.grammar.statements;

import com.stratio.meta.core.grammar.ParsingTest;

import org.testng.annotations.Test;

public class AttachConnectorStatementTest extends ParsingTest {

  @Test
  public void attachConnectorSimple1() {
    String inputText = "ATTACH CONNECTOR 'cass_con_native' TO \"cassandraCluster\" WITH OPTIONS {\"ConsistencyLevel\": \"Quorum\", DefaultLimit: 999};";
    String expectedText = "ATTACH CONNECTOR 'cass_con_native' TO \"cassandraCluster\" WITH OPTIONS {ConsistencyLevel: Quorum, DefaultLimit: 999};";
    testRegularStatement(inputText, expectedText, "attachConnectorSimple1");
  }

  @Test
  public void attachConnectorSimple2() {
    String inputText = "ATTACH CONNECTOR \"cass_con_native\" TO 'cassandraCluster' WITH OPTIONS {ConsistencyLevel: 'Quorum', \"DefaultLimit\": 999};";
    String expectedText = "ATTACH CONNECTOR \"cass_con_native\" TO 'cassandraCluster' WITH OPTIONS {ConsistencyLevel: Quorum, DefaultLimit: 999};";
    testRegularStatement(inputText, expectedText, "attachConnectorSimple2");
  }

  @Test
  public void attachConnectorWrongConnectorName() {
    String inputText = "ATTACH CONNECTOR ^cass_con_native TO 'cassandraCluster' WITH OPTIONS {'ConsistencyLevel': Quorum};";
    testParserFails(inputText, "attachConnectorWrongName");
  }

  @Test
  public void attachConnectorWrongClusterName() {
    String inputText = "ATTACH CONNECTOR cass_con_native TO :cassandraCluster WITH OPTIONS {DefaultLimit: 999};";
    testParserFails(inputText, "attachConnectorWrongDataStore");
  }

  @Test
  public void attachConnectorWrongJson() {
    String inputText = "ATTACH CONNECTOR productionMadrid ON DATASTORE 'cassandra' WITH OPTIONS {connector.path: /home/stratio/connector/cass_con_native.xml};";
    testParserFails(inputText, "attachConnectorWrongJson");
  }

}
