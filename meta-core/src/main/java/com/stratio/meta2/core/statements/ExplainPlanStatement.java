/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.statements;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.core.validator.ValidationRequirements;

/**
 * Class that models an {@code EXPLAIN PLAN} statement from the META language.
 */
public class ExplainPlanStatement extends MetaStatement {

  /**
   * The {@link com.stratio.meta2.core.statements.MetaStatement} to be analyzed.
   */
  private MetaStatement metaStatement;

  /**
   * Class constructor.
   * 
   * @param metaStatement The {@link com.stratio.meta2.core.statements.MetaStatement} to be analyzed.
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

  /**
   * Validate the semantics of the current statement. This method checks the existing metadata to
   * determine that all referenced entities exists in the {@code targetKeyspace} and the types are
   * compatible with the assignations or comparisons.
   *
   * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides the
   *        required information.
   * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
   */
  @Override
  public Result validate(MetadataManager metadata, EngineConfig config) {
    return metaStatement.validate(metadata, config);
  }

  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements();
  }
}
