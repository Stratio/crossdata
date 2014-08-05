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

package com.stratio.meta.core.engine;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

public class EngineTests extends BasicCoreCassandraTest{
    private EngineConfig engineConfig;

    @BeforeClass
    public void setUp(){
        engineConfig = new EngineConfig();
        String [] cassandraHosts = {"127.0.0.1"};
        engineConfig.setCassandraHosts(cassandraHosts);
        engineConfig.setCassandraPort(9042);
        engineConfig.setSparkMaster("local");
        engineConfig.setClasspathJars("/");
        engineConfig.setJars(Arrays.asList("akka-1.0.jar", "deep-0.2.0.jar"));
    }

    @Test
    public void testCreateEngine(){
        Engine engine = new Engine(engineConfig);
        Assert.assertNotNull(engine);
        engine.shutdown();
    }
}
