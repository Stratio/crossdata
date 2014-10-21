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

package com.stratio.crossdata.common.statements.structures.relationships;

/**
 * Operators supported in an {@link com.stratio.crossdata.common.statements.structures.relationships.Relation}.
 */
public enum Operator {
    ADD(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "+";
        }
    },
    SUBTRACT(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "-";
        }
    },
    DIVISION(Group.ARITHMETIC) {
        @Override
        public String toString() {
            return "/";
        }
    },
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
    MATCH(Group.COMPARATOR) {
        public String toString() {
            return "MATCH";
        }
    },
    GT(Group.COMPARATOR) {
        public String toString() {
            return ">";
        }
    },
    LT(Group.COMPARATOR) {
        public String toString() {
            return "<";
        }
    },
    GET(Group.COMPARATOR) {
        public String toString() {
            return ">=";
        }
    },
    LET(Group.COMPARATOR) {
        public String toString() {
            return "<=";
        }
    },
    DISTINCT(Group.COMPARATOR) {
        public String toString() {
            return "<>";
        }
    },
    LIKE(Group.COMPARATOR) {
        public String toString() {
            return "LIKE";
        }
    };

    public enum Group {
        COMPARATOR,
        ARITHMETIC;
    }

    private Group group;

    Operator(Group group) {
        this.group = group;
    }

    public boolean isInGroup(Group group) {
        return this.group == group;
    }

}
