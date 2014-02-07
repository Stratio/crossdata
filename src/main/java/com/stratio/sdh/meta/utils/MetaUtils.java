package com.stratio.sdh.meta.utils;

import java.util.HashMap;
import java.util.List;

public class MetaUtils {   

    public static String StringList(List ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for(Object value: ids){
            sb.append(value.toString()).append(separator);
        }
        return sb.substring(0, sb.length()-separator.length());
    }
    
    public static String StringHashMap(HashMap ids, String separator) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        for(Object key: ids.keySet()){
            Object vp = ids.get(key);
            sb.append(key).append(": ").append(vp.toString()).append(separator);
        }        
        return sb.substring(0, sb.length()-separator.length());
    }
    
}
