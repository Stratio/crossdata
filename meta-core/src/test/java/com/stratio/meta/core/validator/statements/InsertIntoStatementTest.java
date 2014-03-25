/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.validator.statements;


import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.MetaStatement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class InsertIntoStatementTest extends BasicCoreCassandraTest {

    private static MetadataManager _metadataManager = null;

    private static final ParsingTest _pt = new ParsingTest();

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        _metadataManager = new MetadataManager(_session);
        _metadataManager.loadMetadata();
    }

    @Test
    public void validate_basic_ok(){
        String inputText = "INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_basic_ok");
        MetaResult result = stmt.validate(_metadataManager, "");
        assertNotNull(result, "Sentence validation not supported");
        assertFalse(result.hasError(), "Cannot validate sentence");
    }

    @Test
    public void validate_basic_userKs_ok(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_basic_userKs_ok");
        MetaResult result = stmt.validate(_metadataManager, "demo");
        assertNotNull(result, "Sentence validation not supported");
        assertFalse(result.hasError(), "Cannot validate sentence");
    }

    @Test
    public void validate_unknownColumn(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_unknownColumn");
        MetaResult result = stmt.validate(_metadataManager, "demo");
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }

    @Test
    public void validate_booleanColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, 'true', '');";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_booleanColumnFail");
        MetaResult result = stmt.validate(_metadataManager, "demo");
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }

    @Test
    public void validate_integerColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', '10', true, '');";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_integerColumnFail");
        MetaResult result = stmt.validate(_metadataManager, "demo");
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }

    @Test
    public void validate_textColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES (true, 'male', 'name_0@domain.com', 10, true, '');";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_textColumnFail");
        MetaResult result = stmt.validate(_metadataManager, "demo");
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }

}
