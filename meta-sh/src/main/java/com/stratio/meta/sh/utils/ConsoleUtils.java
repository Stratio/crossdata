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

package com.stratio.meta.sh.utils;

import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ConsoleUtils {

    public static String stringResult(Result result){
        if(result.hasError()){
            return result.getErrorMessage();
        }
        if(result instanceof QueryResult){
            QueryResult queryResult = (QueryResult) result;
            return stringQueryResult(queryResult);
        } else if (result instanceof CommandResult){
            CommandResult commandResult = (CommandResult) result;
            return commandResult.getResult();
        } else if (result instanceof ConnectResult){
            ConnectResult connectResult = (ConnectResult) result;
            return String.valueOf("Connected with SessionId=" + connectResult.getSessionId());
        } else {
            return "Unknown result";
        }
    }

    private static String stringQueryResult(QueryResult queryResult){
        ResultSet resultSet = queryResult.getResultSet();

        HashMap<String, Integer> colWidths = calculateColWidths(resultSet);

        String bar = StringUtils.repeat('-', getTotalWidth(colWidths) + (colWidths.values().size() * 3) + 1);

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

    private static HashMap<String, Integer> calculateColWidths(ResultSet resultSet) {
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

    private static int getTotalWidth(HashMap<String, Integer> colWidths) {
        int totalWidth = 0;
        for(int width: colWidths.values()){
            totalWidth+=width;
        }
        return totalWidth;
    }

}
