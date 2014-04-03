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
        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        sb.append("------------------------------------------------------------------");
        sb.append(System.getProperty("line.separator"));
        boolean firstRow = true;
        for(Row row: resultSet){
            sb.append(" | ");
            if(firstRow){
                for(String key: row.getCells().keySet()){
                    sb.append(key+" | ");
                }
                sb.append(System.getProperty("line.separator"));
                sb.append("------------------------------------------------------------------");
                sb.append(System.getProperty("line.separator"));
                sb.append(" | ");
                firstRow = false;
            }
            Map<String, Cell> cells = row.getCells();
            for(String key: cells.keySet()){
                Cell cell = cells.get(key);
                sb.append(cell.getDatatype().cast(cell.getValue())).append(" | ");
            }
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("------------------------------------------------------------------");
        return sb.toString();
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
