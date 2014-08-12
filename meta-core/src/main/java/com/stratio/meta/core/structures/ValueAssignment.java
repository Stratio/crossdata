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

package com.stratio.meta.core.structures;

import com.stratio.meta2.common.statements.structures.terms.Term;
import com.stratio.meta2.core.structures.IdentMap;

/**
 * Class that contains the value assignment of the
 * {@link com.stratio.meta.core.statements.UpdateTableStatement}.
 */
public class ValueAssignment {

  /**
   * Constant for term value types.
   */
  public static final int TYPE_TERM = 1;

  /**
   * Constant for int or literal types.
   */
  public static final int TYPE_IDENT_INT_OR_LITERAL = 2;

  /**
   * Constant for map types.
   */
  public static final int TYPE_IDENT_MAP = 3;

  /**
   * The term being assigned.
   */
  private Term<?> term;

  /**
   * Int or literal assignation.
   */
  private IdentIntOrLiteral iiol;

  /**
   * Map of assigned values.
   */
  private IdentMap identMap;

  /**
   * Assignation type.
   */
  private int type;

  /**
   * Class constructor.
   * 
   * @param term The term being assigned.
   * @param iiol Int or literal assignation.
   * @param identMap Map of values being assigned.
   * @param type The type of assignation.
   */
  public ValueAssignment(Term<?> term, IdentIntOrLiteral iiol, IdentMap identMap, int type) {
    this.term = term;
    this.iiol = iiol;
    this.identMap = identMap;
    this.type = type;
  }

  /**
   * Class constructor.
   * 
   * @param term The term being assigned.
   */
  public ValueAssignment(Term<?> term) {
    this(term, null, null, TYPE_TERM);
  }

  /**
   * Class constructor.
   * 
   * @param iiol Int or literal assignation.
   */
  public ValueAssignment(IdentIntOrLiteral iiol) {
    this(null, iiol, null, TYPE_IDENT_INT_OR_LITERAL);
  }

  /**
   * Class constructor.
   * 
   * @param identMap Map of values being assigned.
   */
  public ValueAssignment(IdentMap identMap) {
    this(null, null, identMap, TYPE_IDENT_MAP);
  }

  public int getType() {
    return type;
  }

  public Term<?> getTerm() {
    return term;
  }

  public void setTerm(Term<?> term) {
    this.term = term;
  }

  public IdentIntOrLiteral getIiol() {
    return iiol;
  }

  public IdentMap getIdentMap() {
    return identMap;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (type == TYPE_TERM) {
      sb.append(term.toString());
    } else if (type == TYPE_IDENT_INT_OR_LITERAL) {
      sb.append(iiol.toString());
    } else if (type == TYPE_IDENT_MAP) {
      sb.append(identMap.toString());
    }
    return sb.toString();
  }

}
