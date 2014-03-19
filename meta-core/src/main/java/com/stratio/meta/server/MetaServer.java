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
    
    private final Logger logger = Logger.getLogger(MetaServer.class);
    
    private Cluster cluster;
    private Session session;    
    
    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Session getSession() {
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
    
    public MetaResult executeQuery(Statement query) {
        return executeQuery(query.toString());
    }
    
    public MetaResult executeQuery(String query){
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
