/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author aalcocer
 */
public class CreateTableStatement extends Statement{

    
    private String name_table;
    
    private LinkedHashMap<String, String> columns;
    
    private List<String> primaryKey;
    
    private List<String> clusterKey;
    
    private LinkedHashMap<String, ValueProperty> propierties;
   
    private int Type_Primary_Key;

    private boolean ifNotExists;

    private boolean withClusterKey;

    private int columnNumberPK;

    private boolean withPropierties;

    

    



    public CreateTableStatement(String name_table, LinkedHashMap<String, String> columns, List<String> primaryKey, List<String> clusterKey, LinkedHashMap<String, ValueProperty> propierties, int Type_Primary_Key, boolean ifNotExists, boolean withClusterKey, int columnNumberPK, boolean withPropierties ) {
        this.name_table = name_table;
        this.columns = columns;
        this.primaryKey = primaryKey;
        this.clusterKey = clusterKey;
        this.propierties = propierties;
        this.Type_Primary_Key = Type_Primary_Key;
        this.ifNotExists = ifNotExists;
        this.withClusterKey = withClusterKey;
        this.columnNumberPK = columnNumberPK;
        this.withPropierties=withPropierties;
    }
    
    public boolean isWithPropierties() {
        return withPropierties;
    }

    public void setWithPropierties(boolean withPropierties) {
        this.withPropierties = withPropierties;
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


    public LinkedHashMap<String, ValueProperty> getPropierties() {
        return propierties;
    }

    public void setPropierties(LinkedHashMap<String, ValueProperty> propierties) {
        this.propierties = propierties;
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
        this.name_table = name_table;
    }

    
    
    @Override
    public Path estimatePath() {
         return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create table ");
        if(ifNotExists) sb.append("if not exit ");
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
        Set keySet = propierties.keySet();
        //if (withPropierties) sb.append(" with:\n\t");
        if (withPropierties) sb.append(" with");
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            ValueProperty vp = propierties.get(key);
            //sb.append(key).append(": ").append(String.valueOf(vp)).append("\n\t");
            sb.append(" ").append(key).append("=").append(String.valueOf(vp));
            if(it.hasNext()) sb.append(" AND");
        }
        return sb.toString();
    
    }

    
}
