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

import com.stratio.meta.common.statements.structures.selectors.SelectorGroupBy;
import com.stratio.meta.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.meta.common.statements.structures.selectors.SelectorMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectionClause {

  public static final int TYPE_SELECTION = 1;
  public static final int TYPE_COUNT = 2;

  protected int type;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public abstract List<SelectorGroupBy> getSelectorsGroupBy();

  public abstract void addTablename(String tablename);

  public abstract List<String> getIds();

  @Override
  public abstract String toString();

  public List<String> getFields() {
    List<String> ids = new ArrayList<>();
    if (type == TYPE_COUNT) {
      return ids;
    }
    SelectionList sList = (SelectionList) this;
    Selection selection = sList.getSelection();
    if (selection.getType() == Selection.TYPE_ASTERISK) {
      ids.add("*");
      return ids;
    }
    SelectionSelectors sSelectors = (SelectionSelectors) selection;
    for (SelectionSelector sSelector : sSelectors.getSelectors()) {
      SelectorMeta selector = sSelector.getSelector();
      if (selector.getType() == SelectorMeta.TYPE_IDENT) {
        SelectorIdentifier selectorId = (SelectorIdentifier) selector;
        ids.add(selectorId.getField());
      }
    }
    return ids;
  }

  /**
   * Checks whether the selection clause contains some function or not
   * 
   * @return true, if functions are used; false, otherwise.
   */
  public abstract boolean containsFunctions();
}
