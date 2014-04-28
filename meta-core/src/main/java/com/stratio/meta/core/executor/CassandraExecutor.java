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
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import org.apache.log4j.Logger;

public class CassandraExecutor {

    private static CoreUtils utils = new CoreUtils();

    private static final Logger LOG = Logger.getLogger(CassandraExecutor.class);

    public static Result execute(MetaStep step, Session session){
        Result result;
        LOG.info("Executing step: " + step.toString());
        if(step.getStmt() != null){
            result = execute(step.getStmt(), session);
        }else{
            result = execute(step.getQuery(), session);
        }
        return result;
    }

    protected static Result execute(String query, Session session) {
        try {
            ResultSet resultSet = session.execute(query);
            return QueryResult.createSuccessQueryResult(utils.transformToMetaResultSet(resultSet));
        } catch (UnsupportedOperationException unSupportException){
            return QueryResult.createFailQueryResult("Unsupported operation by C*: " + unSupportException.getMessage());
        } catch (Exception ex) {
            return processException(ex, query);
        }
    }

    protected static Result execute(MetaStatement stmt, Session session) {
        Statement driverStmt = null;
        try {
            driverStmt = stmt.getDriverStatement();
            ResultSet resultSet;
            if (driverStmt != null) {
                System.out.println("TRACE: "+driverStmt.toString());
                resultSet = session.execute(driverStmt);
            } else {
                resultSet = session.execute(stmt.translateToCQL());
            }
            if (stmt instanceof UseStatement) {
                UseStatement useStatement = (UseStatement) stmt;
                return QueryResult.createSuccessQueryResult(utils.transformToMetaResultSet(resultSet), useStatement.getKeyspaceName());
            } else {
                return QueryResult.createSuccessQueryResult(utils.transformToMetaResultSet(resultSet));
            }
        } catch (UnsupportedOperationException unSupportException){
            return QueryResult.createFailQueryResult("Unsupported operation by C*: " + unSupportException.getMessage());
        } catch (Exception ex) {
            String queryStr;
            if(driverStmt != null){
                queryStr = driverStmt.toString();
            } else {
                queryStr = stmt.translateToCQL();
            }
            return processException(ex, queryStr);
        }

    }

    public static Result processException(Exception ex, String queryStr){
        if(ex.getMessage().contains("line") && ex.getMessage().contains(":")){
            String[] cMessageEx =  ex.getMessage().split(" ");
            StringBuilder sb = new StringBuilder();
            sb.append(cMessageEx[2]);
            for(int i=3; i<cMessageEx.length; i++){
                sb.append(" ").append(cMessageEx[i]);
            }
            AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
            queryStr = ParserUtils.getQueryWithSign(queryStr, ae);
            return QueryResult.createFailQueryResult(ex.getMessage() + System.lineSeparator() + "\t" + queryStr);
        } else{
            return QueryResult.createFailQueryResult(ex.getMessage());
        }
    }

}
