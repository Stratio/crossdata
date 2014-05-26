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

package com.stratio.meta.core.metadata;

import com.datastax.driver.core.DataType;
import com.stratio.meta.common.metadata.structures.ColumnType;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Cassandra mapping of META column types to their underlying Java
 * class implementations. The following conversions are considered:
 * <table>
 *     <tr><th>Cassandra Type</th><th>Cassandra Java class</th><th>META Column Type</th></tr>
 *     <tr><td>BIGINT</td><td>Long</td><td>BIGINT</td></tr>
 *     <tr><td>BOOLEAN</td><td>Boolean</td>BOOLEAN<td></td></tr>
 *     <tr><td>COUNTER</td><td>Long</td>NATIVE<td></td></tr>
 *     <tr><td>DOUBLE</td><td>Double</td><td>DOUBLE</td></tr>
 *     <tr><td>FLOAT</td><td>Float</td><td>FLOAT</td></tr>
 *     <tr><td>INT</td><td>Integer</td><td>INT</td></tr>
 *     <tr><td>TEXT</td><td>String</td><td>TEXT</td></tr>
 *     <tr><td>VARCHAR</td><td>String</td><td>VARCHAR</td></tr>
 *     <tr><td>TEXT</td><td>String</td><td>TEXT</td></tr>
 * </table>
 */
public class CassandraMetadataHelper extends AbstractMetadataHelper{

    /**
     * Mapping of native datatypes to SQL types
     */
    private static Map<DataType.Name, String> nativeODBCType = new HashMap<>();

    static {

        dbClass.put(ColumnType.BIGINT, Long.class);
        dbClass.put(ColumnType.BOOLEAN, Boolean.class);
        dbClass.put(ColumnType.DOUBLE, Double.class);
        dbClass.put(ColumnType.FLOAT, Float.class);
        dbClass.put(ColumnType.INT, Integer.class);
        dbClass.put(ColumnType.TEXT, String.class);
        dbClass.put(ColumnType.VARCHAR, String.class);

        dbType.put(ColumnType.BIGINT, "BIGINT");
        dbType.put(ColumnType.BOOLEAN, "BOOLEAN");
        dbType.put(ColumnType.DOUBLE, "DOUBLE");
        dbType.put(ColumnType.FLOAT, "FLOAT");
        dbType.put(ColumnType.INT, "INT");
        dbType.put(ColumnType.TEXT, "TEXT");
        dbType.put(ColumnType.VARCHAR, "VARCHAR");

        nativeODBCType.put(DataType.Name.COUNTER, "SQL_INTEGER");

    }

    /**
     * Mapping between Cassandra datatypes and META datatypes.
     */
    private static Map<String, ColumnType> typeMapping = new HashMap<>();

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(CassandraMetadataHelper.class.getName());

    /**
     * Class constructor.
     */
    public CassandraMetadataHelper(){
        for(Map.Entry<ColumnType, String> entry: dbType.entrySet()){
            typeMapping.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public ColumnType toColumnType(String dbTypeName) {
        ColumnType result = typeMapping.get(dbTypeName.toUpperCase());
        if(result == null) {
            try {
                DataType.Name cassandraType = DataType.Name.valueOf(dbTypeName);
                result = ColumnType.NATIVE;
                result.setDBMapping(cassandraType.name(), cassandraType.asJavaClass());
                result.setODBCType(nativeODBCType.get(cassandraType));
            }catch (IllegalArgumentException iae) {
                LOG.error("Invalid database type: " + dbTypeName, iae);
                result = null;
            }
        }else{
            result.setDBMapping(dbType.get(result), dbClass.get(result));
        }

        return result;
    }
}
