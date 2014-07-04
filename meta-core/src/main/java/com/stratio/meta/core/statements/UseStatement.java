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

    if (!keyspace.contains("'")) {
      this.setKeyspace(keyspace.toLowerCase());
    } else {
      this.setKeyspace(keyspace);
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
    tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
    return tree;
  }

}
