package com.stratio.meta.common.utils;

import com.stratio.meta.core.utils.AntlrError;

public class MetaUtils {
    
    public static int getCharPosition(AntlrError antlrError) {
        if(antlrError.getHeader().contains(":")){
            return Integer.parseInt(antlrError.getHeader().split(":")[1]);
        } else {
            return -1;
        }
    }
    
}
