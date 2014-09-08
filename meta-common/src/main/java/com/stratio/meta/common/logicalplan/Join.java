/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.common.logicalplan;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.statements.structures.relationships.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Join operator.
 */
public class Join extends UnionStep{

  /**
   * List of join relations.
   */
  private final List<Relation> joinRelations = new ArrayList<>();

  /**
   * Class constructor.
   *
   * @param operation The operation to be applied.
   */
  public Join(Operations operation) {
    super(operation);
  }

  public void addJoinRelation(Relation r){
    joinRelations.add(r);
  }

  public void addJoinRelations(List<Relation> list){
    joinRelations.addAll(list);
  }

  public List<Relation> getJoinRelations() {
    return joinRelations;
  }
}
