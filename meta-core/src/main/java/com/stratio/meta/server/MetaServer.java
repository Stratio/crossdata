package com.stratio.meta.server;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.planner.Planner;
import com.stratio.meta.core.validator.Validator;
import org.apache.log4j.Logger;

public class MetaServer {
    
    private static final Logger logger = Logger.getLogger(MetaServer.class);
    
    private static Cluster cluster;
    private static Session session;    
    
    public static Cluster getCluster() {
        return cluster;
    }

    public static void setCluster(Cluster cluster) {
        MetaServer.cluster = cluster;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        MetaServer.session = session;
    }
    
    public static MetaResult connect(String host){
        try {            
            if(cluster == null){
                cluster = Cluster.builder().addContactPoint(host).build();
            }                

            if(session == null){
                session = cluster.connect();
            }
        } catch(Exception ex){
            ConnectResult connResult = new ConnectResult();
            connResult.setHasError();
            connResult.setErrorMessage(ex.getMessage());
            return connResult;
        }
        
        return new ConnectResult("Success");
    }
    
    public static MetaResult connect(){
        return connect("127.0.0.1");
    }
            
    public static boolean close(){
        if(session != null){
            session.close();
        }
        
        if(cluster != null){
            cluster.close();
        }       
        
        return true;
    }    
    
    public static MetaResult executeQuery(Statement query) {
        return executeQuery(query.toString());
    }
    
    public static MetaResult executeQuery(String query){
        // PARSER ACTOR    
        Parser parser = new Parser();
        MetaQuery metaQuery = parser.parseStatement(query);
        if(metaQuery.hasError()){ // parser error
            return metaQuery.getResult();
        }
        // VALIDATOR ACTOR
        Validator validator = new Validator();
        validator.validateQuery(metaQuery);
        if(metaQuery.hasError()){ // Invalid metadata
            return metaQuery.getResult();
        }
        // PLANNER ACTOR
        Planner planner = new Planner();
        planner.planQuery(metaQuery);
        if(metaQuery.hasError()){ // Cannot plan
            return metaQuery.getResult();
        }                
        // EXECUTOR ACTOR
        Executor executor = new Executor();
        executor.executeQuery(metaQuery); 
        return metaQuery.getResult();
    }

    public static Metadata getMetadata() {
        return cluster.getMetadata();
    }

    public static PreparedStatement prepare(String query) {
        return session.prepare(query);
    }

    public static PreparedStatement prepare(RegularStatement rs) {
        return session.prepare(rs);
    }    
    
}
