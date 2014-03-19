package com.stratio.meta.common.result;

public class ConnectResult extends MetaResult {

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
        System.out.println("Not implemented yet.");
    }
    
}
