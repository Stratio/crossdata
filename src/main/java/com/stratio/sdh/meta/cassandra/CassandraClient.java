package com.stratio.sdh.meta.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Select;

public class CassandraClient {
    
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
    
    public static ResultSet executeQuery(String query){        
        //query = "SELECT * FROM mykeyspace.usuarios";        
        PreparedStatement cqlStatement = session.prepare(query);        
        if(cqlStatement == null){
            return null;
        }
        
        ResultSet resultSet = session.execute(query);             
        //System.out.println("ResultSet: "+resultSet.all().size()+" rows");               
        
        return resultSet;
    }
       
    public static ResultSet executeQuery(Statement query){        
        //query = "SELECT * FROM mykeyspace.usuarios";       
        if(query instanceof RegularStatement){
            PreparedStatement cqlStatement = session.prepare((RegularStatement) query);        
            if(cqlStatement == null){
                return null;
            }
        }
        
        ResultSet resultSet = session.execute(query);             
        //System.out.println("ResultSet: "+resultSet.all().size()+" rows");               
        
        return resultSet;
    }
    
    public static void close(){
        if(session != null){
            session.shutdown();
        }
        
        if(cluster != null){
            cluster.shutdown();
        }                        
    }
    
}
