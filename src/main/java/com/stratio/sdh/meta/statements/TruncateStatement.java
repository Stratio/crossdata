package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import static com.datastax.driver.core.querybuilder.QueryBuilder.truncate;
import com.datastax.driver.core.querybuilder.Truncate;
import com.stratio.sdh.meta.cassandra.CassandraClient;
import com.stratio.sdh.meta.structures.Path;

public class TruncateStatement extends MetaStatement {
    
    private String ident;
    
    public TruncateStatement(String ident){
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Truncate "+ident);
        return sb.toString();        
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        //Statement exampleQuery = update("table").with(set("col1", "val1")).and(set("col2","val2")).where(eq("col3","val3"));
        /*
        String[] ksAndTablename = ident.split("\\.");
        Truncate truncateQuery = truncate(ksAndTablename[0], ksAndTablename[1]);        
        ResultSet resultSet = CassandraClient.executeQuery(truncateQuery);
        System.out.println("executed: "+resultSet.toString());
        System.out.println(truncateQuery.toString());
        return truncateQuery.toString();
        */
        return this.toString();
    }
    
    @Override
    public Statement getDriverStatement() {
        Truncate truncateQuery = null;
        if(ident.contains(".")){
            String[] ksAndTablename = ident.split("\\.");
            truncateQuery = truncate(ksAndTablename[0], ksAndTablename[1]);
        } else {
            truncateQuery = truncate(ident);
        }
        return truncateQuery;
    }
    
    @Override
    public String parseResult(ResultSet resultSet) {
        return "\t"+resultSet.toString();
    }    
    
}
