package com.stratio.sdh.meta.utils;

import com.stratio.sdh.meta.statements.Statement;

public class AntlrResult {

    private Statement statement;
    private ErrorsHelper foundErrors;

    public AntlrResult(Statement statement, ErrorsHelper foundErrors) {
        this.statement = statement;
        this.foundErrors = foundErrors;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ErrorsHelper getFoundErrors() {
        return foundErrors;
    }

    public void setFoundErrors(ErrorsHelper foundErrors) {
        this.foundErrors = foundErrors;
    }        
    
    public String toString(String query){        
        return foundErrors.toString(query, statement);      
    }
    
}
