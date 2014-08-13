/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.core.structures.assignments;

import com.stratio.meta2.common.statements.structures.terms.GenericTerm;
import com.stratio.meta2.common.statements.structures.terms.Term;

public class CompoundValueAssign extends ValueAssignment {

  private char operator;
  private GenericTerm genericTerm;

  /**
   * Class constructor.
   *
   * @param term The value of the assignment.
   */
  public CompoundValueAssign(Term<?> term,
                             char operator,
                             GenericTerm genericTerm) {
    super(term, TYPE_COMPOUND);
    this.operator = operator;
    this.genericTerm = genericTerm;
  }

}
