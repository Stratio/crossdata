/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.core.validator.statements;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.AlterKeyspaceStatement;
import com.stratio.meta.core.structures.IdentifierProperty;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.validator.BasicValidatorTest;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class AlterKeyspaceStatementTest extends BasicValidatorTest {

  @Test
  public void testValidateAlterKeyspace() {

    // "ALTER KEYSPACE demo WITH replication = {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '2'};"

    Map<String, ValueProperty> properties = new HashMap<>();
    properties.put("replication", new IdentifierProperty(
        "{'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '2'}"));

    AlterKeyspaceStatement aks = new AlterKeyspaceStatement("demo", properties);
    Result result = aks.validate(metadataManager, null);

    assertNotNull(result, "Sentence validation not supported");
    assertFalse(result.hasError(), "Cannot validate sentence");
  }


  @Test
  public void testValidateAlterKeyspaceWith2properties() {

    // "ALTER KEYSPACE demo WITH replication = {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '2'} AND durable_writes = True;"

    Map<String, ValueProperty> properties = new HashMap<>();
    properties.put("replication", new IdentifierProperty(
        "{'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '2'}"));
    properties.put("durable_writes", new IdentifierProperty("True"));

    AlterKeyspaceStatement aks = new AlterKeyspaceStatement("demo", properties);
    Result result = aks.validate(metadataManager, null);

    assertNotNull(result, "Sentence validation not supported");
    assertFalse(result.hasError(), "Cannot validate sentence");
  }

  @Test
  public void testValidateAlterKeyspaceWithWrongKeyspace() {
    String
        inputText =
        "ALTER KEYSPACE unknown WITH replication = {'class': 'org.apache.cassandra.locator.NetworkTopologyStrategy'};";
    validateFail(inputText, "testValidateAlterKeyspaceWithWrongProperty");
  }

  @Test
  public void testValidateAlterKeyspaceWithWrongProperty() {
    String inputText = "ALTER KEYSPACE demo WITH comment = 'demo keyspace';";
    validateFail(inputText, "testValidateAlterKeyspaceWithWrongProperty");
  }
}
