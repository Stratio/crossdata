/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.metadata.structures;

/**
 * Types of columns supported by META with their equivalence in
 * ODBC data types. Notice that a NATIVE type has been added to map
 * those types that are not generic and database dependant.
 */
public enum ColumnType {
    BIGINT("SQL_BIGINT"),
    BOOLEAN("BOOLEAN"),
    DOUBLE("SQL_DOUBLE"),
    FLOAT("SQL_FLOAT"),
    INT("SQL_INTEGER"),
    TEXT("SQL_VARCHAR"),
    VARCHAR("SQL_VARCHAR"),
    NATIVE(null);

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
     * Build a new column type.
     * @param odbcType The ODBC equivalent type.
     */
    ColumnType(String odbcType) {
        this.odbcType = odbcType;
    }

    /**
     * Set the database implementation mapping.
     * @param dbType The String representation of the database equivalent type.
     * @param dbClass The underlying class implementation.
     */
    public void setDBMapping(String dbType, Class<?> dbClass){
        this.dbType = dbType;
        this.dbClass = dbClass;
    }

    /**
     * Get the database type.
     * @return The type.
     */
    public String getDbType(){
        return dbType;
    }

    /**
     * Get the database class.
     * @return The class.
     */
    public Class<?> getDbClass() {
        return dbClass;
    }

    /**
     * Set the ODBCType. This method should only be used
     * with NATIVE column types.
     * @param odbcType The ODBC equivalent type.
     */
    public void setODBCType(String odbcType){
        this.odbcType = odbcType;
    }

    /**
     * Get the ODBC SQL type associated with the META data type. For
     * NATIVE types, use the appropriate AbstractMetadataHelper to retrieve
     * the ODBC equivalent.
     * @return The ODBC equivalence or null if NATIVE type is being
     * used.
     */
    public String getODBCType(){
        return odbcType;
    }
}
