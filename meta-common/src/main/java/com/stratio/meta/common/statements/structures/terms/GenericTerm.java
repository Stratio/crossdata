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

package com.stratio.meta.common.statements.structures.terms;

public abstract class GenericTerm<T extends Comparable<T>> {

  public static final int SIMPLE_TERM = 1;
  public static final int COLLECTION_TERMS = 2;

  protected int type;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  /**
   * Get the String value representation.
   *
   * @return The String value.
   */
  @Override
  public abstract String toString();

}
