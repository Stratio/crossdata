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

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models a {@code CREATE KEYSPACE} statement from the META language.
 */
public class CreateKeyspaceStatement extends MetaStatement {

  /**
   * The name of the keyspace.
   */
  private String name;

  /**
   * Whether the keyspace should be created only if it not exists.
   */
  private boolean ifNotExists;

  /**
   * The map of properties of the keyspace. The different options accepted by a keyspace
   * are determined by the selected {@link com.datastax.driver.core.ReplicationStrategy}.
   */
  private Map<String, ValueProperty> properties;

  /**
   * Class constructor.
   * @param name The name of the keyspace.
   * @param ifNotExists Whether it should be created only if it not exists.
   * @param properties The map of properties.
   */
  public CreateKeyspaceStatement(String name, boolean ifNotExists, Map<String, ValueProperty> properties) {
    this.name = name;
    this.command = false;
    this.ifNotExists = ifNotExists;
    this.properties = new HashMap<>();
    for(Map.Entry<String, ValueProperty> entry : properties.entrySet()){
      this.properties.put(entry.getKey().toLowerCase(), entry.getValue());
    }
  }

  /**
   * Get the name of the keyspace.
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the keyspace.
   * @param name The name of the keyspace.
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CREATE KEYSPACE ");
    if(ifNotExists){
      sb.append("IF NOT EXISTS ");
    }
    sb.append(name);
    sb.append(" WITH ");
    sb.append(ParserUtils.stringMap(properties, " = ", " AND "));
    return sb.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = QueryResult.createSuccessQueryResult();
    if(name!= null && name.length() > 0) {
      KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(name);
      if(ksMetadata != null && !ifNotExists){
        result = Result.createValidationErrorResult("Keyspace " + name + " already exists.");
      }
    }else{
      result = Result.createValidationErrorResult("Empty keyspace name found.");
    }

    if(properties.isEmpty() || !properties.containsKey("replication")){
      result = Result.createValidationErrorResult("Missing mandatory replication property.");
    }

    return result;
  }

  @Override
  public String translateToCQL(MetadataManager metadataManager) {
    String metaStr = this.toString();
    if(metaStr.contains("{")){
      return ParserUtils.translateLiteralsToCQL(metaStr);
    } else {
      return metaStr;
    }
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
