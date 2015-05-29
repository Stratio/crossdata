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

import java.io.Serializable;
import java.util.*;

import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;

/**
 * Types of columns supported by CROSSDATA with their equivalence in ODBC data types. Notice that a
 * NATIVE type has been added to map those types that are not generic and database dependant.
 */
public class ColumnType implements Serializable {
    /**
     * Class constructor.
     * @param dataType The {@link com.stratio.crossdata.common.metadata.DataType}
     */
    public ColumnType(DataType dataType) {
        this.dataType = dataType;
        switch (dataType){
        case BIGINT:
            this.odbcType = "SQL_BIGINT";
            break;
        case BOOLEAN:
            this.odbcType = "BOOLEAN";
            break;
        case DOUBLE:
            this.odbcType = "SQL_DOUBLE";
            break;
        case FLOAT:
            this.odbcType = "SQL_FLOAT";
            break;
        case INT:
            this.odbcType = "SQL_INTEGER";
            break;
        case TEXT:
            this.odbcType = "SQL_VARCHAR";
            break;
        case VARCHAR:
            this.odbcType = "SQL_VARCHAR";
            break;
        case NATIVE:
            this.odbcType = "NATIVE";
            break;
        case SET:
            this.odbcType = "SET";
            break;
        case LIST:
            this.odbcType = "LIST";
            break;
        case MAP:
            this.odbcType = "MAP";
            break;
        }
    }
    public ColumnType(DataType dataType, Map<String, List<String>>  columnProperties) {
        this(dataType);
        this.columnProperties = columnProperties;
    }


    private DataType dataType;

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
     * Database specific options for columns/fields.
     */
    private Map<String, List<String>>  columnProperties = new HashMap();

    /**
     * The underlying database value type for map-like collections.
     */
    private ColumnType dbInnerValueType;

    public DataType getDataType() {
        return dataType;
    }

    public String getOdbcType() {
        return odbcType;
    }

    public ColumnType getDbInnerType() {
        return dbInnerType;
    }

    public ColumnType getDbInnerValueType() {
        return dbInnerValueType;
    }

    public void addColumnProperty(String propertyName, String propertyValue){

       List properties = columnProperties.get(propertyName);
        if (properties == null){
            properties = new ArrayList();
            columnProperties.put(propertyName, properties);
        }
        properties.add(propertyValue);
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
        Class<?> clazz = dbClass;
        if(clazz == null){
            clazz = getJavaType();
        }
        return clazz;
    }

    /**
     * Get the ODBC SQL type associated with the CROSSDATA data type. For NATIVE types, use the appropriate
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

    /**
     * Set the dbInnerType.
     * @param dbInnerType The inner db type.
     */
    public void setDBCollectionType(ColumnType dbInnerType) {
        this.dbInnerType = dbInnerType;
    }

    /**
     * Set the dbInnerType and the dbInnerValueType.
     * @param keyType The db inner type.
     * @param valueType The db inner value.
     */
    public void setDBMapType(ColumnType keyType, ColumnType valueType) {
        this.dbInnerType = keyType;
        this.dbInnerValueType = valueType;
    }

    /**
     * Get the dbInnerType.
     * @return A crossdata {@link com.stratio.crossdata.common.metadata.ColumnType} .
     */
    public ColumnType getDBInnerType() {
        return dbInnerType;
    }

    /**
     * Get the dbInnerValueType.
     * @return A crossdata {@link com.stratio.crossdata.common.metadata.ColumnType} .
     */
    public ColumnType getDBInnerValueType() {
        return dbInnerValueType;
    }

    /**
     * Set de data base original type.
     * @param dbType The data base type.
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Get the columnProperties.
     * @return a {@link java.util.Map<String, java.util.List<String>>} where the key is the
     * property name, and the value is a List of values of this property.
     */
    public Map<String, List<String>> getColumnProperties() {
        return columnProperties;
    }

    /**
     * Set the columnProperties map.
     * @param columnProperties
     */
    public void setColumnProperties(Map<String, List<String>> columnProperties) {
        this.columnProperties = columnProperties;
    }

    /**
     * Get the java class that is the equivalent with the crossdata enum column type.
     * @return The java class.
     */
    public Class getJavaType(){
        Class clazz = null;
        switch (dataType){
        case BIGINT:
            clazz = Long.class;
            break;
        case BOOLEAN:
            clazz = Boolean.class;
            break;
        case DOUBLE:
            clazz = Double.class;
            break;
        case FLOAT:
            clazz = Float.class;
            break;
        case INT:
            clazz = Integer.class;
            break;
        case TEXT:
            clazz = String.class;
            break;
        case VARCHAR:
            clazz = String.class;
            break;
        case NATIVE:
            clazz = Object.class;
            break;
        case SET:
            clazz = Set.class;
            break;
        case LIST:
            clazz = List.class;
            break;
        case MAP:
            clazz = Map.class;
            break;
        }
        return clazz;
    }

    /**
     * Get the crossdata type that is the equivalent with the crossdata enum column type.
     * @return A string with the crossdata type name.
     */
    public String getCrossdataType(){
        String clazz = null;
        switch (dataType){
        case BIGINT:
            clazz = "BigInt";
            break;
        case BOOLEAN:
            clazz = "Boolean";
            break;
        case DOUBLE:
            clazz = "Double";
            break;
        case FLOAT:
            clazz = "Float";
            break;
        case INT:
            clazz = "Int";
            break;
        case TEXT:
            clazz = "Text";
            break;
        case VARCHAR:
            clazz = "Varchar";
            break;
        case NATIVE:
            clazz = "Native";
            break;
        case SET:
            clazz = "Set";
            break;
        case LIST:
            clazz = "List";
            break;
        case MAP:
            clazz = "Map";
            break;
        }
        return clazz;
    }

    /**
     * Create a Selector with the dataType of the class.
     * @return A type {@link com.stratio.crossdata.common.statements.structures.Selector} of the given dataType.
     */
    public Selector createSelector() {
        Selector selector = null;
        switch (dataType){
        case BIGINT:
            selector = new IntegerSelector(0);
            break;
        case BOOLEAN:
            selector = new BooleanSelector(false);
            break;
        case DOUBLE:
            selector = new FloatingPointSelector(0);
            break;
        case FLOAT:
            selector = new FloatingPointSelector(0);
            break;
        case INT:
            selector = new IntegerSelector(0);
            break;
        case TEXT:
            selector = new StringSelector("");
            break;
        case VARCHAR:
            selector = new StringSelector("");
            break;
        case NATIVE:
            selector = new StringSelector("");
            break;
        case SET:
            break;
        case LIST:
            break;
        case MAP:
            break;
        }
        return selector;
    }

    /**
     * Get a ColumnType of a name of a dataType.
     * @param name The name of the dataType.
     * @return A {@link com.stratio.crossdata.common.metadata.ColumnType}.
     */
    public static ColumnType valueOf(String name){
        return new ColumnType(DataType.valueOf(name));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(dataType.toString());
        switch (dataType){
        case BIGINT:
        case BOOLEAN:
        case DOUBLE:
        case FLOAT:
        case INT:
        case TEXT:
        case VARCHAR:
        case NATIVE:
            break;
        case SET:
        case LIST:
            sb.append("<").append(getDBInnerType()).append(">");
            break;
        case MAP:
            sb.append("<").append(getDBInnerType()).append(", ").append(getDBInnerValueType()).append(">");
            break;
        }

        if (columnProperties != null && !columnProperties.isEmpty()){
            sb.append("(");
            String comma = "";
            for(String propertieName:columnProperties.keySet()){
                for(String propertyValue:columnProperties.get(propertieName)){
                    sb.append(comma).append(propertieName).append(":").append(propertyValue);
                    comma = ", ";
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ColumnType that = (ColumnType) o;

        if (dataType != that.dataType) {
            return false;
        }
        if (dbClass != null ? !dbClass.equals(that.dbClass) : that.dbClass != null) {
            return false;
        }
        if (dbInnerType != null ? !dbInnerType.equals(that.dbInnerType) : that.dbInnerType != null) {
            return false;
        }
        if (dbInnerValueType != null ?
                !dbInnerValueType.equals(that.dbInnerValueType) :
                that.dbInnerValueType != null) {
            return false;
        }
        if (dbType != null ? !dbType.equals(that.dbType) : that.dbType != null) {
            return false;
        }
        if (odbcType != null ? !odbcType.equals(that.odbcType) : that.odbcType != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataType != null ? dataType.hashCode() : 0;
        result = 31 * result + (odbcType != null ? odbcType.hashCode() : 0);
        result = 31 * result + (dbType != null ? dbType.hashCode() : 0);
        result = 31 * result + (dbClass != null ? dbClass.hashCode() : 0);
        result = 31 * result + (dbInnerType != null ? dbInnerType.hashCode() : 0);
        result = 31 * result + (dbInnerValueType != null ? dbInnerValueType.hashCode() : 0);
        return result;
    }
}
