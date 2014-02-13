package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AlterKeyspaceStatement extends Statement {
    
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
    
}
