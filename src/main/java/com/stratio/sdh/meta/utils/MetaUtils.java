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
        if(sb.length() > separator.length()){
            return sb.substring(0, sb.length()-separator.length());
        } else {
            return "";
        }
    }
    
    public static String StringMap(Map ids, String conjunction, String separator) {
        //StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(Object key: ids.keySet()){
            Object vp = ids.get(key);
            sb.append(key).append(conjunction).append(vp.toString()).append(separator);
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
