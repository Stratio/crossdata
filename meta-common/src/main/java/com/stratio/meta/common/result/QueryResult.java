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

package com.stratio.meta.common.result;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class QueryResult extends Result {

    private final ResultSet resultSet;

    private QueryResult(ResultSet resultSet,boolean error, String errorMessage, boolean ksChanged, String currentKeyspace){
        super(error,errorMessage,ksChanged,currentKeyspace);
        this.resultSet=resultSet;
    }
    
    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public String toString(){

        HashMap<String, Integer> colWidths = calculateColWidths();

        String bar = StringUtils.repeat('-', getTotalWidth(colWidths)+(colWidths.values().size()*3)+1);

        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        sb.append(bar).append(System.getProperty("line.separator"));
        boolean firstRow = true;
        for(Row row: resultSet){
            sb.append("| ");

            if(firstRow){
                for(String key: row.getCells().keySet()){
                    sb.append(StringUtils.rightPad("\033[34;1m"+key+"\033[0m ", colWidths.get(key)+12)).append("| ");
                }
                sb.append(System.getProperty("line.separator"));
                sb.append(bar);
                sb.append(System.getProperty("line.separator"));
                sb.append("| ");
                firstRow = false;
            }

            Map<String, Cell> cells = row.getCells();
            for(String key: cells.keySet()){
                Cell cell = cells.get(key);
                String str = String.valueOf(cell.getDatatype().cast(cell.getValue()));
                sb.append(StringUtils.rightPad(str, colWidths.get(key)));
                sb.append(" | ");
            }
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(bar).append(System.getProperty("line.separator"));
        return sb.toString();
    }

    private int getTotalWidth(HashMap<String, Integer> colWidths) {
        int totalWidth = 0;
        for(int width: colWidths.values()){
            totalWidth+=width;
        }
        return totalWidth;
    }

    private HashMap<String, Integer> calculateColWidths() {
        long start = System.currentTimeMillis();
        HashMap colWidths = new HashMap<String, Integer>();
        // Get column names
        Row firstRow = (Row) resultSet.iterator().next();
        for(String key: firstRow.getCells().keySet()){
            colWidths.put(key, 0);
        }
        // Find widest cell content of every column
        for(Row row: resultSet){
            for(String key: row.getCells().keySet()){
                String cellContent = String.valueOf(row.getCell(key).getValue());
                int currentWidth = (int) colWidths.get(key);
                if(cellContent.length() > currentWidth){
                    colWidths.put(key, cellContent.length());
                }
            }
        }
        return colWidths;
    }

    public static QueryResult CreateSuccessQueryResult(){
        return  CreateSuccessQueryResult(null);
    }

    public static QueryResult CreateSuccessQueryResult(ResultSet resultSet){
        return new QueryResult(resultSet,false,null,false,null);
    }
    public static QueryResult CreateSuccessQueryResult(ResultSet resultSet, String keySpace){
        return new QueryResult(resultSet,false,null,true,keySpace);
    }


    public static QueryResult CreateFailQueryResult(String errorMessage){
        return new QueryResult(null,true,errorMessage,false,null);
    }

}
