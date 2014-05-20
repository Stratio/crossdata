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

package com.stratio.meta.core.utils;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ColumnDefinition;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreUtils {

    public static final List<String> supportedTypes = Arrays.asList("bigint", "boolean", "counter",
            "double", "float", "int", "varchar");
    // SOON: "date", "uuid", "timeuuid"

    /**
     * Map of methods required to transform a {@link com.datastax.driver.core.DataType} into the
     * corresponding object.
     */
    private static Map<String, Method> transformations = new HashMap<>();

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(CoreUtils.class);

    static{
        try {
            transformations.put(DataType.ascii().toString(), Row.class.getMethod("getString", String.class));
            transformations.put(DataType.text().toString(), Row.class.getMethod("getString", String.class));
            transformations.put(DataType.varchar().toString(), Row.class.getMethod("getString", String.class));
            transformations.put(DataType.bigint().toString(), Row.class.getMethod("getLong", String.class));
            transformations.put(DataType.counter().toString(), Row.class.getMethod("getLong", String.class));
            transformations.put(DataType.cboolean().toString(), Row.class.getMethod("getBool", String.class));
            transformations.put(DataType.blob().toString(), Row.class.getMethod("getBytes", String.class));
            transformations.put(DataType.decimal().toString(), Row.class.getMethod("getDecimal", String.class));
            transformations.put(DataType.cdouble().toString(), Row.class.getMethod("getDouble", String.class));
            transformations.put(DataType.cfloat().toString(), Row.class.getMethod("getFloat", String.class));
            transformations.put(DataType.inet().toString(), Row.class.getMethod("getInet", String.class));
            transformations.put(DataType.cint().toString(), Row.class.getMethod("getInt", String.class));
            transformations.put(DataType.timestamp().toString(), Row.class.getMethod("getDate", String.class));
            transformations.put(DataType.uuid().toString(), Row.class.getMethod("getUUID", String.class));
            transformations.put(DataType.timeuuid().toString(), Row.class.getMethod("getUUID", String.class));
            transformations.put(DataType.varint().toString(), Row.class.getMethod("getVarint", String.class));
        } catch (NoSuchMethodException e) {
            LOG.error("Cannot create transformation map", e);
        }
    }

    /**
     * Get a {@link com.stratio.meta.common.data.Cell} with the column contents of a Row.
     * @param type The {@link com.datastax.driver.core.DataType} of the column.
     * @param r The row that contains the column.
     * @param columnName The column name.
     * @return A {@link com.stratio.meta.common.data.Cell} with the contents.
     * @throws InvocationTargetException If the required method cannot be invoked.
     * @throws IllegalAccessException If the method cannot be accessed.
     */
    protected Cell getCell(DataType type, Row r, String columnName) throws InvocationTargetException, IllegalAccessException {
        Method m = transformations.get(type.toString());
        Object value = m.invoke(r, columnName);
        return new Cell(value);
    }

    /**
     * Transforms a Cassandra {@link com.datastax.driver.core.ResultSet} into a {@link com.stratio.meta.common.data.ResultSet}.
     * @param resultSet The input Cassandra resultset.
     * @return An equivalent Meta ResultSet
     */
    public com.stratio.meta.common.data.ResultSet transformToMetaResultSet(ResultSet resultSet) {
        CassandraResultSet crs = new CassandraResultSet();
        List<ColumnDefinitions.Definition> definitions = resultSet.getColumnDefinitions().asList();

        boolean firstRow = true;

        try {
            Map colDefs = new HashMap<String, ColumnDefinition>();
            for(Row row: resultSet.all()){
                com.stratio.meta.common.data.Row metaRow = new com.stratio.meta.common.data.Row();
                for (ColumnDefinitions.Definition def: definitions){
                    if(def.getName().toLowerCase().startsWith("stratio")){
                        continue;
                    }
                    Cell metaCell = getCell(def.getType(), row, def.getName());
                    metaRow.addCell(def.getName(), metaCell);

                    if(firstRow){
                        colDefs.put(def.getName(), new ColumnDefinition(def.getType().asJavaClass()));
                    }
                }
                if(firstRow){
                    crs.setColumnDefinitions(colDefs);
                    firstRow = false;
                }
                crs.add(metaRow);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOG.error("Cannot transform result set", e);
            crs = new CassandraResultSet();
        }
        return crs;
    }

}
