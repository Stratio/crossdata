/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.statements;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import com.stratio.streaming.api.IStratioStreamingAPI;

/**
 * Class that models an {@code EXPLAIN PLAN} statement from the META language.
 */
public class ExplainPlanStatement extends MetaStatement {

  /**
   * The {@link com.stratio.meta.core.statements.MetaStatement} to be analyzed.
   */
  private MetaStatement metaStatement;

  /**
   * Class constructor.
   * @param metaStatement The {@link com.stratio.meta.core.statements.MetaStatement} to be analyzed.
   */
  public ExplainPlanStatement(MetaStatement metaStatement) {
    this.command = true;
    this.metaStatement = metaStatement;
  }

  public MetaStatement getMetaStatement() {
    return metaStatement;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Explain plan for ");
    sb.append(metaStatement.toString());
    return sb.toString();
  }

  @Override
  public String translateToCQL() {
    return this.toString();
  }

  /**
   * Validate the semantics of the current statement. This method checks the
   * existing metadata to determine that all referenced entities exists in the
   * {@code targetKeyspace} and the types are compatible with the assignations
   * or comparisons.
   *
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
   *                 the required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    return metaStatement.validate(metadata, config);
  }

  @Override
  public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, this));
    return tree;
  }

  public Result execute(Session session, IStratioStreamingAPI stratioStreamingAPI){
    System.out.println("ŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋŋ");
    System.out.println("Explain Plan: " + getMetaStatement().toString());
    return CommandResult.createCommandResult(
        getMetaStatement().getPlan(new MetadataManager(session, stratioStreamingAPI),
                                                     getMetaStatement().getEffectiveKeyspace()).toStringDownTop());
  }

}
