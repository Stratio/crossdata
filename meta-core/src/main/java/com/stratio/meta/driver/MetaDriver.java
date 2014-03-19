package com.stratio.meta.driver;

import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.server.MetaServer;

import org.apache.log4j.Logger;

public class MetaDriver {
    
    private static final Logger logger = Logger.getLogger(MetaDriver.class);
    
    public static MetaResult connect() {
        return MetaServer.connect();
    }

    public static void close() {
        MetaServer.close();
    }
    
    /**
     * Get the cluster {@link com.datastax.driver.core.Metadata}.
     * @return The Metadata if the cluster connection have been established or null otherwise.
     */
    public static Metadata getClusterMetadata(){
    	Metadata result = null;
        if(!MetaServer.connect().hasError()){
                result = MetaServer.getMetadata();
        }
        return result;
    }
    
    public static PreparedStatement parseStatementWithCassandra(String query){
        return MetaServer.prepare(query);
    }
    
    public static PreparedStatement parseStatementWithCassandra(RegularStatement rs){
        return MetaServer.prepare(rs);
    }
    
    public static MetaResult executeQuery(String query, boolean showInfo){         
        if(showInfo){
            logger.info("\033[34;1mQuery:\033[0m "+query);
        }                      
        return MetaServer.executeQuery(query);
    }
       
    public static MetaResult executeQuery(Statement query, boolean showInfo) {      
        if(showInfo){
            logger.info("\033[34;1mStatement:\033[0m "+query.toString());
        }        
        return MetaServer.executeQuery(query);
    }        
        
//    public static void executeMetaCommand(MetaQuery metaQuery) {
//        executeMetaCommand(metaQuery, true);
//    }
//
//    public static void executeMetaCommand(MetaQuery metaQuery, boolean selectWithFiltering){
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
