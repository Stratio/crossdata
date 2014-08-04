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

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code USE} statement from the META language.
 */
public class UseStatement extends MetaStatement {

  /**
   * Class constructor.
   * 
   * @param keyspace The name of the target keyspace.
   */
  public UseStatement(String keyspace) {
    if (keyspace.equals("-")) {
      this.setKeyspace("");
    } else {
      if (!keyspace.contains("'")) {
        this.setKeyspace(keyspace.toLowerCase());
      } else {
        this.setKeyspace(keyspace);
      }
    }
    this.command = false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("USE ");
    sb.append(this.getEffectiveKeyspace());
    return sb.toString();
  }

  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    Result result = QueryResult.createSuccessQueryResult();
    if (this.getEffectiveKeyspace() != null && this.getEffectiveKeyspace().length() > 0) {
      if (!metadata.getKeyspacesNames().contains(this.getEffectiveKeyspace().toLowerCase())) {
        result =
            Result.createValidationErrorResult("Keyspace " + this.getEffectiveKeyspace()
                + " does not exist.");
      }
    } else if (this.getEffectiveKeyspace().equals("")) {

    } else {
      result = Result.createValidationErrorResult("Missing keyspace name.");
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
    if (getKeyspace().isEmpty())
      tree.setNode(new MetaStep(MetaPath.COMMAND, this));
    else
      tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
