package com.stratio.meta.core.structures;

public abstract class IdentIntOrLiteral {
    
    protected String identifier;    
    protected char operator;   

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }
    
    @Override
    public abstract String toString();
    
    public String string(){
        StringBuilder sb = new StringBuilder(identifier);
        sb.append(" ").append(operator).append(" ");
        return sb.toString();
    }
    
}
