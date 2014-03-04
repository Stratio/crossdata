package com.stratio.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.structures.ValueProperty;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaStep;
import com.stratio.meta.utils.MetaUtils;
import com.stratio.meta.utils.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateKeyspaceStatement extends MetaStatement {
    
    private String ident;
    private boolean ifNotExists;
    private HashMap<String, ValueProperty> properties;

    public CreateKeyspaceStatement(String ident, boolean ifNotExists, Map<String, ValueProperty> properties) {
        this.ident = ident;
        this.ifNotExists = ifNotExists;
        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }   
    
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public HashMap<String, ValueProperty> getProperties() {
        return properties;
    }

    public void setProperties(HashMap properties) {
        this.properties = properties;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE ");
        if(ifNotExists){
            sb.append("IF NOT EXISTS ");
        }
        sb.append(ident);
        sb.append(" WITH ");
        sb.append(MetaUtils.StringMap(properties, " = ", " AND "));
        return sb.toString();
    }

    @Override
    public void validate() {
        /*
        if(properties.isEmpty()){
            throw new ValidationException("CREATE KEYSPACE must include at least property 'replication'");
        }
        // Check if there are innapropiate properties
        for(String key: properties.keySet()){
            if(!key.equalsIgnoreCase("replication") && !key.equalsIgnoreCase("durable_writes")){
                throw new ValidationException("CREATE KEYSPACE can only include properties 'replication' and 'durable_writes'");
            }
        }
        // Check if replication is present and it's built properly
        if(!properties.containsKey("replication")){
            throw new ValidationException("CREATE KEYSPACE must include property 'replication'");
        }
        if(properties.get("replication").getType() != ValueProperty.TYPE_MAPLT){
            throw new ValidationException("'replication' property must be a map");
        }
        MapLiteralProperty mlp = (MapLiteralProperty) properties.get("replication");
        if(mlp.isEmpty()){
            throw new ValidationException("'replication' property cannot be empty");
        }*/   
        // if durable_writes is present then it must be a boolean type
        if(properties.containsKey("durable_writes")){
            if(properties.get("durable_writes").getType() != ValueProperty.TYPE_BOOLEAN){
                throw new ValidationException("Property 'replication' must be a boolean");
            }
        }
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        String metaStr = this.toString();
        if(metaStr.contains("{")){
            return MetaUtils.translateLiteralsToCQL(metaStr);
        } else {
            return metaStr;
        }        
    }
    
    @Override
    public String parseResult(ResultSet resultSet) {
        return "Executed successfully"+System.getProperty("line.separator");
    }
    
    @Override
    public Statement getDriverStatement() {
        Statement statement = null;
        return statement;
    }

    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }
    
    @Override
    public List<MetaStep> getPlan() {
        ArrayList<MetaStep> steps = new ArrayList<>();
        return steps;
    }
    
}
