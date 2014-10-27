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

package com.stratio.crossdata.common.statements.structures;

/**
 * Operators supported in an {@link Relation}.
 */
//TODO:javadoc for class methods
public enum Operator {

    /**
     * Addition operator.
     */
    ADD(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "+";
        }
    },

    /**
     * Substraction operator.
     */
    SUBTRACT(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "-";
        }
    },

    /**
     * Division operator.
     */
    DIVISION(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "/";
        }
    },

    /**
     * Multiplication operator.
     */
    MULTIPLICATION(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "*";
        }
    },

    /**
     * Constant to define inclusion relationships.
     */
    IN(Group.COMPARATOR) {
        @Override
        public String toString() {
            return "IN";
        }
    },

    /**
     * Constant to define range comparisons.
     */
    BETWEEN(Group.COMPARATOR) {
        @Override
        public String toString() {
            return "BETWEEN";
        }
    },

    /**
     * Constant to define compare relationships (e.g., >, <, =, etc.).
     */
    EQ(Group.COMPARATOR) {
        @Override
        public String toString() {
            return "=";
        }
    },

    /**
     * Assign relationship for update-like statements.
     */
    ASSIGN(Group.ARITHMETIC) {
        public String toString() {
            return "=";
        }
    },

    /**
     * Match comparator for full-text search queries.
     */
    MATCH(Group.COMPARATOR) {
        public String toString() {
            return "MATCH";
        }
    },

    /**
     * Greater than comparator.
     */
    GT(Group.COMPARATOR) {
        public String toString() {
            return ">";
        }
    },

    /**
     * Less than comparator.
     */
    LT(Group.COMPARATOR) {
        public String toString() {
            return "<";
        }
    },

    /**
     * Greater or equal than comparator.
     */
    GET(Group.COMPARATOR) {
        public String toString() {
            return ">=";
        }
    },

    /**
     * Less or equal than compartor.
     */
    LET(Group.COMPARATOR) {
        public String toString() {
            return "<=";
        }
    },

    /**
     * Distinct comparator.
     */
    DISTINCT(Group.COMPARATOR) {
        public String toString() {
            return "<>";
        }
    },

    /**
     * Like comparator.
     */
    LIKE(Group.COMPARATOR) {
        public String toString() {
            return "LIKE";
        }
    };

    /**
     * Enumeration with the groups of operators.
     */
    public enum Group {

        /**
         * Operators used in relationships.
         */
        COMPARATOR,

        /**
         * Arithmetic operators.
         */
        ARITHMETIC;
    }

    /**
     * The enum group.
     */
    private Group group;

    /**
     * Enum constructor.
     *
     * @param group The group the enum belongs to.
     */
    Operator(Group group) {
        this.group = group;
    }

    /**
     * Determine whether the operator is in a particular group.
     * @param group The group.
     * @return Whether it belongs to the group.
     */
    public boolean isInGroup(Group group) {
        return this.group == group;
    }

}
