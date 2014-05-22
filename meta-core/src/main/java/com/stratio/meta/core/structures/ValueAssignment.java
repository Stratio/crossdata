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

package com.stratio.meta.core.structures;

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
