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

package com.stratio.meta2.common.statements.structures.terms;

import com.stratio.meta.common.statements.structures.relationships.Operator;

import java.io.Serializable;
import java.util.Iterator;

public abstract class Term<T extends Comparable<T>> extends GenericTerm implements Comparable<T>,
                                                                                    Serializable {

  private static final long serialVersionUID = -4258938152892510227L;

  protected Class<? extends Comparable<?>> comparableClass;
  protected T value;

  public Term(Class<? extends Comparable<?>> comparableClass, T value) {
    type = GenericTerm.SIMPLE_TERM;
    this.comparableClass = comparableClass;
    this.value = value;
  }

  /**
   * Get the Term Java Class.
   *
   * @return A {@link java.lang.Class}.
   */
  public Class<? extends Comparable<?>> getTermClass() {
    return comparableClass;
  }

  /**
   * Get the term value.
   *
   * @return A {@link java.lang.Object} with the value.
   */
  public T getTermValue() {
    return value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(value.toString());
    if(hasCompoundTerms()){
      Iterator<GenericTerm> termsIter = getCompoundTerms().iterator();
      for(Operator operator: getValueOperators()){
        GenericTerm gTerm = termsIter.next();
        sb.append(" ").append(operator).append(" ").append(gTerm.toString());
        if(termsIter.hasNext()){
          sb.append(", ");
        }
      }
    }
    return sb.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(T o) {
    return this.value.compareTo(o);
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of hash
   * tables such as those provided by {@link java.util.HashMap}.
   *
   * @return a hash code value for this object.
   * @see Object#equals(Object)
   * @see System#identityHashCode
   */
  @Override
  public int hashCode() {
    return comparableClass.hashCode() * this.getTermValue().hashCode();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p/>
   * The {@code equals} method implements an equivalence relation on non-null object references:
   *
   * @param obj the reference object with which to compare.
   * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
   * @see #hashCode()
   * @see java.util.HashMap
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(this.comparableClass.isInstance(obj))) {
      return super.equals(obj);
    }
    return this.value.equals((T) obj);
  }

  public boolean isConstant(){
    return ((comparableClass == Integer.class) || (comparableClass == Long.class));
  }

  public boolean isDecimal(){
    return ((comparableClass == Double.class) || (comparableClass == Float.class));
  }

  public boolean isNumber(){
    return (isConstant() || isDecimal());
  }

}
