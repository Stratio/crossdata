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

import java.util.List;

import com.stratio.meta.core.utils.ParserUtils;

public class SelectorGroupBy extends SelectorMeta {

  private GroupByFunction gbFunction;
  private List<SelectorMeta> params;

  public SelectorGroupBy(GroupByFunction gbFunction, List<SelectorMeta> params) {
    this.type = SelectorMeta.TYPE_GROUPBY;
    this.gbFunction = gbFunction;
    this.params = params;
  }

  public GroupByFunction getGbFunction() {
    return gbFunction;
  }

  public void setGbFunction(GroupByFunction gbFunction) {
    this.gbFunction = gbFunction;
  }

  public List<SelectorMeta> getParams() {
    return params;
  }

  public void setParams(List<SelectorMeta> params) {
    this.params = params;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(gbFunction.name());
    sb.append("(").append(ParserUtils.stringList(params, ", ")).append(")");
    return sb.toString();
  }

}
