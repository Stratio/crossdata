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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapTerms extends CollectionTerms {

  public MapTerms() {
    super();
    terms = new HashMap<Term, Term>();
    clazz = HashMap.class;
  }

  @Override
  public void addTerm(Term... term) {
    if(term.length > 1){
      ((HashMap<Term, Term>) terms).put(term[0], term[1]);
    }
  }

  @Override
  public void deleteTerm(Term term) {
    ((HashMap<Term, Term>) terms).remove(term);
  }

  @Override
  public void clearTerms() {
    ((HashMap<Term, Term>) terms).clear();
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
    Iterator<Map.Entry<Term,Term>> iter = ((HashMap<Term, Term>) terms).entrySet().iterator();
    while(iter.hasNext()){
      Map.Entry<Term, Term> term = iter.next();
      sb.append(term.getKey()).append(": ").append(term.getValue());
      if(iter.hasNext()){
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }
}
