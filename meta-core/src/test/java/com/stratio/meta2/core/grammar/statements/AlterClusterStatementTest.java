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

package com.stratio.meta2.core.grammar.statements;

import com.stratio.meta.core.grammar.ParsingTest;

import org.testng.annotations.Test;

/**
 * Alter storage parsing tests.
 */
public class AlterClusterStatementTest extends ParsingTest {

  @Test
  public void alterClusterEmptyJSON() {
    String inputText = "ALTER CLUSTER dev_environment1 WITH OPTIONS {};";
    testRegularStatement(inputText, "alterClusterEmptyJSON");
  }

  @Test
  public void alterClusterBasic() {
    String inputText = "ALTER CLUSTER dev_environment1 WITH OPTIONS {"
                       + "\"hosts\": [\"127.0.0.1\", \"127.0.0.2\"], "
                       + "\"port\": 1234};";
    testRegularStatement(inputText, "alterClusterBasic");
  }

  @Test
  public void alterClusterSimple() {
    String inputText = "ALTER CLUSTER production_madrid WITH OPTIONS {host: 127.0.0.1, port: 9160, mode: \"random\"};";
    String expectedText = "ALTER CLUSTER production_madrid ON DATASTORE cassandra WITH OPTIONS {host: 127.0.0.1, port: 9160, mode: random};";
    testRegularStatement(inputText, expectedText, "alterClusterSimple");
  }

  @Test
  public void alterClusterIfExists() {
    String inputText = "ALTER CLUSTER IF EXISTS productionMadrid WITH OPTIONS {'host': '127.0.0.1', \"port\": 9160, exhaustive: false};";
    String expectedText = "ALTER CLUSTER IF EXISTS productionMadrid ON DATASTORE cassandra WITH OPTIONS {host: '127.0.0.1', port: 9160, exhaustive: false};";
    testRegularStatement(inputText, expectedText, "alterClusterIfExists");
  }

  @Test
  public void alterClusterWrongClusterName() {
    String inputText = "ALTER CLUSTER ^productionMadrid WITH OPTIONS {'host': '127.0.0.1'};";
    testParserFails(inputText, "alterClusterWrongName");
  }

  @Test
  public void alterClusterWrongJson() {
    String inputText = "ALTER CLUSTER productionMadrid WITH OPTIONS {25: com.stratio.35.executor};";
    testParserFails(inputText, "alterClusterWrongJson");
  }

}
