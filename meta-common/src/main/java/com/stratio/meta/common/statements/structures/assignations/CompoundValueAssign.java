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

package com.stratio.meta.common.statements.structures.assignations;

import com.stratio.meta2.common.statements.structures.terms.GenericTerm;

import java.util.ArrayList;
import java.util.List;

public class CompoundValueAssign {

  protected List<Operator> valueOperators;
  protected List<GenericTerm> compoundTerms;

  public CompoundValueAssign() {
    valueOperators = new ArrayList<>();
    compoundTerms = new ArrayList<>();
  }

  public CompoundValueAssign(Operator operator, GenericTerm compoundTerm) {
    this();
    valueOperators.add(operator);
    compoundTerms.add(compoundTerm);
  }

  public CompoundValueAssign(List<Operator> valueOperators, List<GenericTerm> compoundTerms) {
    this.valueOperators = valueOperators;
    this.compoundTerms = compoundTerms;
  }

  public List<Operator> getValueOperators() {
    return valueOperators;
  }

  public void setValueOperators(List<Operator> valueOperators) {
    this.valueOperators = valueOperators;
  }

  public List<GenericTerm> getCompoundTerms() {
    return compoundTerms;
  }

  public void setCompoundTerms(List<GenericTerm> compoundTerms) {
    this.compoundTerms = compoundTerms;
  }

  public void addCompoundTerm(Operator valueOperator, GenericTerm genericTerm){
    valueOperators.add(valueOperator);
    compoundTerms.add(genericTerm);
  }

}
