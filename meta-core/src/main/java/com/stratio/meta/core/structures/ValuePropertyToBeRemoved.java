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
 */package com.stratio.meta.core.structures;

public abstract class ValuePropertyToBeRemoved {

  public static final int TYPE_IDENT = 1;
  public static final int TYPE_CONST = 2;
  public static final int TYPE_MAPLT = 3;
  public static final int TYPE_FLOAT = 4;
  public static final int TYPE_BOOLEAN = 5;
  public static final int TYPE_LITERAL = 6;

  protected int type;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public abstract String toString();

  public abstract String getStringValue();

}
