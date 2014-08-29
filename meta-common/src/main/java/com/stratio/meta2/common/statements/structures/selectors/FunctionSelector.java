/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.common.statements.structures.selectors;

import com.stratio.meta2.common.data.ColumnName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Selector composed by a function and the list of columns required by the function.
 */
public class FunctionSelector extends Selector{

  /**
   * Name of the function.
   */
  private final String functionName;

  /**
   * List of columns.
   */
  private List<Selector> functionColumns;

  /**
   * Class constructor.
   * @param functionName Name of the function.
   */
  public FunctionSelector(String functionName, List<Selector> functionColumns) {
    this.functionName = functionName;
    this.functionColumns = functionColumns;
  }

  /**
   * This function determines whether the target function could be used with a group by clause.
   * Allowed functions are: SUM, MAX, MIN, AVG, COUNT
   * @return Whether it could be used or not.
   */
  public boolean isGroupByFunction(){
    if("sum".equalsIgnoreCase(functionName)
       || "max".equalsIgnoreCase(functionName)
       || "min".equalsIgnoreCase(functionName)
       || "avg".equalsIgnoreCase(functionName)
       || "count".equalsIgnoreCase(functionName)){
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(functionName);
    sb.append("(");
    Iterator<Selector> selectors = functionColumns.iterator();
    while(selectors.hasNext()){
      sb.append(selectors.next().toString());
      if(selectors.hasNext()){
        sb.append(", ");
      }
    }
    sb.append(")");
    if(this.alias != null){
      sb.append(" AS ").append(alias);
    }
    return sb.toString();
  }

  public List<Selector> getFunctionColumns() {
    return functionColumns;
  }

}
