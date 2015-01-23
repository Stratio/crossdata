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

package com.stratio.crossdata.core.engine;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.File;
import java.util.Random;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.core.grid.Grid;

public class EngineTest {

    private Engine engine;
    private EngineConfig engineConfig;
    private String path;

    @BeforeClass
    public void setUp() {
        path = "/tmp/com.stratio.crossdata-test-" + new Random().nextInt(100000);
        Grid.initializer().withPort(7810).withListenAddress("localhost").withPersistencePath(path).init();
        engineConfig = new EngineConfig();
        engineConfig.setGridContactHosts(new String[]{"localhost"});
        engineConfig.setGridJoinTimeout(5000);
        engineConfig.setGridListenAddress("localhost");
        engineConfig.setGridMinInitialMembers(1);
        engineConfig.setGridPersistencePath(path);
        engineConfig.setGridPort(7810);
        engine = new Engine(engineConfig);
    }

    @AfterClass
    public void tearDown() {
        engine.shutdown();
        File file = new File(path);
        file.delete();
    }

    @Test
    public void testGetParser() throws Exception {
        assertNotNull(engine.getParser(), "Parser is null");
    }

    @Test
    public void testGetValidator() throws Exception {
        assertNotNull(engine.getValidator(), "Validator is null");
    }

    @Test
    public void testGetPlanner() throws Exception {
        assertNotNull(engine.getPlanner(), "Planner is null");
    }

    @Test
    public void testGetCoordinator() throws Exception {
        assertNotNull(engine.getCoordinator(), "Coordinator is null");
    }

    @Test
    public void testGetAPIManager() throws Exception {
        assertNotNull(engine.getAPIManager(), "APIManager is null");
    }

    @Test
    public void testConstructorFail(){
        EngineConfig engineConfigTest = new EngineConfig();
        engineConfigTest.setGridContactHosts(new String[]{"whatever"});
        engineConfigTest.setGridJoinTimeout(-1);
        engineConfigTest.setGridListenAddress(null);
        engineConfigTest.setGridMinInitialMembers(-1);
        engineConfigTest.setGridPersistencePath(null);
        engineConfigTest.setGridPort(-1);
        try {
            Engine testEngine = new Engine(engineConfigTest);
            testEngine.getParser().parseStatement("whatever", "whatever");
            fail("testConstructorFail should have thrown a Exception");
        } catch (Exception ex){
            assertNotNull(ex, "testConstructorFail should have thrown a Exception");
        }
    }

}
