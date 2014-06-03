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

public class SelectorGroupBy extends SelectorMeta {

  private GroupByFunction gbFunction;
  private SelectorMeta param;

  public SelectorGroupBy(GroupByFunction gbFunction, SelectorMeta param) {
    this.type = SelectorMeta.TYPE_GROUPBY;
    this.gbFunction = gbFunction;
    this.param = param;
  }

  public GroupByFunction getGbFunction() {
    return gbFunction;
  }

  public void setGbFunction(GroupByFunction gbFunction) {
    this.gbFunction = gbFunction;
  }

  public SelectorMeta getParam() {
    return param;
  }

  public void setParam(SelectorMeta param) {
    this.param = param;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(gbFunction.name());
    sb.append("(").append(param).append(")");
    return sb.toString();
  }

  @Override
  public void addTablename(String tablename) {
    param.addTablename(tablename);
  }

}
