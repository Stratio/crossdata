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

import com.stratio.meta2.common.statements.structures.selectors.Selector;

public class PropertyNameValue extends Property {

  private Selector name;
  private Selector vp;

  public PropertyNameValue() {
    super(TYPE_NAME_VALUE);
  }

  public PropertyNameValue(Selector name, Selector vp) {
    super(TYPE_NAME_VALUE);
    this.name = name;
    this.vp = vp;
  }

  public Selector getName() {
    return name;
  }

  public void setName(Selector name) {
    this.name = name;
  }

  public Selector getVp() {
    return vp;
  }

  public void setVp(Selector vp) {
    this.vp = vp;
  }

  @Override
  public String toString() {
    return name+"="+vp.toString();
  }

}
