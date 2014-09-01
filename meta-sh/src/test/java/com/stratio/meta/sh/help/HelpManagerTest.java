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

package com.stratio.meta.sh.help;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class HelpManagerTest {

  private HelpManager manager = new HelpManager();

  @Test
  public void testLoadHelpContent() throws Exception {
    HelpContent content = manager.loadHelpContent();
    assertNotNull(content, "Cannot load help");
    assertNotNull(content.getContent(), "Null help content returned.");

    HelpEntry entry = content.getContent().get(0);
    assertNotNull(entry.getEntry(), "Cannot read entry");
    assertNotNull(entry.getHelp(), "Cannot read help");

    assertNotNull(content.getHelp(), "Null help map returned.");
    for(HelpType type : HelpType.values()){
      assertNotNull(content.getHelp().containsKey(type), "Cannot find help associated with " + type + " in help map");
      assertNotNull(content.searchHelp(type), "Cannot find help associated with " + type);
    }
  }
}
