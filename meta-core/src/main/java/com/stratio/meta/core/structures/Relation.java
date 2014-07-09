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

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.TableMetadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that models the different types of relationships that can be found on a WHERE clause.
 */
public abstract class Relation {

  /**
   * Constant to define compare relationships (e.g., >, <, =, etc.).
   */
  public static final int TYPE_COMPARE = 1;

  /**
   * Constant to define inclusion relationships.
   */
  public static final int TYPE_IN = 2;

  /**
   * Constant to define comparisons with the {@code TOKEN} cassandra function.
   */
  public static final int TYPE_TOKEN = 3;

  /**
   * Constant to define range comparisons.
   */
  public static final int TYPE_BETWEEN = 4;

  /**
   * List of identifiers in the left part of the relationship.
   */
  protected List<SelectorIdentifier> identifiers;

  /**
   * Operator to be applied to solve the relationship.
   */
  protected String operator;

  /**
   * List of terms on the right part of the relationship.
   */
  protected List<Term<?>> terms;

  /**
   * Type of relationship.
   */
  protected int type;

  public List<SelectorIdentifier> getIdentifiers() {
    return identifiers;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public List<Term<?>> getTerms() {
    return terms;
  }

  public int numberOfTerms() {
    return this.terms.size();
  }

  public void setTerms(List<Term<?>> terms) {
    this.terms = terms;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public void updateTermClass(TableMetadata tableMetadata) {
    for (int i = 0; i < terms.size(); i++) {

      String columnFullName = identifiers.get(0).toString();
      String columnName = columnFullName.substring(columnFullName.indexOf(".") + 1);

      ColumnMetadata column = tableMetadata.getColumn(columnName);
      if (column != null) {
        Class<? extends Comparable<?>> dataType =
            (Class<? extends Comparable<?>>) column.getType().asJavaClass();
        if (terms.get(i) instanceof Term) {
          Term<?> term = terms.get(i);
          if (dataType == Integer.class && term.getTermClass() == Long.class) {
            terms.set(i, new IntegerTerm((Term<Long>) term));
          } else if (dataType == Float.class && term.getTermClass() == Double.class) {
            terms.set(i, new FloatTerm((Term<Double>) term));
          }
        }
      }
    }
  }

  public void updateTermClass(
      com.stratio.meta.common.metadata.structures.TableMetadata streamingMetadata) {
    for (int i = 0; i < terms.size(); i++) {

      String columnFullName = identifiers.get(0).toString();
      String columnName = columnFullName.substring(columnFullName.indexOf(".") + 1);

      com.stratio.meta.common.metadata.structures.ColumnMetadata
          column =
          streamingMetadata.getColumn(columnName);
      if (column != null) {
        Class<? extends Comparable<?>> dataType =
            (Class<? extends Comparable<?>>) column.getType().getDbClass();
        if (terms.get(i) instanceof Term) {
          Term<?> term = terms.get(i);
          if (dataType == Integer.class && term.getTermClass() == Long.class) {
            terms.set(i, new IntegerTerm((Term<Long>) term));
          } else if (dataType == Float.class && term.getTermClass() == Double.class) {
            terms.set(i, new FloatTerm((Term<Double>) term));
          }
        }
      }
    }
  }

  @Override
  public abstract String toString();

  /**
   * Gets the string values list for the terms
   * 
   * @return Terms string values
   */
  public List<String> getTermsStringValues() {

    List<String> termsValuesList = new ArrayList<>();

    Iterator<Term<?>> terms = this.getTerms().iterator();
    while (terms.hasNext()) {
      Term<?> term = terms.next();
      termsValuesList.add(term.getStringValue());
    }

    return termsValuesList;
  }

  public String getSiddhiOperator() {
    String siddhiOperator = getOperator();
    if(siddhiOperator.equalsIgnoreCase("=")){
      siddhiOperator = "==";
    } else if(siddhiOperator.equalsIgnoreCase("<>")){
      siddhiOperator = "!=";
    }
    return siddhiOperator;
  }
}
