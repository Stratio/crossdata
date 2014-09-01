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

package com.stratio.meta.streaming;


import org.apache.log4j.Logger;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineConnectionException;

public class StreamTestIT extends BasicCoreCassandraTest {

  private static final Logger logger = Logger.getLogger(StreamTestIT.class);

  private EngineConfig config = new EngineConfig();

  private IStratioStreamingAPI stratioStreamingAPI;

  // TODO: (Streaming)
  // @BeforeClass
  public void removeEphemeralTable() {
    try {
      stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
    } catch (StratioEngineConnectionException e) {
      logger.error(e);
    }
    config.setSparkMaster("local");
    MetaStream.dropEphemeralTable("qid1", stratioStreamingAPI, "demo.temporal_test");
  }

  // TODO: (Streaming)
  /*
   * //@Test public void testEphemeralCreation() { String streamName = "demo.temporal_test";
   * Map<String, String> columns = new HashMap<>(); columns.put("name", "text"); columns.put("id",
   * "long"); columns.put("age", "int"); columns.put("rating", "double"); DeepSparkContext spc = new
   * DeepSparkContext("local","null"); CreateTableStatement cts = new
   * CreateTableStatement(streamName, columns, Collections.singletonList("name"),
   * Collections.EMPTY_LIST, 1, 1); Property property = new PropertyNameValue("ephemeral", new
   * BooleanProperty(true)); cts.setProperties(Collections.singletonList(property));
   * 
   * Result result = StreamExecutor.execute(cts, stratioStreamingAPI, null);
   * 
   * String resultStr = ((CommandResult) result).getResult().toString();
   * assertEquals("Ephemeral table '"+streamName+"' created.", resultStr,
   * "testEphemeralCreation: Ephemeral table couldn't be created"); }
   */
}
