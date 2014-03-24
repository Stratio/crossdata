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

package com.stratio.meta.driver;

import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.server.MetaServer;

import org.apache.log4j.Logger;

public class MetaDriver {
    
    private final Logger logger = Logger.getLogger(MetaDriver.class);
    private final MetaServer metaServer = new MetaServer();
    
    public MetaResult connect() {
        return metaServer.connect();
    }

    public void close() {
        metaServer.close();
    }
    
    /**
     * Get the cluster {@link com.datastax.driver.core.Metadata}.
     * @return The Metadata if the cluster connection have been established or null otherwise.
     */
    public Metadata getClusterMetadata(){
    	Metadata result = null;
        if(!metaServer.connect().hasError()){
                result = metaServer.getMetadata();
        }
        return result;
    }
    
    public PreparedStatement parseStatementWithCassandra(String query){
        return metaServer.prepare(query);
    }
    
    public PreparedStatement parseStatementWithCassandra(RegularStatement rs){
        return metaServer.prepare(rs);
    }
    
    public MetaResult executeQuery(String targetKs, String query, boolean showInfo){         
        if(showInfo){
            logger.info("\033[34;1mQuery:\033[0m "+query);
        }                      
        return metaServer.executeQuery(query);
    }
       
    public MetaResult executeQuery(String targetKs, Statement query, boolean showInfo) {      
        if(showInfo){
            logger.info("\033[34;1mStatement:\033[0m "+query.toString());
        }        
        return metaServer.executeQuery(targetKs, query);
    }
        
//    public void executeMetaCommand(MetaQuery metaQuery, boolean selectWithFiltering){
//        boolean error = false;
//        //logger.info("\033[32mParsed:\033[0m " + stmt.toString());
//        /*
//        stmt.setQuery(cmd);
//        try{
//            stmt.validate();
//        } catch(ValidationException ex){
//            logger.error("\033[31mValidation exception:\033[0m "+ex.getMessage());
//            return null;
//        }
//        */
//        
//        MetaStatement stmt = metaQuery.getParserResult().getStatement();
//
//        QueryResult queryResult = null;  
//        Statement driverStmt = null;
//
//        if(selectWithFiltering){
//            if(stmt instanceof SelectStatement){
//                SelectStatement selectStmt = (SelectStatement) stmt;
//                selectStmt.setNeedsAllowFiltering(selectWithFiltering);
//                stmt = selectStmt;
//            } else {
//                return;
//            }
//        }
//
//        try{
//            driverStmt = stmt.getDriverStatement();
//            if(driverStmt != null){
//                queryResult = MetaDriver.executeQuery(driverStmt, true);
//            } else {
//                queryResult = MetaDriver.executeQuery(stmt.translateToCQL(), true);   
//            }
//        } catch (DriverException | UnsupportedOperationException ex) {
//            Exception e = ex;
//            if(ex instanceof DriverException){
//                logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage());
//                if(ex.getMessage().contains("ALLOW FILTERING")){
//                    logger.info("Executing again including ALLOW FILTERING");
//                    executeMetaCommand(metaQuery, true);
//                    return;
//                }
//            } else if (ex instanceof UnsupportedOperationException){
//                logger.error("\033[31mUnsupported operation by C*:\033[0m "+ex.getMessage());
//            }
//            error = true;
//            if(e.getMessage().contains("line") && e.getMessage().contains(":")){
//                String queryStr;
//                if(driverStmt != null){
//                    queryStr = driverStmt.toString();
//                } else {
//                    queryStr = stmt.translateToCQL();
//                }
//                String[] cMessageEx =  e.getMessage().split(" ");
//                StringBuilder sb = new StringBuilder();
//                sb.append(cMessageEx[2]);
//                for(int i=3; i<cMessageEx.length; i++){
//                    sb.append(" ").append(cMessageEx[i]);
//                }
//                AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
//                queryStr = MetaUtils.getQueryWithSign(queryStr, ae);
//                logger.error(queryStr);
//            }
//        }
//        if(!error){
//            if(queryResult != null){
//                logger.info("\033[32mResult:\033[0m "+stmt.parseResult(queryResult.getResultSet())+System.getProperty("line.separator"));
//            } else {
//                logger.info("\033[32mResult:\033[0m Cannot execute command"+System.getProperty("line.separator"));
//            }
//            
//        } else {
//            List<MetaStep> steps = stmt.getPlan();
//            for(MetaStep step: steps){
//                logger.info(step.getPath()+"-->"+step.getQuery());
//            }
//            DeepResult deepResult = stmt.executeDeep();
//            if(deepResult.hasErrors()){
//                logger.error("\033[31mUnsupported operation by Deep:\033[0m "+deepResult.getErrors()+System.getProperty("line.separator"));
//            } else {
//                logger.info("\033[32mResult:\033[0m "+deepResult.getResult()+System.getProperty("line.separator"));
//            }
//        }
//    }           
               
}
