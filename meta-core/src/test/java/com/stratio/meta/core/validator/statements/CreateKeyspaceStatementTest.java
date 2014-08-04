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
import com.stratio.meta.core.statements.CreateKeyspaceStatement;
import com.stratio.meta.core.structures.IdentifierProperty;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class CreateKeyspaceStatementTest extends BasicValidatorTest {

    @Test
    public void validateOk(){
        //TODO Migrate the tests to validateOk or validateFail. Take into account the mapping order.
        String name = "not_exists";
        boolean ifNotExists = false;
        Map<String, ValueProperty> properties = new HashMap<>();
        IdentifierProperty ip = new IdentifierProperty("{class: SimpleStrategy, replication_factor: 1}");
        properties.put("replication", ip);

        CreateKeyspaceStatement cks = new CreateKeyspaceStatement(name, ifNotExists, properties);
        Result result = cks.validate(metadataManager, null);
        assertNotNull(result, "Sentence validation not supported");
        assertFalse(result.hasError(), "Cannot validate sentence");
    }

    @Test
    public void validateIfNotExistsOk(){
        String name = "demo";
        boolean ifNotExists = true;
        Map<String, ValueProperty> properties = new HashMap<>();
        IdentifierProperty ip = new IdentifierProperty("{class: SimpleStrategy, replication_factor: 1}");
        properties.put("replication", ip);

        CreateKeyspaceStatement cks = new CreateKeyspaceStatement(name, ifNotExists, properties);
        Result result = cks.validate(metadataManager, null);
        assertNotNull(result, "Sentence validation not supported");
        assertFalse(result.hasError(), "Cannot validate sentence");
    }


    @Test
    public void validateMissingProperties(){
        String name = "not_exists";
        boolean ifNotExists = false;
        Map<String, ValueProperty> properties = new HashMap<>();

        CreateKeyspaceStatement cks = new CreateKeyspaceStatement(name, ifNotExists, properties);
        Result result = cks.validate(metadataManager, null);
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }

    @Test
    public void validateMissingReplication(){
        String name = "demo";
        boolean ifNotExists = true;
        Map<String, ValueProperty> properties = new HashMap<>();
        IdentifierProperty ip = new IdentifierProperty("{invalid_value}");
        properties.put("missing", ip);

        CreateKeyspaceStatement cks = new CreateKeyspaceStatement(name, ifNotExists, properties);
        Result result = cks.validate(metadataManager, null);
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }
}
