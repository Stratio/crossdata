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

import com.stratio.meta.common.data.ResultSet;

public class QueryResult extends Result {

    private static final long serialVersionUID = -5367895767326893839L;
    private final ResultSet resultSet;

    private QueryResult(ResultSet resultSet,boolean error, String errorMessage, boolean ksChanged, String currentKeyspace){
        super(error,errorMessage,ksChanged,currentKeyspace);
        this.resultSet=resultSet;
    }
    
    public ResultSet getResultSet() {
        return resultSet;
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
