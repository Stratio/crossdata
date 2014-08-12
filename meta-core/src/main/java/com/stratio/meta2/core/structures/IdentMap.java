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

package com.stratio.meta2.core.structures;

import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta2.common.statements.structures.terms.Term;

import java.util.HashMap;
import java.util.Map;

public class IdentMap {

  private String identifier;
  private Map<String, Term> mapTerms;

  public IdentMap(String identifier) {
    this.identifier = identifier;
    this.mapTerms = new HashMap<>();
  }

  public IdentMap(String identifier, Map mapTerms) {
    this(identifier);
    this.mapTerms = mapTerms;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Map<String, Term> getMapTerms() {
    return mapTerms;
  }

  public void setMapTerms(Map mapTerms) {
    this.mapTerms = mapTerms;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder(identifier);
    sb.append(" + ");
    sb.append(ParserUtils.stringMap(mapTerms, ": ", ", "));
    return sb.toString();
  }

}
