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

package com.stratio.meta.rest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ColumnDefinition;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.rest.models.ResultSet;
import com.stratio.meta2.common.metadata.ColumnType;

public class RestServerTestUtils {

    public static Row createDemoRow(String name, String gender, String email, int age, Boolean bool,
            String phrase) {
        Row r = new Row();
        if (!name.isEmpty()) {
            r.addCell("name", new Cell(name));
        }
        if (!gender.isEmpty()) {
            r.addCell("gender", new Cell(gender));
        }
        if (!email.isEmpty()) {
            r.addCell("email", new Cell(email));
        }
        if (age > 0) {
            r.addCell("age", new Cell(age));
        }
        if(bool!=null){
            if (bool.equals(true) || bool.equals(false)) {
                r.addCell("bool", new Cell(bool));
            }
        }
        if (!phrase.isEmpty()) {
            r.addCell("phrase", new Cell(phrase));
        }
        return r;

    }

    public static ResultSet toResultSet(ArrayList<Row> rows, Map<String, ColumnDefinition> map) {
        ResultSet rs = new ResultSet();
        rs.setRows(rows);
        List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
        for (Entry<String, ColumnDefinition> data : map.entrySet()) {
            String col_name = data.getValue().getDatatype().getName();
            String key = data.getKey();
            ColumnMetadata aux = new ColumnMetadata("Deep", key, ColumnType.VARCHAR);
            columns.add(aux);
        }
        rs.setColumnMetadata(columns);
        return rs;
    }

    public static boolean assertEqualsResultSets(ResultSet r1, ResultSet r2) {

        ArrayList<Row> rowsList1 = new ArrayList<Row>();
        ArrayList<Row> rowsList2 = new ArrayList<Row>();

        for (Row r : r1.getRows()) {
            rowsList1.add(r);
        }
        for (Row r : r2.getRows()) {
            rowsList2.add(r);
        }

        if (rowsList1.size() != rowsList2.size()) {
            return false;
        }

        for (int i = 0; i < rowsList1.size(); i++) {
            if (!assertEqualsRows(rowsList1.get(i), rowsList2.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean assertEqualsRowsArrayList(List<Row> rowsList1, ArrayList<Row> rowsList2) {
        boolean existsEqualRow = false;
        if (rowsList1.size() != rowsList2.size()) {
            return false;
        }

        for (int i = 0; i < rowsList1.size(); i++) {
            for (int j = 0; i < rowsList2.size(); j++) {
                if (assertEqualsRows(rowsList1.get(i), rowsList2.get(j))) {
                    existsEqualRow = true;
                    break;
                }
            }
            if (!existsEqualRow) {
                return false;
            }
        }

        return true;
    }

    public static boolean assertEqualsRows(Row r1, Row r2) {

        for (Entry<String, Cell> e : r1.getCells().entrySet()) {
            System.out.println(e.getKey());
            Cell c = r2.getCell(e.getKey());
            if (c == null) {
                System.out.println(e.getKey());
                return false;
            } else {
                if (!assertEqualsCells(c, e.getValue())) {
                    System.out.println(e.getValue().getValue());
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean assertEqualsCells(Cell c1, Cell c2) {
        return (c1.getValue().equals(c2.getValue()));
    }

}
