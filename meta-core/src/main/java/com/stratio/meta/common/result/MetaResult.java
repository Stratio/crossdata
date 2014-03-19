package com.stratio.meta.common.result;

public class MetaResult {

    private boolean hasError = false;
    private String errorMessage = null;

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
    
    public void print(){
        System.out.println(errorMessage);
    }
    
}
