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

package com.stratio.connector.inmemory.datastore;

import java.math.BigDecimal;
import java.math.BigInteger;

import sun.security.util.BigInt;

/**
 * Enumeration of the different operations that can be applied
 */
public enum InMemoryOperations {
    /**
     * Equal.
     */
    EQ {
        @Override public boolean compare(Object o1, Object o2) {
            if(Number.class.isInstance(o1) && Number.class.isInstance(o2)){
               return compareNumbers(Number.class.cast(o1), Number.class.cast(o2)) == 0;
            }
            return o1.equals(o2);
        }
    },

    /**
     * Greater than.
     */
    GT {
        @Override public boolean compare(Object o1, Object o2) {
            if(Number.class.isInstance(o1) && Number.class.isInstance(o2)){
                return compareNumbers(Number.class.cast(o1), Number.class.cast(o2)) > 0;
            }else if(Comparable.class.isInstance(o1)
                    && Comparable.class.isInstance(o2.getClass())) {
                return Comparable.class.cast(o1).compareTo(Comparable.class.cast(o2)) > 0;
            }
            return false;
        }
    },

    /**
     * Less than.
     */
    LT {
        @Override public boolean compare(Object o1, Object o2) {
            if(Number.class.isInstance(o1) && Number.class.isInstance(o2)){
                return compareNumbers(Number.class.cast(o1), Number.class.cast(o2)) < 0;
            }else if(Comparable.class.isInstance(o1)
                    && Comparable.class.isInstance(o2.getClass())) {
                return Comparable.class.cast(o1).compareTo(Comparable.class.cast(o2)) < 0;
            }
            return false;
        }
    },

    /**
     * Greater or equal than.
     */
    GET {
        @Override public boolean compare(Object o1, Object o2) {
            if(Number.class.isInstance(o1) && Number.class.isInstance(o2)){
                return compareNumbers(Number.class.cast(o1), Number.class.cast(o2)) >= 0;
            }else if(Comparable.class.isInstance(o1)
                    && Comparable.class.isInstance(o2.getClass())) {
                return Comparable.class.cast(o1).compareTo(Comparable.class.cast(o2)) >= 0;
            }
            return false;
        }
    },

    /**
     * Less or equal than.
     */
    LET {
        @Override public boolean compare(Object o1, Object o2) {
            if(Number.class.isInstance(o1) && Number.class.isInstance(o2)){
                return compareNumbers(Number.class.cast(o1), Number.class.cast(o2)) <= 0;
            }else if(Comparable.class.isInstance(o1)
                    && Comparable.class.isInstance(o2.getClass())) {
                return Comparable.class.cast(o1).compareTo(Comparable.class.cast(o2)) <= 0;
            }
            return false;
        }
    };

    public abstract boolean compare(Object o1, Object o2);

    /**
     * Return the comparison between two values.
     * @param n1 First number.
     * @param n2 Second number.
     * @return Distance according to the {@link java.lang.Comparable} semantics.
     */
    private static int compareNumbers(Number n1, Number n2){
        return toBigDecimal(n1).compareTo(toBigDecimal(n2));
    }

    /**
     * Transform a number into a BigDecimal for comparison reasons.
     * @param n The number.
     * @return A {@link java.math.BigDecimal}.
     */
    private static BigDecimal toBigDecimal(Number n){
        BigDecimal result = null;
        if(BigDecimal.class.isInstance(n)){
            result = (BigDecimal)n;
        }else if(BigInteger.class.isInstance(n)){
            result = new BigDecimal((BigInteger) n);
        }else if(Integer.class.isInstance(n) || Long.class.isInstance(n)){
            result = new BigDecimal(n.longValue());
        }else if(Float.class.isInstance(n) || Double.class.isInstance(n)){
            result = new BigDecimal(n.doubleValue());
        }
        return result;
    }
}
