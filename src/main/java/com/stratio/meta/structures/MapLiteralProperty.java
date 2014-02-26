package com.stratio.meta.structures;

import com.stratio.meta.utils.MetaUtils;

import java.util.HashMap;
import java.util.Map;

public class MapLiteralProperty extends ValueProperty {
    
    private Map<String, String> literals;

    public MapLiteralProperty() {
        literals = new HashMap<>();
        this.type = TYPE_MAPLT;
    }   
    
    public MapLiteralProperty(String key, String value){
        this();
        addLiteral(key, value);
    }
    
    public MapLiteralProperty(Map<String, String> literals){
        this();
        setLiterals(literals);
    }
    
    public Map<String, String> getLiterals() {
        return literals;
    }

    public void setLiterals(Map<String, String> literals) {
        this.literals = literals;
    }   
    
    public void addLiteral(String key, String value){
        literals.put(key, value);
    }
    
    public String getLiteral(String key){
        return literals.get(key);
    }
    
    public void removeLiteral(String key){
        literals.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(MetaUtils.StringMap(literals, ": ", ", ")).append("}");        
        return sb.toString();
    }
    
}
    
