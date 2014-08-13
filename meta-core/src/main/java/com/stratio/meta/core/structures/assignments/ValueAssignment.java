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

package com.stratio.meta.core.structures.assignments;

import com.stratio.meta2.common.statements.structures.terms.Term;

/**
 * Class that contains the value assignment of the
 * {@link com.stratio.meta.core.statements.UpdateTableStatement}.
 */
public class ValueAssignment {

  public static final int TYPE_SIMPLE = 1;
  public static final int TYPE_COMPOUND = 2;

  /**
   * The value of the assignment.
   */
  private Term<?> term;

  /**
   * Assignation type.
   */
  private int type;

  /**
   * Class constructor.
   *
   * @param term The value of the assignment.
   */
  public ValueAssignment(Term<?> term) {
    this(term, TYPE_SIMPLE);
  }

  /**
   * Class constructor.
   * 
   * @param term The value of the assignment.
   * @param type The type of assignation.
   */
  public ValueAssignment(Term<?> term, int type) {
    this.term = term;
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public Term<?> getTerm() {
    return term;
  }

  @Override
  public String toString() {
    return term.toString();
  }

}
