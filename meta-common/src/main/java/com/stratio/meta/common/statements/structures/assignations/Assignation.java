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

package com.stratio.meta.common.statements.structures.assignations;

import com.stratio.meta.common.statements.structures.ColumnName;
import com.stratio.meta2.common.statements.structures.terms.GenericTerm;

/**
 * Representation of the semantic operation of assigning a value to a column. That operation
 * may be assigning a value, comparing a column with a value, comparing two columns, etc.
 */
public class Assignation {

  /**
   * Target column of the assignation (left side).
   */
  private final ColumnName targetColumn;

  /**
   * Operator to be applied.
   */
  private final Operator operation;

  /**
   * Value to be assigned (right side).
   */
  private GenericTerm value;

  /**
   * Define an assignation.
   * @param targetColumn Target column.
   * @param operation Operation to be applied.
   * @param value Value.
   */
  public Assignation(ColumnName targetColumn, Operator operation, GenericTerm value) {
    this.targetColumn = targetColumn;
    this.operation = operation;
    this.value = value;
  }

  public Assignation(ColumnName targetColumn, GenericTerm value) {
    this(targetColumn, Operator.ASSIGN, value);
  }

  public ColumnName getTargetColumn() {
    return targetColumn;
  }

  public Operator getOperation() {
    return operation;
  }

  public GenericTerm getValue() {
    return value;
  }

  public void setValue(GenericTerm value){
    this.value = value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(targetColumn.toString());
    sb.append(" ").append(operation).append(" ");
    sb.append(value);
    return sb.toString();
  }
}
