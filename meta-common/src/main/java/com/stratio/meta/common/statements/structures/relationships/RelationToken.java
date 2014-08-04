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

package com.stratio.meta.common.statements.structures.relationships;

import java.util.ArrayList;
import java.util.List;

import com.stratio.meta.common.statements.structures.terms.Term;
import com.stratio.meta.common.utils.StringUtils;

/**
 * Class that models a relationship in a {@code WHERE} clause that includes a reference to the
 * {@code TOKEN} Cassandra function.
 */
public class RelationToken extends Relation {

  private boolean rightSideTokenType = false;

  public RelationToken(List<String> identifiers) {
    this.terms = new ArrayList<>();
    this.type = TYPE_TOKEN;
    this.identifiers = new ArrayList<>();
  }

  public RelationToken(List<String> identifiers, String operator) {
    this(identifiers);
    this.operator = operator;
  }

  public RelationToken(List<String> identifiers, String operator, Term term) {
    this(identifiers, operator);
    this.terms.add(term);
  }

  public RelationToken(List<String> identifiers, String operator, List<Term<?>> terms) {
    this(identifiers, operator);
    this.terms = terms;
    this.rightSideTokenType = true;
  }

  public boolean isRightSideTokenType() {
    return rightSideTokenType;
  }

  public void setRightSideTokenType(boolean rightSideTokenType) {
    this.rightSideTokenType = rightSideTokenType;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TOKEN(");
    sb.append(StringUtils.stringList(identifiers, ", ")).append(")");
    sb.append(" ").append(operator).append(" ");
    if (rightSideTokenType) {
      sb.append("TOKEN(").append(StringUtils.stringList(terms, ", ")).append(")");
    } else {
      sb.append(StringUtils.stringList(terms, ", "));
    }
    return sb.toString();
  }

}
