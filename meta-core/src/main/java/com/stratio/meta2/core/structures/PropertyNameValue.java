/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta2.core.structures;

import com.stratio.meta.common.statements.structures.terms.Term;

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
