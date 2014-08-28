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

import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.TableMetadata;
import com.stratio.meta.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.terms.FloatTerm;
import com.stratio.meta2.common.statements.structures.terms.IntegerTerm;
import com.stratio.meta2.common.statements.structures.terms.Term;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that models the different types of relationships that can be found on a WHERE clause.
 */
public class Relation {

  /**
   * Identifier in the left part of the relationship.
   */
  protected Selector identifier;

  /**
   * Operator to be applied to solve the relationship.
   */
  protected Operator operator;

  /**
   * List of terms on the right part of the relationship.
   */
  protected List<Term<?>> terms;

  public Relation(Selector selector, Operator operator, List<Term<?>> terms){
    this.identifier = selector;
    this.operator = operator;
    this.terms = terms;
  }

  public Selector getIdentifier() {
    return identifier;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
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



  /*
  public void updateTermClass(TableMetadata tableMetadata) {
    for (int i = 0; i < terms.size(); i++) {

      String columnFullName = identifiers.get(0).toString();
      String columnName = columnFullName.substring(columnFullName.indexOf(".") + 1);

      ColumnMetadata column = tableMetadata.getColumn(columnName);
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
  */

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(identifier.toString());
    sb.append(" ").append(operator).append(" ");
    if(Operator.BETWEEN.equals(operator)) {
      sb.append(StringUtils.stringList(terms, " AND "));
    }else if(Operator.IN.equals(operator)){
      sb.append("(").append(StringUtils.stringList(terms, ", ")).append(")");
    }else{
      sb.append(StringUtils.stringList(terms, ", "));
    }

    return sb.toString();
  }

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
      termsValuesList.add(term.toString());
    }

    return termsValuesList;
  }
}
