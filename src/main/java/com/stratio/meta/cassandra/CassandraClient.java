package com.stratio.meta.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import org.apache.log4j.Logger;

public class CassandraClient {
    
    private static final Logger logger = Logger.getLogger(CassandraClient.class);
    
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
    
    public static ResultSet executeQuery(String query, boolean showInfo){        
        //query = "SELECT * FROM mykeyspace.usuarios";     
        if(showInfo){
            logger.info("\033[34;1mQuery:\033[0m "+query);
        }
        PreparedStatement cqlStatement = session.prepare(query);        
        if(cqlStatement == null){
            return null;
        }
        
        ResultSet resultSet = session.execute(query);             
        //System.out.println("ResultSet: "+resultSet.all().size()+" rows");               
        
        return resultSet;
    }
       
    public static ResultSet executeQuery(Statement query, boolean showInfo){      
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
            session.shutdown();
        }
        
        if(cluster != null){
            cluster.shutdown();
        }                        
    }
    
}
