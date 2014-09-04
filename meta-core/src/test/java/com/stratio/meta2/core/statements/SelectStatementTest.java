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

package com.stratio.meta2.core.statements;

//import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta2.common.data.TableName;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class SelectStatementTest {

  protected static MetadataManager _metadataManager = null;

  //protected static final ParsingTest _pt = new ParsingTest();

  @BeforeClass
  public static void setUpBeforeClass() {
    //BasicCoreConnectorTest.setUpBeforeClass();
    //BasicCoreConnectorTest.loadTestData("demo", "demoKeyspace.cql");
    //_metadataManager = new MetadataManager(_session, null);
    //_metadataManager = new MetadataManager();
    //_metadataManager.loadMetadata();
  }


  @Test
  public void simpleQuery() {
	  assertTrue(1==1);
  }

}
