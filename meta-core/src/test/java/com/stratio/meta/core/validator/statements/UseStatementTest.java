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


public class UseStatementTest extends BasicCoreCassandraTest {

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
    public void validate_ok(){
        String inputText = "USE demo;";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_ok");
        MetaResult result = stmt.validate(_metadataManager, "");
        assertNotNull(result, "Sentence validation not supported");
        assertFalse(result.hasError(), "Cannot validate sentence");
    }

    @Test
    public void validate_notExists(){
        String inputText = "USE demo_unknown;";
        MetaStatement stmt = _pt.testRegularStatement(inputText, "validate_notExists");
        MetaResult result = stmt.validate(_metadataManager, "");
        assertNotNull(result, "Sentence validation not supported");
        assertTrue(result.hasError(), "Validation should fail");
    }
}
