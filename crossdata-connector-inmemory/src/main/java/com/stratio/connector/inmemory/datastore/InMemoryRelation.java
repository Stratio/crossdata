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

/**
 * Relation to specify column comparisons.
 */
public class InMemoryRelation {

    /**
     * Column name.
     */
    private final String columnName;

    /**
     * Relationship.
     */
    private final InMemoryOperations relation;

    /**
     * Right part of the relation.
     */
    private final Object rightPart;

    /**
     * Class constructor.
     * @param columnName The column name.
     * @param relation The relationship.
     * @param rightPart The right part of the relation.
     */
    public InMemoryRelation(String columnName, InMemoryOperations relation, Object rightPart){
        this.columnName = columnName;
        this.relation = relation;
        this.rightPart = rightPart;
    }

    /**
     * Get the column name.
     * @return The name of the column.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Get the relation.
     * @return The relationship.
     */
    public InMemoryOperations getRelation() {
        return relation;
    }

    /**
     * Get the right part.
     * @return The right part of the relationship.
     */
    public Object getRightPart() {
        return rightPart;
    }
}
