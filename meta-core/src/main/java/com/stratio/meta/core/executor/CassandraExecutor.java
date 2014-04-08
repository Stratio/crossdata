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

package com.stratio.meta.core.executor;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.UseStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.CoreUtils;
import com.stratio.meta.core.utils.ParserUtils;

public class CassandraExecutor {

    public static Result execute(MetaStatement stmt, Session session) {
        Statement driverStmt = null;
        try {
            driverStmt = stmt.getDriverStatement();
            ResultSet resultSet;
            if (driverStmt != null) {
                resultSet = session.execute(driverStmt);
            } else {
                resultSet = session.execute(stmt.translateToCQL());
            }
            System.out.println("resultSet="+resultSet.toString());
            if (stmt instanceof UseStatement) {
                UseStatement useStatement = (UseStatement) stmt;
                return QueryResult.CreateSuccessQueryResult(CoreUtils.transformToMetaResultSet(resultSet), useStatement.getKeyspaceName());
            } else {
                return QueryResult.CreateSuccessQueryResult(CoreUtils.transformToMetaResultSet(resultSet));
            }
        } catch (UnsupportedOperationException unSupportException){
            return QueryResult.CreateFailQueryResult("Unsupported operation by C*: "+unSupportException.getMessage());
        } catch (Exception ex) {
            if(ex.getMessage().contains("line") && ex.getMessage().contains(":")){
                String queryStr;
                if(driverStmt != null){
                    queryStr = driverStmt.toString();
                } else {
                    queryStr = stmt.translateToCQL();
                }
                String[] cMessageEx =  ex.getMessage().split(" ");
                StringBuilder sb = new StringBuilder();
                sb.append(cMessageEx[2]);
                for(int i=3; i<cMessageEx.length; i++){
                    sb.append(" ").append(cMessageEx[i]);
                }
                AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
                queryStr = ParserUtils.getQueryWithSign(queryStr, ae);
                return QueryResult.CreateFailQueryResult(ex.getMessage()+System.lineSeparator()+"\t"+queryStr);
            } else{
                return QueryResult.CreateFailQueryResult(ex.getMessage());
            }
        }
    }
}
