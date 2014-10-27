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

package com.stratio.crossdata.common.metadata;

/**
 * Types of columns supported by META with their equivalence in ODBC data types. Notice that a
 * NATIVE type has been added to map those types that are not generic and database dependant.
 */
public enum ColumnType {
    BIGINT("SQL_BIGINT"), BOOLEAN("BOOLEAN"), DOUBLE("SQL_DOUBLE"), FLOAT("SQL_FLOAT"),
    INT("SQL_INTEGER"), TEXT("SQL_VARCHAR"), VARCHAR("SQL_VARCHAR"), NATIVE(null),
    SET("SET") {
        @Override
        public String toString() {
            return "SET<" + getDBInnerType() + ">";
        }
    },
    LIST("LIST") {
        @Override
        public String toString() {
            return "LIST<" + getDBInnerType() + ">";
        }
    },
    MAP("MAP") {
        @Override
        public String toString() {
            return "MAP<" + getDBInnerType() + ", " + getDBInnerValueType() + ">";
        }
    };

    /**
     * ODBC type equivalent.
     */
    private String odbcType;

    /**
     * The database type.
     */
    private String dbType;

    /**
     * The underlying class.
     */
    private Class<?> dbClass;

    /**
     * The underlying database type for collections.
     */
    private ColumnType dbInnerType;

    /**
     * The underlying database value type for map-like collections.
     */
    private ColumnType dbInnerValueType;

    /**
     * Build a new column type.
     *
     * @param odbcType The ODBC equivalent type.
     */
    ColumnType(String odbcType) {
        this.odbcType = odbcType;
    }

    /**
     * Set the database implementation mapping.
     *
     * @param dbType  The String representation of the database equivalent type.
     * @param dbClass The underlying class implementation.
     */
    public void setDBMapping(String dbType, Class<?> dbClass) {
        this.dbType = dbType;
        this.dbClass = dbClass;
    }

    /**
     * Get the database type.
     *
     * @return The type.
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Get the database class.
     *
     * @return The class.
     */
    public Class<?> getDbClass() {
        return dbClass;
    }

    /**
     * Get the ODBC SQL type associated with the META data type. For NATIVE types, use the appropriate
     * AbstractMetadataHelper to retrieve the ODBC equivalent.
     *
     * @return The ODBC equivalence or null if NATIVE type is being used.
     */
    public String getODBCType() {
        return odbcType;
    }

    /**
     * Set the ODBCType. This method should only be used with NATIVE column types.
     *
     * @param odbcType The ODBC equivalent type.
     */
    public void setODBCType(String odbcType) {
        this.odbcType = odbcType;
    }

    public void setDBCollectionType(ColumnType dbInnerType) {
        this.dbInnerType = dbInnerType;
    }

    public void setDBMapType(ColumnType keyType, ColumnType valueType) {
        this.dbInnerType = keyType;
        this.dbInnerValueType = valueType;
    }

    public ColumnType getDBInnerType() {
        return dbInnerType;
    }

    public ColumnType getDBInnerValueType() {
        return dbInnerValueType;
    }

}
