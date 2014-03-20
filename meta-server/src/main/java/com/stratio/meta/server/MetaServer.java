package com.stratio.meta.server;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.planner.MetaPlan;
import com.stratio.meta.core.planner.Planner;
import com.stratio.meta.core.validator.MetaValidation;
import com.stratio.meta.core.validator.Validator;
import org.apache.log4j.Logger;

public class MetaServer {
    
    private final Logger logger = Logger.getLogger(MetaServer.class);
    
    private Cluster cluster;
    private Session session;  

    public MetaServer() {
        connect();
    }   
    
    public MetaServer(String host) {
        connect(host);
    }
    
    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Session getSession() {
        if (session == null){
            connect();
        }
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    public MetaResult connect(String host){
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
    
    public MetaResult connect(){
        return connect("127.0.0.1");
    }
            
    public boolean close(){
        if(session != null){
            session.close();
        }
        
        if(cluster != null){
            cluster.close();
        }       
        
        return true;
    }    
    
    public MetaResult executeQuery(String targetKs, Statement query) {
        return executeQuery(targetKs, query.toString());
    }
    
    public QueryResult executeQuery(String targetKs, String query){
        // PARSER ACTOR    
        Parser parser = new Parser();
        MetaQuery metaQuery = parser.parseStatement(query);
        if(metaQuery.hasError()){ // parser error
            return metaQuery.getResult();
        }
        // VALIDATOR ACTOR
        Validator validator = new Validator();
        MetaValidation validation = validator.validateQuery(metaQuery);
        if(validation.hasError()){ // Invalid metadata
            QueryResult queryResult = new QueryResult();
            queryResult.setErrorMessage(validation.getMessage());
            return queryResult;
        }
        // PLANNER ACTOR
        Planner planner = new Planner();
        MetaPlan metaPlan = planner.planQuery(metaQuery);
        metaQuery.setPlan(metaPlan);
        if(metaQuery.hasError()){ // Cannot plan
            return metaQuery.getResult();
        }                
        // EXECUTOR ACTOR
        Executor executor = new Executor(getSession());
        QueryResult result = executor.executeQuery(metaQuery); 
        return result;
    }

    public Metadata getMetadata() {
        return cluster.getMetadata();
    }

    public PreparedStatement prepare(String query) {
        return session.prepare(query);
    }

    public PreparedStatement prepare(RegularStatement rs) {
        return session.prepare(rs);
    }    
    
}
