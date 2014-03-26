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

import com.datastax.driver.core.*;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.QueryStatus;
import org.apache.log4j.Logger;

public class Executor {

    private final Logger logger = Logger.getLogger(Executor.class);
    private final Session session;

    public Executor(String [] hosts, int port){
        Cluster cluster = Cluster.builder().addContactPoints(hosts)
                .withPort(port).build();
        this.session=cluster.connect();
    }
    
    public Executor(Session session) {
        this.session = session;
    }
    
    public MetaQuery executeQuery(MetaQuery metaQuery) {
        
        metaQuery.setStatus(QueryStatus.EXECUTED);
        MetaStatement stmt = metaQuery.getStatement();
        
        if(stmt.isCommand()){
            if(stmt instanceof DescribeStatement){
                DescribeStatement descrStmt =  (DescribeStatement) stmt;
                metaQuery.setResult(new CommandResult(System.getProperty("line.separator")+descrStmt.execute(session)));
            } else {
                metaQuery.setErrorMessage("Not supported yet.");
                return metaQuery;
            }
            return metaQuery;
        }
        
        StringBuilder sb = new StringBuilder();
        if(!stmt.getPlan().isEmpty()){
            sb.append("PLAN: ").append(System.getProperty("line.separator"));
            sb.append(stmt.getPlan().toStringDownTop());
            logger.info(sb.toString());
            metaQuery.setErrorMessage("Deep execution is not supported yet");
            return metaQuery;
        }       
                
        QueryResult queryResult = new QueryResult();
        Statement driverStmt = null;
        
        ResultSet resultSet;
        try{
            driverStmt = stmt.getDriverStatement();
            if(driverStmt != null){
                resultSet = session.execute(driverStmt);
            } else {
                resultSet = session.execute(stmt.translateToCQL());
            }
                          
            queryResult.setResultSet(resultSet);
        } catch (Exception ex) {
            metaQuery.hasError();
            queryResult.setErrorMessage("Cassandra exception: "+ex.getMessage());
            if (ex instanceof UnsupportedOperationException){
                queryResult.setErrorMessage("Unsupported operation by C*: "+ex.getMessage());
            }
            if(ex.getMessage().contains("line") && ex.getMessage().contains(":")){
                String queryStr;
                if(driverStmt != null){
                    queryStr = driverStmt.toString();
                } else {
                    queryStr = stmt.translateToCQL();
                }
                String[] cMessageEx =  ex.getMessage().split(" ");
                sb = new StringBuilder();
                sb.append(cMessageEx[2]);
                for(int i=3; i<cMessageEx.length; i++){
                    sb.append(" ").append(cMessageEx[i]);
                }
                AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
                queryStr = ParserUtils.getQueryWithSign(queryStr, ae);
                queryResult.setErrorMessage(ex.getMessage()+System.getProperty("line.separator")+"\t"+queryStr);
                logger.error(queryStr);
            }
        }
        /*
        if(!queryResult.hasError()){            
            logger.info("\033[32mResult:\033[0m "+stmt.parseResult(resultSet)+System.getProperty("line.separator"));
            //logger.info("\033[32mResult:\033[0m Cannot execute command"+System.getProperty("line.separator"));        
        } else {
            List<MetaStep> steps = stmt.getPlan();
            for(MetaStep step: steps){
                logger.info(step.getPath()+"-->"+step.getQuery());
            }
            DeepResult deepResult = stmt.executeDeep();
            if(deepResult.hasErrors()){
                logger.error("\033[31mUnsupported operation by Deep:\033[0m "+deepResult.getErrors()+System.getProperty("line.separator"));
            } else {
                logger.info("\033[32mResult:\033[0m "+deepResult.getResult()+System.getProperty("line.separator"));
            }
        }*/
        metaQuery.setResult(queryResult);
        return metaQuery;
    }
    
}
