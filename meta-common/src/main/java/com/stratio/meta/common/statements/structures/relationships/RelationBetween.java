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
