/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.tests.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.Decimal;
import org.apache.spark.sql.types.StructField;
import org.assertj.core.api.AbstractAssert;

import com.stratio.crossdata.common.SQLResult;
import com.stratio.crossdata.common.result.SuccessfulQueryResult;

/**
 * Created by hdominguez on 19/10/15.
 */
public class SQLResultAssert extends AbstractAssert<SQLResultAssert, SQLResult>{

    /**
     * Generic constructor.
     *
     * @param actual
     */
    public SQLResultAssert(SQLResult actual) {
        super(actual, SQLResultAssert.class);
    }

    /**
     * Checks the "DataFrame".
     *
     * @param actual
     * @return DataFrameAssert
     */
    public static SQLResultAssert asserThat(SQLResult actual){
        return new SQLResultAssert(actual);
    }

    public SQLResultAssert hasLength(int length){
        if(actual.resultSet().length != length){
            failWithMessage("Expected DataFrame length to be <%s> but was <%s>", length, actual.resultSet().length);
        }
        return this;
    }

    public SQLResultAssert isSuccesfullResult(){
        if(!(actual instanceof SuccessfulQueryResult)){
            failWithMessage("Expected a successfull result but the obtained result is an ErrorResult");
        }
        return this;
    }

    public SQLResultAssert isErrorResult(){
        if(!(actual instanceof SuccessfulQueryResult)){
            failWithMessage("Expected a ErrorResult but the obtained result is an SuccessfulQueryResult");
        }
        return this;
    }

    public SQLResultAssert assertSuccesfulMetadataResult(List<String> firstRow){
        SuccessfulQueryResult result = (SuccessfulQueryResult)actual;
        StructField[] actualStructField = result.schema().fields();
        if(actualStructField.length != firstRow.size()){
            failWithMessage("Expected number of columns to be <%s> but was <%s>", firstRow.size(), actualStructField
                    .length);
        }
        for(int i = 0; i < firstRow.size(); i++){
            String[] columnExpected = firstRow.get(i).split("-");
            if(!columnExpected[0].equals(actualStructField[i].name())){
                failWithMessage("Expected column name to be <%s> but was <%s>",columnExpected[0],actualStructField[i]
                        .name());
            }
            if(!columnExpected[1].equals(actualStructField[i].dataType().typeName())){
                failWithMessage("Expected type for column <%s> to be <%s> but was <%s>", columnExpected[0],
                        columnExpected[1],actualStructField[i].dataType().typeName());
            }

        }
        return this;
    }

    public SQLResultAssert equalsSQLResults(List<List<String>> table){
        try {
            Row[] actualRows = actual.resultSet();
            List<String> firstRow = table.get(0);
            boolean isEquals = false;
            for (int i = 0; i < actualRows.length; i++) {
                Row actualRow = actualRows[i];
                for (int x = 0; x < actualRow.size(); x++) {
                    String[] columnExpected = firstRow.get(x).split("-");
                    switch (columnExpected[1]) {
                    case "boolean":
                        if (!(actualRow.get(x) instanceof Boolean)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Boolean\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getBoolean(x) != (Boolean.parseBoolean(table.get(i + 1).get(x)))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Boolean.parseBoolean(table.get(i + 1).get(x)),
                                    actualRow.getBoolean(x));
                        }
                        break;
                    case "byte":
                        if (!(actualRow.get(x) instanceof Byte)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Byte\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getByte(x) != Byte.parseByte(table.get(i + 1).get(x))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Byte.parseByte(table.get(i + 1).get(x)),
                                    actualRow.getByte(x));
                        }
                        break;
                    case "date":
                        if (!(actualRow.get(x) instanceof java.sql.Date)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.sql.Date\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (!actualRow.getDate(x).equals(Date.valueOf(table.get(i + 1).get(x)))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Date.valueOf(table.get(i + 1).get(x)),
                                    actualRow.getDate(x));
                        }
                        break;
                    case "decimal":
                        if (!(actualRow.get(x) instanceof java.math.BigDecimal)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.math.BigDecimal\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (!actualRow.getDecimal(x).equals(Decimal.apply(table.get(i + 1).get(x)))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Decimal.apply(table.get(i + 1).get(x)),
                                    actualRow.getDecimal(x));
                        }
                        break;
                    case "double":
                        if (!(actualRow.get(x) instanceof Double)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Double\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getDouble(x) != (Double.parseDouble(table.get(i + 1).get(x)))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Double.parseDouble(table.get(i + 1).get(x)),
                                    actualRow.getDouble(x));
                        }
                        break;
                    case "float":
                        if (!(actualRow.get(x) instanceof Float)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Float\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getFloat(x) != (Float.parseFloat(table.get(i + 1).get(x)))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Float.parseFloat(table.get(i + 1).get(x)),
                                    actualRow.getFloat(x));
                        }
                        break;
                    case "integer":
                        if (!(actualRow.get(x) instanceof Integer)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Integer\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getInt(x) != Integer.parseInt(table.get(i + 1).get(x))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Integer.parseInt(table.get(i + 1).get(x)), actualRow.getInt(x));
                        }
                        break;
                    case "long":
                        if (!(actualRow.get(x) instanceof Long)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Long\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getLong(x) != Long.parseLong(table.get(i + 1).get(x))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Long.parseLong(table.get(i + 1).get(x)), actualRow.getLong(x));
                        }
                        break;
                    case "short":
                        if (!(actualRow.get(x) instanceof Short)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Short\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (actualRow.getShort(x) != Short.parseShort(table.get(i + 1).get(x))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Short.parseShort(table.get(i + 1).get(x)),
                                    actualRow.getShort(x));
                        }
                        break;
                    case "string":
                        if (!(actualRow.get(x) instanceof String)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.String\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (!actualRow.getString(x).equals(table.get(i + 1).get(x))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], table.get(i + 1).get(x), actualRow.getString(x));
                        }
                        break;
                    case "timestamp":
                        if (!(actualRow.get(x) instanceof java.sql.Timestamp)) {
                            failWithMessage("Expected type for row <%s> for column <%s> to be \"java.sql.Timestamp\" "
                                            + "but  was <%s>", i,
                                    columnExpected[0], actualRow.get(x).getClass().getName());
                        }
                        if (!actualRow.getTimestamp(x).equals(Timestamp.valueOf(table.get(i + 1).get(x)))) {
                            failWithMessage("Expected value for row <%s> for column <%s> to be <%s> but was <%s>", i,
                                    columnExpected[0], Timestamp.valueOf(table.get(i + 1).get(x)),
                                    actualRow.getTimestamp(x));
                        }
                        break;
                    default:
                        failWithMessage("The type <%s> is not implemented", columnExpected[1]);
                    }
                }
            }
        }catch(RuntimeException e){
            failWithMessage("Expected a successfull result but the obtained result is an ErrorResult");
        }
        return this;

    }


    public SQLResultAssert equalsResultsIgnoringOrderNative(List<List<String>> table){
        Row[] actualRows = actual.resultSet();
        for(int i = 0; i < actualRows.length; i++) {
            Row actualRow = actualRows[i];
            if(!rowIsContainedInDataTable(table, actualRow)){
                failWithMessage("The row <%s> is not conained in the expected result result", i);
            }
        }
        return this;
    }

    private boolean rowIsContainedInDataTable(List<List<String>> table, Row row){
        boolean isContained = false;
        List<String> firstRow = table.get(0);
        for(int i = 1; i < table.size(); i++){
            List<String> tableRow = table.get(i);
            if( isContained = equalsRows(firstRow,tableRow, row)){
                break;
            }

        }
        return isContained;
    }

    private boolean equalsRows(List<String> firstRow, List<String> tableRow, Row actualRow){
        boolean equals = true;
        for(int i = 0; i < tableRow.size() && equals; i++) {
            String[] columnExpected = firstRow.get(i).split("-");
            switch (columnExpected[1]) {
            case "boolean":
                if (!(actualRow.get(i) instanceof Boolean)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Boolean\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getBoolean(i) != (Boolean.parseBoolean(tableRow.get(i)))){
                    return equals = false;
                }
                break;
            case "byte":
                if (!(actualRow.get(i) instanceof Byte)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Byte\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getByte(i) != Byte.parseByte(tableRow.get(i))){
                    return equals = false;

                }
                break;
            case "date":
                if (!(actualRow.get(i) instanceof java.sql.Date)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.sql.Date\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(!actualRow.getDate(i).equals(Date.valueOf(tableRow.get(i)))){
                    return equals = false;

                }
                break;
            case "decimal":
                if (!(actualRow.get(i) instanceof java.math.BigDecimal)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.math.BigDecimal\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(!actualRow.getDecimal(i).equals(Decimal.apply(tableRow.get(i)))){
                    return equals = false;

                }
                break;
            case "double":
                if (!(actualRow.get(i) instanceof Double)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Double\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getDouble(i) != (Double.parseDouble(tableRow.get(i)))) {
                    return equals = false;

                }
                break;
            case "float":
                if (!(actualRow.get(i) instanceof Float)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Float\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getFloat(i) != (Float.parseFloat(tableRow.get(i)))) {
                    return equals = false;

                }
                break;
            case "integer":
                if (!(actualRow.get(i) instanceof Integer)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Integer\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getInt(i) != Integer.parseInt(tableRow.get(i))){
                    return equals = false;

                }
                break;
            case "long":
                if (!(actualRow.get(i) instanceof Long)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Long\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getLong(i) != Long.parseLong(tableRow.get(i))){
                    return equals = false;

                }
                break;
            case "short":
                if (!(actualRow.get(i) instanceof Short)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.Short\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(actualRow.getShort(i) != Short.parseShort(tableRow.get(i))){
                    return equals = false;

                }
                break;
            case "string":
                if (!(actualRow.get(i) instanceof String)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.lang.String\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(!actualRow.getString(i).equals(tableRow.get(i))){
                    return equals = false;

                }
                break;
            case "timestamp":
                if (!(actualRow.get(i) instanceof java.sql.Timestamp)){
                    failWithMessage("Expected type for row <%s> for column <%s> to be \"java.sql.Timestamp\" "
                                    + "but  was <%s>", i,
                            columnExpected[0], actualRow.get(i).getClass().getName());
                }
                if(!actualRow.getTimestamp(i).equals(Timestamp.valueOf(tableRow.get(i)))){
                    return equals = false;
                }
                break;
            default:
                failWithMessage("The type <%s> is not implemented", columnExpected[1]);
            }
        }
        return equals;
    }
}
