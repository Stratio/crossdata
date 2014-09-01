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

package com.stratio.meta.core.statements;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models an {@code ALTER KEYSPACE} statement from the META language.
 */
public class AlterKeyspaceStatement extends MetaStatement {

  /**
   * The map of properties of the keyspace. The different options accepted by a keyspace are
   * determined by the selected {@link com.datastax.driver.core.ReplicationStrategy}.
   */
  private Map<String, ValueProperty> properties;

  /**
   * Class constructor.
   * 
   * @param keyspace The name of the keyspace.
   * @param properties The map of properties.
   */
  public AlterKeyspaceStatement(String keyspace, Map<String, ValueProperty> properties) {
    this.command = false;
    this.setKeyspace(keyspace);
    this.properties = new HashMap<>();
    for (Map.Entry<String, ValueProperty> entry : properties.entrySet()) {
      this.properties.put(entry.getKey().toLowerCase(), entry.getValue());
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ALTER KEYSPACE ");
    sb.append(this.getEffectiveKeyspace()).append(" WITH ");
    sb.append(ParserUtils.stringMap(properties, " = ", " AND "));
    return sb.toString();
  }

  @Override
  public String translateToCQL(MetadataManager metadataManager) {
    return this.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {

    Result result = QueryResult.createSuccessQueryResult();

    if (this.getEffectiveKeyspace() != null && this.getEffectiveKeyspace().length() > 0) {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(this.getEffectiveKeyspace());
      if (ksMetadata == null) {
        result =
            Result.createValidationErrorResult("Keyspace " + this.getEffectiveKeyspace()
                + " not found.");
      }
    } else {
      result = Result.createValidationErrorResult("Empty keyspace name found.");
    }

    if (properties.isEmpty()
        || (!properties.containsKey("replication") & !properties.containsKey("durable_writes"))) {
      result =
          Result
              .createValidationErrorResult("At least one property must be included: 'replication' or 'durable_writes'.");
    }

    return result;
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
