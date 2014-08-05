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
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code DROP KEYSPACE} statement from the META language.
 */
public class DropKeyspaceStatement extends MetaStatement {

  /**
   * Whether the keyspace should be removed only if exists.
   */
  private boolean ifExists;

  /**
   * Class constructor.
   * 
   * @param keyspace The name of the keyspace.
   * @param ifExists Whether it should be removed only if exists.
   */
  public DropKeyspaceStatement(String keyspace, boolean ifExists) {
    this.command = false;
    this.setKeyspace(keyspace);
    this.ifExists = ifExists;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Drop keyspace ");
    if (ifExists) {
      sb.append("if exists ");
    }
    sb.append(this.getEffectiveKeyspace());
    return sb.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = QueryResult.createSuccessQueryResult();
    KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(this.getEffectiveKeyspace());
    if (ksMetadata == null && !ifExists) {
      result =
          Result.createValidationErrorResult("Keyspace " + this.getEffectiveKeyspace()
              + " does not exist.");
    }
    return result;
  }

  @Override
  public String translateToCQL(MetadataManager metadataManager) {
    return this.toString();
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
