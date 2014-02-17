package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.ValueProperty;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class AlterTableStatement extends Statement{
    
    private String name_table;
    private int prop;
    private String column;
    private String type;
    private LinkedHashMap<String, ValueProperty> option;
        
        
    public AlterTableStatement(String name_table, String column, String type, LinkedHashMap<String, ValueProperty> option, int prop) {
        this.name_table = name_table;
        this.column = column;
        this.type = type;
        this.option = option;
        this.prop = prop;          
    }
    
    //Setters and getters Name table
    public String getName_table() {
        return name_table;
    }
    
    public void setName_table(String name_table) {
        this.name_table = name_table;
    }
    
    //Seeters and getters columns
    public String getColumn() {
        return column;
    }  
    
    public void setColumn(String column) {
        this.column = column;
    }
    
    //Setter and getter type 
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    //Setter and getter option
     public LinkedHashMap<String, ValueProperty> getOption() {
        return option;
    }

    public void setOption(LinkedHashMap<String, ValueProperty> option) {
        this.option = option;
    }
    
    //Setter and getter  prop
    public int getProp() {
        return prop;
    }
    
    public void setProp(int prop) {
        this.prop = prop;
    }  

    @Override
    public Path estimatePath() {
           return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Alter table ");
        sb.append(name_table);
        switch(prop){
            case 1: {
                sb.append(" alter ");
                sb.append(column);
                sb.append(" type ");
                sb.append(type);
            }break;
            case 2: {
                sb.append(" add ");
                sb.append(column).append(" ");
                sb.append(type);
            }break;
            case 3: {
                sb.append(" drop ");
                sb.append(column);
            }break;
            case 4: {
                Set keySet = option.keySet();
                //sb.append(" with:\n\t");
                sb.append(" with");
                for (Iterator it = keySet.iterator(); it.hasNext();) {
                    String key = (String) it.next();
                    ValueProperty vp = option.get(key);
                    //sb.append(key).append(": ").append(String.valueOf(vp)).append("\n\t");
                    sb.append(" ").append(key).append("=").append(String.valueOf(vp));
                    if(it.hasNext()) sb.append(" AND");
                }
            }break;
            default:{
                sb.append("bad option");
            }break;
        }        
        return sb.toString();
    }

    @Override
    public boolean validate() {
        return true;
    }
        
}
