package com.stratio.meta.utils;

import com.stratio.meta.statements.MetaStatement;

public class AntlrResult {

    private MetaStatement statement;
    private ErrorsHelper foundErrors;

    public AntlrResult(MetaStatement statement, ErrorsHelper foundErrors) {
        this.statement = statement;
        this.foundErrors = foundErrors;
    }

    public MetaStatement getStatement() {
        return statement;
    }

    public void setStatement(MetaStatement statement) {
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
