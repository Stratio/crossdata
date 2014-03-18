package com.stratio.meta.common.utils;

public class MetaQuery {
    
    private String query; 
    private AntlrResult parserResult;
    private ValidationResult validationResult;
    private PlanResult planResult;
    private ExecutionResult executionResult;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }  
    
    public AntlrResult getParserResult() {
        return parserResult;
    }

    public void setParserResult(AntlrResult parserResult) {
        this.parserResult = parserResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public PlanResult getPlanResult() {
        return planResult;
    }

    public void setPlanResult(PlanResult planResult) {
        this.planResult = planResult;
    }

    public ExecutionResult getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(ExecutionResult executionResult) {
        this.executionResult = executionResult;
    }

    public boolean hasParserErrors() {
        return false;
    }

    public boolean hasValidationErrors() {
        return false;
    }

    public boolean hasPlanErrors() {
        return false;
    }

    public boolean hasExecutionErrors() {
        return false;
    }

    public void printResult() {
        System.out.println("Not supported yet.");
    }
    
    public void printParserErrors() {
        System.out.println("Not supported yet.");
    }

    public void printValidationErrors() {
        System.out.println("Not supported yet.");
    }

    public void printPlanErrors() {
        System.out.println("Not supported yet.");
    }

    public void printExecutionErrors() {
        System.out.println("Not supported yet.");
    }

    public void printParserInfo() {
        System.out.println("Not supported yet.");
    }

    public void printValidationInfo() {
        System.out.println("Not supported yet.");
    }

    public void printPlanInfo() {
        System.out.println("Not supported yet.");
    }

    public void printExecutionInfo() {
        System.out.println("Not supported yet.");
    }

    public boolean needsAllowFiltering() {
        return false;
    }
    
}
