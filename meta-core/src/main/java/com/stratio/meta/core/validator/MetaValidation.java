package com.stratio.meta.core.validator;

public class MetaValidation {
    
    private boolean hasError = false;    
    private String message;   

    public boolean hasError() {
        return hasError;
    }

    public void setHasError() {
        this.hasError = true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
