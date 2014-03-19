package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.core.structures.MetaProperty;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.sh.utils.ShUtils;
import com.stratio.meta.core.utils.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class CreateTableStatement extends MetaStatement{
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String name_table;    
    private LinkedHashMap<String, String> columns;    
    private List<String> primaryKey;    
    private List<String> clusterKey;    
    //private LinkedHashMap<String, ValueProperty> properties;
    private List<MetaProperty> properties;
    private int Type_Primary_Key;
    private boolean ifNotExists;
    private boolean withClusterKey;
    private int columnNumberPK;
    private boolean withProperties;

    public CreateTableStatement(String name_table, 
                                LinkedHashMap<String, String> columns, 
                                List<String> primaryKey, 
                                List<String> clusterKey, 
                                List<MetaProperty> properties, 
                                int Type_Primary_Key, 
                                boolean ifNotExists, 
                                boolean withClusterKey, 
                                int columnNumberPK, 
                                boolean withProperties) {       
        if(name_table.contains(".")){
            String[] ksAndTablename = name_table.split("\\.");
            keyspace = ksAndTablename[0];
            name_table = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.name_table = name_table;
        this.columns = columns;
        this.primaryKey = primaryKey;
        this.clusterKey = clusterKey;
        this.properties = properties;
        this.Type_Primary_Key = Type_Primary_Key;
        this.ifNotExists = ifNotExists;
        this.withClusterKey = withClusterKey;
        this.columnNumberPK = columnNumberPK;
        this.withProperties=withProperties;
    }

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }

    public void setKeyspaceInc(boolean keyspaceInc) {
        this.keyspaceInc = keyspaceInc;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }        
            
    public boolean isWithProperties() {
        return withProperties;
    }

    public void setWithProperties(boolean withProperties) {
        this.withProperties = withProperties;
    }
    public int getColumnNumberPK() {
        return columnNumberPK;
    }

    public void setColumnNumberPK(int columnNumberPK) {
        this.columnNumberPK = columnNumberPK;
    }
    
    public boolean isWithClusterKey() {
        return withClusterKey;
    }

    public void setWithClusterKey(boolean withClusterKey) {
        this.withClusterKey = withClusterKey;
    }
    
    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public int getType_Primary_Key() {
        return Type_Primary_Key;
    }

    public void setType_Primary_Key(int Type_Primary_Key) {
        this.Type_Primary_Key = Type_Primary_Key;
    }

    public List<MetaProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MetaProperty> properties) {
        this.properties = properties;
    }

    public List<String> getClusterKey() {
        return clusterKey;
    }

    public void setClusterKey(List<String> clusterKey) {
        this.clusterKey = clusterKey;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }

    public void setColumns(LinkedHashMap<String, String> columns) {
        this.columns = columns;
    }   
    
    public String getName_table() {
        return name_table;
    }

    public void setName_table(String name_table) {
        if(name_table.contains(".")){
            String[] ksAndTablename = name_table.split("\\.");
            keyspace = ksAndTablename[0];
            name_table = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.name_table = name_table;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create table ");
        if(ifNotExists) sb.append("if not exit ");
        
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        } 
        sb.append(name_table);
        
        switch(Type_Primary_Key){
            case 1: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append("(");
                
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    if (i==0) sb.append(key).append(" ").append(vp).append(" PRIMARY KEY");
                    else sb.append(key).append(" ").append(vp);
                    i++;
                    if (it.hasNext()) sb.append(", ");
                    else sb.append(")");                      
                }         
            }break;
                
            case 2: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append("(");
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    if (i == columnNumberPK) sb.append(key).append(" ").append(vp).append(" PRIMARY KEY");
                    else sb.append(key).append(" ").append(vp);
                    i++;
                    if (it.hasNext()) sb.append(", ");
                    else sb.append(")");  
                }
            }break;
            case 3: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append("(");
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    sb.append(key).append(" ").append(vp).append(", ");
                    i++;
                    
                }
                
                sb.append("PRIMARY KEY (");
                int j=0;
                for (Iterator it = primaryKey.iterator();it.hasNext();){
                    String key = (String) it.next();
                    if (j== 0) sb.append(key);
                    else sb.append(", ").append(key);
                    j++;
                    if (!it.hasNext()) sb.append("))");
                }
            
            }break;
                
            case 4: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append("(");
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    sb.append(key).append(" ").append(vp).append(", ");
                    i++;
                    
                }
                sb.append("PRIMARY KEY ((");
                int j=0;
                for (Iterator it = primaryKey.iterator();it.hasNext();){
                    String key = (String) it.next();
                    if (j== 0) sb.append(key);
                    else sb.append(", ").append(key);
                    j++;
                    if (!it.hasNext()) sb.append(")");
                }
                System.out.println(withClusterKey);
                if (withClusterKey){
                    for (Iterator it = clusterKey.iterator();it.hasNext();){
                        String key = (String) it.next();
                        sb.append(", ").append(key);
                        if (!it.hasNext()) sb.append("))");
                    } 
                }
                else sb.append("))");
            }break;
                
            default:{
                sb.append("bad option");
            }break;
                
        }
        if(withProperties){
            sb.append(" WITH ").append(ShUtils.StringList(properties, " AND "));
        }
        /*
        Set keySet = properties.keySet();
        //if (withProperties) sb.append(" with:\n\t");
        if (withProperties) sb.append(" with");
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            ValueProperty vp = properties.get(key);
            //sb.append(key).append(": ").append(String.valueOf(vp)).append("\n\t");
            sb.append(" ").append(key).append("=").append(String.valueOf(vp));
            if(it.hasNext()) sb.append(" AND");
        }
        */
        return sb.toString();    
    }

    @Override
    public void validate() {  
        if(withProperties){
            for(MetaProperty property: properties){
                if(property.getType() == MetaProperty.TYPE_NAME_VALUE){
                    PropertyNameValue propertyNameValue = (PropertyNameValue) property;
                    // If property ephemeral is present, it must be a boolean type
                    if(propertyNameValue.getName().equalsIgnoreCase("ephemeral")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            throw new ValidationException("Property 'ephemeral' must be a boolean");
                        }
                        break;
                    // If property ephemeral_tuples is present, it must be a integer type    
                    } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_tuples")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            throw new ValidationException("Property 'ephemeral' must be a boolean");
                        }
                        break;
                    // If property ephemeral_persist_on is present, it must be a string type
                    } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_persist_on")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            throw new ValidationException("Property 'ephemeral_persist_on' must be a string");
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        String cqlString = this.toString();
        if(!cqlString.contains(" WITH ")){
            return cqlString;
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<cqlString.length(); i++){
            char c = cqlString.charAt(i);
            if(c=='{'){
                sb.append("{");
                int newI = cqlString.indexOf("}", i);
                String insideBracket = cqlString.substring(i+1, newI);
                insideBracket = insideBracket.replace(":", " ");
                insideBracket = insideBracket.replace(",", " ");
                System.out.println("|"+insideBracket+"|");
                boolean wasChanged = true;
                while(wasChanged){
                    int before = insideBracket.length();
                    insideBracket = insideBracket.replace("  ", " ");
                    int after = insideBracket.length();
                    if(before==after){
                        wasChanged = false;
                    }
                }
                System.out.println("|"+insideBracket+"|");
                insideBracket = insideBracket.trim();
                String[] strs = insideBracket.split(" ");
                for(int j=0; j<strs.length; j++){
                    String currentStr = strs[j];
                    if(currentStr.matches("[0123456789.]+")){
                        sb.append(strs[j]);
                    } else {
                        sb.append("\'").append(strs[j]).append("\'");
                    }
                    if(j % 2 == 0){
                        sb.append(": ");
                    } else {
                        if(j<(strs.length-1)){
                            sb.append(", ");
                        }
                    }
                }                
                sb.append("}");
                i = newI;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    @Override
    public String parseResult(ResultSet resultSet) {
        //return "\t"+resultSet.toString();
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
