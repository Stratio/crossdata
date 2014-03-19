package com.stratio.meta.core.utils;

import java.util.List;

public class DeepResult {
    
    private String result;
    private List<String> errors;

    public DeepResult(String result, List<String> errors) {
        this.result = result;
        this.errors = errors;
    }   
    
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }   
    
    public boolean hasErrors(){
        return errors.size() > 0;
    }
    
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
}
