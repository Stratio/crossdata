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
import com.stratio.meta.common.data.Cell;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreUtils {

    private static Map<String, Method> transformations = new HashMap<>();

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

    public Cell getCell(DataType type, Row r, String columnName) throws InvocationTargetException, IllegalAccessException {
        Method m = transformations.get(type.toString());
        Object value = m.invoke(r, columnName);
        return new Cell(type.asJavaClass(), value);
    }

    public com.stratio.meta.common.data.ResultSet transformToMetaResultSet(ResultSet resultSet) {
        com.stratio.meta.common.data.CassandraResultSet crs = new com.stratio.meta.common.data.CassandraResultSet();
        List<ColumnDefinitions.Definition> definitions = resultSet.getColumnDefinitions().asList();

        try {
            for(Row row: resultSet.all()){
                com.stratio.meta.common.data.Row metaRow = new com.stratio.meta.common.data.Row();
                for (ColumnDefinitions.Definition def: definitions){
                    if(def.getName().toLowerCase().startsWith("stratio")){
                        continue;
                    }
                    Cell metaCell = getCell(def.getType(), row, def.getName());
                    metaRow.addCell(def.getName(), metaCell);
                }
                crs.add(metaRow);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOG.error("Cannot transform result set", e);
            crs = new com.stratio.meta.common.data.CassandraResultSet();
        }
        return crs;
    }



    /*public static com.stratio.meta.common.data.ResultSet transformToMetaResultSet(ResultSet resultSet) {
        com.stratio.meta.common.data.CassandraResultSet crs = new com.stratio.meta.common.data.CassandraResultSet();

        List<ColumnDefinitions.Definition> definitions = resultSet.getColumnDefinitions().asList();

        for(Row row: resultSet.all()){
            com.stratio.meta.common.data.Row metaRow = new com.stratio.meta.common.data.Row();
            for (ColumnDefinitions.Definition def: definitions){
                if(def.getName().toLowerCase().startsWith("stratio")){
                    continue;
                }
                Cell metaCell = null;

                new Cell(defasdf as javaClass, eval("row.get"+def.getType()+()));

                Map<nombre,transformacion>();
                method=mimap.get(nombre);



                if((def.getType() == DataType.ascii())
                        || (def.getType() == DataType.text())
                        || (def.getType() == DataType.varchar())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getString(def.getName()));
                } else if ((def.getType() == DataType.bigint())
                        || (def.getType() == DataType.counter())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getLong(def.getName()));
                } else if ((def.getType() == DataType.cboolean())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getBool(def.getName()));
                } else if ((def.getType() == DataType.blob())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getBytes(def.getName()));
                } else if ((def.getType() == DataType.decimal())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getDecimal(def.getName()));
                } else if ((def.getType() == DataType.cdouble())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getDouble(def.getName()));
                } else if ((def.getType() == DataType.cfloat())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getFloat(def.getName()));
                } else if ((def.getType() == DataType.inet())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getInet(def.getName()));
                } else if ((def.getType() == DataType.cint())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getInt(def.getName()));
                } else if ((def.getType() == DataType.timestamp())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getDate(def.getName()));
                } else if ((def.getType() == DataType.uuid())
                        || (def.getType() == DataType.timeuuid())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getUUID(def.getName()));
                } else if ((def.getType() == DataType.varint())){
                    metaCell = new Cell(def.getType().asJavaClass(), row.getVarint(def.getName()));
                }
                metaRow.addCell(def.getName(), metaCell);
            }
            crs.add(metaRow);
        }
        return crs;
    }*/

}
