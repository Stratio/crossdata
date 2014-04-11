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

public class CoreUtils {

    public static com.stratio.meta.common.data.ResultSet transformToMetaResultSet(ResultSet resultSet) {
        com.stratio.meta.common.data.CassandraResultSet crs = new com.stratio.meta.common.data.CassandraResultSet();
        for(Row row: resultSet.all()){
            com.stratio.meta.common.data.Row metaRow = new com.stratio.meta.common.data.Row();
            for (ColumnDefinitions.Definition def: row.getColumnDefinitions().asList()){
                if(def.getName().toLowerCase().startsWith("stratio")){
                    continue;
                }
                Cell metaCell = null;
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
                /*
                - ASCII     (1,  String.class),
                - BIGINT    (2,  Long.class),
                - BLOB      (3,  ByteBuffer.class),
                - BOOLEAN   (4,  Boolean.class),
                - COUNTER   (5,  Long.class),
                - DECIMAL   (6,  BigDecimal.class),
                - DOUBLE    (7,  Double.class),
                - FLOAT     (8,  Float.class),
                - INET      (16, InetAddress.class),
                - INT       (9,  Integer.class),
                - TEXT      (10, String.class),
                - TIMESTAMP (11, Date.class),
                - UUID      (12, UUID.class),
                - VARCHAR   (13, String.class),
                - VARINT    (14, BigInteger.class),
                - TIMEUUID  (15, UUID.class),
                LIST      (32, List.class),
                SET       (34, Set.class),
                MAP       (33, Map.class),
                CUSTOM    (0,  ByteBuffer.class);
                */
            }
            crs.add(metaRow);
        }
        return crs;
    }
}
