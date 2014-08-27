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

package com.stratio.meta.common.statements.structures.selectors;

import com.stratio.meta2.common.data.TableName;

import java.io.Serializable;

public class SelectorGroupBy extends SelectorMeta implements Serializable {

  private static final long serialVersionUID = 7595223293190216610L;

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
  public void addTablename(TableName tablename) {
    param.addTablename(tablename);
  }

}
