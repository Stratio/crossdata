package com.stratio.meta.core.utils;

import com.stratio.meta.common.utils.MetaUtils;

public class CassandraUtils {    
    
    public static String getQueryWithSign(String query, AntlrError ae) {
        StringBuilder sb = new StringBuilder(query);
        int pos = MetaUtils.getCharPosition(ae);
        if(pos >= 0){
        sb.insert(MetaUtils.getCharPosition(ae), "\033[35m|\033[0m");
        }
        return sb.toString();
    }
    
}
