package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        StringBuilder sb = new StringBuilder("Creating keyspace ");
        sb.append(ident);
        if(ifNotExists){
            sb.append(" (if not exists) ");
        }
        sb.append(" with: ");
        Set keySet = properties.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            ValueProperty vp = properties.get(key);
            sb.append(key).append(": ").append(vp.toString());
        }
        return sb.toString();
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }
    
}
