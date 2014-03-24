package com.stratio.meta.common.result;

import org.apache.log4j.Logger;

public class ConnectResult extends MetaResult {

    private final Logger logger = Logger.getLogger(ConnectResult.class);
    
    private String message;

    public ConnectResult() {
    }    
    
    public ConnectResult(String message) {
        this.message = message;
    }   

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }        
    
    @Override
    public void print() {
        logger.info("Not implemented yet.");
    }
    
}
