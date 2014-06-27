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

package com.stratio.meta.core.structures;

import com.stratio.meta.core.utils.ParserUtils;

import java.util.List;

public class SelectorFunction extends SelectorMeta {

  private String name;
  private List<SelectorMeta> params;

  public SelectorFunction(String name, List<SelectorMeta> params) {
    this.type = TYPE_FUNCTION;
    this.name = name;
    this.params = params;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SelectorMeta> getParams() {
    return params;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(name);
    sb.append("(").append(ParserUtils.stringList(params, ", ")).append(")");
    return sb.toString();
  }

  @Override
  public void addTablename(String tablename) {
    for(SelectorMeta param: params){
      param.addTablename(tablename);
    }
  }

}
