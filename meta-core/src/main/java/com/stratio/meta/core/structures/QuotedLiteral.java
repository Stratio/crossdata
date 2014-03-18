package com.stratio.meta.core.structures;

public class QuotedLiteral extends ValueProperty {
    
    private String literal;

    public QuotedLiteral(String literal) {
        this.literal = literal;
        this.type = TYPE_LITERAL;
    }
        
    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }        

    @Override
    public String toString() {
        return "'"+literal+"'";
    }        
    
}
