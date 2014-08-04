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

import com.stratio.meta.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.meta.common.statements.structures.terms.Term;
import com.stratio.meta.common.utils.StringUtils;

public class RelationBetween extends Relation {

  /**
   * Class constructor.
   * 
   * @param identifier The element to be compared.
   */
  public RelationBetween(String identifier) {
    this.terms = new ArrayList<>();
    this.type = TYPE_BETWEEN;
    this.operator = "BETWEEN";
    this.identifiers = new ArrayList<>();
    this.identifiers.add(new SelectorIdentifier(identifier));
  }

  /**
   * Class constructor.
   * 
   * @param identifier The element to be compared.
   * @param term1 The lower limit.
   * @param term2 The upper limit.
   */
  public RelationBetween(String identifier, Term<?> term1, Term<?> term2) {
    this(identifier);
    this.terms.add(term1);
    this.terms.add(term2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(identifiers.get(0).toString());
    sb.append(" ").append(operator).append(" ").append(StringUtils.stringList(terms, " AND "));
    return sb.toString();
  }

}
