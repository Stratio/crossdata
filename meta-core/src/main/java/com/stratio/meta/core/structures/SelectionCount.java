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

import java.util.ArrayList;
import java.util.List;

public class SelectionCount extends SelectionClause {

  /**
   * The selection symbol.
   */
  private char symbol;
  private boolean identInc;
  private String identifier;

  /**
   * Class constructor.
   */
  public SelectionCount() {
    this.type = TYPE_COUNT;
  }

  public SelectionCount(char symbol, boolean identInc, String identifier) {
    this.type = TYPE_COUNT;
    this.symbol = symbol;
    this.identInc = identInc;
    this.identifier = identifier;
  }

  public SelectionCount(char symbol) {
    this(symbol, false, null);
  }

  public char getSymbol() {
    return symbol;
  }

  public void setSymbol(char symbol) {
    this.symbol = symbol;
  }

  public boolean isIdentInc() {
    return identInc;
  }

  public void setIdentInc(boolean identInc) {
    this.identInc = identInc;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("COUNT(");
    sb.append(symbol).append(")");
    if (identInc) {
      sb.append(" AS ").append(identifier);
    }
    return sb.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.stratio.meta.core.structures.SelectionClause#getIds()
   */
  @Override
  public List<String> getIds() {

    return new ArrayList<>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.stratio.meta.core.structures.SelectionClause#getSelectorsGroupBy()
   */
  @Override
  public List<SelectorGroupBy> getSelectorsGroupBy() {

    return new ArrayList<>();
  }
}
