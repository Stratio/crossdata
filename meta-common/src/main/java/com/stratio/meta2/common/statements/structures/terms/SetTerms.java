/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.common.statements.structures.terms;

import com.stratio.meta.common.statements.structures.terms.Term;

import java.util.HashSet;
import java.util.Iterator;

public class SetTerms extends CollectionTerms {

  public SetTerms() {
    super();
    terms = new HashSet<Term>();
    clazz = HashSet.class;
  }

  @Override
  public void addTerm(Term... term) {
    if(term.length > 0){
      ((HashSet<Term>) terms).add(term[0]);
    }
  }

  @Override
  public void deleteTerm(Term term) {
    ((HashSet<Term>) terms).remove(term);
  }

  @Override
  public void clearTerms() {
    ((HashSet<Term>) terms).clear();
  }

  /**
   * Get the String value representation.
   *
   * @return The String value.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    Iterator<Term> iter = ((HashSet<Term>) terms).iterator();
    while(iter.hasNext()){
      Term term = iter.next();
      sb.append(term.toString());
      if(iter.hasNext()){
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }
}
