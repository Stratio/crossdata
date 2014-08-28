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

package com.stratio.meta2.common.data;



public abstract class Name  {

  protected final String UNKNOWN_NAME="<UNKNOWN_NAME>";

  public abstract boolean isCompletedName();

  public abstract String getQualifiedName();

  public abstract NameType getType();

  @Override
  public String toString() {
    return this.getQualifiedName();
  }

  @Override
  public boolean equals(Object o){
    boolean result=false;
    if(o instanceof Name) {
      Name name= (Name)o;
      result = this.toString().equals(name.toString()) && this.getType()==name.getType();
    }
    return result;
  }
}
