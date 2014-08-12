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

import com.stratio.meta2.common.statements.structures.terms.Term;

public class PropertyNameValue extends Property {

  private String name;
  private Term vp;

  public PropertyNameValue() {
    super(TYPE_NAME_VALUE);
  }

  public PropertyNameValue(String name, Term vp) {
    super(TYPE_NAME_VALUE);
    this.name = name;
    this.vp = vp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Term getVp() {
    return vp;
  }

  public void setVp(Term vp) {
    this.vp = vp;
  }

  @Override
  public String toString() {
    return name+"="+vp.toString();
  }

}
