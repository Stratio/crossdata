package com.stratio.meta.common.result;

import org.apache.log4j.Logger;

public class MetaResult {

    private final Logger logger = Logger.getLogger(MetaResult.class);
    
    private boolean hasError = false;
    private String errorMessage = null;
    private boolean ksChanged = false;
    private String currentKeyspace = null;

    public boolean hasError() {
        return hasError;
    }

    public void setHasError() {
        this.hasError = true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }   

    public boolean isKsChanged() {
        return ksChanged;
    }

    public void setKsChanged() {
        this.ksChanged = true;
    }   
    
    public String getCurrentKeyspace() {
        return currentKeyspace;
    }

    public void setCurrentKeyspace(String currentKeyspace) {
        this.currentKeyspace = currentKeyspace;
    }        
    
    public void print(){
        logger.info("\033[32mResult:\033[0m"+System.getProperty("line.separator")+errorMessage);
    }

    public static MetaResult createMetaResultError(String errorMessage){
        MetaResult result=new MetaResult();
        result.setErrorMessage(errorMessage);
        result.setHasError();
        return result;
    }
    
}
