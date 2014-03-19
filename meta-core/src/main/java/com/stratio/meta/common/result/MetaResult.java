package com.stratio.meta.common.result;

public class MetaResult {

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
        System.out.println(errorMessage);
    }
    
}
