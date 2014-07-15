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

package com.stratio.meta.common.logicalplan;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.relationships.RelationType;

/**
 * Filter the results retrieved through a Project operation.
 */
public class Filter extends LogicalStep{

  private final Operations operation;

  /**
   * Type of relation.
   */
  private final RelationType type;

  /**
   * Relationship.
   */
  private final Relation relation;

  /**
   * Create filter operation to be executed over a existing dataset.
   * @param operation The operation to be executed.
   * @param type The type of relation.
   * @param relation The relationship.
   */
  public Filter(Operations operation, RelationType type, Relation relation) {
    this.operation = operation;
    this.type = type;
    this.relation = relation;
  }

  /**
   * Get the type of operation associated with this filter.
   * @return A {@link com.stratio.meta.common.connector.Operations}.
   */
  public Operations getOperation(){
    return operation;
  }

  /**
   * Get the type of relationship.
   * @return A {@link com.stratio.meta.common.statements.structures.relationships.RelationType}.
   */
  public RelationType getType() {
    return type;
  }

  /**
   * Get the relationship.
   * @return A {@link com.stratio.meta.common.statements.structures.relationships.Relation}
   */
  public Relation getRelation() {
    return relation;
  }

}
