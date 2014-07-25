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

package com.stratio.meta.streaming;

import org.apache.log4j.Logger;
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

  private static final Logger logger = Logger.getLogger(StreamIT.class);

  // TODO: To be included when streaming integration is fully accomplished
  /*
   * @BeforeClass public void removeEphemeralTable(){
   * MetaStream.dropEphemeralTable("demo.temporal_test"); }
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
    logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    logger.info("!!!!!!!!!!!!!!Test must just be manually executed!!!!!!!!!!!!!!!!!!!!!");
    logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
  }
}
