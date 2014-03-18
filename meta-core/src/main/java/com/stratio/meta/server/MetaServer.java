package com.stratio.meta.server;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.log4j.Logger;

public class MetaServer {
    
    private static final Logger logger = Logger.getLogger(MetaServer.class);
    
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
            
    public static void close(){
        if(session != null){
            session.close();
        }
        
        if(cluster != null){
            cluster.close();
        }                        
    }

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
    
    
    
}
