package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.HashMap;
import java.util.Map;

public class AlterKeyspaceStatement extends MetaStatement {
    
    private String keyspaceName;
    private HashMap<String, ValueProperty> properties;

    public AlterKeyspaceStatement(String keyspaceName, Map<String, ValueProperty> properties) {
        this.keyspaceName = keyspaceName;
        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }   
    
    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public HashMap<String, ValueProperty> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, ValueProperty> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ALTER KEYSPACE ");
        sb.append(keyspaceName).append(" WITH ");
        sb.append(MetaUtils.StringMap(properties, " = ", " AND "));
        /*Set keySet = properties.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            ValueProperty vp = properties.get(key);
            sb.append(key).append(": ").append(vp.toString());
        }*/
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
        return this.toString();
    }
    
    @Override
    public String parseResult(ResultSet resultSet) {
        return "\t"+resultSet.toString();
    }
    
    @Override
    public Statement getDriverStatement() {
        Statement statement = null;
        return statement;
    }
    
}
