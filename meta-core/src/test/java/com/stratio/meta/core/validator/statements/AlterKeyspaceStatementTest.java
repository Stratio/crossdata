/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
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