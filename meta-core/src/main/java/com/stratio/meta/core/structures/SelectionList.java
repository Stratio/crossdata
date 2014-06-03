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
import java.util.Arrays;
import java.util.List;

public class SelectionList extends SelectionClause {

  private boolean distinct;
  private Selection selection;

  public SelectionList(boolean distinct, Selection selection) {
    this.type = TYPE_SELECTION;
    this.distinct = distinct;
    this.selection = selection;
  }

  public SelectionList(Selection selection) {
    this(false, selection);
  }

  public boolean isDistinct() {
    return distinct;
  }

  public void setDistinct(boolean distinct) {
    this.distinct = distinct;
  }

  public Selection getSelection() {
    return selection;
  }

  public void setSelection(Selection selection) {
    this.selection = selection;
  }

  public int getTypeSelection() {
    return selection.getType();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (distinct) {
      sb.append("DISTINCT ");
    }
    sb.append(selection.toString());
    return sb.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.stratio.meta.core.structures.SelectionClause#getIds()
   */
  @Override
  public List<String> getIds() {

    List<String> ids = new ArrayList<>();

    Selection selection = this.getSelection();
    if (selection.getType() == Selection.TYPE_SELECTOR) {
      SelectionSelectors sSelectors = (SelectionSelectors) selection;
      for (SelectionSelector sSelector : sSelectors.getSelectors()) {
        SelectorMeta selector = sSelector.getSelector();
        if (selector.getType() == SelectorMeta.TYPE_IDENT) {
          SelectorIdentifier selectorId = (SelectorIdentifier) selector;
          ids.add(selectorId.getIdentifier());
        } else {
          ids.addAll(retrieveIdsFromFunctionSelector(selector));
        }
      }
    }

    return ids;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.stratio.meta.core.structures.SelectionClause#getSelectorsGroupBy()
   */
  @Override
  public List<SelectorGroupBy> getSelectorsGroupBy() {

    List<SelectorGroupBy> selectorsList = new ArrayList<>();

    Selection selection = this.getSelection();
    if (selection.getType() == Selection.TYPE_SELECTOR) {

      SelectionSelectors selectors = (SelectionSelectors) selection;
      for (SelectionSelector selectionSelector : selectors.getSelectors()) {

        SelectorMeta selector = selectionSelector.getSelector();
        if (selector.getType() == SelectorMeta.TYPE_GROUPBY) {
          SelectorGroupBy selectorGroupBy = (SelectorGroupBy) selector;
          selectorsList.add(selectorGroupBy);
        }
      }
    }

    return selectorsList;
  }

  private List<String> retrieveIdsFromFunctionSelector(SelectorMeta selector) {

    List<String> ids = new ArrayList<>();
    if (selector instanceof SelectorGroupBy) {
      SelectorGroupBy selectorGroupBy = (SelectorGroupBy) selector;
      if (!selectorGroupBy.getGbFunction().equals(GroupByFunction.COUNT))
        ids.addAll(retrieveIdsFromFunctionSelector(selectorGroupBy.getParam()));
    } else if (selector instanceof SelectorFunction) {
      SelectorFunction selectorFunction = (SelectorFunction) selector;
      List<SelectorMeta> params = selectorFunction.getParams();
      for (SelectorMeta subselector : params) {
        ids.addAll(retrieveIdsFromFunctionSelector(subselector));
      }
    } else {
      return Arrays.asList(((SelectorIdentifier) selector).getIdentifier());
    }

    return ids;
  }

  @Override
  public void addTablename(String tablename) {
    selection.addTablename(tablename);
  }

}
