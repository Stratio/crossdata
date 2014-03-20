package com.stratio.meta.core.structures;

import com.stratio.meta.core.utils.ParserUtils;

import java.util.Map;

public class InnerJoin {
    
    private String tablename;
    private Map<String, String> fields;

    public InnerJoin(String tablename, Map<String, String> fields) {
        this.tablename = tablename;
        this.fields = fields;
    }   
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(tablename);
        sb.append(" ON ").append(ParserUtils.stringMap(fields, "=", " "));
        return sb.toString();
    }
    
}
