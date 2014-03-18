package com.stratio.meta.common.utils;

import org.apache.log4j.Logger;

public class AntlrError {
    
    /**
     * Class logger.
     */
    private final Logger logger = Logger.getLogger(AntlrError.class);
    
    private String header;
    private String message;   
    
    public AntlrError(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("\tError recognized: ");        
        sb.append(header).append(": ").append(message);
        return sb.toString();        
    }
    
    public String toStringWithTokenTranslation(){
        StringBuilder sb = new StringBuilder("\tError recognized: ");        
        sb.append(header).append(": ");
        sb.append(MetaUtils.translateToken(message));
        return sb.toString();        
    }
    
}
