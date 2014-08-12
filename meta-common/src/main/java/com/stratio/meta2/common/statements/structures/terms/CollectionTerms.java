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

package com.stratio.meta2.common.statements.structures.terms;

import java.io.Serializable;

public abstract class CollectionTerms extends GenericTerm implements Serializable {

  protected Class collectionClass;
  protected Object terms;

  protected CollectionTerms() {
    type = GenericTerm.COLLECTION_TERMS;
  }

  /**
   * Get the Iterable Java Class.
   *
   * @return A {@link java.lang.Class}.
   */
  public Class getCollectionClass() {
    return collectionClass;
  }

  /**
   * Get the collection values.
   *
   * @return A {@link java.util.Collection} with the values.
   */
  public Object getTerms() {
    return terms;
  }

  public void setTerms(Object terms){
    this.terms = terms;
  }

  public abstract void addTerm(Term... term);

  public abstract void deleteTerm(Term term);

  public abstract void clearTerms();

  public abstract int size();

  public abstract boolean isEmpty();

  public abstract boolean containsKey(StringTerm key);

}
