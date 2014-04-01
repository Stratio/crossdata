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
import com.stratio.meta.core.engine.Engine;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.utils.MetaQuery;
import org.apache.log4j.Logger;

public class MetaServer {
    
    private final Logger logger = Logger.getLogger(MetaServer.class);
    
    private Cluster cluster;
    private Session session;  

    public MetaServer() {
        connect();
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
            connResult.setErrorMessage("\033[31mCannot connect with Cassandra:\033[0m "+System.getProperty("line.separator")+ex.getMessage());
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
        return executeQuery(query.toString());
    }
    
    public MetaResult executeQuery(String query){
        EngineConfig config =new EngineConfig();
        config.setCassandraHosts(new String[]{"127.0.0.1"});
        config.setCassandraPort(9042);

        Engine engine = new Engine(config);
        // PARSER ACTOR    
        MetaQuery metaQuery = engine.getParser().parseStatement(query);
        if(metaQuery.hasError()){ // parser error
            return metaQuery.getResult();
        }
        // VALIDATOR ACTOR
        metaQuery = engine.getValidator().validateQuery(metaQuery);
        if(metaQuery.hasError()){ // Invalid metadata
            return metaQuery.getResult();
        }
        // PLANNER ACTOR
        metaQuery = engine.getPlanner().planQuery(metaQuery);
        if(metaQuery.hasError()){ // Cannot plan
            return metaQuery.getResult();
        }                
        // EXECUTOR ACTOR
        metaQuery = engine.getExecutor().executeQuery(metaQuery);
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
