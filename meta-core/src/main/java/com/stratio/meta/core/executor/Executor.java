package com.stratio.meta.core.executor;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.AntlrError;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.ParserUtils;
import org.apache.log4j.Logger;

public class Executor {

    private final Logger logger = Logger.getLogger(Executor.class);
    private final Session session;
    
    public Executor(Session session) {
        this.session = session;
    }
    
    public QueryResult executeQuery(MetaQuery metaQuery) {
        MetaStatement stmt = metaQuery.getStatement();
        
        QueryResult queryResult = new QueryResult();
        Statement driverStmt = null;
        
        ResultSet resultSet = null;
        try{
            resultSet = session.execute(stmt.translateToCQL());              
            queryResult.setResultSet(resultSet);
        } catch (DriverException | UnsupportedOperationException ex) {
            Exception e = ex;
            if(ex instanceof DriverException){
                logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage());                
            } else if (ex instanceof UnsupportedOperationException){
                logger.error("\033[31mUnsupported operation by C*:\033[0m "+ex.getMessage());
            }
            queryResult.setHasError();
            if(e.getMessage().contains("line") && e.getMessage().contains(":")){
                String queryStr;
                if(driverStmt != null){
                    queryStr = driverStmt.toString();
                } else {
                    queryStr = stmt.translateToCQL();
                }
                String[] cMessageEx =  e.getMessage().split(" ");
                StringBuilder sb = new StringBuilder();
                sb.append(cMessageEx[2]);
                for(int i=3; i<cMessageEx.length; i++){
                    sb.append(" ").append(cMessageEx[i]);
                }
                AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
                queryStr = ParserUtils.getQueryWithSign(queryStr, ae);
                logger.error(queryStr);
            }
        }
        if(!queryResult.hasError()){            
            logger.info("\033[32mResult:\033[0m "+stmt.parseResult(resultSet)+System.getProperty("line.separator"));
            //logger.info("\033[32mResult:\033[0m Cannot execute command"+System.getProperty("line.separator"));        
        } /*else {
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
        return queryResult;
    }
    
}
