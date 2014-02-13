package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.HashMap;
import java.util.Map;

public class CreateKeyspaceStatement extends Statement {
    
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
        sb.append(MetaUtils.StringMap(properties, ": ", ", "));
        return sb.substring(0, sb.length()-5);
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }
    
}
