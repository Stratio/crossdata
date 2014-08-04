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

import org.testng.annotations.Test;

// TODO: To be included when streaming integration is fully accomplished
/*
 * import com.stratio.meta.common.result.CommandResult; import
 * com.stratio.meta.common.result.Result; import
 * com.stratio.meta.core.cassandra.BasicCoreCassandraTest; import
 * com.stratio.meta.core.executor.StreamExecutor; import
 * com.stratio.meta.core.statements.CreateTableStatement; import
 * com.stratio.meta.core.structures.BooleanProperty; import
 * com.stratio.meta.core.structures.Property; import
 * com.stratio.meta.core.structures.PropertyNameValue;
 * 
 * import java.util.Collections; import java.util.HashMap; import java.util.Map;
 * 
 * import static org.testng.Assert.assertEquals;
 */

// TODO: To be included when streaming integration is fully accomplished
public class StreamIT {

  // TODO: To be included when streaming integration is fully accomplished
  /*
   * @BeforeClass public void removeEphemeralTable(){ MetaStream.dropEphemeralTable("demo.temporal_test"); }
   * 
   * @Test public void testEphemeralCreation() { String streamName = "demo.temporal_test";
   * Map<String, String> columns = new HashMap<>(); columns.put("name", "text"); columns.put("id",
   * "long"); columns.put("age", "int"); columns.put("rating", "double"); CreateTableStatement cts =
   * new CreateTableStatement(streamName, columns, Collections.singletonList("name"),
   * Collections.EMPTY_LIST, 1, 1); Property property = new PropertyNameValue("ephemeral", new
   * BooleanProperty(true)); cts.setProperties(Collections.singletonList(property));
   * logger.info("TRACE: "+cts.toString()); Result result = StreamExecutor.execute(cts); String
   * resultStr = ((CommandResult) result).getResult().toString();
   * assertEquals("Ephemeral table '"+streamName+"' created.", resultStr,
   * "testEphemeralCreation: Ephemeral table couldn't be created"); }
   */

  @Test
  public void testTestAreNotAutomaticallyLaunched() {
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    System.out.println("!!!!!!!!!!!!!!Test must just be manually executed!!!!!!!!!!!!!!!!!!!!!");
    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
  }
}
