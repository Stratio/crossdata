package com.stratio.sdh.meta.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetaUtils {   

    public static String StringList(List<?> ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for(Object value: ids){
            sb.append(value.toString()).append(separator);
        }
        return sb.substring(0, sb.length()-separator.length());
    }
    
    public static String StringMap(Map ids, String separator) {
        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        for(Object key: ids.keySet()){
            Object vp = ids.get(key);
            sb.append(key).append(": ").append(vp.toString()).append(separator);
        }        
        return sb.substring(0, sb.length()-separator.length());
    }

    public static String StringSet(Set<String> ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for(String id: ids){
            sb.append(id).append(separator);
        }
        return sb.substring(0, sb.length()-separator.length());
    }   
    
}
