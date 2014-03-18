package com.stratio.meta.driver;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;
import com.stratio.meta.grammar.generated.MetaLexer;
import com.stratio.meta.grammar.generated.MetaParser;
import com.stratio.meta.statements.MetaStatement;
import com.stratio.meta.statements.SelectStatement;
import com.stratio.meta.utils.AntlrError;
import com.stratio.meta.utils.AntlrResult;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.ErrorsHelper;
import com.stratio.meta.utils.ExecutionResult;
import com.stratio.meta.utils.MetaQuery;
import com.stratio.meta.utils.MetaStep;
import com.stratio.meta.utils.MetaUtils;
import com.stratio.meta.utils.PlanResult;
import com.stratio.meta.utils.ValidationException;
import com.stratio.meta.utils.ValidationResult;
import java.util.List;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import org.apache.log4j.Logger;

public class MetaDriver {
    
    private static final Logger logger = Logger.getLogger(MetaDriver.class);
    
    private static Cluster cluster;
    private static Session session;    
    
    public static void connect(){
        if(cluster == null){
            cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        }                
        
        if(session == null){
            session = cluster.connect();
        }                
    }
    
    /**
     * Get the cluster {@link com.datastax.driver.core.Metadata}.
     * @return The Metadata if the cluster connection have been established or null otherwise.
     */
    public static Metadata getClusterMetadata(){
    	Metadata result = null;
		if(session != null){
			result = cluster.getMetadata();
		}
		return result;
    }
    
    public static PreparedStatement parseStatementWithCassandra(String query){
        return session.prepare(query);
    }
    
    public static PreparedStatement parseStatementWithCassandra(RegularStatement rs){
        return session.prepare(rs);
    }
    
    public static ResultSet executeQuery(String query, boolean showInfo){        
        //query = "SELECT * FROM mykeyspace.usuarios";     
        if(showInfo){
            logger.info("\033[34;1mQuery:\033[0m "+query);
        }
        /*
        PreparedStatement cqlStatement = session.prepare(query);        
        if(cqlStatement == null){
            return null;
        }
        */
        ResultSet resultSet = session.execute(query);             
        //System.out.println("ResultSet: "+resultSet.all().size()+" rows");               
        
        return resultSet;
    }
       
    public static ResultSet executeQuery(Statement query, boolean showInfo) {      
        //query = "SELECT * FROM mykeyspace.usuarios";    
        if(showInfo){
            logger.info("\033[34;1mStatement:\033[0m "+query.toString());
        }
        /*if(query instanceof RegularStatement){            
            System.out.println(query.toString());
            RegularStatement tmp = (RegularStatement) query;
            System.out.println(tmp.getQueryString());
            ByteBuffer[] bytes = tmp.getValues();
            if(bytes != null){
                for(ByteBuffer bb: bytes){
                    System.out.println(Charsets.UTF_8.decode(bb).toString());
                }
            }                        
            PreparedStatement cqlStatement = session.prepare((RegularStatement) query);        
            if(cqlStatement == null){
                return null;
            }            
        }*/        
        ResultSet resultSet = session.execute(query);             
        //System.out.println("ResultSet: "+resultSet.toString());  
        //System.out.println(resultSet.all().size());
        /*Iterator<Row> iter = resultSet.iterator();
        while(iter.hasNext()){
            Row row = iter.next();
            System.out.println(row.toString());
        }*/
        
        return resultSet;
    }
    
    public static void close(){
        if(session != null){
            session.close();
        }
        
        if(cluster != null){
            cluster.close();
        }                        
    }
    
    /**
     * Parse a input text and return the equivalent Statement.
     * @param inputText The input text.
     * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     */ 
    public static AntlrResult parseStatement(String inputText){
        MetaStatement result = null;
        ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MetaParser parser = new MetaParser(tokens);        
        ErrorsHelper foundErrors = null;                
        try {
            result = parser.query();
            foundErrors = parser.getFoundErrors();
        } catch (Exception e) {
            logger.error("Cannot parse statement", e);
            if(foundErrors == null){                                    
                foundErrors = new ErrorsHelper();
            }
            if(foundErrors.isEmpty()){
                foundErrors.addError(new AntlrError("Unkown parser error", e.getMessage()));
            }
            return new AntlrResult(result, foundErrors);
        } 
        return new AntlrResult(result, foundErrors);                 
    }
    
    public static MetaQuery parserMetaQuery(String cmd){
        MetaQuery mq = new MetaQuery();
        mq.setParserResult(parseStatement(cmd));
        return mq;
    }
    
    public static void validateMetaQuery(MetaQuery metaQuery) {
        metaQuery.setValidationResult(new ValidationResult());
    }

    public static void planMetaQuery(MetaQuery metaQuery) {
        metaQuery.setPlanResult(new PlanResult());
    } 
    
    public static void executeMetaCommand(MetaQuery metaQuery) {
        executeMetaCommand(metaQuery, true);
    }

    public static void executeMetaCommand(MetaQuery metaQuery, boolean selectWithFiltering){
        boolean error = false;
        //logger.info("\033[32mParsed:\033[0m " + stmt.toString());
        /*stmt.setQuery(cmd);
        try{
            stmt.validate();
        } catch(ValidationException ex){
            logger.error("\033[31mValidation exception:\033[0m "+ex.getMessage());
            return null;
        }*/
        
        MetaStatement stmt = metaQuery.getParserResult().getStatement();

        ResultSet resultSet = null;  
        Statement driverStmt = null;

        if(selectWithFiltering){
            if(stmt instanceof SelectStatement){
                SelectStatement selectStmt = (SelectStatement) stmt;
                selectStmt.setNeedsAllowFiltering(selectWithFiltering);
                stmt = selectStmt;
            } else {
                return;
            }
        }

        try{
            driverStmt = stmt.getDriverStatement();
            if(driverStmt != null){
                resultSet = MetaDriver.executeQuery(driverStmt, true);
            } else {
                resultSet = MetaDriver.executeQuery(stmt.translateToCQL(), true);   
            }
        } catch (DriverException | UnsupportedOperationException ex) {
            Exception e = ex;
            if(ex instanceof DriverException){
                logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage());
                if(ex.getMessage().contains("ALLOW FILTERING")){
                    logger.info("Executing again including ALLOW FILTERING");
                    executeMetaCommand(metaQuery, true);
                    return;
                }
            } else if (ex instanceof UnsupportedOperationException){
                logger.error("\033[31mUnsupported operation by C*:\033[0m "+ex.getMessage());
            }
            error = true;
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
                queryStr = MetaUtils.getQueryWithSign(queryStr, ae);
                logger.error(queryStr);
            }
        }
        if(!error){
            logger.info("\033[32mResult:\033[0m "+stmt.parseResult(resultSet)+System.getProperty("line.separator"));
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
        }
    }        
               
}
